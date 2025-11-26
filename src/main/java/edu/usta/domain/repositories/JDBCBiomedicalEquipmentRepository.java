package edu.usta.domain.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import edu.usta.domain.entities.BiomedicalEquipment;
import edu.usta.domain.enums.EquipmentStatus;
import edu.usta.domain.enums.EquipmentType;
import edu.usta.infrastructure.db.DatabaseConnection;

public class JDBCBiomedicalEquipmentRepository implements GenericRepository<BiomedicalEquipment> {

    private final DatabaseConnection db;
    private final String BASE_SQL = """
                                    SELECT

                                        be.id AS be_id,
                                        be.risk_class AS be_risk_class,
                                        be.calibration_cert AS be_calibration_cert,

                                        e.id AS e_id,
                                        e.serial AS e_serial,
                                        e.brand AS e_brand,
                                        e.model AS e_model,
                                        e.type::text AS e_type,
                                        e.state::text AS e_state,
                                        e.provider_id AS e_provider_id,
                                        e.image_path AS e_image_path

                                    FROM biomedical_equipment as be
                                    JOIN equipment as e ON be.id = e.id
            """;

    private static final Map<String, String> ALLOWED_FIELDS = Map.of(
            "serial", "e.serial",
            "brand", "e.brand",
            "riskclass", "be.riskclass");

    public JDBCBiomedicalEquipmentRepository(DatabaseConnection db) {
        this.db = db;
    }

    private BiomedicalEquipment mapResultSetToBiomedicalEquipment(ResultSet result) throws SQLException {
        return new BiomedicalEquipment(
                result.getObject("be_id", String.class),
                result.getObject("e_serial", String.class),
                result.getObject("e_brand", String.class),
                result.getObject("e_model", String.class),
                EquipmentType.valueOf(result.getObject("e_type", String.class)),
                EquipmentStatus.valueOf(result.getObject("e_state", String.class)),
                null,
                result.getObject("e_image_path", String.class),
                result.getObject("be_risk_class", String.class),
                result.getObject("be_calibration_cert", String.class));

    }

    @Override
    public BiomedicalEquipment create(BiomedicalEquipment entity) {
        if (entity.getId() == null) {
            // Primero insertar en equipment y obtener el ID
            final String equipmentSql = """
                    INSERT INTO equipment (serial, brand, model, type, state, provider_id, image_path)
                    VALUES (?, ?, ?, ?::equipment_type, ?::equipment_status, ?::UUID, ?)
                    RETURNING id
                    """;

            // Luego insertar en biomedical_equipment con el ID obtenido
            final String biomedicalSql = """
                    INSERT INTO biomedical_equipment (id, risk_class, calibration_cert)
                    VALUES (?::UUID, ?, ?)
                    """;

            try (Connection connection = db.getConnection()) {
                connection.setAutoCommit(false); // Iniciar transacción

                try {
                    // Insertar en equipment
                    UUID equipmentId;
                    try (PreparedStatement equipmentStmt = connection.prepareStatement(equipmentSql)) {
                        equipmentStmt.setString(1, entity.getSerial());
                        equipmentStmt.setString(2, entity.getBrand());
                        equipmentStmt.setString(3, entity.getModel());
                        equipmentStmt.setString(4, entity.getType().name());
                        equipmentStmt.setString(5, entity.getState().name());

                        // Extraer el ID del Provider
                        if (entity.getProvider() != null) {
                            equipmentStmt.setObject(6, UUID.fromString(entity.getProvider().getId()));
                        } else {
                            equipmentStmt.setNull(6, java.sql.Types.OTHER);
                        }

                        equipmentStmt.setString(7, entity.getImagePath());

                        try (ResultSet result = equipmentStmt.executeQuery()) {
                            if (result.next()) {
                                // CAMBIO: obtener directamente como UUID
                                equipmentId = result.getObject("id", UUID.class);
                            } else {
                                throw new SQLException("No ID returned from equipment insert");
                            }
                        }
                    }

                    // Insertar en biomedical_equipment
                    try (PreparedStatement biomedicalStmt = connection.prepareStatement(biomedicalSql)) {
                        biomedicalStmt.setObject(1, equipmentId); // Ya es UUID, no necesita conversión
                        biomedicalStmt.setString(2, entity.getRiskClass());
                        biomedicalStmt.setString(3, entity.getCalibrationCert());

                        biomedicalStmt.executeUpdate();
                    }

                    connection.commit(); // Confirmar transacción

                    return new BiomedicalEquipment(
                            equipmentId.toString(), // Convertir a String solo para el constructor
                            entity.getSerial(),
                            entity.getBrand(),
                            entity.getModel(),
                            entity.getType(),
                            entity.getState(),
                            entity.getProvider(),
                            entity.getImagePath(),
                            entity.getRiskClass(),
                            entity.getCalibrationCert());

                } catch (SQLException e) {
                    connection.rollback(); // Revertir en caso de error
                    throw e;
                } finally {
                    connection.setAutoCommit(true);
                }

            } catch (SQLException exception) {
                throw new RuntimeException("Error inserting biomedical equipment", exception);
            }
        } else {
            throw new RuntimeException("This method only create a new Entity, for update, use the update method");
        }
    }

    @Override
    public Optional<BiomedicalEquipment> findById(UUID id) {
        final String sql = BASE_SQL + " WHERE e.id = ?::UUID";

        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, id);

            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    return Optional.of(mapResultSetToBiomedicalEquipment(result));
                }
            }

            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el equipo con el id:" + id, e);
        }

    }

    @Override
    public List<BiomedicalEquipment> findAll() {
        List<BiomedicalEquipment> biomedicalEquipments = new ArrayList<>();

        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(BASE_SQL)) {
            try (ResultSet result = preparedStatement.executeQuery()) {

                while (result.next()) {
                    biomedicalEquipments.add(mapResultSetToBiomedicalEquipment(result));
                }
            }
            return biomedicalEquipments;
        } catch (SQLException exception) {
            throw new RuntimeException("Error fetching biomedical equipments", exception);
        }
    }

    @Override
    public List<BiomedicalEquipment> findBy(String attribute, String value) {
        String column = ALLOWED_FIELDS.get(attribute);

        if (column == null) {
            throw new IllegalArgumentException("Atributo no permitido: " + attribute);
        }

        final String sql = BASE_SQL + " WHERE LOWER(" + column + ") LIKE LOWER(?)";

        List<BiomedicalEquipment> biomedicalEquipments = new ArrayList<>();
        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + value + "%");

            try (ResultSet result = preparedStatement.executeQuery()) {

                while (result.next()) {
                    biomedicalEquipments.add(mapResultSetToBiomedicalEquipment(result));
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Error al listar equipos biomédicos por " + attribute, exception);
        }
        return biomedicalEquipments;
    }

    @Override
    public BiomedicalEquipment update(BiomedicalEquipment entity) {
        final String sql = """
                UPDATE biomedical_equipment SET
                    risk_class = ?,
                    calibration_cert = ?
                WHERE id = ?::UUID
                """;

        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entity.getRiskClass());
            preparedStatement.setString(2, entity.getCalibrationCert());
            preparedStatement.setObject(3, entity.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se encontró el equipo biomédico para actualizar");
            }
            return entity;
        } catch (SQLException exception) {
            throw new RuntimeException("Error actualizando el equipo biomédico", exception);
        }
    }

    @Override
    public boolean delete(UUID id) {
        final String sql = "DELETE FROM biomedical_equipment WHERE id = ?::UUID";

        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException exception) {
            throw new RuntimeException("Error eliminando el equipo biomédico", exception);
        }
    }

}
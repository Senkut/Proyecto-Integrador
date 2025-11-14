package edu.usta.domain.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
                                        be.riskclass AS be_riskclass,
                                        be.calibrationCert AS be_calibrationCert,

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
                result.getObject("be_riskclass", String.class),
                result.getObject("be_calibrationCert", String.class));

    }

    @Override
    public BiomedicalEquipment create(BiomedicalEquipment entity) {
        if (entity.getId() == null) {
            final String sql = "INSERT INTO biomedical_equipment (riskclass, calibrationCert) VALUES (?, ?) RETURNING id";

            try (Connection connection = db.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, entity.getRiskClass());
                preparedStatement.setString(2, entity.getCalibrationCert());

                preparedStatement.setString(3, entity.getSerial());
                preparedStatement.setString(4, entity.getBrand());
                preparedStatement.setString(5, entity.getModel());
                preparedStatement.setObject(6, entity.getType());
                preparedStatement.setObject(7, entity.getState());
                preparedStatement.setObject(8, entity.getProvider());
                preparedStatement.setString(9, entity.getImagePath());

                try (ResultSet result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        String id = result.getObject("id", String.class);
                        return new BiomedicalEquipment(id, entity.getSerial(), entity.getBrand(), entity.getModel(),
                                entity.getType(), entity.getState(), entity.getProvider(), entity.getImagePath(),
                                entity.getRiskClass(), entity.getCalibrationCert());
                    }
                    throw new SQLException("No ID returned");
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
        final String sql = BASE_SQL + " WHERE e.id = ?::uuid";

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
        // Falta el String column

        List<BiomedicalEquipment> biomedicalEquipments = new ArrayList<>();
        final String sql = BASE_SQL + " WHERE e." + attribute + " = ?";

        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, value);

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
                    riskclass = ?,
                    calibrationCert = ?
                WHERE id = ?::uuid
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
        final String sql = "DELETE FROM biomedical_equipment WHERE id = ?::uuid";

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
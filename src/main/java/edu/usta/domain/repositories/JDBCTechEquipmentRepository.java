package edu.usta.domain.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import edu.usta.domain.entities.Provider;
import edu.usta.domain.entities.TechEquipment;
import edu.usta.domain.enums.EquipmentStatus;
import edu.usta.domain.enums.EquipmentType;
import edu.usta.infrastructure.db.DatabaseConnection;

public class JDBCTechEquipmentRepository implements GenericRepository<TechEquipment> {

    private final DatabaseConnection db;
    private final String BASE_SQL = """
                                    SELECT
                                        tc.os as tc_os,
                                        tc.ram_gb as tc_ram_gb,

                                        e.id AS e_id,
                                        e.serial AS e_serial,
                                        e.brand AS e_brand,
                                        e.model AS e_model,
                                        e.type AS e_type,
                                        e.state AS e_state,
                                        e.provider_id AS e_provider_id,
                                        e.image_path AS e_image_path,

                                        p.id AS p_id,
                                        p.name AS p_name,
                                        p.taxId AS p_taxId,
                                        p.contact_email AS p_contact_email

                                    FROM tech_equipment tc
                                    JOIN equipment e ON e.id = tc.id
                                    JOIN provider p ON p.id = e.provider_id
            """;

    public JDBCTechEquipmentRepository(DatabaseConnection db) {
        this.db = db;
    }

    private static final Map<String, String> ALLOWED_FIELDS = Map.of(
            "serial", "e.serial",
            "brand", "e.brand",
            "provider.name", "p.name",
            "provider.contact_email", "p.contact_email");

    private TechEquipment mapResultSetToTechEquipment(ResultSet result) throws SQLException {

        Provider provider = new Provider(
                result.getObject("p_id", String.class),
                result.getObject("p_name", String.class),
                result.getObject("p_taxId", String.class),
                result.getObject("p_contact_email", String.class));

        return new TechEquipment(
                result.getObject("e_id", String.class),
                result.getObject("e_serial", String.class),
                result.getObject("e_brand", String.class),
                result.getObject("e_model", String.class),
                result.getObject("e_type", EquipmentType.class),
                result.getObject("e_state", EquipmentStatus.class),
                provider,
                result.getObject("e_image_path", String.class),
                result.getObject("tc_os", String.class),
                result.getObject("tc_ram_gb", Integer.class));
    }

    @Override
    public TechEquipment create(TechEquipment entity) {
        if (entity.getId() == null) {
            final String sql = "INSERT INTO tech_equipment (serial, brand, model, type, state, provider, image_path, os, ram_gb) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

            try (Connection connection = db.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, entity.getSerial());
                preparedStatement.setString(2, entity.getBrand());
                preparedStatement.setString(3, entity.getModel());
                preparedStatement.setString(4, entity.getType().name());
                preparedStatement.setString(5, entity.getState().name());
                preparedStatement.setString(6, entity.getProvider().getName());
                preparedStatement.setString(7, entity.getImagePath());
                preparedStatement.setString(8, entity.getOs());
                preparedStatement.setInt(9, entity.getRamGb());

                try (ResultSet result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        String id = result.getObject("id", String.class);
                        return new TechEquipment(id, entity.getSerial(), entity.getBrand(),
                                entity.getModel(), entity.getType(), entity.getState(),
                                entity.getProvider(), entity.getImagePath(), entity.getOs(), entity.getRamGb());
                    }
                    throw new SQLException("No ID returned");
                }
            } catch (SQLException exception) {
                throw new RuntimeException("Error inserting tech equipment", exception);
            }
        } else {
            throw new RuntimeException("This method only create a new Entity, for update, use the update method");
        }
    }

    @Override
    public Optional<TechEquipment> findById(UUID id) {
        final String sql = BASE_SQL + " WHERE e.id = ?";

        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, id);

            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    TechEquipment techEquipment = mapResultSetToTechEquipment(result);
                    return Optional.of(techEquipment);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Error buscando equipo tecnológico por ID", exception);
        }
    }

    @Override
    public List<TechEquipment> findAll() {
        final String sql = BASE_SQL;

        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet result = preparedStatement.executeQuery()) {

            List<TechEquipment> techEquipments = new java.util.ArrayList<>();

            while (result.next()) {
                TechEquipment techEquipment = mapResultSetToTechEquipment(result);
                techEquipments.add(techEquipment);
            }

            return techEquipments;
        } catch (SQLException exception) {
            throw new RuntimeException("Error al listar equipos tecnológicos", exception);
        }
    }

    @Override
    public List<TechEquipment> findBy(String attribute, String value) {

        String column = ALLOWED_FIELDS.get(attribute);
        if (column == null) {
            throw new IllegalArgumentException("Atributo no permitido: " + attribute);
        }

        final String sql = BASE_SQL + " WHERE LOWER(" + column + ") LIKE LOWER(?)";

        List<TechEquipment> techEquipments = new java.util.ArrayList<>();

        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + value + "%");

            try (ResultSet result = preparedStatement.executeQuery()) {

                while (result.next()) {
                    techEquipments.add(mapResultSetToTechEquipment(result));
                }

            }
        } catch (SQLException exception) {
            throw new RuntimeException("Error buscando equipos tecnológicos por " + attribute, exception);
        }
        return techEquipments;
    }

    @Override
    public TechEquipment update(TechEquipment entity) {
        final String sql = """
                UPDATE tech_equipment SET os = ?, ram_gb = ? WHERE id = ?::UUID
                """;

        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entity.getOs());
            preparedStatement.setInt(2, entity.getRamGb());
            preparedStatement.setObject(3, entity.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se encontró el equipo tecnológico para actualizar");
            }

            return entity;
        } catch (SQLException exception) {
            throw new RuntimeException("Error actualizando equipo tecnológico", exception);
        }
    }

    @Override
    public boolean delete(UUID id) {
        final String sql = "DELETE FROM tech_equipment WHERE id = ?";

        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id.toString());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException exception) {
            throw new RuntimeException("Error eliminando equipo tecnológico", exception);
        }
    }

}
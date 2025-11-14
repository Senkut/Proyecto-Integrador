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

import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.RuntimeCryptoException;

import edu.usta.domain.entities.Equipment;
import edu.usta.domain.entities.Provider;
import edu.usta.domain.enums.EquipmentStatus;
import edu.usta.domain.enums.EquipmentType;
import edu.usta.infrastructure.db.DatabaseConnection;

public class JDBCEquipmentRepository implements GenericRepository<Equipment> {
    private final DatabaseConnection db;
    private final String BASE_SQL = """
                                    SELECT

                                        e.id AS e_id,
                                        e.serial AS e_serial,
                                        e.brand AS e_brand,
                                        e.model AS e_model,
                                        e.type::text AS e_type,
                                        e.state::text AS e_state,
                                        e.provider_id AS e_provider_id,
                                        e.image_path AS e_image_path,

                                        p.id AS p_id,
                                        p.name AS p_name,
                                        p.taxId AS p_taxId,
                                        p.contact_email AS p_contact_email
                                    FROM equipment as e
                                    JOIN provider as p ON p.id = e.provider_id
            """;

    private static final Map<String, String> ALLOWED_FIELDS = Map.of(
            "serial", "e.serial",
            "brand", "e.brand",
            "provider.name", "p.name",
            "provider.contact_email", "p.contact_email");

    public JDBCEquipmentRepository(DatabaseConnection db) {
        this.db = db;
    }

    private Equipment mapResultSetToEquipment(ResultSet result) throws SQLException {

        Provider provider = new Provider(
                result.getObject("p_id", UUID.class).toString(),
                result.getObject("p_name", String.class),
                result.getObject("p_taxId", String.class),
                result.getObject("p_contact_email", String.class));

        return new Equipment(
                result.getObject("id", UUID.class).toString(),
                result.getObject("serial", String.class),
                result.getObject("brand", String.class),
                result.getObject("model", String.class),
                EquipmentType.valueOf(result.getString("type")),
                EquipmentStatus.valueOf(result.getString("state")),
                provider,
                result.getObject("imagePath", String.class));
    }

    @Override
    public Equipment create(Equipment entity) {
        if (entity.getId() == null) {
            final String sql = """
                    INSERT INTO equipment (serial, brand, model, type, state, provider_id, imagePath)
                    VALUES (?, ?, ?, ?::equipment_type, ?::equipment_state, ?::uuid, ?)
                    RETURNING id
                    """;

            try (Connection connection = db.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, entity.getSerial());
                preparedStatement.setString(2, entity.getBrand());
                preparedStatement.setString(3, entity.getModel());
                preparedStatement.setString(4, entity.getType().toString());
                preparedStatement.setString(5, entity.getState().toString());
                preparedStatement.setString(6, entity.getProvider().getId());
                preparedStatement.setString(7, entity.getImagePath());

                try (ResultSet result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        UUID id = result.getObject("id", UUID.class);
                        return new Equipment(id.toString(), entity.getSerial(), entity.getBrand(),
                                entity.getModel(), entity.getType(), entity.getState(),
                                entity.getProvider(), entity.getImagePath());
                    }
                    throw new SQLException("No ID returned");
                }
            } catch (SQLException exception) {
                throw new RuntimeException("Error inserting equipment", exception);
            }
        } else {
            throw new RuntimeException("This method only create a new Entity, for update, use the update method");
        }
    }

    @Override
    public Optional<Equipment> findById(UUID id) {
        final String sql = BASE_SQL + " WHERE e.id = ?::uuid";

        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, id);

            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    return Optional.of(mapResultSetToEquipment(result));
                }
            }

            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el equipo con el id:" + id, e);
        }
    }

    @Override
    public List<Equipment> findAll() {
        List<Equipment> equipments = new ArrayList<>();

        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(BASE_SQL)) {
            try (ResultSet result = preparedStatement.executeQuery()) {

                while (result.next()) {
                    equipments.add(mapResultSetToEquipment(result));
                }
            }
            return equipments;
        } catch (Exception e) {
            throw new RuntimeCryptoException("Error al listar los equipos");
        }
    }

    @Override
    public List<Equipment> findBy(String attribute, String value) {
        String column = ALLOWED_FIELDS.get(attribute);

        if (column == null) {
            throw new IllegalArgumentException("Campo de busqueda no permitido " + attribute);
        }

        final String sql = BASE_SQL + " WHERE LOWER(" + column + ") LIKE LOWER(?)";

        List<Equipment> equipments = new ArrayList<>();

        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + value + "%");

            try (ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    equipments.add(mapResultSetToEquipment(result));
                }
            }
        } catch (Exception e) {
            throw new RuntimeCryptoException("Error al listar los equipos por el atributo " + attribute);
        }

        return equipments;
    }

    @Override
    public Equipment update(Equipment entity) {
        final String sql = """
                        UPDATE equipment
                        SET serial = ?,brand = ?,model = ?,
                        type = ?::equipment_type, state = ?::equipment_state,
                        provider_id = ?::uuid, imagePath = ?
                        WHERE id = ?::uuid
                """;
        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entity.getSerial());
            preparedStatement.setString(2, entity.getBrand());
            preparedStatement.setString(3, entity.getModel());
            preparedStatement.setString(4, entity.getType().toString());
            preparedStatement.setString(5, entity.getState().toString());
            preparedStatement.setObject(6, entity.getProvider().getId());
            preparedStatement.setString(7, entity.getImagePath());
            preparedStatement.setObject(8, entity.getId());

            int rows = preparedStatement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("No se encontró el equipo para actualiza");
            }

            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Error al intentar actualizar un equipo", e);
        }
    }

    @Override
    public boolean delete(UUID id) {
        final String sql = "DELETE FROM equipment WHERE id = ?::uuid";

        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id.toString());

            int rows = preparedStatement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al intentar eliminar un equipo", e);
        }
    }
}
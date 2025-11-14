package edu.usta.domain.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.RuntimeCryptoException;

import edu.usta.domain.entities.Provider;
import edu.usta.infrastructure.db.DatabaseConnection;

public class JDBCProviderRepository implements GenericRepository<Provider> {
    private final DatabaseConnection db;
    private final String BASE_SQL = """
                                    SELECT
                                        id,
                                        name,
                                        tax_id,
                                        contactEmail
                                    FROM provider
            """;

    private static final Map<String, String> ALLOWED_FIELDS = Map.of(
            "name", "name",
            "taxId", "tax_id",
            "contactEmail", "contactEmail");

    public JDBCProviderRepository(DatabaseConnection db) {
        this.db = db;
    }

    private Provider mapResultSetToProvider(ResultSet result) throws SQLException {
        return new Provider(
                result.getObject("id", UUID.class).toString(),
                result.getObject("name", String.class),
                result.getObject("tax_id", String.class),
                result.getObject("contactEmail", String.class));
    }

    @Override
    public Provider create(Provider entity) {
        if (entity.getId() == null) {
            final String sql = "INSERT INTO provider (name, tax_id, contactEmail) VALUES (?, ?, ?) RETURNING id";

            try (Connection connection = db.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, entity.getName());
                preparedStatement.setString(2, entity.getTaxId());
                preparedStatement.setString(3, entity.getContactEmail());

                try (ResultSet result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        String id = result.getObject("id", String.class);
                        return new Provider(id, entity.getName(), entity.getTaxId(),
                                entity.getContactEmail());
                    }
                    throw new SQLException("No ID returned");
                }
            } catch (SQLException exception) {
                throw new RuntimeException("Error inserting provider", exception);
            }
        } else {
            throw new RuntimeException("This method only create a new Entity, for update, use the update method");
        }
    }

    // Verificar el funcionamiento de los metodos
    @Override
    public Optional<Provider> findById(UUID id) {

        final String sql = BASE_SQL + " WHERE id = ?::uuid";

        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id.toString());

            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    Provider provider = new Provider(
                            result.getObject("id", UUID.class).toString(),
                            result.getObject("name", String.class),
                            result.getObject("tax_id", String.class),
                            result.getObject("contactEmail", String.class));
                    return Optional.of(provider);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Error al buscar el proveedor por ID", exception);
        }
    }

    @Override
    public List<Provider> findAll() {
        final String sql = "SELECT id, name, tax_id, contactEmail FROM provider";

        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet result = preparedStatement.executeQuery()) {

            List<Provider> providers = new java.util.ArrayList<>();

            while (result.next()) {
                Provider provider = new Provider(
                        result.getObject("id", UUID.class).toString(),
                        result.getObject("name", String.class),
                        result.getObject("tax_id", String.class),
                        result.getObject("contactEmail", String.class));
                providers.add(provider);
            }

            return providers;
        } catch (SQLException exception) {
            throw new RuntimeException("Error al obtener todos los proveedores", exception);
        }
    }

    @Override
    public List<Provider> findBy(String attribute, String value) {

        String column = ALLOWED_FIELDS.get(attribute);
        if (column == null) {
            throw new IllegalArgumentException("Atributo no permitido: " + attribute);
        }

        final String sql = BASE_SQL + " WHERE LOWER(" + column + ") LIKE LOWER(?)";

        List<Provider> providers = new java.util.ArrayList<>();

        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + value + "%");

            try (ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    providers.add(mapResultSetToProvider(result));
                }
            }
        } catch (Exception e) {
            throw new RuntimeCryptoException("Error al listar los equipos por el atributo " + attribute);
        }

        return providers;
    }

    @Override
    public Provider update(Provider entity) {
        final String sql = """
                        UPDATE provider SET name = ?,
                        tax_id = ?, contactEmail = ? WHERE id = ?::uuid
                """;
        ;
        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getTaxId());
            preparedStatement.setString(3, entity.getContactEmail());
            preparedStatement.setObject(4, UUID.fromString(entity.getId()));

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No hay un proveedor con el id: " + entity.getId());
            }
            return entity;
        } catch (SQLException exception) {
            throw new RuntimeException("Error al actualizar el proveedor", exception);
        }
    }

    @Override
    public boolean delete(UUID id) {
        final String sql = "DELETE FROM provider WHERE id = ?::uuid";
        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException exception) {
            throw new RuntimeException("Error al eliminar el proveedor", exception);
        }
    }

}
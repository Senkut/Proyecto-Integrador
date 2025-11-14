package edu.usta.domain.repositories;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import edu.usta.domain.entities.Person;
import edu.usta.domain.enums.Role;
import edu.usta.infrastructure.db.DatabaseConnection;

public class JDBCPersonRepository implements GenericRepository<Person> {

    private final DatabaseConnection db;
    private final String BASE_SQL = "SELECT * FROM person ";

    public JDBCPersonRepository(DatabaseConnection db) {
        this.db = db;
    }

    private Person mapResultSetToPerson(ResultSet result) throws SQLException {
        return new Person(
                result.getObject("id", String.class),
                result.getObject("fullname", String.class),
                result.getObject("document", String.class),
                result.getObject("role", Role.class));
    }

    @Override
    public Person create(Person entity) {
        if (entity.getId() == null) {
            final String sql = "INSERT INTO person (fullname, document, role) VALUES (?, ?, ?) RETURNING id";

            try (Connection connection = db.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, entity.getFullname());
                preparedStatement.setString(2, entity.getDocument());
                preparedStatement.setString(3, entity.getRole().name());

                try (ResultSet result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        String id = result.getObject("id", String.class);
                        return new Person(id, entity.getFullname(), entity.getDocument(), entity.getRole());
                    }
                    throw new SQLException("No ID returned");
                }
            } catch (SQLException exception) {
                throw new RuntimeException("Error inserting person", exception);
            }
        } else {
            throw new RuntimeException("This method only create a new Entity, for update, use the update method");
        }
    }

    @Override
    public Optional<Person> findById(UUID id) {
        final String sql = BASE_SQL + " WHERE id = ?::uuid";

        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, id);

            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    Person person = mapResultSetToPerson(result);
                    return Optional.of(person);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Error finding person by ID", exception);
        }
    }

    @Override
    public List<Person> findAll() {
        List<Person> persons = new ArrayList<>();

        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(BASE_SQL);
                ResultSet result = preparedStatement.executeQuery()) {

            while (result.next()) {
                Person person = mapResultSetToPerson(result);
                persons.add(person);
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Error retrieving all persons", exception);
        }
        return persons;
    }

    @Override
    public List<Person> findBy(String attribute, String value) {

        // Falta el strging column

        final String sql = BASE_SQL + " WHERE " + attribute + " = ?";

        List<Person> persons = new ArrayList<>();

        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, value);

            try (ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    Person person = mapResultSetToPerson(result);
                    persons.add(person);
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Error finding persons by " + attribute, exception);
        }
        return persons;
    }

    @Override
    public Person update(Person entity) {
        final String sql = "UPDATE person SET fullname = ?, document = ?, role = ? WHERE id = ?::uuid";
        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entity.getFullname());
            preparedStatement.setString(2, entity.getDocument());
            preparedStatement.setString(3, entity.getRole().name());
            preparedStatement.setObject(4, UUID.fromString(entity.getId()));

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se encontró persona con id: " + entity.getId());
            }
            return entity;
        } catch (SQLException exception) {
            throw new RuntimeException("Error al actualizar persona", exception);
        }
    }

    @Override
    public boolean delete(UUID id) {
        final String sql = "DELETE FROM person WHERE id = ?::uuid";
        try (Connection connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException exception) {
            throw new RuntimeException("Error al eliminar persona", exception);
        }
    }

}
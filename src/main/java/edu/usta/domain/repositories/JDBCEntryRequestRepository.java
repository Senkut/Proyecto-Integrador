package edu.usta.domain.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import edu.usta.domain.entities.EntryRequest;
import edu.usta.domain.entities.Equipment;
import edu.usta.domain.entities.Person;
import edu.usta.domain.enums.RequestStatus;
import edu.usta.infrastructure.db.DatabaseConnection;

/**
 * Implementación JDBC del repositorio genérico para la entidad EntryRequest.
 *
 * Se encarga de hacer las operaciones CRUD contra la tabla entry_request
 * y de reconstruir el objeto completo usando los repositorios de Equipment y
 * Person.
 */
public class JDBCEntryRequestRepository implements GenericRepository<EntryRequest> {

    /**
     * Conexión a la base de datos (abstracción).
     */
    private final DatabaseConnection db;

    /**
     * Repositorio genérico para consultar equipos (Equipment) relacionados a la
     * solicitud.
     */
    private final GenericRepository<Equipment> equipmentRepo;

    /**
     * Repositorio genérico para consultar personas (Person) relacionadas a la
     * solicitud.
     */
    private final GenericRepository<Person> personRepo;

    /**
     * Consulta base para recuperar EntryRequest desde la tabla entryrequest
     * usando alias 'r' para la tabla.
     */
    private static final String BASE_SQL = """
                SELECT
                    r.id AS r_id,
                    r.purpose AS r_purpose,
                    r.requested_at AS r_requested_at,
                    r.status::text AS r_status,

                    r.equipment_id AS r_equipment_id,
                    r.requester_id AS r_requester_id,
                    r.internal_responsible_id AS r_internal_responsible_id
                FROM entryrequest r
            """;

    /**
     * Campos permitidos para búsquedas dinámicas en findBy.
     * La clave es el nombre lógico que recibe el método,
     * el valor es la columna real (con alias) en la base de datos.
     */
    private static final Map<String, String> ALLOWED_FIELDS = Map.of(
            "purpose", "r.purpose",
            "status", "r.status");

    /**
     * Constructor del repositorio de EntryRequest.
     *
     * @param db            conexión a base de datos
     * @param equipmentRepo repositorio para consultar Equipment
     * @param personRepo    repositorio para consultar Person
     */
    public JDBCEntryRequestRepository(
            DatabaseConnection db,
            GenericRepository<Equipment> equipmentRepo,
            GenericRepository<Person> personRepo) {

        this.db = db;
        this.equipmentRepo = equipmentRepo;
        this.personRepo = personRepo;
    }

    /**
     * Convierte la fila actual de un ResultSet en un objeto EntryRequest,
     * incluyendo la carga de sus relaciones (Equipment y Persons) usando
     * los repositorios correspondientes.
     *
     * @param result ResultSet posicionado en la fila a mapear
     * @return EntryRequest construido con los datos de la fila
     * @throws SQLException si no se encuentran las entidades relacionadas
     */
    private EntryRequest map(ResultSet result) throws SQLException {

        String id = result.getObject("r_id", UUID.class).toString();
        String purpose = result.getString("r_purpose");

        // requested_at puede ser null, por eso se hace la validación
        Timestamp ts = result.getTimestamp("r_requested_at");
        var requestedAt = ts != null ? ts.toLocalDateTime() : null;

        String statusStr = result.getString("r_status");
        RequestStatus status = RequestStatus.valueOf(statusStr);

        // FK IDs
        UUID equipmentId = result.getObject("r_equipment_id", UUID.class);
        UUID requesterId = result.getObject("r_requester_id", UUID.class);
        UUID internalRespId = result.getObject("r_internal_responsible_id", UUID.class);

        // Se buscan las entidades relacionadas. Si no existen, se lanza excepción.
        Equipment equipment = equipmentRepo.findById(equipmentId)
                .orElseThrow(() -> new SQLException("Equipment not found: " + equipmentId));

        Person requester = personRepo.findById(requesterId)
                .orElseThrow(() -> new SQLException("Person requester not found: " + requesterId));

        Person internalResponsible = personRepo.findById(internalRespId)
                .orElseThrow(() -> new SQLException("Person internalResponsible not found: " + internalRespId));

        return new EntryRequest(
                id,
                equipment,
                requester,
                internalResponsible,
                purpose,
                requestedAt,
                status);
    }

    /**
     * Crea un nuevo registro de EntryRequest en la tabla entry_request.
     *
     * Inserta la solicitud con los IDs de Equipment y Person,
     * y devuelve la entidad con el ID generado.
     *
     * @param entity EntryRequest a crear
     * @return EntryRequest creado, con su ID asignado
     */
    @Override
    public EntryRequest create(EntryRequest entity) {

        final String sql = """
                    INSERT INTO entry_request
                    (equipment_id, requester_id, internal_responsible_id, purpose, requested_at, status)
                    VALUES (?::uuid, ?::uuid, ?::uuid, ?, ?, ?::request_status)
                    RETURNING id
                """;

        try (Connection con = db.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(sql)) {

            preparedStatement.setString(1, entity.getEquipment().getId());
            preparedStatement.setString(2, entity.getRequester().getId());
            preparedStatement.setString(3, entity.getInternalResponsible().getId());
            preparedStatement.setString(4, entity.getPurpose());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(entity.getRequestedAt()));
            preparedStatement.setString(6, entity.getStatus().toString());

            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    String id = result.getObject("id", UUID.class).toString();
                    entity.setId(id);
                    // Se retorna una nueva instancia con el ID asignado
                    return new EntryRequest(
                            id.toString(),
                            entity.getEquipment(),
                            entity.getRequester(),
                            entity.getInternalResponsible(),
                            entity.getPurpose(),
                            entity.getRequestedAt(),
                            entity.getStatus());
                }
                throw new SQLException("No ID returned");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting entry_request", e);
        }
    }

    /**
     * Busca una EntryRequest por su ID.
     *
     * @param id UUID de la solicitud
     * @return Optional con la EntryRequest si existe, o vacío si no se encuentra
     */
    @Override
    public Optional<EntryRequest> findById(UUID id) {

        final String sql = BASE_SQL + " WHERE r.id = ?::uuid";

        try (Connection con = db.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(sql)) {

            preparedStatement.setObject(1, id);

            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    return Optional.of(map(result));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error finding EntryRequest by id " + id, e);
        }
    }

    /**
     * Lista todas las EntryRequest almacenadas.
     *
     * @return lista de EntryRequest
     */
    @Override
    public List<EntryRequest> findAll() {

        List<EntryRequest> entryRequests = new ArrayList<>();

        try (Connection con = db.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(BASE_SQL)) {

            try (ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    entryRequests.add(map(result));
                }
            }

            return entryRequests;
        } catch (SQLException e) {
            throw new RuntimeException("Error entry}requestsing entry_requests", e);
        }
    }

    /**
     * Filtra EntryRequest por un campo permitido (purpose o status),
     * usando un LIKE case-insensitive.
     *
     * @param attribute nombre del atributo lógico (clave del mapa ALLOWED_FIELDS)
     * @param value     valor a buscar
     * @return lista de EntryRequest que coinciden con el filtro
     */
    @Override
    public List<EntryRequest> findBy(String attribute, String value) {
        String column = ALLOWED_FIELDS.get(attribute);
        if (column == null) {
            throw new IllegalArgumentException("Campo no permitido: " + attribute);
        }

        final String sql = BASE_SQL + " WHERE LOWER(" + column + ") LIKE LOWER(?)";

        List<EntryRequest> entryRequests = new ArrayList<>();

        try (Connection con = db.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(sql)) {

            preparedStatement.setString(1, "%" + value + "%");

            try (ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    entryRequests.add(map(result));
                }
            }

            return entryRequests;

        } catch (SQLException e) {
            throw new RuntimeException("Error filtering EntryRequests by " + attribute, e);
        }
    }

    /**
     * Actualiza una EntryRequest existente en la tabla entry_request.
     *
     * @param entity EntryRequest con los datos actualizados
     * @return la misma entidad que se pasó como parámetro (ya persistida)
     */
    @Override
    public EntryRequest update(EntryRequest entity) {

        final String sql = """
                    UPDATE entry_request SET
                        equipment_id = ?::uuid,
                        requester_id = ?::uuid,
                        internal_responsible_id = ?::uuid,
                        purpose = ?,
                        requested_at = ?,
                        status = ?::request_status
                    WHERE id = ?::uuid
                """;

        try (Connection con = db.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(sql)) {

            preparedStatement.setString(1, entity.getEquipment().getId());
            preparedStatement.setString(2, entity.getRequester().getId());
            preparedStatement.setString(3, entity.getInternalResponsible().getId());
            preparedStatement.setString(4, entity.getPurpose());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(entity.getRequestedAt()));
            preparedStatement.setString(6, entity.getStatus().toString());
            preparedStatement.setString(7, entity.getId());

            int rows = preparedStatement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("EntryRequest no encontrado para actualizar");
            }

            return entity;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating EntryRequest", e);
        }
    }

    /**
     * Elimina una EntryRequest por su ID.
     *
     * @param id UUID de la solicitud a eliminar
     * @return true si se eliminó al menos un registro, false en caso contrario
     */
    @Override
    public boolean delete(UUID id) {

        final String sql = "DELETE FROM entry_request WHERE id = ?::uuid";

        try (Connection con = db.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(sql)) {

            preparedStatement.setString(1, id.toString());

            int rows = preparedStatement.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting entry_request", e);
        }
    }
}
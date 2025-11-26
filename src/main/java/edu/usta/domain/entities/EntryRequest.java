package edu.usta.domain.entities;

import java.time.LocalDateTime;

import edu.usta.domain.enums.RequestStatus;

/**
 * Representa una solicitud de ingreso (EntryRequest) de un equipo a algún lugar
 * o sistema. Contiene la información del equipo, quién la solicita, quién es
 * el responsable interno, el propósito, la fecha de solicitud y el estado.
 */
public class EntryRequest {

    // Atributos

    /**
     * Identificador único de la solicitud (puede ser un String tipo UUID u otro).
     */
    private String id;

    /**
     * Equipo para el cual se está realizando la solicitud de ingreso.
     */
    private Equipment equipment;

    /**
     * Persona que realiza la solicitud (quien pide el ingreso del equipo).
     */
    private Person requester;

    /**
     * Persona interna responsable de esta solicitud o del equipo.
     */
    private Person internalResponsible;

    /**
     * Propósito o motivo de la solicitud de ingreso (por qué se hace la solicitud).
     */
    private String purpose;

    /**
     * Fecha y hora en la que se realizó la solicitud.
     */
    private LocalDateTime requestedAt;

    /**
     * Estado actual de la solicitud (por ejemplo: PENDING, APPROVED, REJECTED).
     */
    private RequestStatus status;

    // Constructores

    /**
     * Constructor completo, incluyendo el id.
     *
     * @param id                  - identificador de la solicitud
     * @param equipment           - equipo asociado a la solicitud
     * @param requester           - persona que solicita el ingreso
     * @param internalResponsible - responsable interno de la solicitud
     * @param purpose             - propósito/motivo de la solicitud
     * @param requestedAt         - fecha y hora de la solicitud
     * @param status              - estado actual de la solicitud
     */
    public EntryRequest(String id, Equipment equipment, Person requester, Person internalResponsible, String purpose,
            LocalDateTime requestedAt, RequestStatus status) {
        this.id = id;
        this.equipment = equipment;
        this.requester = requester;
        this.internalResponsible = internalResponsible;
        this.purpose = purpose;
        this.requestedAt = requestedAt;
        this.status = status;

    }

    /**
     * Constructor sin id, útil cuando el id se genera automáticamente
     * (por ejemplo en la base de datos).
     *
     * @param equipment           - equipo asociado a la solicitud
     * @param requester           - persona que solicita el ingreso
     * @param internalResponsible - responsable interno de la solicitud
     * @param purpose             - propósito/motivo de la solicitud
     * @param requestedAt         - fecha y hora de la solicitud
     * @param status              - estado actual de la solicitud
     */
    public EntryRequest(Equipment equipment, Person requester, Person internalResponsible, String purpose,
            LocalDateTime requestedAt, RequestStatus status) {
        this.equipment = equipment;
        this.requester = requester;
        this.internalResponsible = internalResponsible;
        this.purpose = purpose;
        this.requestedAt = requestedAt;
        this.status = status;
    }

    // Getters

    /**
     * Obtiene el identificador de la solicitud.
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el equipo asociado a la solicitud.
     */
    public Equipment getEquipment() {
        return equipment;
    }

    /**
     * Obtiene la persona que realizó la solicitud.
     */
    public Person getRequester() {
        return requester;
    }

    /**
     * Obtiene el responsable interno de la solicitud.
     */
    public Person getInternalResponsible() {
        return internalResponsible;
    }

    /**
     * Obtiene el propósito de la solicitud.
     */
    public String getPurpose() {
        return purpose;
    }

    /**
     * Obtiene la fecha y hora en que se realizó la solicitud.
     */
    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    /**
     * Obtiene el estado actual de la solicitud.
     */
    public RequestStatus getStatus() {
        return status;
    }

    // Setters

    /**
     * Asigna el identificador de la solicitud.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Asigna el equipo asociado a la solicitud.
     */
    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    /**
     * Asigna la persona que realizó la solicitud.
     */
    public void setRequester(Person requester) {
        this.requester = requester;
    }

    /**
     * Asigna el responsable interno de la solicitud.
     */
    public void setInternalResponsible(Person internalResponsible) {
        this.internalResponsible = internalResponsible;
    }

    /**
     * Asigna el propósito de la solicitud.
     */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    /**
     * Asigna la fecha y hora en que se realizó la solicitud.
     */
    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    /**
     * Asigna el estado actual de la solicitud.
     */
    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    /**
     * Representación en texto de la solicitud, útil para logs o depuración.
     */
    @Override
    public String toString() {
        return "EntryRequest [id=" + id + ", equipment=" + equipment + ", requester=" + requester
                + ", internalResponsible=" + internalResponsible + ", purpose=" + purpose + ", requestedAt="
                + requestedAt + ", status=" + status + "]";
    }

}
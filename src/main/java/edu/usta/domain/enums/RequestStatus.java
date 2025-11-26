package edu.usta.domain.enums;

/**
 * Representa el estado de una solicitud (por ejemplo, de ingreso de equipo).
 */
public enum RequestStatus {

    DRAFT, // Borrador: la solicitud está en edición y aún no ha sido enviada formalmente.
    SUBMITTED, // Enviada: la solicitud ya fue enviada por el solicitante pero aún no se
               // revisa.
    UNDER_REVIEW, // En revisión: alguien (responsable, comité, etc.) está evaluando la solicitud.
    APPROVED, // Aprobada: la solicitud fue aceptada.
    REJECTED, // Rechazada: la solicitud fue denegada.
    SCHEDULED, // Programada: la solicitud fue aprobada y tiene una fecha/hora asignada.
    CHECKED_IN, // Registrada la entrada el equipo o recurso ya ingresó al sistema/lugar
    IN_USE, // El recurso asociado a la solicitud está siendo utilizado actualmente.
    CHECKED_OUT, // Registrada la salida el equipo o recurso salió o terminó su uso.
    CANCELLED, // Cancelada: la solicitud fue anulada por el solicitante o el sistema.
    EXPIRED, // Expirada: la solicitud ya no es válida porque pasó su fecha/hora límite.
}
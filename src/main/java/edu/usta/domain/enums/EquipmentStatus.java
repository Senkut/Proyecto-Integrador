package edu.usta.domain.enums;

public enum EquipmentStatus {
    NEW, // Equipo recién adquirido, sin uso previo
    IN_USE, // Equipo en uso activo
    UNDER_MAINTENCE, // Equipo en mantenimiento preventivo o correctivo
    DAMAGED, // Equipo averiado o con daños
    LOST, // Equipo extraviado
    DECOMISSIONED, // Equipo dado de baja (retirado del inventario)
    IN_STORAGE, // Equipo almacenado temporalmente
    RESERVED, // Equipo apartado para un uso futuro
    PENDING_INSPECTION, // Equipo pendiente de revisión o auditoría
    REPLACEMENT_NEEDED, // Equipo que requiere ser reemplazado
    RECOVERED, // Equipo recuperado tras estar perdido o inactivo
    DONATED, // Equipo donado a otra institución o entidad
    DISPOSED // Equipo desechado de manera definitiva
}

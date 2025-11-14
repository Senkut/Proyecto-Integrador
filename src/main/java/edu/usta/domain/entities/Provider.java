package edu.usta.domain.entities;

import edu.usta.infrastructure.config.UUIDGenerator;

/**
 * Representa un proveedor dentro del sistema.
 *
 * <p>
 * La clase Provider gestiona información básica de un proveedor,
 * incluyendo un identificador único, nombre, número de identificación
 * tributaria
 * y un correo de contacto.
 * </p>
 */
public class Provider {

    /** Identificador único del proveedor (UUID). */
    private String id;

    /** Nombre del proveedor. */
    private String name;

    /** Número de identificación tributaria del proveedor. */
    private String taxId;

    /** Correo electrónico de contacto del proveedor. */
    private String contactEmail;

    /*
     * Constructor por defecto.
     */
    public Provider(String name, String contactEmail) {

        this.name = name;
        this.taxId = "taxId- " + UUIDGenerator.generate();
        this.contactEmail = contactEmail;
    }

    /**
     * Constructor con todos los parámetros, incluyendo el id.
     *
     * @param id           Identificador único del proveedor
     * @param name         Nombre del proveedor
     * @param taxId        Número de identificación tributaria
     * @param contactEmail Correo electrónico de contacto
     */
    public Provider(String id, String name, String taxId, String contactEmail) {
        this.id = id;
        this.name = name;
        this.taxId = taxId;
        this.contactEmail = contactEmail;
    }

    /**
     * Obtiene el identificador único del proveedor.
     *
     * @return id del proveedor
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el nombre del proveedor.
     *
     * @return nombre del proveedor
     */
    public String getName() {
        return name;
    }

    /**
     * Modifica el nombre del proveedor.
     *
     * @param name nuevo nombre del proveedor
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene el número de identificación tributaria.
     *
     * @return taxId del proveedor
     */
    public String getTaxId() {
        return taxId;
    }

    /**
     * Obtiene el correo electrónico de contacto del proveedor.
     *
     * @return correo de contacto
     */
    public String getContactEmail() {
        return contactEmail;
    }
}

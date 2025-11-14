package edu.usta.domain.entities;

import edu.usta.domain.enums.Role;

/**
 * Representa a una persona dentro del sistema.
 *
 * <p>
 * La clase Person contiene información esencial de una persona
 * como su nombre completo, documento de identificación y el rol que
 * desempeña en la organización.
 * </p>
 */
public class Person {

    /** Identificador único de la persona (UUID). */
    private String id;

    /** Nombre completo de la persona. */
    private String fullname;

    /** Documento de identificación de la persona. */
    private String document;

    /** Rol asignado a la persona dentro del sistema. */
    private Role role;

    /**
     * Constructor por defecto.
     * Genera automáticamente un ID único para la persona.
     * 
     * @param fullname
     * @param document
     * @param role
     */
    public Person(String fullname, String document, Role role) {
        this.fullname = fullname;
        this.document = document;
        this.role = role;
    }

    /**
     * Constructor con todos los parámetros, incluyendo el id.
     * 
     * @param id       - Identificador único de la persona
     * @param fullname - Nombre completo de la persona
     * @param document - Documento de identificación
     * @param role     - Rol asignado a la persona
     */
    public Person(String id, String fullname, String document, Role role) {
        this.id = id;
        this.fullname = fullname;
        this.document = document;
        this.role = role;
    }

    /**
     * Obtiene el identificador único de la persona.
     *
     * @return id de la persona
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el nombre completo de la persona.
     *
     * @return nombre completo
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * Modifica el nombre completo de la persona.
     *
     * @param fullname nuevo nombre completo
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     * Obtiene el documento de identificación.
     *
     * @return documento
     */
    public String getDocument() {
        return document;
    }

    /**
     * Modifica el documento de identificación.
     *
     * @param document nuevo documento
     */
    public void setDocument(String document) {
        this.document = document;
    }

    /**
     * Obtiene el rol de la persona.
     *
     * @return rol de la persona
     */
    public Role getRole() {
        return role;
    }

    /**
     * Modifica el rol de la persona.
     *
     * @param role nuevo rol
     */
    public void setRole(Role role) {
        this.role = role;
    }
}

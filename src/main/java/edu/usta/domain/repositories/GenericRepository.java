package edu.usta.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interfaz genérica para definir las operaciones CRUD (Crear, Leer, Actualizar
 * y Eliminar)
 * y de búsqueda dentro de un repositorio de entidades del dominio.
 *
 * <p>
 * Esta interfaz permite abstraer el acceso a los datos, de modo que las
 * implementaciones
 * concretas (por ejemplo, en memoria, bases de datos SQL o NoSQL) puedan ser
 * intercambiables
 * sin afectar la lógica de negocio.
 * </p>
 *
 * @param <Entity> Tipo genérico que representa la entidad de dominio manejada
 *                 por el repositorio.
 */
public interface GenericRepository<Entity> {
    // Create
    /**
     * Crea una nueva entidad en el repositorio.
     *
     * @param entity Entidad que se desea almacenar.
     * @return La entidad creada con sus valores actualizados (por ejemplo, con un
     *         ID asignado).
     */
    Entity create(Entity entity);

    // Read
    /**
     * Busca una entidad por su identificador único.
     *
     * @param id Identificador único (UUID) de la entidad.
     * @return Un {@link Optional} que contiene la entidad si existe, o vacío si no
     *         se encontró.
     */
    Optional<Entity> findById(UUID id);

    /**
     * Obtiene todas las entidades almacenadas en el repositorio.
     *
     * @return Lista de todas las entidades disponibles.
     */
    List<Entity> findAll();

    // Search
    /**
     * Busca entidades basadas en un atributo determinado.
     *
     * <p>
     * La implementación concreta definirá cómo se interpreta el atributo
     * (por ejemplo, una búsqueda por nombre, código o descripción).
     * </p>
     *
     * @param attribute Atributo por el cual se desea realizar la búsqueda.
     * @return Lista de entidades que coinciden con el criterio de búsqueda.
     */
    List<Entity> findBy(String attribute, String value);

    // Update
    /**
     * Actualiza una entidad existente en el repositorio.
     *
     * @param entity Entidad con los datos actualizados.
     * @return La entidad actualizada.
     */
    Entity update(Entity entity);

    // Delete

    /**
     * Elimina una entidad del repositorio según su identificador.
     *
     * @param id Identificador único (UUID) de la entidad que se desea eliminar.
     * @return {@code true} si la entidad fue eliminada exitosamente,
     *         {@code false} si no se encontró o no pudo eliminarse.
     */
    boolean delete(UUID id);

}

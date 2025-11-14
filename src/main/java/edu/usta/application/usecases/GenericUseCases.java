package edu.usta.application.usecases;

import edu.usta.domain.repositories.GenericRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Caso de uso genérico que define las operaciones básicas de manipulación de
 * entidades
 * del dominio (crear, leer, actualizar y eliminar).
 *
 * <p>
 * Esta clase actúa como una capa intermedia entre la lógica de negocio y el
 * repositorio
 * de datos, permitiendo mantener una separación clara de responsabilidades.
 * </p>
 *
 * 
 * El propósito principal de este caso de uso es centralizar la lógica común
 * que se repite en múltiples entidades, evitando la duplicación de código en
 * los
 * casos de uso específicos.
 * 
 *
 * @param <T> Tipo genérico que representa la entidad de dominio administrada.
 */
public class GenericUseCases<T> {

    /** Repositorio genérico encargado de la persistencia de la entidad. */
    private final GenericRepository<T> repository;

    /**
     * Constructor que inyecta el repositorio genérico correspondiente.
     *
     * @param repository Implementación concreta de {@link GenericRepository}
     *                   para la entidad específica.
     */
    public GenericUseCases(GenericRepository<T> repository) {
        this.repository = repository;
    }

    /**
     * Crea una nueva entidad a través del repositorio.
     *
     * @param entity Entidad que se desea crear.
     * @return La entidad creada, posiblemente con campos generados (como un ID).
     */
    public T create(T entity) {
        return repository.create(entity);
    }

    /**
     * Busca una entidad por su identificador único.
     *
     * @param id Identificador único (UUID) de la entidad.
     * @return Un {@link Optional} que contiene la entidad si existe, o vacío si no
     *         se encontró.
     */
    public Optional<T> findById(UUID id) {
        return repository.findById(id);
    }

    /**
     * Obtiene todas las entidades registradas en el repositorio.
     *
     * @return Lista con todas las entidades disponibles.
     */
    public List<T> findAll() {
        return repository.findAll();
    }

    /**
     * Busca entidades basadas en un atributo determinado.
     *
     * <p>
     * La interpretación del atributo depende de la implementación
     * concreta del repositorio.
     * </p>
     *
     * @param attribute Atributo por el cual se desea realizar la búsqueda.
     * @return Lista de entidades que cumplen con el criterio.
     */
    public List<T> findBy(String attribute, String value) {
        return repository.findBy(attribute, value);
    }

    /**
     * Actualiza una entidad existente.
     *
     * @param entity Entidad con los datos actualizados.
     * @return La entidad luego de ser actualizada en el repositorio.
     */
    public T update(T entity) {
        return repository.update(entity);
    }

    /**
     * Elimina una entidad identificada por su UUID.
     *
     * @param id Identificador único (UUID) de la entidad que se desea eliminar.
     * @return {@code true} si la eliminación fue exitosa,
     *         {@code false} si la entidad no fue encontrada o no pudo eliminarse.
     */
    public boolean delete(UUID id) {
        return repository.delete(id);
    }
}

package edu.usta.application.usecases;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import edu.usta.domain.repositories.GenericRepository;

public class GenericUseCase<Entity> {
    public final GenericRepository<Entity> repo;

    public GenericUseCase(GenericRepository<Entity> repository) {
        this.repo = repository;
    }

    public Entity create(Entity entity) {
        return this.repo.create(entity);
    }

    public Optional<Entity> findById(UUID id) {
        return this.repo.findById(id);
    }

    public List<Entity> findAll() {
        return this.repo.findAll();
    }

    public List<Entity> findBy(String attribute, String value) {
        return this.repo.findBy(attribute, value);
    }

    public Entity update(Entity entity) {
        return this.update(entity);
    }

    public boolean delete(UUID id) {
        return this.delete(id);
    }
}

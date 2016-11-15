package com.ote.test.service.persistence;

import com.ote.test.model.IEntity;
import com.ote.test.model.Parameter;
import com.ote.test.service.persistence.repository.IEntityRepository;
import com.ote.test.utils.BeanMerger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Slf4j
public abstract class EntityPersistenceService<TE extends IEntity<TK>, TK extends IEntity.Key> implements IEntityPersistenceService<TE, TK> {

    @Autowired
    private BeanMerger beanMerger;

    private IEntityRepository<TE, TK> entityRepository;

    protected EntityPersistenceService(IEntityRepository<TE, TK> entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public boolean exists(TK key) {
        return entityRepository.exists(key);
    }

    @Override
    public Optional<TE> findOne(TK key) {
        return Optional.ofNullable(entityRepository.findOne(key));
    }

    @Override
    public Optional<Page<TE>> findMany(Parameter<TE> parameter, Pageable pageRequest) {

        Pageable pageable = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(), parameter.getSort());

        Page<TE> entities = entityRepository.findAll(parameter.getFilter(), pageable);

        return entities.getNumberOfElements() == 0 ?
                Optional.empty() :
                Optional.of(entities);
    }

    @Override
    public Result<TE> create(TE entity) {

        entity.setKey(null);
        TE result = entityRepository.save(entity);
        return new Result<>(Status.CREATED, result);
    }

    @Override
    public Result<TE> partialUpdate(TE partialEntity) {

        if (partialEntity.getKey() == null) {
            return new Result<>(Status.NO_IMPACT, null);
        }

        TE entity = entityRepository.findOne(partialEntity.getKey());

        if (entity == null) {
            return new Result<>(Status.NOT_FOUND, null);
        }

        // Copy non null properties from partialEntity to person
        try {
            beanMerger.copyNonNullProperties(partialEntity, entity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result<>(Status.NO_IMPACT, null);
        }

        TE result = entityRepository.save(entity);
        return new Result<>(Status.UPDATED, result);
    }

    @Override
    public Result<TE> update(TE entity) {

        if (entity.getKey() == null) {
            return new Result<>(Status.NO_IMPACT, null);
        }

        if (!exists(entity.getKey())) {
            return new Result<>(Status.NOT_FOUND, null);
        }

        TE result = entityRepository.save(entity);
        return new Result<>(Status.UPDATED, result);
    }

    @Override
    public Status delete(TK key) {

        if (!entityRepository.exists(key)) {
            return Status.NO_IMPACT;
        }

        entityRepository.delete(key);
        return Status.DELETED;
    }

    @Override
    public Status deleteAll() {

        if (entityRepository.count() == 0) {
            return Status.NO_IMPACT;
        }

        entityRepository.deleteAllInBatch();
        return Status.DELETED;
    }
}

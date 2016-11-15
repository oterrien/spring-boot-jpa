package com.ote.test.service.persistence;

import com.ote.test.model.IEntity;
import com.ote.test.model.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IEntityPersistenceService<TE extends IEntity<TK>, TK extends IEntity.Key> {

    boolean exists(TK key);

    Optional<TE> findOne(TK key);

    Optional<Page<TE>> findMany(Parameter<TE> parameter, Pageable pageRequest);

    Result<TE> create(TE entity);

    Result<TE> update(TE entity);

    Result<TE> partialUpdate(TE partialEntity);

    Status delete(TK key);

    Status deleteAll();

    enum Status {
        CREATED, UPDATED, DELETED, NO_IMPACT, NOT_FOUND;
    }

    @Getter
    @AllArgsConstructor
    class Result<TE> {

        Status status;
        TE entity;
    }
}

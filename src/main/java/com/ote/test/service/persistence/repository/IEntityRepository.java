package com.ote.test.service.persistence.repository;

import com.ote.test.model.IEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IEntityRepository<TE extends IEntity<TK>, TK extends IEntity.Key> extends JpaRepository<TE, TK>, JpaSpecificationExecutor<TE> {
}

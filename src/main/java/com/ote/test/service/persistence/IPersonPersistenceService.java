package com.ote.test.service.persistence;

import com.ote.test.model.Person;
import com.ote.test.model.PersonParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IPersonPersistenceService {

    Optional<Person> findOne(Integer id);

    Optional<Page<Person>> findAll(Pageable pageRequest);

    Optional<Page<Person>> findAll(PersonParameter parameter, Pageable pageRequest);

    Status createOrUpdate(Optional<Integer> id, Person person);

    Status create(Person person);

    Status patch(Person person);

    Status update(Person person);

    Status delete(Integer id);

    Status deleteAll();

    boolean exists(Integer id);

    enum Status {
        CREATED, UPDATED, DELETED, NO_IMPACT;
    }
}

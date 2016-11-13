package com.ote.test.service.persistence;

import com.ote.test.model.Person;
import com.ote.test.model.PersonParameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IPersonPersistenceService {

    Optional<Person> findOne(Integer id);

    Optional<Page<Person>> findAll(Pageable pageRequest);

    Optional<Page<Person>> findAll(PersonParameter parameter, Pageable pageRequest);

    Result create(Person person);

    Result patch(Person personSample);

    Result update(Person person);

    Status delete(Integer id);

    Status deleteAll();

    boolean exists(Integer id);

    enum Status {
        CREATED, UPDATED, DELETED, NO_IMPACT, NOT_FOUND;
    }

    @Getter
    @AllArgsConstructor
    class Result {

        Status status;
        Person person;
    }
}

package com.ote.test.service.persistence.repository;

import com.ote.test.model.Person;
import org.springframework.stereotype.Repository;

@Repository
public interface IPersonRepository extends IEntityRepository<Person, Person.Key> {

}

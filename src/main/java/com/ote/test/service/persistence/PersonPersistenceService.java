package com.ote.test.service.persistence;

import com.ote.test.model.Person;
import com.ote.test.service.persistence.repository.IPersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class PersonPersistenceService extends EntityPersistenceService<Person, Person.Key> {

    public PersonPersistenceService(@Autowired IPersonRepository personRepository) {
        super(personRepository);
    }
}

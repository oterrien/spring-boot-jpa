package com.ote.test.service.persistence;

import com.ote.test.model.Person;
import com.ote.test.model.PersonParameter;
import com.ote.test.service.persistence.repository.IPersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

import static org.springframework.data.jpa.domain.Specifications.where;

@Service
@Transactional(readOnly = true)
@Slf4j
public class PersonPersistenceService implements IPersonPersistenceService {

    @Autowired
    private IPersonRepository personRepository;

    @Override
    public boolean exists(Integer id) {
        return personRepository.exists(id);
    }

    @Override
    public Optional<Person> findOne(Integer id) {
        return Optional.ofNullable(personRepository.findOne(id));
    }

    @Override
    public Optional<Page<Person>> findAll(PersonParameter parameter, Pageable pageRequest) {

        log.info("Find All with parameter " + parameter);

        Pageable pageable = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(), parameter.sort());

        Page<Person> persons = personRepository.findAll(parameter.filter(), pageable);

        return persons.getNumberOfElements() == 0 ?
                Optional.empty() :
                Optional.of(persons);
    }

    @Transactional(readOnly = false)
    @Override
    public Status createOrUpdate(Optional<Integer> id, Person person) {

        if (id.isPresent() && exists(id.get())) {
            person.setId(id.get());
            return update(person);
        } else {
            return create(person);
        }
    }

    @Transactional(readOnly = false)
    @Override
    public Status create(Person person) {

        person.setId(null);
        Person personResult = personRepository.save(person);
        BeanUtils.copyProperties(personResult, person);
        return Status.CREATED;
    }

    @Transactional(readOnly = false)
    @Override
    public Status update(Person person) {

        personRepository.save(person);
        return Status.UPDATED;
    }

    @Transactional(readOnly = false)
    @Override
    public Status patch(Person person) {

        if (person.getId() == null) {
            return Status.NO_IMPACT;
        }

        Person personResult = personRepository.findOne(person.getId());
        BeanUtils.copyProperties(person, personResult);

        personRepository.save(personResult);
        return Status.UPDATED;
    }

    @Transactional(readOnly = false)
    @Override
    public Status delete(Integer id) {

        if (!personRepository.exists(id)) {
            return Status.NO_IMPACT;
        }

        personRepository.delete(id);
        return Status.DELETED;
    }

    @Transactional(readOnly = false)
    @Override
    public Status deleteAll() {

        if (personRepository.count() == 0) {
            return Status.NO_IMPACT;
        }

        personRepository.deleteAllInBatch();
        return Status.DELETED;
    }


}

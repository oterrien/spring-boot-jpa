package com.ote.test.service.persistence;

import com.ote.test.aop.Counter;
import com.ote.test.model.Person;
import com.ote.test.model.PersonParameter;
import com.ote.test.service.persistence.repository.IPersonRepository;
import com.ote.test.utils.BeanMerger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class PersonPersistenceService implements IPersonPersistenceService {

    @Autowired
    private IPersonRepository personRepository;

    @Autowired
    private BeanMerger beanMerger;

    @Override
    public boolean exists(Integer id) {
        return personRepository.exists(id);
    }

    @Override
    public Optional<Person> findOne(Integer id) {
        return Optional.ofNullable(personRepository.findOne(id));
    }

    @Override
    public Optional<Page<Person>> findAll(Pageable pageRequest) {

        Page<Person> persons = personRepository.findAll(pageRequest);

        return persons.getNumberOfElements() == 0 ?
                Optional.empty() :
                Optional.of(persons);
    }

    @Override
    public Optional<Page<Person>> findAll(PersonParameter parameter, Pageable pageRequest) {

        log.info(String.format("####-%d-Find All with parameters %s and pageRequest %s", Counter.nextValue(), parameter.toString(), pageRequest.toString()));

        Pageable pageable = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(), parameter.sort());

        Page<Person> persons = personRepository.findAll(parameter.filter(), pageable);

        return persons.getNumberOfElements() == 0 ?
                Optional.empty() :
                Optional.of(persons);
    }

    @Transactional(readOnly = false)
    @Override
    public Result create(Person person) {

        person.setId(null);
        Person result = personRepository.save(person);
        return new Result(Status.CREATED, result);
    }

    @Transactional(readOnly = false)
    @Override
    public Result update(Person person) {

        if (person.getId() == null) {
            return new Result(Status.NO_IMPACT, null);
        }

        if (!exists(person.getId())) {
            return new Result(Status.NOT_FOUND, null);
        }

        Person result = personRepository.save(person);
        return new Result(Status.UPDATED, result);
    }

    @Transactional(readOnly = false)
    @Override
    public Result patch(Person personSample) {

        if (personSample.getId() == null) {
            return new Result(Status.NO_IMPACT, null);
        }

        Person person = personRepository.findOne(personSample.getId());

        if (person == null) {
            return new Result(Status.NOT_FOUND, null);
        }

        // Copy non null properties from personSample to person
        try {
            beanMerger.merge(person, personSample);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(Status.NO_IMPACT, null);
        }

        Person result = personRepository.save(person);
        return new Result(Status.UPDATED, result);
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

package com.ote.test.controller;

import com.ote.test.aop.Traceable;
import com.ote.test.model.Person;
import com.ote.test.model.PersonParameter;
import com.ote.test.service.persistence.IEntityPersistenceService;
import com.ote.test.service.persistence.IEntityPersistenceService.Result;
import com.ote.test.service.persistence.IEntityPersistenceService.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.function.Supplier;

@RestController
@RequestMapping("/person")
@Slf4j
public class PersonController {

    @Autowired
    private IEntityPersistenceService<Person, Person.Key> personPersistenceService;

    @Traceable
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Page<Person>> getAll(PersonParameter parameter, Pageable pageRequest) {

        return get(() -> personPersistenceService.findAll(parameter, pageRequest));
    }

    @Traceable
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    public ResponseEntity<Person> get(@PathVariable(name = "id") Integer id) {

        return get(() -> personPersistenceService.findOne(new Person.Key(id)));
    }

    private <T> ResponseEntity<T> get(Supplier<Optional<T>> supplier) {

        Optional<T> result = supplier.get();

        return result.isPresent() ?
                new ResponseEntity<>(result.get(), HttpStatus.FOUND) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Traceable
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Integer id) {

        return delete(() -> personPersistenceService.delete(new Person.Key(id)));
    }

    @Traceable
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteAll() {

        return delete(() -> personPersistenceService.deleteAll());
    }

    private ResponseEntity<Void> delete(Supplier<Status> supplier) {

        Status status = supplier.get();

        switch (status) {
            case DELETED:
                return new ResponseEntity<>((Void) null, HttpStatus.NO_CONTENT);
            default:
                return new ResponseEntity<>((Void) null, HttpStatus.NOT_FOUND);
        }
    }

    @Traceable
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Person> create(@RequestBody Person person) {

        //person.setKey(new Person.Key());
        Result<Person> result = personPersistenceService.create(person);

        switch (result.getStatus()) {
            case CREATED:
                return new ResponseEntity<>(result.getEntity(), HttpStatus.CREATED);
            default:
                return new ResponseEntity<>((Person) null, HttpStatus.BAD_REQUEST);
        }
    }

    @Traceable
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    @ResponseBody
    public ResponseEntity<Person> update(@PathVariable(name = "id") Integer id,
                                         @RequestBody Person person) {

        return updateOrPatch(() -> {
            person.setKey(new Person.Key(id));
            return personPersistenceService.update(person);
        });
    }

    @Traceable
    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    @ResponseBody
    public ResponseEntity<Person> patch(@PathVariable(name = "id") Integer id,
                                        @RequestBody Person person) {

        return updateOrPatch(() -> {
            person.setKey(new Person.Key(id));
            return personPersistenceService.partialUpdate(person);
        });
    }

    private ResponseEntity<Person> updateOrPatch(Supplier<Result<Person>> supplier) {

        Result<Person> result = supplier.get();

        switch (result.getStatus()) {
            case UPDATED:
                return new ResponseEntity<>(result.getEntity(), HttpStatus.OK);
            case NOT_FOUND:
                return new ResponseEntity<>((Person) null, HttpStatus.NOT_FOUND);
            default:
                return new ResponseEntity<>((Person) null, HttpStatus.BAD_REQUEST);
        }
    }
}

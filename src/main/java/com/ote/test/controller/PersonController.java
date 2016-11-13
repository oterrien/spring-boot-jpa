package com.ote.test.controller;

import com.ote.test.aop.Traceable;
import com.ote.test.model.Person;
import com.ote.test.model.PersonParameter;
import com.ote.test.service.persistence.IPersonPersistenceService;
import com.ote.test.service.persistence.IPersonPersistenceService.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/person")
@Slf4j
public class PersonController {

    @Autowired
    private IPersonPersistenceService personPersistenceService;

    @Traceable
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Page<Person>> getAll(PersonParameter parameter,
                                               Pageable pageRequest) {

        log.info(pageRequest.toString());

        Optional<Page<Person>> persons = personPersistenceService.findAll(parameter, pageRequest);
        //Optional<Page<Person>> persons = personPersistenceService.findAll(pageRequest);

        return persons.isPresent() ?
                new ResponseEntity<>(persons.get(), HttpStatus.FOUND) :
                new ResponseEntity<>((Page<Person>) null, HttpStatus.NOT_FOUND);
    }

    @Traceable
    @RequestMapping(method = RequestMethod.GET,
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Person> get(@PathVariable(name = "id") Integer id) {

        Optional<Person> person = personPersistenceService.findOne(id);

        return person.isPresent() ?
                new ResponseEntity<>(person.get(), HttpStatus.FOUND) :
                new ResponseEntity<>((Person) null, HttpStatus.NOT_FOUND);

    }

    @Traceable
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Integer id) {

        Status status = personPersistenceService.delete(id);

        switch (status) {
            case DELETED:
                return new ResponseEntity<>((Void) null, HttpStatus.NO_CONTENT);
            default:
                return new ResponseEntity<>((Void) null, HttpStatus.NOT_FOUND);
        }
    }

    @Traceable
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteAll() {

        Status status = personPersistenceService.deleteAll();

        switch (status) {
            case DELETED:
                return new ResponseEntity<>((Void) null, HttpStatus.NO_CONTENT);
            default:
                return new ResponseEntity<>((Void) null, HttpStatus.NOT_FOUND);
        }    }

    @Traceable
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Person> update(@PathVariable(name = "id") Integer id,
                                         @RequestBody Person person) {

        person.setId(id);
        IPersonPersistenceService.Result result = personPersistenceService.update(person);

        switch (result.getStatus()) {
            case UPDATED:
                return new ResponseEntity<>(result.getPerson(), HttpStatus.OK);
            case NOT_FOUND:
                return new ResponseEntity<>((Person) null, HttpStatus.NOT_FOUND);
            default:
                return new ResponseEntity<>((Person) null, HttpStatus.BAD_REQUEST);
        }
    }

    @Traceable
    @RequestMapping(method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Person> create(@RequestBody Person person) {

        person.setId(null);
        IPersonPersistenceService.Result result = personPersistenceService.create(person);

        switch (result.getStatus()) {
            case CREATED:
                return new ResponseEntity<>(result.getPerson(), HttpStatus.CREATED);
            default:
                return new ResponseEntity<>((Person) null, HttpStatus.BAD_REQUEST);
        }


    }

    @Traceable
    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Person> patch(@PathVariable(name = "id") Integer id,
                                        @RequestBody Person personSample) {

        personSample.setId(id);
        IPersonPersistenceService.Result result = personPersistenceService.patch(personSample);

        switch (result.getStatus()) {
            case UPDATED:
                return new ResponseEntity<>(result.getPerson(), HttpStatus.OK);
            case NOT_FOUND:
                return new ResponseEntity<>((Person) null, HttpStatus.NOT_FOUND);
            default:
                return new ResponseEntity<>((Person) null, HttpStatus.BAD_REQUEST);
        }
    }
}

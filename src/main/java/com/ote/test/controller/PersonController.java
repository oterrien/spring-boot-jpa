package com.ote.test.controller;

import com.ote.test.aop.Traceable;
import com.ote.test.model.Clause;
import com.ote.test.model.Person;
import com.ote.test.service.persistence.IPersonPersistenceService;
import com.ote.test.service.persistence.IPersonPersistenceService.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private IPersonPersistenceService personPersistenceService;



    @RequestMapping(method = RequestMethod.POST)
    public void test(Specification spec) {

        System.out.println(spec);
    }



    @Traceable
    @RequestMapping(method = RequestMethod.GET, params = {"sortingBy", "sortingDirection"})
    public ResponseEntity<Page<Person>> getAll(@RequestParam(name = "sortingBy", required = false, defaultValue = "id") String sortingBy,
                                               @RequestParam(name = "sortingDirection", required = false, defaultValue = "ASC") String sortingDirection,
                                               Pageable pageRequest) {

        Optional<Page<Person>> persons = personPersistenceService.findAll(sortingBy, sortingDirection, pageRequest);
        HttpStatus status = persons.isPresent() ? HttpStatus.FOUND : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(persons.orElse(null), status);
    }

    @Traceable
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Person> get(@PathVariable(name = "id") Integer id) {

        Optional<Person> person = personPersistenceService.findOne(id);
        HttpStatus status = person.isPresent() ? HttpStatus.FOUND : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(person.orElse(null), status);
    }

    @Traceable
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Integer id) {

        Status status = personPersistenceService.delete(id);

        HttpStatus httpStatus;

        switch (status) {
            case DELETED:
                httpStatus = HttpStatus.NO_CONTENT;
                break;
            default:
                httpStatus = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>((Void) null, httpStatus);
    }

    @Traceable
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteAll() {

        Status status = personPersistenceService.deleteAll();

        HttpStatus httpStatus;

        switch (status) {
            case DELETED:
                httpStatus = HttpStatus.NO_CONTENT;
                break;
            default:
                httpStatus = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>((Void) null, httpStatus);
    }

    @Traceable
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Person> createOrUpdate(@RequestParam(name = "id", required = false) Integer id,
                                                 @RequestBody Person person) {

        Status status = personPersistenceService.createOrUpdate(Optional.ofNullable(id), person);

        HttpStatus httpStatus;

        switch (status) {
            case CREATED:
                httpStatus = HttpStatus.CREATED;
                break;
            case UPDATED:
                httpStatus = HttpStatus.OK;
                break;
            default:
                httpStatus = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(person, httpStatus);
    }

    @Traceable
    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    public ResponseEntity<Person> partialUpdate(@PathVariable(name = "id") Integer id,
                                                @RequestBody Person person) {

        person.setId(id);
        Status status = personPersistenceService.patch(person);

        HttpStatus httpStatus;

        switch (status) {
            case UPDATED:
                httpStatus = HttpStatus.OK;
                break;
            default:
                httpStatus = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(person, httpStatus);
    }

}
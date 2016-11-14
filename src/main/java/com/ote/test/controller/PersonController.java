package com.ote.test.controller;

import com.ote.test.aop.Traceable;
import com.ote.test.model.Person;
import com.ote.test.model.PersonParameter;
import com.ote.test.service.persistence.IEntityPersistenceService;
import com.ote.test.service.persistence.IEntityPersistenceService.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/persons")
@Slf4j
public class PersonController extends EntityController<Person, Person.Key>{

    public PersonController(@Autowired IEntityPersistenceService<Person, Person.Key> personPersistenceService){
        super(personPersistenceService);
    }

    @Traceable
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Page<Person>> getMany(PersonParameter parameter, Pageable pageRequest) {

        return getMany(() -> entityPersistenceService.findMany(parameter, pageRequest));
    }

    @Traceable
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    public ResponseEntity<Person> get(@PathVariable(name = "id") Integer id) {

        return getOne(() -> entityPersistenceService.findOne(new Person.Key(id)));
    }

    @Traceable
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Integer id) {

        return delete(() -> entityPersistenceService.delete(new Person.Key(id)));
    }

    @Traceable
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteAll() {

        return delete(() -> entityPersistenceService.deleteAll());
    }

    @Traceable
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Person> create(@RequestBody Person person) {

        Result<Person> result = entityPersistenceService.create(person);

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
            return entityPersistenceService.update(person);
        });
    }

    @Traceable
    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    @ResponseBody
    public ResponseEntity<Person> patch(@PathVariable(name = "id") Integer id,
                                        @RequestBody Person person) {

        return updateOrPatch(() -> {
            person.setKey(new Person.Key(id));
            return entityPersistenceService.partialUpdate(person);
        });
    }
}

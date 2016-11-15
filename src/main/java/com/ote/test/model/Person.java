package com.ote.test.model;

import com.ote.test.service.persistence.KeyGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "PERSON_INFO")
@Data
@NoArgsConstructor
public class Person implements IEntity<Person.Key> {

    @EmbeddedId
    @GenericGenerator(name = "Person", strategy = KeyGenerator.NAME, parameters = {
            @Parameter(name = KeyGenerator.PARAMETER_STRUCTURE_NAME, value = "S_PERSON_INFO"),
            @Parameter(name = KeyGenerator.PARAMETER_ENTITY_CLASS_NAME, value = "com.ote.test.model.Person")
    })
    @GeneratedValue(generator = "Person")
    private Key key;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Key implements IEntity.Key {

        @Column(name = "ID", nullable = false)
        private Integer id;
    }

    @Override
    public Person.Key newKey() {
        return new Key();
    }

    @Override
    public boolean isEmpty(Person.Key key) {
        return (key.getId() == null);
    }

    @Override
    public void populateKey(Person.Key key, Object id) {
        key.setId((Integer) id);
    }
}

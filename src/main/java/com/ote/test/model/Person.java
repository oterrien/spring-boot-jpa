package com.ote.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "PERSON_INFO")
@Data
@NoArgsConstructor
public class Person implements IEntity<Person.Key> {

    public static final String GENERATOR_NAME = "seq_id";

    //@GenericGenerator(name = GENERATOR_NAME, strategy = Key.GENERATOR_CLASS)
    //@GeneratedValue(generator = GENERATOR_NAME)
    @EmbeddedId
    private Key key = new Key();

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Key implements IEntity.Key {

        @Column(name = "ID")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
    }

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;
}
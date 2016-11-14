package com.ote.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.engine.spi.SessionImplementor;

import javax.persistence.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Entity
@Table(name = "PERSON_INFO")
@Data
@NoArgsConstructor
public class Person implements IEntity<Person.Key> {

    public static final String GENERATOR_NAME = "seq_id";

    @EmbeddedId
    @GenericGenerator(name = GENERATOR_NAME, strategy = "com.ote.test.model.Person$KeyGenerator")
    @GeneratedValue(generator = GENERATOR_NAME)
    private Key key;

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Key implements IEntity.Key {

        @Column(name = "ID", nullable = false)
        private Integer id;
    }

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @NoArgsConstructor
    public static class KeyGenerator extends IdentifierGenerator<Person, Person.Key> {

        @Override
        protected Class<Person> getEntityClass() {
            return Person.class;
        }

        @Override
        protected Person.Key newKey() {
            return new Person.Key();
        }

        @Override
        protected boolean isEmpty(Person.Key key) {
            return key.getId() == null;
        }

        @Override
        protected void populateKey(Key key, SessionImplementor session) {
            key.setId(getNextId(session));
        }

        private int getNextId(SessionImplementor session) {

            Connection connection = session.connection();
            try {
                PreparedStatement ps = connection.prepareStatement("SELECT S_PERSON_INFO.nextval from dual");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
            throw new IllegalStateException("Can't get id from sequence");
        }
    }
}

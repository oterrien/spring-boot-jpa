package com.ote.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import javax.persistence.*;
import java.io.Serializable;
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

    public static class KeyGenerator implements IdentifierGenerator {

        @Override
        public Serializable generate(SessionImplementor session, Object object) throws HibernateException {

            if (!(object instanceof Person)) {
                return null;
            }

            Person entity = (Person) object;
            Person.Key id = entity.getKey();

            if (id == null) {
                id = new Person.Key();
            }

            if (id.getId() == null) {
                id.setId(getNextId(session));
            }

            return id;
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

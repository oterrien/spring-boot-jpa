package com.ote.test.service.generator;

import com.ote.test.aop.Traceable;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Profile("mysql")
@Service
public class MySqlIdGeneratorService implements IIDGeneratorService {

    @Traceable
    public Integer generateId(SessionImplementor session, String structureName) {

        Connection connection = session.connection();

        try {
            UUID id = UUID.randomUUID();
            PreparedStatement ps = connection.prepareStatement(String.format("INSERT INTO %s (UID) VALUES (?)", structureName));
            ps.setString(1, id.toString());
            ps.execute();

            ps = connection.prepareStatement(String.format("SELECT ID FROM %s WHERE UID=?", structureName));
            ps.setString(1, id.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        throw new IllegalStateException("Can't get id from auto_increment table " + structureName);
    }
}

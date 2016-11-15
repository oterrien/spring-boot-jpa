package com.ote.test.service.generator;

import com.ote.test.aop.Traceable;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Profile("oracle")
@Service(IIdGeneratorService.ID_GENERATOR_BEAN_NAME)
public class OracleIdGeneratorService implements IIdGeneratorService {

    @Traceable
    public Integer generateId(SessionImplementor session, String structureName) {

        Connection connection = session.connection();

        try {
            PreparedStatement ps = connection.prepareStatement(String.format("SELECT %s.NEXTVAL FROM DUAL", structureName));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        throw new IllegalStateException("Can't get id from sequence " + structureName);
    }
}

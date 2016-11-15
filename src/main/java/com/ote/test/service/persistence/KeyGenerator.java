package com.ote.test.service.persistence;

import com.ote.test.ApplicationContextProvider;
import com.ote.test.model.IEntity;
import com.ote.test.service.generator.IIdGeneratorService;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

/**
 * This class is instantiated with the stream
 * ApplicationContextProvider.getContext() helps to retrieve a bean which would be loaded for the current profile
 */
public class KeyGenerator implements IdentifierGenerator, Configurable {

    public final static String NAME = "com.ote.test.service.persistence.KeyGenerator";
    public final static String PARAMETER_STRUCTURE_NAME = "structureName";
    public final static String PARAMETER_ENTITY_CLASS_NAME = "entityClassName";

    private String structureName;
    private Class<? extends IEntity> entityClass;

    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {

        if (!this.entityClass.isInstance(object)) {
            return null;
        }

        return generate(session, this.entityClass.cast(object));
    }

    private <TE extends IEntity<TK>, TK extends IEntity.Key> TK generate(SessionImplementor session, TE entity) throws HibernateException {

        TK key = entity.getKey();

        if (key == null) {
            key = entity.newKey();
        }

        if (entity.isEmpty(key)) {
            entity.populateKey(key, nextId(session));
        }

        return key;
    }

    private final Integer nextId(SessionImplementor session) {
        IIdGeneratorService keyGenerator = (IIdGeneratorService) ApplicationContextProvider.getContext().getBean(IIdGeneratorService.ID_GENERATOR_BEAN_NAME);
        return keyGenerator.generateId(session, structureName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
        this.structureName = properties.getProperty(PARAMETER_STRUCTURE_NAME);
        try {
            this.entityClass = (Class<IEntity>) Class.forName(properties.getProperty(PARAMETER_ENTITY_CLASS_NAME));
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new MappingException(e);
        }
    }


}

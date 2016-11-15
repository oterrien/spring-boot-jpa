package com.ote.test.service.generator;

import org.hibernate.engine.spi.SessionImplementor;

@FunctionalInterface
public interface IIDGeneratorService {

    Integer generateId(SessionImplementor session, String structureName);

    String ID_GENERATOR_BEAN_NAME = "idGenerator";
}
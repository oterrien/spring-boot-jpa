package com.ote.test.model;

public interface IKeyGenerator<TK extends IEntity.Key> {

    TK newKey();

    boolean isEmpty(TK key);

    void populateKey(TK key, Object id);
}
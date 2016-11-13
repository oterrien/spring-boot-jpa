package com.ote.test.model;

import java.io.Serializable;

public interface IEntity<TK extends IEntity.Key> extends Serializable{

    TK getKey();
    void setKey(TK key);

    interface Key extends Serializable {

    }
}

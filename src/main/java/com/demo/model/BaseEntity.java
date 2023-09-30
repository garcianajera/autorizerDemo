package com.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

//All entities that needs to be stored using repository have to extend from this class
public abstract class BaseEntity implements Cloneable {

    @JsonIgnore
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}

package model.services;

import java.io.Serializable;

public abstract class Service implements Serializable, Cloneable {

    private final long id;
    String name;

    Service(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public abstract String getType();
}

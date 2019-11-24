package model.services;

import java.io.Serializable;
import java.util.Date;

public abstract class Service implements Serializable, Cloneable {

    private static int counter = 0;

    private String name;
    private final int objectID = counter++; // (^_^)

    Service(String name) {
        this.name = name;
    }

    Service(Service service) {
        this.name = service.name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    abstract public String getType();

    public int getObjectID() {
        return objectID;
    }

    @Override
    public String toString() {
        return "Service{" +
                "name='" + name + "\'}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Service))
            return false;
        return this.name.equals(((Service) obj).name);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

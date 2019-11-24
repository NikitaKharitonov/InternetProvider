package model.services;

import java.io.Serializable;
import java.util.Date;

public abstract class Service implements Serializable, Cloneable {

    enum Status{ON, OFF}

    private static int counter = 0;

    private String name;
    private Date activationDate;
    private Status status;
    private final int objectID = counter++; // (^_^)

    Service(String name, Date activationDate, Status status) {
        this.name = name;
        this.status = status;
        this.activationDate = activationDate;
    }

    Service(Service service) {
        this.name = service.name;
        this.status = service.status;
        this.activationDate = service.activationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    abstract public String getType();

    public int getObjectID() {
        return objectID;
    }

    @Override
    public String toString() {
        return "Service{" +
                "name='" + name + '\'' +
                ", activationDate=" + activationDate +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Service))
            return false;
        return this.status == ((Service) obj).status
                && this.activationDate == ((Service) obj).activationDate
                && this.name.equals(((Service) obj).name);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

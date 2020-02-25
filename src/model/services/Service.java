package model.services;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.Serializable;
import java.util.Date;

public abstract class Service implements Serializable, Cloneable {

    public enum Status{PLANNED, ACTIVE, DISCONNECTED, SUSPENDED};

    final long id;
    final Date activationDate;
    Status status;

    Service(long id, Date activationDate, Status status) {
        this.id = id;
        this.activationDate = activationDate;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public abstract String getType();

}

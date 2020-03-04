package model.services;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.Serializable;
import java.util.Date;

public abstract class Service implements Serializable, Cloneable {

    public enum Status{PLANNED, ACTIVE, DISCONNECTED, SUSPENDED};

    final long id;
    final Date activationDate;
    final Date dateBegin;
    final Date dateEnd;
    Status status;

    Service(long id, Date activationDate, Date dateBegin, Date dateEnd, Status status) {
        this.id = id;
        this.activationDate = activationDate;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
        this.status = status;
    }

    Service(Date activationDate, Date dateBegin, Date dateEnd, Status status) {
        this.id = 0;
        this.activationDate = activationDate;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public Date getDateBegin() {
        return dateBegin;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public abstract String getType();

}

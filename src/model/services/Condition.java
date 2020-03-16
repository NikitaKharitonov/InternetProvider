package model.services;

import java.util.Date;

public class Condition<T extends Service> {

    public enum Status {
        SUSPENDED, ACTIVE, DISCONNECTED
    }

    private final long id;
    private final Date dateBegin;
    private final Date dateEnd;
    private final Status status;
    private final T service;

    public Condition(long id, Date dateBegin, Date dateEnd, Status status, T service) {
        this.id = id;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
        this.status = status;
        this.service = service;
    }

    public Condition(Date dateBegin, Date dateEnd, Status status, T service) {
        this.id = 0;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
        this.status = status;
        this.service = service;
    }

    public long getId() {
        return id;
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

    public T getService() {
        return service;
    }

    @Override
    public String toString() {
        return "Condition{" +
                "id=" + id +
                ", dateBegin=" + dateBegin +
                ", dateEnd=" + dateEnd +
                ", status=" + status +
                ", service=" + service.toString() +
                '}';
    }
}

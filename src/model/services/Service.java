package model.services;

import java.io.Serializable;
import java.util.Date;

public abstract class Service implements Serializable, Cloneable {
    private String name;
    private Date activationDate;
    private int status;

    Service(String name, Date activationDate, Integer status) {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    abstract public String getType();
    abstract public int getId();

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

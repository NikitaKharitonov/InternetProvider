package model.services;

import java.io.Serializable;
import java.util.Date;

public abstract class Service implements Serializable {
    private String name;
    private Date activationDate;
    private int status;

    Service(String name, Date activationDate, Integer status) {
        this.name = name;
        this.status = status;
        this.activationDate = activationDate;
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

    @Override
    public String toString() {
        return "Service{" +
                "name='" + name + '\'' +
                ", activationDate=" + activationDate +
                ", status=" + status +
                '}';
    }
}

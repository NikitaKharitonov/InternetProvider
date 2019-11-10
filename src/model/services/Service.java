package model.services;

import java.io.Serializable;
import java.util.Date;

public class Service implements Serializable {
    private String name;
    private Date activationDate;
    private int status;

    Service(String name, Date activationDate, int status) {
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

    public void setStatus(int status) {
        this.status = status;
    }

    public String toString() {
        return "name: " + name + " activation date: " + activationDate.toString() + " status: " + status;
    }
}

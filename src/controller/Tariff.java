package controller;

import model.services.Service;

public class Tariff {

    private Service service;
    private int id;
    private boolean isEdited;

    Tariff(Service service) {
        this.service = service;
        this.isEdited = false;
    }

    public void setService(Service service) {
        this.service = service;
        isEdited = true;
    }
    // copying ctr
    public Service getService() {
        return service;
    }

    public Integer getId() {
        return id;
    }

    public boolean isEdited() {
        return isEdited;
    }
}

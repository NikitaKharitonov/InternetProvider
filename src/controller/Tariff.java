package controller;

import model.services.Service;
/**
 * Class Tariff represents tariff for View and controlling if it change
 * @author anteii
 * @version 0.1
 * */
public class Tariff {
    /**
     * Service object
     * */
    private Service service;
    /**
     * Service id
     * */
    private int id;
    /**
     * Flag if service was changed
     * */
    private boolean isEdited;
    /**
     * Package-Private constructor
     * @param service
     *          Service object wich will be aggregated
     * */
    Tariff(Service service, int id) {
        this.service = service;
        this.id = id;
        this.isEdited = false;
    }
    /**
     * Default setter for service field
     * It also set up flag
     * */
    public void setService(Service service) {
        this.service = service;
        isEdited = true;
    }
    /**
     *@return clone of service object
     * */
    public Service getService() throws CloneNotSupportedException {
        return (Service) service.clone();
    }

    public Integer getId() {
        return id;
    }

    public boolean isEdited() {
        return isEdited;
    }
}

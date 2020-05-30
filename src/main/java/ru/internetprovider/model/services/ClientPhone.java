package ru.internetprovider.model.services;

import java.util.Date;
import java.util.List;

public class ClientPhone implements ClientService {

    private long id;
    private final Date activationDate;
    private ClientService.Status status;
    private List<Phone> serviceList; // history


    public ClientPhone(long id, Date activationDate, ClientService.Status status) {
        this.id = id;
        this.activationDate = activationDate;
        this.status = status;
    }

    public ClientPhone(Date activationDate, ClientService.Status status) {
        this.activationDate = activationDate;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public ClientService.Status getStatus() {
        return status;
    }

    public void setStatus(ClientService.Status status) {
        this.status = status;
    }

    public List<Phone> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<Phone> serviceList) {
        this.serviceList = serviceList;
    }

}

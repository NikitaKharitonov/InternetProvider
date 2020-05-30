package ru.internetprovider.model.services;

import java.util.Date;
import java.util.List;

public class ClientInternet implements ClientService {

    private long id;
    private final Date activationDate;
    private ClientService.Status status;
    private List<Internet> serviceList; // history


    public ClientInternet(long id, Date activationDate, ClientService.Status status) {
        this.id = id;
        this.activationDate = activationDate;
        this.status = status;
    }

    public ClientInternet(Date activationDate, ClientService.Status status) {
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

    public List<Internet> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<Internet> serviceList) {
        this.serviceList = serviceList;
    }

}

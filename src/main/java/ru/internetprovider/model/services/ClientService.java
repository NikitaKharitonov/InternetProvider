package ru.internetprovider.model.services;

import java.util.Date;
import java.util.List;

public interface ClientService {
    enum Status {
        SUSPENDED, ACTIVE, DISCONNECTED
    }

    List<? extends Service> getServiceList();

    Status getStatus();

    long getId();

    Date getActivationDate();

//    void setServiceList(List<Service> serviceList);
}

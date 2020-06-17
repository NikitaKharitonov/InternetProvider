package ru.internetprovider.model.services;

import java.util.Date;

public interface ClientService {

    int getId();

    void setId(int id);

    int getClientId();

    void setClientId(int clientId);

    Date getActivationDate();

    void setActivationDate(Date activationDate);

    Status getStatus();

    void setStatus(Status status);
}

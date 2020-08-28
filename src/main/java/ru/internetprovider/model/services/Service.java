package ru.internetprovider.model.services;

import java.util.Date;
import java.util.List;

public interface Service<T> {

    int getId();

    void setId(int id);

    int getClientId();

    void setClientId(int clientId);

    Date getActivationDate();

    void setActivationDate(Date activationDate);

    Status getStatus();

    void setStatus(Status status);

    List<T> getHistory();

    void setHistory(List<T> history);
}

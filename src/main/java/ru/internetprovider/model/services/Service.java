package ru.internetprovider.model.services;

import java.util.Date;
import java.util.Deque;
import java.util.List;

/**
 * An abstract service which belongs to one and only one
 * client. The service's specification is stored in an ServiceSpecification list,
 * the so-called "history". This list contains the service's current specification
 * as well as all its former specifications which the service once had.
 * @param <T> a service specification implementation corresponding to this interface
 *           implementation.
 */

public interface Service<T extends ServiceSpecification> {

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

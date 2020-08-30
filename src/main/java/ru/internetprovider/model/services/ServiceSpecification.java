package ru.internetprovider.model.services;

import java.util.Date;

/**
 * A specification of an abstract service.
 * The implementations of this interface contain the parameters
 * that may be changed by a user.
 */

public interface ServiceSpecification {
    int getId();
    void setId(int id);
    Date getBeginDate();
    void setBeginDate(Date date);
    Date getEndDate();
    void setEndDate(Date date);
}

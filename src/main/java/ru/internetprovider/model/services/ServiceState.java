package ru.internetprovider.model.services;

import java.util.Date;

/**
 * A temporal state of an abstract service.
 */

public interface ServiceState {
    int getId();
    void setId(int id);
    Date getBeginDate();
    void setBeginDate(Date date);
    Date getEndDate();
    void setEndDate(Date date);
}

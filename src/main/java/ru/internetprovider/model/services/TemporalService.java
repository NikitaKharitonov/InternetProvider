package ru.internetprovider.model.services;

import java.util.Date;

public interface TemporalService {
    int getId();
    void setId(int id);
    Date getBeginDate();
    void setBeginDate(Date date);
    Date getEndDate();
    void setEndDate(Date date);
}

package ru.internetprovider.model.services;

import java.util.Date;

public abstract class Service {

    private final long id;
    private final Date beginDate;
    private final Date endDate;

    public Service(Date dateBegin, Date dateEnd) {
        this.id = 0;
        this.beginDate = dateBegin;
        this.endDate = dateEnd;
    }

    public long getId() {
        return id;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}

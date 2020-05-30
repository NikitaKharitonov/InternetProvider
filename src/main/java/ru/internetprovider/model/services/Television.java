package ru.internetprovider.model.services;

import java.util.Date;

public class Television implements Service {

    private final long id;
    private final Date beginDate;
    private final Date endDate;

    private final int channelsCount;

    public Television(Date dateBegin, Date dateEnd, int channelsCount) {
        this.id = 0;
        this.beginDate = dateBegin;
        this.endDate = dateEnd;
        this.channelsCount = channelsCount;
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

    public int getChannelsCount() {
        return channelsCount;
    }

    @Override
    public String toString() {
        return "Television{" +
                "channelsCount=" + channelsCount +
                '}';
    }
}
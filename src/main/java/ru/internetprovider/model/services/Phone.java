package ru.internetprovider.model.services;

import java.util.Date;

public class Phone implements Service {

    private final long id;
    private final Date beginDate;
    private final Date endDate;

    private final int minsCount;
    private final int smsCount;

    public Phone(Date dateBegin, Date dateEnd, int minsCount, int smsCount) {
        this.id = 0;
        this.beginDate = dateBegin;
        this.endDate = dateEnd;
        this.minsCount = minsCount;
        this.smsCount = smsCount;
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

    public int getMinsCount() {
        return minsCount;
    }

    public int getSmsCount() {
        return smsCount;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "minsCount=" + minsCount +
                ", smsCount=" + smsCount +
                '}';
    }
}
package ru.internetprovider.model.services;

import java.util.Date;

public class Phone extends Service {

    private final int minsCount;
    private final int smsCount;

    public Phone(Date dateBegin, Date dateEnd, int minsCount, int smsCount) {
        super(dateBegin, dateEnd);
        this.minsCount = minsCount;
        this.smsCount = smsCount;
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
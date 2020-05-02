package ru.internetprovider.model.services;

import java.util.Date;

public class Television extends Service {

    private final int channelsCount;

    public Television(Date dateBegin, Date dateEnd, int channelsCount) {
        super(dateBegin, dateEnd);
        this.channelsCount = channelsCount;
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
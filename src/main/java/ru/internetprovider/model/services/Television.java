package ru.internetprovider.model.services;

public class Television implements Service {

    private final int channelsCount;

    public Television(int channelsCount) {
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
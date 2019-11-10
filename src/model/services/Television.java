package model.services;

import java.util.Date;

public class Television extends Service {
    private int numberOfChannels;

    public Television(String name, Date activationDate, int status, int numberOfChannels) {
        super(name, activationDate, status);
        this.numberOfChannels = numberOfChannels;
    }

    public int getNumberOfChannels() {
        return numberOfChannels;
    }

    public void setNumberOfChannels(int numberOfChannels) {
        this.numberOfChannels = numberOfChannels;
    }

    @Override
    public String toString() {
        return super.toString() + ": Television{" +
                "numberOfChannels=" + numberOfChannels +
                '}';
    }
}

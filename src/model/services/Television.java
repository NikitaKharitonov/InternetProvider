package model.services;

import util.Annotations.MethodParameter;

import java.util.Date;

public class Television extends Service {
    private int numberOfChannels;

    public Television(
            @MethodParameter(name = "name", type = String.class)
            String name,
            @MethodParameter(name = "activationDate", type = Date.class)
            Date activationDate,
            @MethodParameter(name = "status", type = Integer.class)
            Integer status,
            @MethodParameter(name = "numberOfChannels", type = Integer.class)
            Integer numberOfChannels ) {

        super(name, activationDate, status);
        this.numberOfChannels = numberOfChannels;
    }

    public int getNumberOfChannels() {
        return numberOfChannels;
    }

    public void setNumberOfChannels(Integer numberOfChannels) {
        this.numberOfChannels = numberOfChannels;
    }

    @Override
    public String toString() {
        return super.toString() + ": Television{" +
                "numberOfChannels=" + numberOfChannels +
                '}';
    }
}

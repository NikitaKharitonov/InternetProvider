package model.services;

import util.Annotations.MethodParameter;

import java.util.Date;

public class Television extends Service {

    private int numberOfChannels;

    public Television(
            @MethodParameter(name = "id", type = Long.class)
                    Long id,
            @MethodParameter(name = "name", type = String.class)
                    String name,
            @MethodParameter(name = "numberOfChannels", type = Integer.class)
                    Integer numberOfChannels ) {
        super(id, name);
        this.numberOfChannels = numberOfChannels;
    }

    public int getNumberOfChannels() {
        return numberOfChannels;
    }

    public void setNumberOfChannels(Integer numberOfChannels) {
        this.numberOfChannels = numberOfChannels;
    }

    @Override
    public String getType() {
        return "Television";
    }

    @Override
    public String toString() {
        return "Television{" +
                "name=" + name +
                ", numberOfChannels=" + numberOfChannels +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(!super.equals(obj))
            return false;
        if (!(obj instanceof Television))
            return false;
        return this.numberOfChannels == ((Television) obj).numberOfChannels;
    }
}

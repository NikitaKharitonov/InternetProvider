package model.services;

import java.util.Date;

public class Television extends Service {

    private int numberOfChannels;

    public Television(Long id, Date activationDate, Status status, Integer numberOfChannels ) {
        super(id, activationDate, status);
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
                "numberOfChannels=" + numberOfChannels +
                ", id=" + id +
                ", activationDate=" + activationDate +
                ", status=" + status +
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

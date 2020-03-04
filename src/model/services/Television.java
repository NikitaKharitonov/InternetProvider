package model.services;

import java.util.Date;

public class Television extends Service {

    private int channelsCount;

    public Television(
            Long id,
            Date activationDate,
            Date dateBegin,
            Date dateEnd,
            Status status,
            Integer channelsCount
    ) {
        super(id, activationDate, dateBegin, dateEnd, status);
        this.channelsCount = channelsCount;
    }

    public Television(
            Date activationDate,
            Date dateBegin,
            Date dateEnd,
            Status status,
            Integer channelsCount
    ) {
        super(activationDate, dateBegin, dateEnd, status);
        this.channelsCount = channelsCount;
    }

    public int getChannelsCount() {
        return channelsCount;
    }

    public void setChannelsCount(Integer channelsCount) {
        this.channelsCount = channelsCount;
    }

    @Override
    public String getType() {
        return "Television";
    }

    @Override
    public String toString() {
        return "Television{" +
                "channelsCount=" + channelsCount +
                ", id=" + id +
                ", activationDate=" + activationDate +
                ", dateBegin=" + dateBegin +
                ", dateEnd=" + dateEnd +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(!super.equals(obj))
            return false;
        if (!(obj instanceof Television))
            return false;
        return this.channelsCount == ((Television) obj).channelsCount;
    }
}

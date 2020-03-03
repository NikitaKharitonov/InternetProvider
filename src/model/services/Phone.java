package model.services;

import java.util.Date;

public class Phone extends Service {

    private int minsCount;
    private int smsCount;

    public Phone(
            Long id,
            Date activationDate,
            Date dateBegin,
            Date dateEnd,
            Status status,
            Integer minsCount,
            Integer smsCount
    ) {
        super(id, activationDate, dateBegin, dateEnd, status);
        this.minsCount = minsCount;
        this.smsCount = smsCount;
    }

    public int getMinsCount() {
        return minsCount;
    }

    public void setMinsCount(Integer minsCount) {
        this.minsCount = minsCount;
    }

    public int getSmsCount() {
        return smsCount;
    }

    public void setSmsCount(Integer smsCount) {
        this.smsCount = smsCount;
    }

    @Override
    public String getType() {
        return "Phone";
    }

    @Override
    public String toString() {
        return "Phone{" +
                "minsCount=" + minsCount +
                ", smsCount=" + smsCount +
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
        if (!(obj instanceof Phone))
            return false;
        return this.minsCount == ((Phone) obj).minsCount
                && this.smsCount == ((Phone) obj).smsCount;
    }
}

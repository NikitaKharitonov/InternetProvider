package model.services;

import java.util.Date;

public class Phone extends Service {

    private int callsMinCount;
    private int smsCount;

    public Phone(Long id, Date activationDate, Status status, Integer callsMinCount, Integer smsCount ) {
        super(id, activationDate, status);
        this.callsMinCount = callsMinCount;
        this.smsCount = smsCount;
    }

    public int getCallsMinCount() {
        return callsMinCount;
    }

    public void setCallsMinCount(Integer callsMinCount) {
        this.callsMinCount = callsMinCount;
    }

    public int getSmsCount() {
        return smsCount;
    }

    public void setSmsCount(Integer smsCount) {
        this.smsCount = smsCount;
    }

    @Override
    public String getType() {
        return getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return "Phone{" +
                "callsMinCount=" + callsMinCount +
                ", smsCount=" + smsCount +
                ", id=" + id +
                ", activationDate=" + activationDate +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(!super.equals(obj))
            return false;
        if (!(obj instanceof Phone))
            return false;
        return this.callsMinCount == ((Phone) obj).callsMinCount
                && this.smsCount == ((Phone) obj).smsCount;
    }
}

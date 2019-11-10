package model.services;

import java.util.Date;

public class Phone extends Service {
    private int callsMinCount;
    private int smsCount;

    public Phone(String name, Date activationDate, int status, int callsMinCount, int smsCount) {
        super(name, activationDate, status);
        this.callsMinCount = callsMinCount;
        this.smsCount = smsCount;
    }

    public int getCallsMinCount() {
        return callsMinCount;
    }

    public void setCallsMinCount(int callsMinCount) {
        this.callsMinCount = callsMinCount;
    }

    public int getSmsCount() {
        return smsCount;
    }

    public void setSmsCount(int smsCount) {
        this.smsCount = smsCount;
    }

    @Override
    public String toString() {
        return super.toString() + ": Phone{" +
                "callsMinCount=" + callsMinCount +
                ", smsCount=" + smsCount +
                '}';
    }
}

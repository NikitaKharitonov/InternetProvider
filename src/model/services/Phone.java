package model.services;

import util.Annotations.MethodParameter;

import java.util.Date;

public class Phone extends Service {
    private int callsMinCount;
    private int smsCount;

    public Phone(
            @MethodParameter(name = "name", type = String.class)
            String name,
            @MethodParameter(name = "activationDate", type = Date.class)
            Date activationDate,
            @MethodParameter(name = "status", type = Integer.class)
            Integer status,
            @MethodParameter(name = "callsMinCount", type = Integer.class)
            Integer callsMinCount,
            @MethodParameter(name = "smsCount", type = Integer.class)
            Integer smsCount ) {

        super(name, activationDate, status);
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
    public String toString() {
        return super.toString() + ": Phone{" +
                "callsMinCount=" + callsMinCount +
                ", smsCount=" + smsCount +
                '}';
    }
}

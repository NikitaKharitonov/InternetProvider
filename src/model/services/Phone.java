package model.services;

import util.Annotations.MethodParameter;

import java.util.Date;

public class Phone extends Service {

    private int callsMinCount;
    private int smsCount;

    public Phone(
            @MethodParameter(name = "name", type = String.class)
            String name,
            @MethodParameter(name = "callsMinCount", type = Integer.class)
            Integer callsMinCount,
            @MethodParameter(name = "smsCount", type = Integer.class)
            Integer smsCount ) {

        super(name);
        this.callsMinCount = callsMinCount;
        this.smsCount = smsCount;
    }

    public Phone(Phone phone) {
        super(phone);
        this.smsCount = phone.smsCount;
        this.callsMinCount = phone.callsMinCount;
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
        return "Phone";
    }

    @Override
    public String toString() {
        return super.toString() + ": Phone{" +
                "callsMinCount=" + callsMinCount +
                ", smsCount=" + smsCount +
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

    @Override
    public Object clone(){
        //deep copying
        return new Phone(this);
    }
}

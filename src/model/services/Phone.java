package model.services;

import util.Annotations.MethodParameter;

public class Phone extends Service {

    private int callsMinCount;
    private int smsCount;

    public Phone(
            @MethodParameter(name = "id", type = Long.class)
                    Long id,
            @MethodParameter(name = "name", type = String.class)
                    String name,
            @MethodParameter(name = "callsMinCount", type = Integer.class)
                    Integer callsMinCount,
            @MethodParameter(name = "smsCount", type = Integer.class)
                    Integer smsCount ) {
        super(id, name);
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
        return getClass().getSimpleName() + "{" +
                "name=" + name +
                ", callsMinCount=" + callsMinCount +
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
}

package model;

public class Phone {
    private int callsMinCount;
    private int smsCount;
    private String name;

    public Phone(int callsMinCount, int smsCount, String name) {
        this.callsMinCount = callsMinCount;
        this.smsCount = smsCount;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

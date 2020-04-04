package ru.internetprovider.model.services;

public class Phone implements Service {

    private final int minsCount;
    private final int smsCount;

    public Phone(int minsCount, int smsCount) {
        this.minsCount = minsCount;
        this.smsCount = smsCount;
    }

    public int getMinsCount() {
        return minsCount;
    }

    public int getSmsCount() {
        return smsCount;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "minsCount=" + minsCount +
                ", smsCount=" + smsCount +
                '}';
    }
}
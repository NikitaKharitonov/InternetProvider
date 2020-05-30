package ru.internetprovider.model.services;

import java.util.Date;

public class Internet implements Service {

    public enum ConnectionType {
        ADSL, DialUp, ISDN, Cable, Fiber
    }

    private final long id;
    private final Date beginDate;
    private final Date endDate;

    private final int speed;
    private final boolean antivirus;
    private final ConnectionType connectionType;

    public Internet(Date dateBegin, Date dateEnd, int speed, boolean antivirus, ConnectionType connectionType) {
        this.id = 0;
        this.beginDate = dateBegin;
        this.endDate = dateEnd;
        this.speed = speed;
        this.antivirus = antivirus;
        this.connectionType = connectionType;
    }

    public long getId() {
        return id;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isAntivirus() {
        return antivirus;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    @Override
    public String toString() {
        return "Internet{" +
                "speed=" + speed +
                ", antivirus=" + antivirus +
                ", connectionType=" + connectionType +
                '}';
    }
}
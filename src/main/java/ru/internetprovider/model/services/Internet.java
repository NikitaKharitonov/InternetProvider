package ru.internetprovider.model.services;

public class Internet implements Service {

    public enum ConnectionType {
        ADSL, DialUp, ISDN, Cable, Fiber
    }

    private final int speed;
    private final boolean antivirus;
    private final ConnectionType connectionType;

    public Internet(int speed, boolean antivirus, ConnectionType connectionType) {
        this.speed = speed;
        this.antivirus = antivirus;
        this.connectionType = connectionType;
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
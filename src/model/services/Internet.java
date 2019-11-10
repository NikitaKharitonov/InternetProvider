package model.services;

import java.util.Date;

public class Internet extends Service {
    private int speed;
    private boolean antivirus;
    private int connectionType;

    public Internet(String name, Date activationDate, int status, int speed, boolean antivirus, int connectionType) {
        super(name, activationDate, status);
        this.speed = speed;
        this.antivirus = antivirus;
        this.connectionType = connectionType;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isAntivirus() {
        return antivirus;
    }

    public void setAntivirus(boolean antivirus) {
        this.antivirus = antivirus;
    }

    public int getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(int connectionType) {
        this.connectionType = connectionType;
    }

    @Override
    public String toString() {
        return super.toString() + ": Internet{" +
                "speed=" + speed +
                ", antivirus=" + antivirus +
                ", connectionType=" + connectionType +
                '}';
    }
}

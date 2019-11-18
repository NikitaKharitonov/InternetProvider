package model.services;

import util.Annotations.MethodParameter;

import java.util.Date;

public class Internet extends Service {
    private int speed;
    private boolean antivirus;
    private int connectionType;

    public Internet(
            @MethodParameter(name = "name", type = String.class)
            String name,
            @MethodParameter(name = "activationDate", type = Date.class)
            Date activationDate,
            @MethodParameter(name = "status", type = Integer.class)
            Integer status,
            @MethodParameter(name = "speed", type = Integer.class)
            Integer speed,
            @MethodParameter(name = "antivirus", type = Boolean.class)
            Boolean antivirus,
            @MethodParameter(name = "connectionType", type = Integer.class)
            Integer connectionType ) {

        super(name, activationDate, status);
        this.speed = speed;
        this.antivirus = antivirus;
        this.connectionType = connectionType;
    }

    public Internet(Internet internet) {
        super(internet);
        this.speed = internet.speed;
        this.antivirus = internet.antivirus;
        this.connectionType = internet.connectionType;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public boolean isAntivirus() {
        return antivirus;
    }

    public void setAntivirus(Boolean antivirus) {
        this.antivirus = antivirus;
    }

    public int getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(Integer connectionType) {
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

    @Override
    public boolean equals(Object obj) {
        if(!super.equals(obj))
            return false;
        if (!(obj instanceof Internet))
            return false;
        return this.connectionType == ((Internet) obj).connectionType
                && this.antivirus == ((Internet) obj).antivirus
                && this.speed == ((Internet) obj).speed;
    }

    @Override
    public Object clone(){
        //deep copying
        return new Internet(this);
    }
}

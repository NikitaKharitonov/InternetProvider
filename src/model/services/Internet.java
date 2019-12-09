package model.services;

import model.ValueReader;
import util.Annotations.MethodParameter;

public class Internet extends Service {
    public enum ConnectionType{ADSL, Dial_up, ISDN, Cable, Fiber}

    private int speed;
    private boolean antivirus;
    private ConnectionType connectionType;

    public Internet(
            @MethodParameter(name = "id", type = Long.class)
                    Long id,
            @MethodParameter(name = "name", type = String.class)
                    String name,
            @MethodParameter(name = "speed", type = Integer.class)
                    Integer speed,
            @MethodParameter(name = "antivirus", type = Boolean.class)
                    Boolean antivirus,
            @MethodParameter(name = "connectionType", type = ConnectionType.class)
                    ConnectionType connectionType ) {
        super(id, name);
        this.speed = speed;
        this.antivirus = antivirus;
        this.connectionType = connectionType;
    }

    public Internet(String str) {
        super(str);
        speed = Integer.parseInt(ValueReader.nextValue());
        antivirus = Boolean.parseBoolean(ValueReader.nextValue());
        connectionType = ConnectionType.valueOf(ValueReader.nextValue());
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

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    @Override
    public String getType() {
        return getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " {id=" + id +
                ", name=" + name +
                ", speed=" + speed +
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
}

package model.services;

import model.util.ValueReader;
import org.w3c.dom.Element;
import util.Annotations.MethodParameter;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;


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

    public Internet(Element element) {
        super(element);
        this.speed = Integer.parseInt(element.getElementsByTagName("speed").item(0).getTextContent());
        this.antivirus = Boolean.parseBoolean(element.getElementsByTagName("antivirus").item(0).getTextContent());
        this.connectionType = ConnectionType.valueOf(element.getElementsByTagName("connection_type").item(0).getTextContent());
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

    public void toXML(XMLStreamWriter xMLStreamWriter) throws XMLStreamException {
        xMLStreamWriter.writeStartElement("service");
        xMLStreamWriter.writeCharacters("\n");

        xMLStreamWriter.writeStartElement("type");
        xMLStreamWriter.writeCharacters(getClass().getSimpleName());
        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");

        xMLStreamWriter.writeStartElement("id");
        xMLStreamWriter.writeCharacters(String.valueOf(id));
        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");

        xMLStreamWriter.writeStartElement("name");
        xMLStreamWriter.writeCharacters(name);
        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");

        xMLStreamWriter.writeStartElement("speed");
        xMLStreamWriter.writeCharacters(String.valueOf(speed));
        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");

        xMLStreamWriter.writeStartElement("antivirus");
        xMLStreamWriter.writeCharacters(String.valueOf(antivirus));
        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");

        xMLStreamWriter.writeStartElement("connection_type");
        xMLStreamWriter.writeCharacters(connectionType.toString());
        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");

        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");
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

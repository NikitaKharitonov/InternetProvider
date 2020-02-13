package model.services;

import model.util.ValueParser;
import org.w3c.dom.Element;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.Serializable;

public abstract class Service implements Serializable, Cloneable {

    final long id;
    String name;

    Service(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public abstract String getType();

    public abstract void toXML(XMLStreamWriter xmlStreamWriter) throws XMLStreamException;

}

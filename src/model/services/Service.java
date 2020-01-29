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

    Service(String str) {
        ValueParser.setString(str);
        this.id = Long.parseLong(ValueParser.getValue("id"));
        this.name = ValueParser.getValue("name");
    }

    Service(Element element) {
        this.id = Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent());
        this.name = element.getElementsByTagName("name").item(0).getTextContent();
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

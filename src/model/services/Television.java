package model.services;

import model.ValueReader;
import org.w3c.dom.Element;
import util.Annotations.MethodParameter;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class Television extends Service {

    private int numberOfChannels;

    public Television(
            @MethodParameter(name = "id", type = Long.class)
                    Long id,
            @MethodParameter(name = "name", type = String.class)
                    String name,
            @MethodParameter(name = "numberOfChannels", type = Integer.class)
                    Integer numberOfChannels ) {
        super(id, name);
        this.numberOfChannels = numberOfChannels;
    }

    public Television(String str) {
        super(str);
        numberOfChannels = Integer.parseInt(ValueReader.nextValue());
    }

    public Television(Element element) {
        super(element);
        numberOfChannels = Integer.parseInt(element.getElementsByTagName("number_of_channels").item(0).getTextContent());
    }

    public int getNumberOfChannels() {
        return numberOfChannels;
    }

    public void setNumberOfChannels(Integer numberOfChannels) {
        this.numberOfChannels = numberOfChannels;
    }

    @Override
    public String getType() {
        return "Television";
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " {id=" + id +
                ", name=" + name +
                ", numberOfChannels=" + numberOfChannels +
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

        xMLStreamWriter.writeStartElement("number_of_channels");
        xMLStreamWriter.writeCharacters(String.valueOf(numberOfChannels));
        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");

        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");
    }

    @Override
    public boolean equals(Object obj) {
        if(!super.equals(obj))
            return false;
        if (!(obj instanceof Television))
            return false;
        return this.numberOfChannels == ((Television) obj).numberOfChannels;
    }
}

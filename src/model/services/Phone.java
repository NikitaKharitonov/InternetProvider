package model.services;

import model.util.ValueReader;
import org.w3c.dom.Element;
import util.Annotations.MethodParameter;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class Phone extends Service {

    private int callsMinCount;
    private int smsCount;

    public Phone(
            @MethodParameter(name = "id", type = Long.class)
                    Long id,
            @MethodParameter(name = "name", type = String.class)
                    String name,
            @MethodParameter(name = "callsMinCount", type = Integer.class)
                    Integer callsMinCount,
            @MethodParameter(name = "smsCount", type = Integer.class)
                    Integer smsCount ) {
        super(id, name);
        this.callsMinCount = callsMinCount;
        this.smsCount = smsCount;
    }

    public Phone(String str) {
        super(str);
        callsMinCount = Integer.parseInt(ValueReader.nextValue());
        smsCount = Integer.parseInt(ValueReader.nextValue());
    }

    public Phone(Element element) {
        super(element);
        callsMinCount = Integer.parseInt(element.getElementsByTagName("number_of_calling_minutes").item(0).getTextContent());
        smsCount = Integer.parseInt(element.getElementsByTagName("number_of_sms").item(0).getTextContent());
    }

    public int getCallsMinCount() {
        return callsMinCount;
    }

    public void setCallsMinCount(Integer callsMinCount) {
        this.callsMinCount = callsMinCount;
    }

    public int getSmsCount() {
        return smsCount;
    }

    public void setSmsCount(Integer smsCount) {
        this.smsCount = smsCount;
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
                ", callsMinCount=" + callsMinCount +
                ", smsCount=" + smsCount +
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

        xMLStreamWriter.writeStartElement("number_of_calling_minutes");
        xMLStreamWriter.writeCharacters(String.valueOf(callsMinCount));
        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");

        xMLStreamWriter.writeStartElement("number_of_sms");
        xMLStreamWriter.writeCharacters(String.valueOf(smsCount));
        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");

        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");
    }

    @Override
    public boolean equals(Object obj) {
        if(!super.equals(obj))
            return false;
        if (!(obj instanceof Phone))
            return false;
        return this.callsMinCount == ((Phone) obj).callsMinCount
                && this.smsCount == ((Phone) obj).smsCount;
    }
}

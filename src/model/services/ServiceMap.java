package model.services;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ServiceMap {
    private HashMap<Long, Service> hashMap = new HashMap<>();

    public ServiceMap() {

    }

    public ServiceMap(String str) {
        String[] lines = str.split("\n");
        for (String line: lines) {
            Service service = ServiceFactory.createService(line);
            hashMap.put(service.getId(), service);
        }
    }

    public ServiceMap(Element element) {
        NodeList nList = element.getElementsByTagName("service");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                org.w3c.dom.Element eElement = (org.w3c.dom.Element) nNode;
                Service service = ServiceFactory.createService(eElement);
                hashMap.put(service.getId(), service);
            }
        }
    }

    public int size() {
        return hashMap.size();
    }

    public boolean isEmpty() {
        return hashMap.isEmpty();
    }

    public boolean containsID(Long id) {
        return hashMap.containsKey(id);
    }

    public boolean containsService(Service service) {
        return hashMap.containsKey(service);
    }

    public Service get(Long id) {
        return hashMap.get(id);
    }

    public void put(Service service) {
        hashMap.put(service.getId(), service);
    }

    public Service remove(Long id) {
        return hashMap.remove(id);
    }

    public void putAll(Map<? extends Long, ? extends Service> m) {
        hashMap.putAll(m);
    }

    public void clear() {
        hashMap.clear();
    }

    public Set<Long> idSet() {
        return hashMap.keySet();
    }

    public Collection<Service> services() {
        return hashMap.values();
    }

    public Set<Map.Entry<Long, Service>> entrySet() {
        return hashMap.entrySet();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Long id: hashMap.keySet()) {
            builder.append(hashMap.get(id).toString());
            builder.append("\n");
        }
        return builder.toString();
    }

    public void writeToXML(XMLStreamWriter xMLStreamWriter) throws XMLStreamException {
        xMLStreamWriter.writeStartElement("services");
        xMLStreamWriter.writeCharacters("\n");
        for (Long id: hashMap.keySet())
            hashMap.get(id).toXML(xMLStreamWriter);
        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");
    }
}

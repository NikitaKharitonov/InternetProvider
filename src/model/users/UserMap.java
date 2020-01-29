package model.users;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.text.ParseException;
import java.util.*;

public class UserMap {

    private HashMap<Long, User> hashMap = new HashMap<>();

    public UserMap() {

    }

//    public UserMap(String str) {
//        String[] lines = str.split("\n");
//        for (String line: lines) {
//            User user = new User(line);
//            hashMap.put(user.getId(), user);
//        }
//    }

    public UserMap(Element element) throws ParseException {
        NodeList nodeList = element.getElementsByTagName("user");
        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            Node nNode = nodeList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                User user = new User(eElement);
                hashMap.put(user.getId(), user);
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

    public boolean containsUser(User user) {
        return hashMap.containsValue(user);
    }

    public User get(Long id) {
        return hashMap.get(id);
    }

    public void put(User user) {
        hashMap.put(user.getId(), user);
    }

    public User remove(Long id) {
        return hashMap.remove(id);
    }

    public void putAll(Map<? extends Long, ? extends User> m) {
        hashMap.putAll(m);
    }

    public void clear() {
        hashMap.clear();
    }

    public Set<Long> idSet() {
        return hashMap.keySet();
    }

    public Collection<User> users() {
        return hashMap.values();
    }

    public Set<Map.Entry<Long, User>> entrySet() {
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
        xMLStreamWriter.writeStartElement("users");
        xMLStreamWriter.writeCharacters("\n");
        for (Long id: hashMap.keySet())
            hashMap.get(id).toXML(xMLStreamWriter);
        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");
    }
}

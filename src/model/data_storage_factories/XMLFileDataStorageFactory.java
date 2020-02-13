package model.data_storage_factories;

import model.services.Service;
import model.services.Services;
import model.users.User;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

public class XMLFileDataStorageFactory implements DataStorageFactory {
    final private String userDataPath = "./data/xml/users.xml";
    final private String serviceDataPath = "./data/xml/services.xml";

    @Override
    public void writeUsers(HashMap<Long, User> users) throws IOException {
        try {
            XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(new FileWriter(userDataPath));
            xMLStreamWriter.writeStartDocument();
            xMLStreamWriter.writeCharacters("\n");
            xMLStreamWriter.writeStartElement("users");
            xMLStreamWriter.writeCharacters("\n");
            for (Long id: users.keySet())
                users.get(id).toXML(xMLStreamWriter);
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeCharacters("\n");
            xMLStreamWriter.writeEndDocument();
            xMLStreamWriter.flush();
            xMLStreamWriter.close();
        } catch (XMLStreamException | IOException e) {
            throw new IOException("Failed to write users to XML file");
        }
    }

    @Override
    public HashMap<Long, User> readUsers() throws IOException {
        try {
            File inputFile = new File(userDataPath);
            if (inputFile.length() == 0)
                return null;
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            Element element = doc.getDocumentElement();
            HashMap<Long, User> users = new HashMap<>();
            NodeList nodeList = element.getElementsByTagName("user");
            for (int temp = 0; temp < nodeList.getLength(); temp++) {
                Node nNode = nodeList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    User user = User.fromXML(eElement);
                    users.put(user.getId(), user);
                }
            }
            return users;
        } catch (ParserConfigurationException | SAXException | ParseException e) {
            throw new IOException("Failed to read users from XML file");
        }
    }

    @Override
    public void writeServices(HashMap<Long, Service> services) throws IOException {
        try {
            XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(new FileWriter(serviceDataPath));
            xMLStreamWriter.writeStartDocument();
            xMLStreamWriter.writeCharacters("\n");
            xMLStreamWriter.writeStartElement("services");
            xMLStreamWriter.writeCharacters("\n");
            for (Long id: services.keySet())
                services.get(id).toXML(xMLStreamWriter);
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeCharacters("\n");
            xMLStreamWriter.writeEndDocument();
            xMLStreamWriter.flush();
            xMLStreamWriter.close();
        } catch (XMLStreamException | IOException e) {
            throw new IOException("Failed to write services to XML file");
        }
    }

    @Override
    public HashMap<Long, Service> readServices() throws IOException {
        try {
            File inputFile = new File(serviceDataPath);
            if (inputFile.length() == 0)
                return null;
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            Element element = doc.getDocumentElement();
            HashMap<Long, Service> services = new HashMap<>();
            NodeList nList = element.getElementsByTagName("service");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element eElement = (org.w3c.dom.Element) nNode;
                    Service service = Services.createService(eElement);
                    services.put(service.getId(), service);
                }
            }
            return services;
        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException("Failed to read services from XML file");
        }
    }
}

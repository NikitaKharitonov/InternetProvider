package model.models;

import model.services.*;
import model.users.User;

import java.io.*;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.*;

import javax.xml.parsers.*;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class Models {
    /*
    Writes the users data to the file
    */
    public static void writeUsersToFile(HashMap<Long, User> users, String filePath) throws IOException {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(usersToString(users));
            writer.close();
        } catch (IOException e) {
            throw new IOException("Failed to write users to file");
        }
    }

    /*
    Writes the users data to the string
    */
    public static String usersToString(HashMap<Long, User> users) {
        StringBuilder builder = new StringBuilder();
        for (Long id : users.keySet())
            builder.append(users.get(id).toString() + "\n");
        return builder.toString();
    }

    /*
    Reads and returns the users data from the file
     */
    public static HashMap<Long, User> readUsersFromFile(String filePath) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            HashMap<Long, User> users = new HashMap<>();
            while ((line = reader.readLine()) != null) {
                User user = new User(line);
                users.put(user.getId(), user);
            }
            reader.close();
            return users;
        } catch (IOException e) {
            throw new IOException("Failed to read users from file");
        }
    }

    /*
    Writes the services data to the file
     */
    public static void writeServicesToFile(HashMap<Long, Service> services, String filePath) throws IOException {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(servicesToString(services));
            writer.close();
        } catch (IOException e) {
            throw new IOException("Failed to write services to file");
        }
    }

    /*
    Writes the services data to string
     */
    public static String servicesToString(HashMap<Long, Service> services) {
        StringBuilder builder = new StringBuilder();
        for (Long id : services.keySet())
            builder.append(services.get(id).toString() + "\n");
        return builder.toString();
    }

    /*
    Reads and returns the services data from the file
     */
    public static HashMap<Long, Service> readServicesFromFile(String filePath) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            HashMap<Long, Service> services = new HashMap<>();
            while ((line = reader.readLine()) != null) {
                Service service = ServiceFactory.createService(line);
                services.put(service.getId(), service);
            }
            reader.close();
            return services;
        } catch (IOException e) {
            throw new IOException("Failed to read services from file");
        }
    }

    public static void writeUsersToXML(HashMap<Long, User> users, String filePath) throws IOException {
        try {
            FileWriter writer = new FileWriter(filePath);
            XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(writer);
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

    public static HashMap<Long, User> readUsersFromXML(String filePath) throws IOException {
        try {
            File inputFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("user");
            HashMap<Long, User> users = new HashMap<>();
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    User user = new User(eElement);
                    users.put(user.getId(), user);
                }
            }
            return users;
        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException("Failed to read users from XML file");
        }
    }

    public static void writeServicesToXML(HashMap<Long, Service> services, String filePath) throws IOException {
        try {
            FileWriter writer = new FileWriter(filePath);
            XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(writer);
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

    public static HashMap<Long, Service> readServicesFromXML(String filePath) throws IOException {
        try {
            File inputFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("service");
            HashMap<Long, Service> users = new HashMap<>();
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    Service service = ServiceFactory.createService(eElement);
                    users.put(service.getId(), service);
                }
            }
            return users;
        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException("Failed to read services from XML file");
        }
    }
}

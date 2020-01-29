package model.data_storage_factories;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import model.services.ServiceMap;
import model.users.UserMap;

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

public class XMLFileDataStorageFactory implements DataStorageFactory {
    final private String userDataPath = "./data/xml/users.xml";
    final private String serviceDataPath = "./data/xml/services.xml";

    @Override
    public void writeUsers(UserMap users) throws IOException {
        try {
            XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(new FileWriter(userDataPath));
            xMLStreamWriter.writeStartDocument();
            xMLStreamWriter.writeCharacters("\n");
            users.writeToXML(xMLStreamWriter);
            xMLStreamWriter.writeEndDocument();
            xMLStreamWriter.flush();
            xMLStreamWriter.close();
        } catch (XMLStreamException | IOException e) {
            throw new IOException("Failed to write users to XML file");
        }
    }

    @Override
    public UserMap readUsers() throws IOException {
        try {
            File inputFile = new File(userDataPath);
            if (inputFile.length() == 0)
                return null;
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            Element element = doc.getDocumentElement();
            return new UserMap(element);
        } catch (ParserConfigurationException | SAXException | ParseException e) {
            throw new IOException("Failed to read users from XML file");
        }
    }

    @Override
    public void writeServices(ServiceMap serviceMap) throws IOException {
        try {
            XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(new FileWriter(serviceDataPath));
            xMLStreamWriter.writeStartDocument();
            xMLStreamWriter.writeCharacters("\n");
            serviceMap.writeToXML(xMLStreamWriter);
            xMLStreamWriter.writeEndDocument();
            xMLStreamWriter.flush();
            xMLStreamWriter.close();
        } catch (XMLStreamException | IOException e) {
            throw new IOException("Failed to write services to XML file");
        }
    }

    @Override
    public ServiceMap readServices() throws IOException {
        try {
            File inputFile = new File(serviceDataPath);
            if (inputFile.length() == 0)
                return null;
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            Element element = doc.getDocumentElement();
            return new ServiceMap(element);
        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException("Failed to read services from XML file");
        }
    }
}

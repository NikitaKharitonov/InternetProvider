package model.users;

import model.services.Internet;
import model.services.Phone;
import model.services.Service;
import model.services.Television;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class User {

    public enum Status{PLANNED, ACTIVE, DISCONNECTED, SUSPENDED};

    public static class UserService {
        final long serviceId;
        final Date activationDate;
        Status status;

        public UserService(long serviceId, Date activationDate, Status status) {
            this.serviceId = serviceId;
            this.activationDate = activationDate;
            this.status = status;
        }

        public long getServiceId() {
            return serviceId;
        }

        public Date getActivationDate() {
            return activationDate;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "ActivationDate: " + activationDate + "\n" +
                    "Status: " + status;
        }
    }

    private final long id;
    private String username;
    private String firstName;
    private String surname;
    private String phoneNumber;
    private String emailAddress;
    private HashMap<String, LinkedList<UserService>> userServiceListHashMap;

    public User(long id, String username, String firstName, String phoneNumber, String emailAddress, HashMap<String, LinkedList<UserService>> userServiceListHashMap) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;

        if (userServiceListHashMap == null) {
            this.userServiceListHashMap = new HashMap<>();
            this.userServiceListHashMap.put(Internet.class.getSimpleName(), new LinkedList<>());
            this.userServiceListHashMap.put(Phone.class.getSimpleName(), new LinkedList<>());
            this.userServiceListHashMap.put(Television.class.getSimpleName(), new LinkedList<>());
        } else
            this.userServiceListHashMap = userServiceListHashMap;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public UserService getUserService(String type, int index) {
        return userServiceListHashMap.get(type).get(index);
    }

    public UserService getCurrentUserService(String type) {
        LinkedList<UserService> userServices = userServiceListHashMap.get(type);
        if (userServices.size() == 0)
            return null;
        Date curDate = new Date();
        if (curDate.before(userServices.get(0).activationDate))
            return null;
        for (int i = 1; i < userServices.size(); ++i) {
            if (curDate.before(userServices.get(i).activationDate)) {
                return userServices.get(i - 1);
            }
        }
        return userServices.getLast();
    }

    public int getUserServiceCount(String type) {
        return userServiceListHashMap.get(type).size();
    }

    public void addUserService(Service service, Date date, Status status) {
        UserService userService = new UserService(service.getId(), date, status);
        LinkedList<UserService> userServices = userServiceListHashMap.get(service.getType());
        for (int i = 0; i < userServices.size(); ++i)
            if (userService.activationDate.before(userServices.get(i).activationDate)) {
                userServices.add(i, userService);
                return;
            }
        userServices.add(userService);
    }

    // todo add remove method

    @Override
    public String toString() {
        return "Username: " +  username + "\n" +
                "First name: " + firstName + "\n" +
                "Surname: " + surname + "\n" +
                "Phone number: " + phoneNumber + "\n" +
                "Email address: " + emailAddress;
    }

    public void toXML(XMLStreamWriter xmlsw) throws XMLStreamException {
        xmlsw.writeCharacters("\t");
        xmlsw.writeStartElement("user");
        xmlsw.writeCharacters("\n");

        xmlsw.writeCharacters("\t\t");
        xmlsw.writeStartElement("id");
        xmlsw.writeCharacters(String.valueOf(id));
        xmlsw.writeEndElement();
        xmlsw.writeCharacters("\n");

        xmlsw.writeCharacters("\t\t");
        xmlsw.writeStartElement("username");
        xmlsw.writeCharacters(username);
        xmlsw.writeEndElement();
        xmlsw.writeCharacters("\n");

        xmlsw.writeCharacters("\t\t");
        xmlsw.writeStartElement("first_name");
        xmlsw.writeCharacters(firstName);
        xmlsw.writeEndElement();
        xmlsw.writeCharacters("\n");

        xmlsw.writeCharacters("\t\t");
        xmlsw.writeStartElement("email_address");
        xmlsw.writeCharacters(emailAddress);
        xmlsw.writeEndElement();
        xmlsw.writeCharacters("\n");

        xmlsw.writeCharacters("\t\t");
        xmlsw.writeStartElement("phone_number");
        xmlsw.writeCharacters(phoneNumber);
        xmlsw.writeEndElement();
        xmlsw.writeCharacters("\n");

        xmlsw.writeCharacters("\t\t");
        xmlsw.writeStartElement("user_services");
        xmlsw.writeCharacters("\n");

        for (String serviceType: userServiceListHashMap.keySet()) {
            LinkedList<UserService> userServices = userServiceListHashMap.get(serviceType);

            if (userServices.size() != 0) {
                xmlsw.writeCharacters("\t\t\t");
                xmlsw.writeStartElement(serviceType);
                xmlsw.writeCharacters("\n");

                for (UserService service : userServices) {
                    xmlsw.writeCharacters("\t\t\t\t");
                    xmlsw.writeStartElement("service");
                    xmlsw.writeCharacters("\n");

                    xmlsw.writeCharacters("\t\t\t\t\t");
                    xmlsw.writeStartElement("service_id");
                    xmlsw.writeCharacters(String.valueOf(service.serviceId));
                    xmlsw.writeEndElement();
                    xmlsw.writeCharacters("\n");

                    xmlsw.writeCharacters("\t\t\t\t\t");
                    xmlsw.writeStartElement("activation_date");
                    xmlsw.writeCharacters(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(service.activationDate));
                    xmlsw.writeEndElement();
                    xmlsw.writeCharacters("\n");

                    xmlsw.writeCharacters("\t\t\t\t\t");
                    xmlsw.writeStartElement("status");
                    xmlsw.writeCharacters(String.valueOf(service.getStatus()));
                    xmlsw.writeEndElement();
                    xmlsw.writeCharacters("\n");

                    xmlsw.writeCharacters("\t\t\t\t");
                    xmlsw.writeEndElement();
                    xmlsw.writeCharacters("\n");
                }

                xmlsw.writeCharacters("\t\t\t");
                xmlsw.writeEndElement();
                xmlsw.writeCharacters("\n");
            }
        }

        xmlsw.writeCharacters("\t\t");
        xmlsw.writeEndElement();
        xmlsw.writeCharacters("\n");
        xmlsw.writeCharacters("\t");
        xmlsw.writeEndElement();
        xmlsw.writeCharacters("\n");
    }

    public static User fromXML(Element eElement) throws ParseException {
        long id = Long.parseLong(eElement.getElementsByTagName("id").item(0).getTextContent());
        String username = eElement.getElementsByTagName("username").item(0).getTextContent();
        String firstName = eElement.getElementsByTagName("first_name").item(0).getTextContent();
        String emailAddress = eElement.getElementsByTagName("email_address").item(0).getTextContent();
        String phoneNumber = eElement.getElementsByTagName("phone_number").item(0).getTextContent();
        Element activatedServicesElement = (Element) eElement.getElementsByTagName("user_services").item(0);
        HashMap<String, LinkedList<UserService>> userServiceListHashMap = new HashMap<>();
        userServiceListHashMap.put(Internet.class.getSimpleName(), new LinkedList<>());
        userServiceListHashMap.put(Phone.class.getSimpleName(), new LinkedList<>());
        userServiceListHashMap.put(Television.class.getSimpleName(), new LinkedList<>());
        for (String type: userServiceListHashMap.keySet()) {
            Element serviceElement = (Element) activatedServicesElement.getElementsByTagName(type).item(0);
            if (serviceElement != null) {
                NodeList nodeList = serviceElement.getElementsByTagName("service");
                for (int j = 0; j < nodeList.getLength(); ++j) {
                    Element element = (Element) nodeList.item(j);
                    long serviceId = Long.parseLong(element.getElementsByTagName("service_id").item(0).getTextContent());
                    Date activationDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(element.getElementsByTagName("activation_date").item(0).getTextContent());
                    Status status = Status.valueOf(element.getElementsByTagName("status").item(0).getTextContent());
                    userServiceListHashMap.get(type).add(new UserService(serviceId, activationDate, status));
                }
            }
        }
        return new User(id, username, firstName, emailAddress, phoneNumber, userServiceListHashMap);
    }
}
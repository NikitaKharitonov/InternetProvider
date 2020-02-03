package model.users;

import model.services.Internet;
import model.services.Phone;
import model.services.Television;
import model.util.ValueParser;
import model.exceptions.ServiceNotFoundException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class User {

    private class ActivatedService {
        final long serviceId;
        final Date activationDate;
        boolean activated = true;

        public boolean isActivated() {
            return activated;
        }

        public void setActivated(boolean activated) {
            this.activated = activated;
        }

        public ActivatedService(long serviceId) {
            this.serviceId = serviceId;
            activationDate = new Date();
        }

        public ActivatedService(long serviceId, Date activationDate) {
            this.serviceId = serviceId;
            this.activationDate = activationDate;
        }
    }

    private class ServiceHistory {

        private int curServiceIndex = 0;
        private LinkedList<ActivatedService> activatedServices = new LinkedList<>();

        public void add(ActivatedService activatedService) {
            for (int i = 0; i < activatedServices.size(); ++i) {
                if (activatedService.activationDate.before(activatedServices.get(i).activationDate)) {
                    activatedServices.add(i, activatedService);
                    return;
                }
            }
            activatedServices.add(activatedService);
        }

        public ActivatedService get() {
            if (activatedServices.size() == 0)
                return null;
            Date curDate = new Date();
            if (curDate.before(activatedServices.get(0).activationDate))
                return null;
            for (int i = 1; i < activatedServices.size(); ++i) {
                if (curDate.before(activatedServices.get(i).activationDate)) {
                    return activatedServices.get(i - 1);
                }
            }
            return activatedServices.getLast();
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("size = " + activatedServices.size());
            for (ActivatedService service: activatedServices) {
                builder.append("{");
                builder.append("service_id=" + service.serviceId + ", ");
                builder.append("activation_date=" + service.activationDate + "}, ");
            }
            return "ServiceHistory{" +
                    "curServiceIndex=" + curServiceIndex +
                    ", " + builder.toString() +
                    '}';
        }


    }

    private final long id;
    private String name;
    private String phoneNumber;
    private String emailAddress;

    private HashMap<String, ServiceHistory> activatedServiceHashMap;

    public User(long id, String name, String phoneNumber, String emailAddress) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.activatedServiceHashMap = new HashMap<>();
    }

    // FIXME
//    public User(String str) {
//        ValueParser.setString(str);
//        id = Long.parseLong(ValueParser.getValue("id"));
//        name = ValueParser.getValue("name");
//        emailAddress = ValueParser.getValue("email_address");
//        phoneNumber = ValueParser.getValue("phone_number");
//        int activatedServiceCount = Integer.parseInt(ValueParser.getValue("activated_services_count"));
//        activatedServiceHashMap = new HashMap<>();
//        for (int i = 0; i < activatedServiceCount; ++i) {
//            String type = ValueParser.getValue("service_type");
//            long serviceId = Long.parseLong(ValueParser.getValue("service_id"));
//            long activationDate = Long.parseLong(ValueParser.getValue("activation_date"));
//            activatedServiceHashMap.put(type, new ActivatedService(serviceId, activationDate));
//        }
//    }

    public User(Element eElement) throws ParseException {
        id = Long.parseLong(eElement.getElementsByTagName("id").item(0).getTextContent());
        name = eElement.getElementsByTagName("name").item(0).getTextContent();
        emailAddress = eElement.getElementsByTagName("email_address").item(0).getTextContent();
        phoneNumber = eElement.getElementsByTagName("phone_number").item(0).getTextContent();
        Element activatedServicesElement = (Element) eElement.getElementsByTagName("activated_services").item(0);
        activatedServiceHashMap = new HashMap<>();
        activatedServiceHashMap.put(Internet.class.getSimpleName(), new ServiceHistory());
        activatedServiceHashMap.put(Phone.class.getSimpleName(), new ServiceHistory());
        activatedServiceHashMap.put(Television.class.getSimpleName(), new ServiceHistory());
        for (String type: activatedServiceHashMap.keySet()) {
            Element serviceElement = (Element) activatedServicesElement.getElementsByTagName(type).item(0);
            if (serviceElement != null) {
                NodeList nodeList = serviceElement.getElementsByTagName("service");
                for (int j = 0; j < nodeList.getLength(); ++j) {
                    Element element = (Element) nodeList.item(j);
                    long serviceId = Long.parseLong(element.getElementsByTagName("service_id").item(0).getTextContent());
                    Date activationDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(element.getElementsByTagName("activation_date").item(0).getTextContent());
                    activatedServiceHashMap.get(type).add(new ActivatedService(serviceId, activationDate));
                }
            }
        }
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public long getServiceIdByType(String serviceType) throws ServiceNotFoundException {
        ServiceHistory serviceHistory = activatedServiceHashMap.get(serviceType);
        if (serviceHistory == null)
            throw new ServiceNotFoundException("Service of type " + serviceType + " not found");
        ActivatedService service = serviceHistory.get();
        if (service == null)
            throw new ServiceNotFoundException("Service of type " + serviceType + " not found");
        return service.serviceId;
    }

    // FIXME
//    public long getActivationDateByType(String serviceType) throws ServiceNotFoundException {
//        ActivatedService activatedService = activatedServiceHashMap.get(serviceType);
//        if (activatedService == null)
//            throw new ServiceNotFoundException("Service of type " + serviceType + " not found");
//        return activatedService.activationDate;
//    }

    public void addService(String type, long serviceId) {
        if(activatedServiceHashMap.get(type) == null)
            activatedServiceHashMap.put(type, new ServiceHistory());
        activatedServiceHashMap.get(type).add(new ActivatedService(serviceId));
    }

    public void addService(String type, long serviceId, Date activationDate) {
        if(activatedServiceHashMap.get(type) == null)
            activatedServiceHashMap.put(type, new ServiceHistory());
        activatedServiceHashMap.get(type).add(new ActivatedService(serviceId, activationDate));
    }

    // FIXME
//    public void removeServiceByType(String serviceType) {
//        activatedServiceHashMap.get(serviceType).get().setActivated(false);
//    }

    // FIXME
//    public void removeServiceById(long serviceId) {
//        for (String serviceType: activatedServiceHashMap.keySet()) {
//            if (activatedServiceHashMap.get(serviceType).serviceId == serviceId)
//                activatedServiceHashMap.remove(serviceType);
//        }
//    }

    public String[] getServiceTypes() {
        Set<String> stringSet = activatedServiceHashMap.keySet();
        String[] strings = new String[stringSet.size()];
        stringSet.toArray(strings);
        return strings;
    }

//    public ArrayList<Long> getServiceIds() {
//        ArrayList<Long> ids = new ArrayList<>(activatedServiceHashMap.size());
//        for (String serviceType: activatedServiceHashMap.keySet())
//            ids.add(activatedServiceHashMap.get(serviceType).serviceId);
//        return ids;
//    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("User {id=" + id + ", ");
        builder.append("name=" + name + ", ");
        builder.append("email_address=" + emailAddress + ", ");
        builder.append("phone_number=" + phoneNumber + ", ");
        builder.append("activated_services_count=" + activatedServiceHashMap.size() + ", ");
        builder.append("activated_services={ ");
        for (String serviceType: activatedServiceHashMap.keySet()) {
            builder.append("{");
            builder.append("service_type=" + serviceType + ", ");
            builder.append(activatedServiceHashMap.get(serviceType).toString());
            builder.append("} ");
        }
        builder.append("}");
        return builder.toString();
    }

    public void toXML(XMLStreamWriter xMLStreamWriter) throws XMLStreamException {
        xMLStreamWriter.writeCharacters("\t");
        xMLStreamWriter.writeStartElement("user");
        xMLStreamWriter.writeCharacters("\n");

        xMLStreamWriter.writeCharacters("\t\t");
        xMLStreamWriter.writeStartElement("id");
        xMLStreamWriter.writeCharacters(String.valueOf(id));
        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");

        xMLStreamWriter.writeCharacters("\t\t");
        xMLStreamWriter.writeStartElement("name");
        xMLStreamWriter.writeCharacters(name);
        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");

        xMLStreamWriter.writeCharacters("\t\t");
        xMLStreamWriter.writeStartElement("email_address");
        xMLStreamWriter.writeCharacters(emailAddress);
        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");

        xMLStreamWriter.writeCharacters("\t\t");
        xMLStreamWriter.writeStartElement("phone_number");
        xMLStreamWriter.writeCharacters(phoneNumber);
        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");

        xMLStreamWriter.writeCharacters("\t\t");
        xMLStreamWriter.writeStartElement("activated_services");
        xMLStreamWriter.writeCharacters("\n");

        for (String serviceType: activatedServiceHashMap.keySet()) {
            ServiceHistory serviceHistory = activatedServiceHashMap.get(serviceType);
            LinkedList<ActivatedService> activatedServices = serviceHistory.activatedServices;

            if (activatedServices.size() != 0) {
                xMLStreamWriter.writeCharacters("\t\t\t");
                xMLStreamWriter.writeStartElement(serviceType);
                xMLStreamWriter.writeCharacters("\n");

                for (ActivatedService service : activatedServices) {
                    xMLStreamWriter.writeCharacters("\t\t\t\t");
                    xMLStreamWriter.writeStartElement("service");
                    xMLStreamWriter.writeCharacters("\n");

                    xMLStreamWriter.writeCharacters("\t\t\t\t\t");
                    xMLStreamWriter.writeStartElement("service_id");
                    xMLStreamWriter.writeCharacters(String.valueOf(service.serviceId));
                    xMLStreamWriter.writeEndElement();
                    xMLStreamWriter.writeCharacters("\n");

                    xMLStreamWriter.writeCharacters("\t\t\t\t\t");
                    xMLStreamWriter.writeStartElement("activation_date");
                    xMLStreamWriter.writeCharacters(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(service.activationDate));
                    xMLStreamWriter.writeEndElement();
                    xMLStreamWriter.writeCharacters("\n");

                    xMLStreamWriter.writeCharacters("\t\t\t\t");
                    xMLStreamWriter.writeEndElement();
                    xMLStreamWriter.writeCharacters("\n");
                }

                xMLStreamWriter.writeCharacters("\t\t\t");
                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeCharacters("\n");
            }
        }

        xMLStreamWriter.writeCharacters("\t\t");
        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");
        xMLStreamWriter.writeCharacters("\t");
        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");
    }
}
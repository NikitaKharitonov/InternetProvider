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
        int activatedServicesCount = Integer.parseInt(eElement.getElementsByTagName("activated_services_count").item(0).getTextContent());
        activatedServiceHashMap = new HashMap<>();
        activatedServiceHashMap.put(Internet.class.getSimpleName(), new ServiceHistory());
        activatedServiceHashMap.put(Phone.class.getSimpleName(), new ServiceHistory());
        activatedServiceHashMap.put(Television.class.getSimpleName(), new ServiceHistory());
        for (int i = 0; i < activatedServicesCount; ++i) {
            NodeList serviceList = eElement.getElementsByTagName("activated_services");
            Element serviceElement = (Element) serviceList.item(i);
            String type = serviceElement.getElementsByTagName("type").item(0).getTextContent();
            activatedServiceHashMap.put(type, new ServiceHistory());
            int serviceCount = Integer.parseInt(serviceElement.getElementsByTagName("service_count").item(0).getTextContent());
            NodeList nodeList = serviceElement.getElementsByTagName("service");
            for (int j = 0; j < serviceCount; ++j) {
                Element element = (Element)nodeList.item(j);
                long serviceId = Long.parseLong(element.getElementsByTagName("service_id").item(0).getTextContent());
                Date activationDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(element.getElementsByTagName("activation_date").item(0).getTextContent());
                activatedServiceHashMap.get(type).add(new ActivatedService(serviceId, activationDate));
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
        xMLStreamWriter.writeStartElement("user");
        xMLStreamWriter.writeCharacters("\n");

        xMLStreamWriter.writeStartElement("id");
        xMLStreamWriter.writeCharacters(String.valueOf(id));
        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");

        xMLStreamWriter.writeStartElement("name");
        xMLStreamWriter.writeCharacters(name);
        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");

        xMLStreamWriter.writeStartElement("email_address");
        xMLStreamWriter.writeCharacters(emailAddress);
        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");

        xMLStreamWriter.writeStartElement("phone_number");
        xMLStreamWriter.writeCharacters(phoneNumber);
        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");

        xMLStreamWriter.writeStartElement("activated_services_count");
        xMLStreamWriter.writeCharacters(String.valueOf(activatedServiceHashMap.size()));
        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");

        xMLStreamWriter.writeStartElement("activated_services");
        xMLStreamWriter.writeCharacters("\n");

        for (String serviceType: activatedServiceHashMap.keySet()) {
            ServiceHistory serviceHistory = activatedServiceHashMap.get(serviceType);
            LinkedList<ActivatedService> activatedServices = serviceHistory.activatedServices;

            if (activatedServices.size() != 0) {
                xMLStreamWriter.writeStartElement("activated_service");
                xMLStreamWriter.writeCharacters("\n");

                xMLStreamWriter.writeStartElement("type");
                xMLStreamWriter.writeCharacters(serviceType);
                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeCharacters("\n");

                xMLStreamWriter.writeStartElement("service_count");
                xMLStreamWriter.writeCharacters(String.valueOf(activatedServices.size()));
                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeCharacters("\n");

                xMLStreamWriter.writeStartElement("service_history");
                xMLStreamWriter.writeCharacters("\n");
                for (ActivatedService service : activatedServices) {
                    xMLStreamWriter.writeStartElement("service");
                    xMLStreamWriter.writeCharacters("\n");

                    xMLStreamWriter.writeStartElement("service_id");
                    xMLStreamWriter.writeCharacters(String.valueOf(service.serviceId));
                    xMLStreamWriter.writeEndElement();
                    xMLStreamWriter.writeCharacters("\n");

                    xMLStreamWriter.writeStartElement("activation_date");
                    xMLStreamWriter.writeCharacters(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(service.activationDate));
                    xMLStreamWriter.writeEndElement();
                    xMLStreamWriter.writeCharacters("\n");

                    xMLStreamWriter.writeEndElement();
                    xMLStreamWriter.writeCharacters("\n");
                }
                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeCharacters("\n");

                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeCharacters("\n");
            }

        }

        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");
        xMLStreamWriter.writeEndElement();
        xMLStreamWriter.writeCharacters("\n");
    }
}
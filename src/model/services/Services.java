package model.services;

import org.w3c.dom.Element;

public class Services {

    public static Service createService(Element element) {
        String type = element.getElementsByTagName("type").item(0).getTextContent();
        if (type.equals(Internet.class.getSimpleName()))
            return createInternet(element);
        else if (type.equals(Phone.class.getSimpleName()))
            return createPhone(element);
        else if (type.equals(Television.class.getSimpleName()))
            return createTelevision(element);
        return null;
    }

    public static Internet createInternet (Element element) {
        long id = Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent());
        String name = element.getElementsByTagName("name").item(0).getTextContent();
        int speed = Integer.parseInt(element.getElementsByTagName("speed").item(0).getTextContent());
        boolean antivirus = Boolean.parseBoolean(element.getElementsByTagName("antivirus").item(0).getTextContent());
        Internet.ConnectionType connectionType = Internet.ConnectionType.valueOf(element.getElementsByTagName("connection_type").item(0).getTextContent());
        return new Internet(id, name, speed, antivirus, connectionType);
    }

    public static Phone createPhone(Element element) {
        long id = Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent());
        String name = element.getElementsByTagName("name").item(0).getTextContent();
        int callsMinCount = Integer.parseInt(element.getElementsByTagName("number_of_calling_minutes").item(0).getTextContent());
        int smsCount = Integer.parseInt(element.getElementsByTagName("number_of_sms").item(0).getTextContent());
        return new Phone(id, name, callsMinCount, smsCount);
    }

    public static Television createTelevision(Element element) {
        long id = Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent());
        String name = element.getElementsByTagName("name").item(0).getTextContent();
        int numberOfChannels = Integer.parseInt(element.getElementsByTagName("number_of_channels").item(0).getTextContent());
        return new Television(id ,name, numberOfChannels);
    }
}

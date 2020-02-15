package model.services;

import org.w3c.dom.Element;

public class Services {

    public static String[] serviceTypes = {
            Internet.class.getSimpleName(),
            Phone.class.getSimpleName(),
            Television.class.getSimpleName()
    };

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

    private static Internet createInternet (Element element) {
        long id = Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent());
        String name = element.getElementsByTagName("name").item(0).getTextContent();
        int speed = Integer.parseInt(element.getElementsByTagName("speed").item(0).getTextContent());
        boolean antivirus = Boolean.parseBoolean(element.getElementsByTagName("antivirus").item(0).getTextContent());
        Internet.ConnectionType connectionType = Internet.ConnectionType.valueOf(element.getElementsByTagName("connection_type").item(0).getTextContent());
        return new Internet(id, name, speed, antivirus, connectionType);
    }

    private static Phone createPhone(Element element) {
        long id = Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent());
        String name = element.getElementsByTagName("name").item(0).getTextContent();
        int callsMinCount = Integer.parseInt(element.getElementsByTagName("number_of_calling_minutes").item(0).getTextContent());
        int smsCount = Integer.parseInt(element.getElementsByTagName("number_of_sms").item(0).getTextContent());
        return new Phone(id, name, callsMinCount, smsCount);
    }

    private static Television createTelevision(Element element) {
        long id = Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent());
        String name = element.getElementsByTagName("name").item(0).getTextContent();
        int numberOfChannels = Integer.parseInt(element.getElementsByTagName("number_of_channels").item(0).getTextContent());
        return new Television(id ,name, numberOfChannels);
    }

    public static String serviceToString(Service service) {
        String type = service.getType();
        if (type.equals(Internet.class.getSimpleName()))
            return internetToString((Internet)service);
        else if (type.equals(Phone.class.getSimpleName()))
            return writePhone((Phone)service);
        else if (type.equals(Television.class.getSimpleName()))
            return writeTelevision((Television)service);
        return null;
    }

    private static String internetToString(Internet internet) {
        return "Name: " + internet.getName() + "\n" +
                "Speed: " + internet.getSpeed() + "\n" +
                "Connection type: " + internet.getConnectionType() + "\n" +
                "Antivirus: " + internet.isAntivirus();
    }

    private static String writePhone(Phone phone) {
        return "Name: " + phone.getName() + "\n" +
                "Number of minutes: " + phone.getCallsMinCount() + "\n" +
                "Number of SMS: " + phone.getSmsCount();
    }

    private static String writeTelevision(Television television) {
        return "Name: " + television.getName() + "\n" +
                "Number of channels: " + television.getNumberOfChannels();
    }
}

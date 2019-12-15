package model.services;

import org.w3c.dom.Element;

public class ServiceFactory {
    public static String[] serviceTypes = {
            Internet.class.getSimpleName(),
            Phone.class.getSimpleName(),
            Television.class.getSimpleName()
    };

    public static Service createService(String line) {
        String type = line.substring(0, line.indexOf(' '));
        String str = line.substring(line.indexOf(' ') + 1);
        if (type.equals(Internet.class.getSimpleName()))
            return new Internet(str);
        else if (type.equals(Phone.class.getSimpleName()))
            return new Phone(str);
        else if (type.equals(Television.class.getSimpleName()))
            return new Television(str);
        return null;
    }

    public static Service createService(Element element) {
        String type = element.getElementsByTagName("type").item(0).getTextContent();
        if (type.equals(Internet.class.getSimpleName()))
            return new Internet(element);
        else if (type.equals(Phone.class.getSimpleName()))
            return new Phone(element);
        else if (type.equals(Television.class.getSimpleName()))
            return new Television(element);
        return null;
    }
}

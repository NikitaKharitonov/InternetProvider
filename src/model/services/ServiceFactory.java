package model.services;

public class ServiceFactory {
    public static String[] serviceTypes = {Internet.class.getSimpleName(), Phone.class.getSimpleName(), Television.class.getSimpleName()};

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
}

package controller;

import model.Model;
import model.User;
import model.services.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class BaseController
 * @author anteii
 * @version 0.1
 * */
public class BaseController implements Controller{

    private final String USER_NAME_STIRNG_PATTERN = "\\w{6}\\w*";
    private final String EMAIL_ADDRESS_STIRNG_PATTERN = "[\\w\\d]+@\\w+\\.\\w+";
    private final String PHONE_NUMBER_STIRNG_PATTERN = "\\+\\d{11}";
    private final String[] SERVICES_NAME = {"Internet", "Phone", "Television"};
    private Model model;
    /**
     * Public constructor
     * @param model
     *          Model object
     * */
    public BaseController(Model model) {
        this.model = model;
    }

    /**
     * Creates new user with such parameters {@code params} and instantly add him to storage
     * @param params
     *          Map with declared keys "name", "emailAddress", "phoneNumber"
     * @throws FailedOperation
     *          If some troubles were happened
     * @return clone of created object
     * */
    @Override
    public User createUser(Map<String, Object> params) throws FailedOperation {

        User user = new User();

        String name, email, phoneNumber;

        try{
            name = (String) params.get("name");
            email = (String) params.get("emailAddress");
            phoneNumber = (String) params.get("phoneNumber");
        } catch (NullPointerException ex){
            throw new FailedOperation("Failed to create new user. There are some empty fields.");
        }
        if (isCorrectString(name, USER_NAME_STIRNG_PATTERN)){
            if (isCorrectString(email, EMAIL_ADDRESS_STIRNG_PATTERN)){
                if (isCorrectString(phoneNumber, PHONE_NUMBER_STIRNG_PATTERN)){
                    user.setName(name);
                    user.setPhoneNumber(phoneNumber);
                    user.setEmailAddress(email);
                    model.addUser(user);
                    try {
                        model.save();
                    } catch (IOException e) {
                        throw new FailedOperation("Failed to add new user. Troubles with DB");
                    }
                    throw new FailedOperation("Failed to add new user. Wrong phone number");
                }
                throw new FailedOperation("Failed to add new user. Wrong email");
            }
            throw new FailedOperation("Failed to add new user. Wrong name");
        }

        return (User) user.clone();
    }
    /**
     * Search user by his id
     * @param userID
     *          user id
     * @throws FailedOperation
     *          If some troubles were happened
     * @return clone of created Service object
     * */
    @Override
    public User getUser(int userID) throws FailedOperation {
        if (isValidID("User", userID)){
            throw new FailedOperation("Incorrect ID");
        }

        return (User) model.getUserById(userID).clone();
    }
    /**
     * Change user data (E.g. name or email address)  and instantly save changes in storage
     * @param userID
     *          User id
     * @param params
     *          Map with with declared parameters specific for User class
     * @throws FailedOperation
     *          If some troubles were happened
     * @return clone of User object
     * */
    @Override
    public User changeUserData(int userID, Map<String, Object> params) throws FailedOperation {

        if (isValidID("User", userID)){
            throw new FailedOperation("Incorrect ID");
        }

        User user = model.getUserById(userID);

        String name = (String) params.getOrDefault("name", null);
        String phoneNumber = (String) params.getOrDefault("phoneNumber", null);
        String email = (String) params.getOrDefault("emailAddress", null);

        if (name != null){
            if (phoneNumber != null){
                if (email != null && isCorrectString(email, EMAIL_ADDRESS_STIRNG_PATTERN)){
                    user.setEmailAddress(email);
                }
                if (isCorrectString(phoneNumber, PHONE_NUMBER_STIRNG_PATTERN)) {
                    user.setPhoneNumber(phoneNumber);
                }
            }
            if (isCorrectString(name, USER_NAME_STIRNG_PATTERN)) {
                user.setName(name);
            }
        }

        try {
            model.save();
        } catch (IOException e) {
            throw new FailedOperation("Failed to add new user. Troubles with DB");
        }
        return (User) user.clone();
    }
    /**
     * Delete user and instantly save changes in a storage
     * @param userID
     *          User id
     * @throws FailedOperation
     *          If some troubles were happened
     * @return User wich was deleted
     * */
    @Override@Deprecated
    public User deleteUser(int userID) throws FailedOperation {
        if (isValidID("User", userID)){
            throw new FailedOperation("Incorrect ID");
        }
        // model.deleteUser(userID)
        return null;
    }
    /**
     * Return all supported services names
     * @return array of supported services names
     * */
    @Override
    public String[] getServices() {
        return this.SERVICES_NAME;
    }
    /**
     * Set {@code tariff} to user by his id  and instantly save changes in storage
     * @param userID
     *          User id
     * @param tariff
     *          Tariff object wich will be set up to user
     * @throws FailedOperation
     *          If some troubles were happened
     * @return tariff wich was set up to user
     * */
    @Override
    public Tariff setTariffToUser(int userID, Tariff tariff) throws FailedOperation {
        if (isValidID("User", userID)){
            throw new FailedOperation("Incorrect ID");
        }

        User user = model.getUserById(userID);

        Service newService, oldService;

        try {
            newService = tariff.getService();
            oldService = (Service) getService(getServiceType(newService), tariff.getId()).clone();
        } catch (CloneNotSupportedException e) {
            throw new FailedOperation("Some troubles with model :(");
        }
        if (tariff.isEdited() && !oldService.equals(newService) && !isServiceFieldCorrect(newService)) {
            throw new FailedOperation("Incorrect tariff data");
        }

        setObjectField(user, getServiceType(newService), newService);

        return tariff;
    }
    /**
     * Disable {@code serviceName} for user with such id
     * @param userID
     *          User id
     * @param serviceName
     *          Service name
     * @throws FailedOperation
     *          If some troubles were happened
     * @return tariff wich was disabled for user
     * */
    @Override
    public Tariff removeTariffFromUser(int userID, String serviceName) throws FailedOperation {

        if (isValidID("User", userID)){
            throw new FailedOperation("Incorrect ID");
        }

        User user = model.getUserById(userID);
        Service oldService = (Service) getObjectField(user, serviceName);
        setObjectField(user, serviceName, null);

        return new Tariff(oldService, -1);
    }
    /**
     * Creates new tariff based on {@code service} and instantly adds him to storage
     * @param service
     *          Configured Service object
     * @throws FailedOperation
     *          If some troubles were happened
     * @return clone of created Service object
     * */
    @Override
    public Tariff createTariff(Service service) throws FailedOperation{
        //TODO make up realization
        return new Tariff(service, 0);
    }
    /**
     * Search tariff by its id and name of its service
     * @param serviceName
     *          Service name
     * @param tariffID
     *          Tariff id
     * @throws FailedOperation
     *          If some troubles were happened
     * @return created Tariff based on found tariff object
     * */
    @Override
    public Tariff getTariff(String serviceName, int tariffID) throws FailedOperation {
        try {
            return new Tariff((Service) getService(serviceName, tariffID).clone(), tariffID);
        } catch (CloneNotSupportedException e) {
            throw new FailedOperation("Some troubles with model :(");
        }
    }
    /**
     * Returns all tariffs of {@code serviceName}
     * @param serviceName
     *          Service name
     * @throws FailedOperation
     *          If some troubles were happened
     * @return Array of created Tariffs
     * */
    @Override
    public Tariff[] getAllTariffs(String serviceName) throws FailedOperation {
        int count;
        Tariff[] result;
        switch (serviceName) {
            case "Internet":
                count = model.getInternetCount();
                result = new Tariff[count];
                for (int i = 0; i < count; i++) {
                    result[i] = new Tariff((Service) model.getInternetById(i).clone(), i);
                }
                break;
            case "Television":
                count = model.getTelevisionCount();
                result = new Tariff[count];
                for (int i = 0; i < count; i++) {
                    result[i] = new Tariff((Service) model.getTelevisionById(i).clone(), i);
                }
                break;
            case "Phone":
                count = model.getPhoneCount();
                result = new Tariff[count];
                for (int i = 0; i < count; i++) {
                    result[i] = new Tariff((Service) model.getPhoneById(i).clone(), i);
                }
                break;
            default:
                throw new FailedOperation("Unexpected service name: " + serviceName);
        }
        return result;
    }
    /**
     * Change tariff parameters and instantly save it in storage
     * @param tariffID
     *          Tariff id
     * @param tariff
     *          Tariff object wich will be changed
     * @throws FailedOperation
     *          If some troubles were happened
     * @return tariff wich was changed
     * */
    @Override
    public Tariff changeTariff(int tariffID, Tariff tariff) throws FailedOperation {
        Service newService;
        try {
            newService = tariff.getService();
        } catch (CloneNotSupportedException ex){
            throw new FailedOperation("Some troubles with model :(");
        }
        String serviceName = getServiceType(newService);

        if (isValidID(serviceName, tariffID)){
            throw new FailedOperation("Incorrect ID");
        }

        Service oldService = getService(serviceName, tariffID);

        if (tariff.isEdited() && !oldService.equals(newService)) {
            if (!isServiceFieldCorrect(newService)) {
                throw new FailedOperation("Incorrect tariff data");
            }
            migrateService(oldService, newService);
        }
        else{
            return tariff;
        }

        return tariff;
    }
    /**
     * Delete tariff and instantly save changes in a storage
     * @param serviceName
     *          Service name
     * @param tariffID
     *          Tariff id
     * @throws FailedOperation
     *          If some troubles were happened
     * @return Tariff wich was deleted
     * */
    @Override
    public Tariff deleteTariff(String serviceName, int tariffID) throws FailedOperation{
        //TODO make up realization
        return null;
    }
/**
 * @return Return Service object by Service name and id
 * */
    private Service getService(String serviceName, int id) throws FailedOperation {
        switch (serviceName) {
            case "Television":
                return model.getTelevisionById(id);
            case "Phone":
                return model.getPhoneById(id);
            case "Internet":
                return model.getInternetById(id);
            default:
                throw new FailedOperation("No such service");
        }
    }
    /**
     * Method update service in storage
     * @param migrateFrom
     *          Service wich should to be updated
     * @param migrateTo
     *          Service wich we want to set up
     * @throws FailedOperation
     *          Throws if some issues were happened
     * @return Updated Service
     **/
    private Service migrateService(Service migrateTo, Service migrateFrom) throws FailedOperation {
        Field[] concreteFields = migrateTo.getClass().getDeclaredFields();
        Field[] baseFields = migrateTo.getClass().getSuperclass().getDeclaredFields();
        for (Field field : concreteFields) {
            try {
                setObjectField(migrateTo, field.getName(), getObjectField(migrateFrom, field.getName()));
            } catch (FailedOperation failedOperation) {
                failedOperation.printStackTrace();
                throw new FailedOperation("Some troubles with model :(");
            }
        }
        for (Field field : baseFields) {
            try {
                field.set(migrateTo, field.get(migrateFrom));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new FailedOperation("Some troubles with model :(");
            }
        }
        return migrateTo;
    }
    /**
     * Get field of some object
     * @param object
     *          Object from what we are going to extract field
     * @param fieldName
     *          Name of field what we are going to extract
     * @throws FailedOperation
     *          Throws if some issues were happened
     * @return Object value of that field
     * */
    private Object getObjectField(Object object, String fieldName) throws FailedOperation {
        String capitalizedFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        try {
            Method a = object.getClass().getMethod("get" + capitalizedFieldName);
            return a.invoke(object);
        } catch (NoSuchMethodException e) {
            try {
                Method a = object.getClass().getMethod("is" + capitalizedFieldName);
                return a.invoke(object);
            } catch (NoSuchMethodException ex) {
                System.out.println(String.format("Object doesn't have field %s", fieldName));
            } catch (IllegalAccessException ex) {
                System.out.println(String.format("Field %s in object is private", fieldName));
            } catch (InvocationTargetException ex) {
                e.printStackTrace();
                System.out.println(String.format(
                        "Exception in model field name = %s",
                        fieldName
                ));
            }
        } catch (IllegalAccessException e) {
            System.out.println(String.format("Field %s in object is private", fieldName));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.out.println(String.format(
                    "Exception in model field name = %s",
                    fieldName
            ));
        }
        throw new FailedOperation("Failed to set such parameters");
    }
    /**
     * Set {@code param} to field of some object
     * @param object
     *          Object from what we are going to extract field
     * @param fieldName
     *          Name of field what we are going to extract
     * @param param
     *          Object that should be set to field
     * @throws FailedOperation
     *          Throws if some issues were happened
     * @return Object value of that field
     * */
    private Service setObjectField(Object object, String fieldName, Object param) throws FailedOperation {
        System.out.println("set " + fieldName + " to " + param + " in " + object.getClass().getName());
        String capitalizedFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        try {
            Method a = object.getClass().getMethod(
                    "set" + capitalizedFieldName,
                    param.getClass()
            );
            return (Service) a.invoke(object, new Object[]{param});
        } catch (NoSuchMethodException e) {
            System.out.println(String.format("Object doesn't have field %s", fieldName));
        } catch (IllegalAccessException e) {
            System.out.println(String.format("Field %s in object is private", fieldName));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.out.println(String.format(
                    "Exception in model field name = %s",
                    fieldName
            ));
        }
        throw new FailedOperation("Failed to set such parameters");
    }
    /**
     * Check whether string is corresponding with pattern or not
     * @param suspect
     *          Suspected string
     * @param stringPattern
     *          RegExp pattern
     * @return boolean
     * */
    private boolean isCorrectString(String suspect, String stringPattern){
        Pattern pattern = Pattern.compile(stringPattern);
        Matcher matcher = pattern.matcher(suspect);
        return matcher.matches();
    }
    /**
     * Check whether service field is correct or not
     * @param service
     *          Service
     * @return boolean
     * */
    private boolean isServiceFieldCorrect(Service service){
        //TODO make up realization
        return true;
    }
    /**
     * @return Type of service without package
     * */
    private String getServiceType(Service service){
        String[] temp = service.getClass().getName().split(".");
        return temp[temp.length - 1];
    }
    /**
     * @throws FailedOperation
     *          Throws if some issues were happened
     * @return If id correspond with some services or not
     * */
    private boolean isValidID(String type, int id) throws FailedOperation {
        int count;
        boolean result;

        switch (type){

            case "USER":
                count = model.getUserCount();
                result = (id >= count || id < 0);
            break;

            case "Internet":
                count = model.getInternetCount();
                result = (id >= count || id < 0);
            break;

            case "Television":
                count = model.getTelevisionCount();
                result = (id >= count || id < 0);
            break;

            case "Phone":
                count = model.getPhoneCount();
                result = (id >= count || id < 0);
            break;
            default:
                throw new FailedOperation("Unexpected serviceName: " + type);
        }

        return result;
    }
}

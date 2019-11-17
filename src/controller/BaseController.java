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

//TODO:
// - выбор готового тарифа
// - изменение тарифа
// - добавление тарифа
// - удаление тарифа
// - добавление пользователя
// - удаление пользователя
// - отказ от сервиса

public class BaseController implements Controller{

    private final String USER_NAME_STIRNG_PATTERN = "\\w{6}\\w*";
    private final String EMAIL_ADDRESS_STIRNG_PATTERN = "[\\w\\d]+@\\w+\\.\\w+";
    private final String PHONE_NUMBER_STIRNG_PATTERN = "\\+\\d{11}";

    private Model model;

    public BaseController(Model model) {
        this.model = model;
    }

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
        //TODO Вернуть копию объекта
        return user;
    }

    @Override
    public User getUser(Integer userID) {
        // model.isUserIDValid(userID)
        //TODO вернуть копию объекта
        return model.getUserById(userID);
    }

    @Override
    public User changeUserData(Integer userID, Map<String, Object> params) throws FailedOperation {
        // model.isUserIDValid(userID)
        User user = model.getUserById(userID);

        String name = (String) params.getOrDefault("name", null);
        String phoneNumber = (String) params.getOrDefault("phoneNumber", null);
        String email = (String) params.getOrDefault("emailAddress", null);

        if (name != null){
            if (phoneNumber != null){
                if (email != null && isCorrectString(email, "")){
                    user.setEmailAddress(email);
                }
                if (isCorrectString(phoneNumber, "")) {
                    user.setPhoneNumber(phoneNumber);
                }
            }
            if (isCorrectString(name, "")) {
                user.setName(name);
            }
        }

        try {
            model.save();
        } catch (IOException e) {
            throw new FailedOperation("Failed to add new user. Troubles with DB");
        }
        //TODO вернуть копию объекта
        return user;
    }

    @Override
    public User deleteUser(Integer userID) {
        // model.isUserIDValid(userID)
        // model.deleteUser(userID)
        return null;
    }

    @Override
    public String[] getServices() {
        String[] servicesName = {"Internet", "Phone", "Television"};
        return servicesName;
    }

    @Override
    public Service setTariffToUser(Integer userID, Tariff tariff) throws FailedOperation {
        // model.isUserIDValid(userID);
        User user = model.getUserById(userID);

        //заменить на конструктор копирования
        Service newService = tariff.getService();
        Service oldService = getService(getServiceType(newService), tariff.getId());
        if (tariff.isEdited() && !oldService.equals(newService) && !isServiceFieldCorrect(newService)) {
            throw new FailedOperation("Incorrect tariff data");
        }

        setObjectField(user, getServiceType(newService), newService);

        return tariff.getService();
    }

    @Override
    public Service changeUserTariff(Integer userID, Tariff tariff) throws FailedOperation {
        // model.isUserIDValid(userID);
        User user = model.getUserById(userID);
        Service oldService = (Service) getObjectField(user, getServiceType(tariff.getService()));
        setTariffToUser(userID, tariff);
        //TODO copying value
        return oldService;
    }

    @Override
    public Service removeTariffFromUser(Integer userID, String serviceName) throws FailedOperation {
        // model.isUserIDValid(userID);
        User user = model.getUserById(userID);
        Service oldService = (Service) getObjectField(user, serviceName);
        setObjectField(user, serviceName, null);
        //TODO copying
        return oldService;
    }

    @Override
    public Service createTariff(Service service) throws FailedOperation {
        if (isServiceFieldCorrect(service) ){
            //TODO copying service
            setObjectField(model, getServiceType(service), service);
        }
        return service;
    }

    @Override
    public Tariff getTariff(String serviceName, Integer tariffID) throws FailedOperation {
        return new Tariff(getService(serviceName, tariffID));
    }

    @Override
    public Service[] getAllTariffs(String serviceName) {
        return new Service[0];
    }

    @Override
    public Service changeTariff(Integer tariffID, Tariff tariff) throws FailedOperation {
        // isValidId(tariffID)
        Service newService = tariff.getService();
        String serviceName = getServiceType(newService);
        Service oldService = getService(serviceName, tariffID);

        if (tariff.isEdited() && !oldService.equals(newService)) {
            if (!isServiceFieldCorrect(newService)) {
                throw new FailedOperation("Incorrect tariff data");
            }
            migrateService(oldService, newService);
        }
        else{
            return newService;
        }

        return newService;
    }

    @Override
    public Service deleteTariff(String serviceName, Integer tariffID) {
        return null;
    }

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
    private Service migrateService(Service migrateTo, Service migrateFrom){
        Field[] concreteFields = migrateTo.getClass().getDeclaredFields();
        Field[] baseFields = migrateTo.getClass().getSuperclass().getDeclaredFields();
        for (Field field : concreteFields) {
            try {
                setObjectField(migrateTo, field.getName(), getObjectField(migrateFrom, field.getName()));
            } catch (FailedOperation failedOperation) {
                failedOperation.printStackTrace();
            }
        }
        for (Field field : baseFields) {
            try {
                field.set(migrateTo, field.get(migrateFrom));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return migrateTo;
    }
    private Object getObjectField(Object object, String fieldName) throws FailedOperation {
        String capitalizedFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        try {
            Method a = object.getClass().getMethod("get" + capitalizedFieldName);
            return a.invoke(object);
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
    private boolean isCorrectString(String suspect, String stringPattern){
        Pattern pattern = Pattern.compile(stringPattern);
        Matcher matcher = pattern.matcher(suspect);
        return matcher.matches();
    }
    private boolean isServiceFieldCorrect(Service service){

        return true;
    }
    private String getServiceType(Service service){
        String[] temp = service.getClass().getName().split(".");
        return temp[temp.length - 1];
    }
}

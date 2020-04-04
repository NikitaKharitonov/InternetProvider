package ru.internetprovider.controller;//package ru.internetprovider.controller;
//
//import ru.internetprovider.model.exceptions.ServiceNotFoundException;
//import ru.internetprovider.model.exceptions.UserNotFoundException;
//import ru.internetprovider.model.Model;
//import ru.internetprovider.model.services.Internet;
//import ru.internetprovider.model.services.Service;
//import ru.internetprovider.model.User;
//import ru.internetprovider.model.util.IdGenerator;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Date;
//
//public class BaseController implements Controller {
//
//    private Model ru.internetprovider.model;
//    private static final String[] SERVICES_NAME = { "Internet", "Phone", "Television" };
//
//    public BaseController(Model ru.internetprovider.model){
//        this.ru.internetprovider.model = ru.internetprovider.model;
//    }
//
//    @Override
//    public void createUser(User user) throws FailedOperation {
//        ru.internetprovider.model.addUser(user);
//        saveChanges();
//    }
//
//    @Override
//    public User getUser(int userID) throws FailedOperation {
//        try {
//            return ru.internetprovider.model.getUser(userID);
//        } catch (UserNotFoundException ex) {
//            throw new FailedOperation(ex);
//        }
//    }
//
//    @Override
//    //TODO: replace ot 'getServicesByUser'
//    public String[] getProvidedServices() {
//        return SERVICES_NAME;
//    }
//
//    @Override
//    public Service getService(long serviceID) throws FailedOperation {
//        try {
//            return ru.internetprovider.model.getServiceById(serviceID);
//        } catch (ServiceNotFoundException ex) {
//            throw new FailedOperation(ex);
//        }
//    }
//
//    @Override
//    public ArrayList<Service> getAllServices(String serviceType) throws FailedOperation {
//        ArrayList<Service> result = new ArrayList<>();
//        Service temp;
//        try{
//            for (long i = 1; i <= ru.internetprovider.model.getServiceCount(); i++) {
//                temp = ru.internetprovider.model.getServiceById(i);
//                if (temp.getType().equals(serviceType)){
//                    result.add(temp);
//                }
//            }
//            return result;
//        }
//        catch (ServiceNotFoundException ex){
//            throw new FailedOperation(ex);
//        }
//    }
//
//    @Override
//    //TODO: remove
//    public void setServiceToUser(long userID, Service service, Date date) throws FailedOperation {
////        try{
////            //ru.internetprovider.model.addServiceToUserById(userID, service, date, User.Status.ACTIVE);
////        }
////        catch (UserNotFoundException ex){
////            throw new FailedOperation(ex);
////        }
//        saveChanges();
//    }
//
//    @Override
//    public void changeUserData(User user) throws FailedOperation {
//        try{
//            ru.internetprovider.model.removeUserById(user.getId());
//            ru.internetprovider.model.addUser(user);
//        }
//        catch (UserNotFoundException ex){
//            throw new FailedOperation(ex);
//        }
//        saveChanges();
//    }
//
//    @Override
//    public void updateService(Service service) throws FailedOperation {
//        try{
//            ru.internetprovider.model.removeServiceById(service.getId());
//            ru.internetprovider.model.addService(service);
//        }
//        catch (ServiceNotFoundException ex){
//            throw new FailedOperation(ex);
//        }
//        saveChanges();
//    }
//
//    @Override
//    public void removeServiceFromUser(long userID, String serviceType) throws FailedOperation {
//        try{
//            User user = ru.internetprovider.model.re(userID);
//            //user.removeServiceByType(serviceType);
//        }
//        catch (UserNotFoundException ex){
//            throw new FailedOperation(ex);
//        }
//        saveChanges();
//    }
//
//    @Override
//    public void deleteUser(long userID) throws FailedOperation {
//        try {
//            ru.internetprovider.model.removeUserById(userID);
//        } catch (UserNotFoundException ex) {
//            throw new FailedOperation(ex);
//        }
//        saveChanges();
//    }
//
//    @Override
//    //todo: remove
//    public void deleteService(long serviceID) throws FailedOperation {
//        try {
//            ru.internetprovider.model.removeServiceById(serviceID);
//        } catch (ServiceNotFoundException e) {
//            throw new FailedOperation(e);
//        }
//        saveChanges();
//    }
//
//    @Override
//    //todo: remove
//    public long getNextServiceId() {
//        return serviceIdGenerator.next();
//    }
//
//    @Override
//    //todo: remove
//    public long getNextUserId() {
//        return userIdGenerator.next();
//    }
//
//    @Override
//    public Internet.ConnectionType getConnectionType(String connectionType) {
//        return Internet.ConnectionType.valueOf(connectionType);
//    }
//    @Override
//    public String getUsersData(){
//        return ru.internetprovider.model.getUserData();
//    }
//    @Override
//    public String getServicesData(){
//        return ru.internetprovider.model.getServiceData();
//    }
//
//    //todo: remove
//    private void saveChanges() throws FailedOperation {
//        try{
//            ru.internetprovider.model.save();
//        }
//        catch (IOException e) {
//            throw new FailedOperation(e);
//        }
//    }
//}

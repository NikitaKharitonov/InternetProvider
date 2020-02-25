//package controller;
//
//import model.exceptions.ServiceNotFoundException;
//import model.exceptions.UserNotFoundException;
//import model.Model;
//import model.services.Internet;
//import model.services.Service;
//import model.User;
//import model.util.IdGenerator;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Date;
//
//public class BaseController implements Controller {
//
//    private Model model;
//    private static final String[] SERVICES_NAME = { "Internet", "Phone", "Television" };
//
//    public BaseController(Model model){
//        this.model = model;
//    }
//
//    @Override
//    public void createUser(User user) throws FailedOperation {
//        model.addUser(user);
//        saveChanges();
//    }
//
//    @Override
//    public User getUser(int userID) throws FailedOperation {
//        try {
//            return model.getUser(userID);
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
//            return model.getServiceById(serviceID);
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
//            for (long i = 1; i <= model.getServiceCount(); i++) {
//                temp = model.getServiceById(i);
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
////            //model.addServiceToUserById(userID, service, date, User.Status.ACTIVE);
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
//            model.removeUserById(user.getId());
//            model.addUser(user);
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
//            model.removeServiceById(service.getId());
//            model.addService(service);
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
//            User user = model.re(userID);
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
//            model.removeUserById(userID);
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
//            model.removeServiceById(serviceID);
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
//        return model.getUserData();
//    }
//    @Override
//    public String getServicesData(){
//        return model.getServiceData();
//    }
//
//    //todo: remove
//    private void saveChanges() throws FailedOperation {
//        try{
//            model.save();
//        }
//        catch (IOException e) {
//            throw new FailedOperation(e);
//        }
//    }
//}

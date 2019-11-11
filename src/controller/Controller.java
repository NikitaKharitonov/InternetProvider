package controller;

import model.Model;
import model.User;
import model.services.Internet;
import model.services.Phone;
import model.services.Service;
import model.services.Television;
import util.Annotations.MethodParameter;
import util.Procedure;
import view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//TODO:
// - работа с тарифами

public class Controller {

    private Model model;
    private View view;


    private class Handler implements Procedure {
        private Controller controller;

        Handler(Controller controller){
            this.controller = controller;
        }
        @Override
        public void execute() {

            String type = view.getRequestType();
            String object = view.getObject();
            String subject = view.getSubject(); // User ID if it is
            Map<String, String> params = view.getParameters();

            if (params == null) params = new HashMap<>();

            if (object.equals("Services")){
                if (subject == null){
                    if (type.equals("get")) Router.routeTo(type + "Services", Map.class, params);
                }
                else{
                    params.put("userID", subject);
                    if (type.equals("get")) Router.routeTo(type + "UserServices", Map.class, params);
                }
            }
            else if (subject == null){
                params.put("serviceName", object);
                Router.routeTo(type + "Service", Map.class, params, "No such command '" + type + "'");
            }
            else{
                params.put("userID", subject);
                params.put("serviceName", object);
                Router.routeTo(type + "UserService", Map.class, params, "No such command '" + type + "'");
            }
        }
    }
    private static class Router{
        private static Controller controller;
        static void routeTo(String methodName, Class clz, Map<String, String> params, String msg){
            try {
                Method a = Controller.class.getDeclaredMethod(methodName, clz);
                a.invoke(controller, params);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                System.out.println(msg);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        static void routeTo(String methodName, Class clz, Map<String, String> params){
            routeTo(methodName, clz, params, "");
        }
        static Method getGetter(Object from, String fieldName) throws NoSuchMethodException {
            return from.getClass().getMethod("get" + fieldName);
        }
        static Method getSetter(Object from, String fieldName, Class type) throws NoSuchMethodException {
            return from.getClass().getMethod("set" + fieldName, type);
        }
    }

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;

        Router.controller = this;

        this.view.setHandler(new Handler(this));
    }

    @Deprecated
    private void getServices(Map<String, String> params){
        System.out.println("Get Services");
    }
    @Deprecated
    private void getService(Map<String, String> params){
        System.out.println("Get Service");
    }
    @Deprecated
    private void setService(Map<String, String> params){
        System.out.println("Set Service");
    }

    //WORK
    private void getUserServices(Map<String, String> params){
        String STATUS;
        String[] response;

        int userID = Integer.parseInt(params.get("userID"));
        User user = getUser(userID);
        String[] names = {"Internet", "Phone", "Television"};
        Service[] services = new Service[names.length];
        try {
            for (int i = 0; i < names.length; i++) {
                services[i] = getUserServiceByName(names[i], user);
            }
            STATUS = "OK";
            response = new String[services.length];
            for (int i = 0; i < response.length; i++) {
                response[i] = services[i] == null ? "User don't use " + names[i] : services[i].toString();
            }
        } catch (FailedOperation failedOperation) {
            STATUS = "BAD";
            response = new String[]{failedOperation.getMessage()};
        }
        view.printResponse(STATUS, response);
    }
    //WORK
    private void getUserService(Map<String, String> params){
        String STATUS;
        String[] response;

        int userID = Integer.parseInt(params.get("userID"));
        String serviceName = params.get("serviceName");

        User user = getUser(userID);
        Service service;

        try {
            service = getUserServiceByName(serviceName, user);
            STATUS = "OK";
            response = new String[]{service == null ? "User don't use " + serviceName : service.toString()};
        } catch (FailedOperation failedOperation) {
            STATUS = "BAD";
            response = new String[]{failedOperation.getMessage()};
        }
        view.printResponse(STATUS, response);
    }
    //WORK
    private void setUserService(Map<String, String> params){
        String STATUS;
        String[] response;

        int userID = Integer.parseInt(params.get("userID"));
        String serviceName = params.get("serviceName");
        Service service;

        params.remove("userID");
        params.remove("serviceName");
        User user = getUser(userID);

        // Стоит ли выполнять, если ошибка возникла не во всех полях?
        try {

            service = getUserServiceByName(serviceName, user);
            if (service == null){
                addServiceToUser(user, serviceName, params);
                STATUS = "OK";
                response = new String[]{"Success!"};
            }
            else{
                for (Map.Entry<String,String> entry : params.entrySet()){
                    setUserServiceField(service, entry.getKey(), getParam(entry.getValue()));
                }
                setServiceToUser(service, serviceName, user);
                STATUS = "OK";
                response = new String[]{"Success!"};
            }
        } catch (FailedOperation e) {
            STATUS = "BAD";
            response = new String[]{e.getMessage()};
        }
        view.printResponse(STATUS, response);
    }

    private void addServiceToUser(User user, String serviceName, Map<String, String> params) throws FailedOperation {

        Service service = createService(serviceName, params);
        setServiceToUser(service, serviceName, user);
    }

    private Service createService(String serviceName, Map<String, String> params) throws FailedOperation {
        ArrayList<Object> ctrParams = new ArrayList<>();
        try {
            // Вообще можно организовать поиск по всем конструкторам, но и так сойдёт, пока у нас один конструктор
            Constructor serviceCtr = Class.forName("model.services." + serviceName).getConstructors()[0];
            Annotation[][] Annotations = serviceCtr.getParameterAnnotations();
            for (java.lang.annotation.Annotation[] annotationRow : Annotations) {
                for (Annotation annotation : annotationRow) {
                    MethodParameter ann = (MethodParameter)annotation;
                    ctrParams.add( getParam(params.get(ann.name())) );
                }
            }
            return (Service) serviceCtr.newInstance(ctrParams.toArray());

        } catch (ClassNotFoundException e) {
            System.out.println("There is no " + serviceName + " service");
            //e.printStackTrace();
        } catch(NullPointerException ex){
            System.out.println("Wrong parameters!");
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new FailedOperation("Failed to create " + serviceName + " object");
    }

    private Object getParam(String p){
        Object result;
        try {
            result = Integer.parseInt(p);
        } catch (NumberFormatException parseToIntEx){
            try{
                result = Double.parseDouble(p);
            } catch (NumberFormatException parseToDoubleEx){
                try{
                    if (p.equals("true")) result = true;
                    else if (p.equals("false")) result = false;
                    else throw new Exception();
                } catch (Exception parseToBooleanEx) {
                    try{
                        result = (Date) new SimpleDateFormat("yyyy.MM.dd").parse(p);
                    } catch (ParseException parseToDateEx) {
                        result = p;
                    }
                }
            }
        }
        return result;
    }
    @Deprecated
    private Internet getUserInternet(String username){
        return model.getInternetById(Integer.parseInt(username));
    }
    @Deprecated
    private Television getUserTelevision(String username){
        return model.getTelevisionById(Integer.parseInt(username));
    }
    @Deprecated
    private Phone getUserPhone(String username){
        return model.getPhoneById(Integer.parseInt(username));
    }

    private User getUser(int userID) {return model.getUserById(userID);}
    //WORK
    private Service getUserServiceByName(String serviceName, User user) throws FailedOperation {
        System.out.println(serviceName);

        try {
            Method a = Router.getGetter(user, serviceName);
            // While testing without files produce nullptr
            return (Service) a.invoke(user);
        } catch (NoSuchMethodException e) {
            System.out.println(String.format("Service %s doesn't exist", serviceName));
            //e.printStackTrace();
        } catch (IllegalAccessException e) {
            //e.printStackTrace();
            System.out.println(String.format("Service '%s' is private", serviceName));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.out.println(String.format("Exception in model service name = %s", serviceName));
        }
        throw new FailedOperation("Failed to get user service");
    }
    //WORK
    private void setServiceToUser(Service service, String serviceName, User user) throws FailedOperation {

        try {
            Method a = Router.getSetter(user, serviceName, service.getClass());
            a.invoke(user, service);
            return;
        } catch (NoSuchMethodException e) {
            System.out.println(String.format("Service %s doesn't exist", serviceName));
        } catch (IllegalAccessException e) {
            System.out.println(String.format("Service '%s' is private", serviceName));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.out.println(String.format("Exception in model service name = %s", serviceName));
        }
        throw new FailedOperation("Failed to set service to user");
    }
    //WORK
    private Service setUserServiceField(Service service, String fieldName, Object param) throws FailedOperation {
        System.out.println("set " + fieldName + " to " + param + " in " + service.getClass().getName());

        try {
            Method a = Router.getSetter(service, fieldName, param.getClass());
            return (Service) a.invoke(service, new Object[]{param});
        } catch (NoSuchMethodException e) {
            System.out.println(String.format("Service %s doesn't have field %s", service.getName(), fieldName));
        } catch (IllegalAccessException e) {
            System.out.println(String.format("Field %s in service '%s' is private", fieldName, service.getName()));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.out.println(String.format(
                    "Exception in model service name = %s, field name = %s",
                    service.getName(),
                    fieldName
            ));
        }
        throw new FailedOperation("Failed to set user service field");
    }
}

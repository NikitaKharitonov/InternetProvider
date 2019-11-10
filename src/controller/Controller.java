package controller;

import model.Model;
import model.User;
import model.services.Internet;
import model.services.Phone;
import model.services.Service;
import model.services.Television;
import util.Procedure;
import view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/*
* QUERY STRUCTURE : TYPE OBJECT [SUBJECT [..PARAMS]]
* TYPE ["get", "set"]
* OBJECT [username]
* PARAMS [.]
**/
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
            String subject = view.getSubject();
            Map<String, String> params = view.getParameters();

            if (subject == null && object.equals("Services")){
                /*
                *   Params structure : null
                *   Because type can be only "get"
                * */
                Router.routeTo(type + "Services", Map.class, params);
            }
            if (object.equals("Services")){
                /*
                 *   Params structure : USERNAME
                 *   Type only "get"
                 * */
                Router.routeTo(type + "Services", Map.class, params);
            }
            else if (subject == null){
                /*
                 *   Params structure : SERVICE_NAME [..OTHER_PARAMS]
                 * */
                Router.routeTo(type + "Service", Map.class, params);
            }
            else{
                /*
                 *   Params structure : USERNAME SERVICE_NAME [..OTHER_PARAMS]
                 * */
                Router.routeTo(type + "UserService", Map.class, params);
            }
        }
    }
    private static class Router{
        private static Controller controller;
        static void routeTo(String methodName, Class clz, Map<String, String> params, String msg){
            try {
                Method a = Controller.class.getMethod(methodName, clz);
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
        private static Method getServiceGetterFromModel(String methodName){
            try {
                System.out.println(methodName);
                return Model.class.getMethod(methodName, int.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;

        Router.controller = this;

        this.view.setHandler(new Handler(this));
    }

    @Deprecated
    public void getServices(Map<String, String> params){
        System.out.println("Get Services");
    }

    @Deprecated
    public void getService(Map<String, String> params){
        System.out.println("Get Service");
    }
    @Deprecated
    public void setService(Map<String, String> params){
        System.out.println("Set Service");
    }

    public void getUserServices(Map<String, String> params){
        System.out.println("Get User Services");

        String username = params.get("Username");

        Internet internet = model.getInternetById(Integer.parseInt(username));
        Television television = model.getTelevisionById(Integer.parseInt(username));
        Phone phone = model.getPhoneById(Integer.parseInt(username));

        view.printResponse("USER_SERVICE_INFO_LIST",
                new String[]{
            internet.toString(), television.toString(), phone.toString()
        });


    }

    public void getUserService(Map<String, String> params){
        String username = params.get("Username");
        String serviceName = params.get("serviceName");
        Service service = getUserServiceByName(serviceName, username);
        view.printResponse("USER_SERVICE_INFO", service.toString());
    }
    public void setUserService(Map<String, String> params){
        //TODO make up this method
        String username = params.get("Username");
        String serviceName = params.get("serviceName");
        User user = getUser(username);

        List<String> parameters = Arrays.asList(username, serviceName);
        Service service = getUserServiceByName(serviceName, username);
    }

    public Internet getUserInternet(String username){
        return model.getInternetById(Integer.parseInt(username));
    }
    public Television getUserTelevision(String username){
        return model.getTelevisionById(Integer.parseInt(username));
    }
    public Phone getUserPhone(String username){
        return model.getPhoneById(Integer.parseInt(username));
    }
    public User getUser(String username) {return model.getUserById(Integer.parseInt(username));}
    public Service getUserServiceByName(String serviceName, String userName){
        Method a = Router.getServiceGetterFromModel("get" + serviceName + "ById");
        System.out.println(serviceName);
        try {
            // While testing without files produce nullptr
            return (Service) a.invoke(model, new Object[]{ Integer.parseInt(userName) });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.out.println("Method is private");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}

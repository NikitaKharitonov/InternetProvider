package view;

import controller.Controller;
import controller.FailedOperation;
import model.*;
import model.services.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleView implements View {

    Controller controller;

    public ConsoleView(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void run() throws IOException, FailedOperation {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String line = reader.readLine();
            if (line.equals("exit")) {
                break;
            } else if (line.equals("")) {
                System.out.println("It was an empty string. Try again please.");
                continue;
            }
            Command command = Command.parseCommand(line);
            if (command != null) {
                processCommand(command, line);
            } else {
                System.out.println("The given command is not a valid. Try again please.");
            }
        }
    }

    private void processCommand(Command command, String line) throws FailedOperation {
        switch (command) {
            case GET_USER:
                Pattern pattern = Pattern.compile(Command.GET_USER.getRegex(), Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(line);
                matcher.find();

                int userID = Integer.parseInt(matcher.group(1));
                User user = controller.getUser(userID);

                System.out.println("GET_USER id=" + userID);
                if (user != null) {
                    System.out.println(user.getId());
                    System.out.println(user.getName());
                    System.out.println(user.getPhoneNumber());
                    System.out.println(user.getEmailAddress());
                } else {
                    System.out.println("User is not found");
                }
                break;

            case GET_USER_SERVICE:
                pattern = Pattern.compile(Command.GET_USER_SERVICE.getRegex(), Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(line);
                matcher.find();

                userID = Integer.parseInt(matcher.group(1));
                String serviceType = matcher.group(2);
                user = controller.getUser(userID);

                System.out.println("GET_USER_SERVICE id=" + userID + " service=" + serviceType);
                if (user != null) {
                    try {
                        System.out.println(user.getServiceIdByType(serviceType));
                    } catch (IllegalArgumentException e) {
                        System.out.println("User service is not found");
                    }
                } else {
                    System.out.println("User is not found");
                }
                break;

            case GET_SERVICE:
                pattern = Pattern.compile(Command.GET_SERVICE.getRegex(), Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(line);
                matcher.find();

                serviceType = matcher.group(1);
                long serviceID = Integer.parseInt(matcher.group(2));
                System.out.println("GET_SERVICE service=" + serviceType + " serviceID=" + serviceID);

                Service service = controller.getService(serviceID);
                if (service != null && service.getClass().getSimpleName().equals(serviceType)) {
                    System.out.println(service.toString());
                } else {
                    System.out.println("Service is not found");
                }
                break;

            case GET_SERVICES:
                pattern = Pattern.compile(Command.GET_SERVICES.getRegex(), Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(line);
                matcher.find();

                serviceType = matcher.group(1);
                System.out.println("GET_SERVICES service=" + serviceType);

                ArrayList<Service> services = controller.getAllServices(serviceType);
                if (!services.isEmpty()) {
                    for (Service curService : services)
                        System.out.println(curService.toString());
                } else {
                    System.out.println("No services found");
                }
                break;

            case CREATE_USER:
                pattern = Pattern.compile(Command.CREATE_USER.getRegex(), Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(line);
                matcher.find();

                String name = matcher.group(1);
                String phoneNumber = matcher.group(2);
                String email = matcher.group(3);
                System.out.println("CREATE_USER name=" + name + " phone=" + phoneNumber + " email=" + email);

                controller.createUser(new User(controller.getNextUserId(), name, phoneNumber, email));
                break;

            case CREATE_INTERNET:
                pattern = Pattern.compile(Command.CREATE_INTERNET.getRegex(), Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(line);
                matcher.find();

                name = matcher.group(1);
                Integer speed = Integer.parseInt(matcher.group(2));
                boolean isAntivirus = Boolean.parseBoolean(matcher.group(3));
                String strConnectionType = matcher.group(4);

                Internet.ConnectionType connectionType = controller.getConnectionType(strConnectionType);
                System.out.println("CREATE_INTERNET name=" + name + " speed=" + speed + " isAntivirus=" + isAntivirus +
                        " connectionType=" + strConnectionType);

                controller.createService(new Internet(controller.getNextServiceId(), name, speed, isAntivirus, connectionType));
                break;

            case CREATE_PHONE:
                pattern = Pattern.compile(Command.CREATE_PHONE.getRegex(), Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(line);
                matcher.find();

                name = matcher.group(1);
                int callsMinCount = Integer.parseInt(matcher.group(2));
                int smsCount = Integer.parseInt(matcher.group(3));
                System.out.println("CREATE_PHONE name=" + name + " callsMinCount=" + callsMinCount + " smsCount=" + smsCount);

                controller.createService(new Phone(controller.getNextServiceId(), name, callsMinCount, smsCount));
                break;

            case CREATE_TELEVISION:
                pattern = Pattern.compile(Command.CREATE_TELEVISION.getRegex(), Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(line);
                matcher.find();

                name = matcher.group(1);
                int numberOfChannels = Integer.parseInt(matcher.group(2));
                System.out.println("CREATE_TELEVISION name=" + name + " numberOfChannels=" + numberOfChannels);

                controller.createService(new Television(controller.getNextServiceId(), name, numberOfChannels));
                break;

            case CHANGE_USER:
                pattern = Pattern.compile(Command.CHANGE_USER.getRegex(), Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(line);
                matcher.find();

                userID = Integer.parseInt(matcher.group(1));
                user = controller.getUser(userID);
                if (user != null) {
                    if (matcher.group(3) != null) {
                        name = matcher.group(3);
                    } else {
                        // Копирование name
                        name = user.getName();
                    }
                    if (matcher.group(5) != null) {
                        phoneNumber = matcher.group(5);
                    } else {
                        // Копирование phone
                        phoneNumber = user.getPhoneNumber();
                    }
                    if (matcher.group(7) != null) {
                        email = matcher.group(7);
                    } else {
                        // Копирование email
                        email = user.getEmailAddress();
                    }
                    controller.changeUserData(new User(userID, name, phoneNumber, email));
                    System.out.println("CHANGE_USER idUser=" + userID + " name=" + name + " phone=" + phoneNumber +
                            " email=" + email);
                } else {
                    System.out.println("User is not found");
                }
                break;

            case CHANGE_USER_INTERNET:
                pattern = Pattern.compile(Command.CHANGE_USER_INTERNET.getRegex(), Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(line);
                matcher.find();

                userID = Integer.parseInt(matcher.group(1));
                user = controller.getUser(userID);
                if (user != null) {
                    try {
                        ActivatedService userService = user.getServiceMap().get("Internet");
                        serviceID = userService.getServiceID();
                        Internet internet = (Internet) controller.getService(serviceID);

                        if (matcher.group(3) != null) {
                            name = matcher.group(3);
                        } else {
                            // Копирование name
                            name = internet.getName();
                        }
                        if (matcher.group(5) != null) {
                            speed = Integer.parseInt(matcher.group(5));
                        } else {
                            // Копирование speed
                            speed = internet.getSpeed();
                        }
                        if (matcher.group(7) != null) {
                            isAntivirus = Boolean.parseBoolean(matcher.group(7));
                        } else {
                            // Копирование isAntivirus
                            isAntivirus = internet.isAntivirus();
                        }
                        if (matcher.group(9) != null) {
                            strConnectionType = matcher.group(9);
                            connectionType = controller.getConnectionType(strConnectionType);
                        } else {
                            // Копирование connectionType
                            connectionType = internet.getConnectionType();
                            strConnectionType = connectionType.toString();
                        }
                        System.out.println("CHANGE_USER_INTERNET userID=" + userID + " name=" + name + " speed=" +
                                speed + " isAntivirus=" + isAntivirus + " connectionType=" + strConnectionType);
                        controller.setServiceToUser(userID, new Internet(serviceID, name, speed, isAntivirus, connectionType));
                    } catch (IllegalArgumentException e) {
                        System.out.println("User service is not found");
                    }
                } else {
                    System.out.println("User is not found");
                }
                break;

            case CHANGE_USER_PHONE:
                pattern = Pattern.compile(Command.CHANGE_USER_PHONE.getRegex(), Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(line);
                matcher.find();

                userID = Integer.parseInt(matcher.group(1));
                user = controller.getUser(userID);
                if (user != null) {
                    try {
                        ActivatedService userService = user.getServiceMap().get("Phone");
                        serviceID = userService.getServiceID();
                        Phone phone = (Phone) controller.getService(serviceID);
                        if (matcher.group(3) != null) {
                            name = matcher.group(3);
                        } else {
                            // Копирование name
                            name = phone.getName();
                        }
                        if (matcher.group(5) != null) {
                            callsMinCount = Integer.parseInt(matcher.group(5));
                        } else {
                            // Копирование callsMinCount
                            callsMinCount = phone.getCallsMinCount();
                        }
                        if (matcher.group(7) != null) {
                            smsCount = Integer.parseInt(matcher.group(7));
                        } else {
                            // Копирование smsCount
                            smsCount = phone.getSmsCount();
                        }
                        System.out.println("CHANGE_USER_PHONE userID=" + userID + " name=" + name + " callsMinCount=" +
                                callsMinCount + " smsCount=" + smsCount);

                        controller.setServiceToUser(userID, new Phone(serviceID, name, callsMinCount, smsCount));
                    } catch (IllegalArgumentException e) {
                        System.out.println("User service is not found");
                    }
                } else {
                    System.out.println("User is not found");
                }
                break;

            case CHANGE_USER_TELEVISION:
                pattern = Pattern.compile(Command.CHANGE_USER_TELEVISION.getRegex(), Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(line);
                matcher.find();

                userID = Integer.parseInt(matcher.group(1));
                user = controller.getUser(userID);
                if (user != null) {
                    try {
                        ActivatedService userService = user.getServiceMap().get("Television");
                        serviceID = userService.getServiceID();
                        Television television = (Television) controller.getService(serviceID);
                        if (matcher.group(3) != null) {
                            name = matcher.group(3);
                        } else {
                            // Копирование name
                            name = television.getName();
                        }
                        if (matcher.group(5) != null) {
                            numberOfChannels = Integer.parseInt(matcher.group(5));
                        } else {
                            // Копирование numberOfChannels
                            numberOfChannels = television.getNumberOfChannels();
                        }
                        System.out.println("CHANGE_USER_TELEVISION userID=" + userID + " name=" + name +
                                " numberOfChannels=" + numberOfChannels);

                        controller.setServiceToUser(userID, new Television(serviceID, name, numberOfChannels));
                    } catch (IllegalArgumentException e) {
                        System.out.println("User service is not found");
                    }
                } else {
                    System.out.println("User is not found");
                }
                break;

            case SET_USER_SERVICE:
                pattern = Pattern.compile(Command.SET_USER_SERVICE.getRegex(), Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(line);
                matcher.find();

                userID = Integer.parseInt(matcher.group(1));
                serviceID = Integer.parseInt(matcher.group(2));
                System.out.println("SET_USER_SERVICE userID=" + userID + " serviceID=" + serviceID);

                controller.setServiceToUser(userID, serviceID);
                break;

            case CHANGE_INTERNET:
                pattern = Pattern.compile(Command.CHANGE_INTERNET.getRegex(), Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(line);
                matcher.find();

                serviceID = Integer.parseInt(matcher.group(1));
                service = controller.getService(serviceID);
                if (service != null) {
                    Internet internet = (Internet) service;
                    if (matcher.group(3) != null) {
                        name = matcher.group(3);
                    } else {
                        // Копирование name
                        name = internet.getName();
                    }
                    if (matcher.group(5) != null) {
                        speed = Integer.parseInt(matcher.group(5));
                    } else {
                        // Копирование speed
                        speed = internet.getSpeed();
                    }
                    if (matcher.group(7) != null) {
                        isAntivirus = Boolean.parseBoolean(matcher.group(7));
                    } else {
                        // Копирование isAntivirus
                        isAntivirus = internet.isAntivirus();
                    }
                    if (matcher.group(9) != null) {
                        strConnectionType = matcher.group(9);
                        connectionType = controller.getConnectionType(strConnectionType);
                    } else {
                        // Копирование connectionType
                        connectionType = internet.getConnectionType();
                        strConnectionType = connectionType.toString();
                    }
                    controller.changeService(new Internet(serviceID, name, speed, isAntivirus, connectionType));
                    System.out.println("CHANGE_INTERNET serviceID=" + serviceID + " name=" + name + " speed=" + speed +
                            " isAntivirus=" + isAntivirus + " connectionType=" + strConnectionType);
                } else {
                    System.out.println("Service is not found");
                }
                break;

            case CHANGE_TELEVISION:
                pattern = Pattern.compile(Command.CHANGE_TELEVISION.getRegex(), Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(line);
                matcher.find();

                serviceID = Integer.parseInt(matcher.group(1));
                service = controller.getService(serviceID);
                if (service != null) {
                    Television television = (Television) service;
                    if (matcher.group(3) != null) {
                        name = matcher.group(3);
                    } else {
                        // Копирование name
                        name = television.getName();
                    }
                    if (matcher.group(5) != null) {
                        numberOfChannels = Integer.parseInt(matcher.group(5));
                    } else {
                        numberOfChannels = television.getNumberOfChannels();
                    }
                    controller.changeService(new Television(serviceID, name, numberOfChannels));
                }
                break;

            case CHANGE_PHONE:
                pattern = Pattern.compile(Command.CHANGE_PHONE.getRegex(), Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(line);
                matcher.find();

                serviceID = Integer.parseInt(matcher.group(1));
                service = controller.getService(serviceID);
                if (service != null) {
                    Phone phone = (Phone) service;
                    if (matcher.group(3) != null) {
                        name = matcher.group(3);
                    } else {
                        // Копирование name
                        name = phone.getName();
                    }
                    if (matcher.group(5) != null) {
                        callsMinCount = Integer.parseInt(matcher.group(5));
                    } else {
                        callsMinCount = phone.getCallsMinCount();
                    }
                    if (matcher.group(7) != null) {
                        smsCount = Integer.parseInt(matcher.group(7));
                    } else {
                        smsCount = phone.getSmsCount();
                    }
                    controller.changeService(new Phone(serviceID, name, callsMinCount, smsCount));
                }
                break;

            case DELETE_USER:
                pattern = Pattern.compile(Command.DELETE_USER.getRegex(), Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(line);
                matcher.find();

                userID = Integer.parseInt(matcher.group(1));
                controller.deleteUser(userID);
                System.out.println("DELETE_USER id=" + userID);
                break;

            case DELETE_SERVICE:
                pattern = Pattern.compile(Command.DELETE_SERVICE.getRegex(), Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(line);
                matcher.find();

                serviceType = matcher.group(1);
                serviceID = Integer.parseInt(matcher.group(2));
                service = controller.getService(serviceID);
                if (service != null && service.getType().equals(serviceType)) {
                    controller.deleteService(serviceID);
                } else {
                    System.out.println("Service is not found");
                }
                System.out.println("DELETE_SERVICE serviceType=" + serviceType + " id=" + serviceID);
                break;

            case DELETE_USER_SERVICE:
                pattern = Pattern.compile(Command.DELETE_USER_SERVICE.getRegex(), Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(line);
                matcher.find();

                userID = Integer.parseInt(matcher.group(1));
                serviceType = matcher.group(2);
                controller.removeServiceFromUser(userID, serviceType);
                break;
        }
    }
}

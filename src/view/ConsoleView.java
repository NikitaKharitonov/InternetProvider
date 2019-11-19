package view;

import controller.Controller;
import controller.FailedOperation;
import controller.Tariff;
import model.User;
import model.services.Internet;
import model.services.Phone;
import model.services.Service;
import model.services.Television;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleView implements View {

    Controller controller;
    boolean isRunning;

    public ConsoleView(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void run() throws IOException {
        isRunning = true;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (isRunning) {
            String line = reader.readLine();
            if (line.equals("exit")) {
                isRunning = false;
            } else if (line.equals("")){
                System.out.println("It was an empty string. Try again please.");
                continue;
            }
            final Optional<Command> optCommand = Command.parseCommand(line);
            if (optCommand.isPresent()) {
                try {
                    processCommand(optCommand.get(), line);
                }
                catch (FailedOperation e){
                    System.out.println(e.getMessage());
                } catch (CloneNotSupportedException e){
                    System.out.println("Some unexpected behavior");
                }
            } else {
                System.out.println("The given command is not a valid. Try again please.");
            }
        }
    }

    void processCommand(final Command command, final String line)
            throws FailedOperation, CloneNotSupportedException {

        switch (command) {
            case GET_USER:
                Pattern p = Pattern.compile(Command.GET_USER.getRegex());
                Matcher m = p.matcher(line);
                m.find();
                User user = controller.getUser(Integer.parseInt(m.group(1)));
                System.out.println("User (");
                System.out.println("\tname: " + user.getName());
                System.out.println("\tphone: " + user.getPhoneNumber());
                System.out.println("\temail: " + user.getEmailAddress());
                System.out.println(")");
                break;
            case GET_SERVICES:
                p = Pattern.compile(Command.GET_SERVICES.getRegex());
                m = p.matcher(line);
                m.find();
                String[] services = controller.getServices();
                for (String service : services) {
                    System.out.println(service);
                }
                break;
            case GET_TARIFF:
                p = Pattern.compile(Command.GET_TARIFF.getRegex());
                m = p.matcher(line);
                m.find();
                System.out.println(controller.getTariff(m.group(1), Integer.parseInt(m.group(2))).getService().toString());
                break;
            case GET_TARIFFS:
                p = Pattern.compile(Command.GET_TARIFFS.getRegex());
                m = p.matcher(line);
                m.find();
                for (Tariff tariff : controller.getAllTariffs(m.group(1))) {
                    System.out.println(tariff.getService().toString());
                }
                break;
            case CREATE_USER:
                p = Pattern.compile(Command.CREATE_USER.getRegex());
                m = p.matcher(line);
                m.find();
                Map<String, Object> params = new HashMap<>();
                params.put("name", m.group(1));
                params.put("phone", m.group(2));
                params.put("email", m.group(3));
                user = controller.createUser(params);
                System.out.println("User (");
                System.out.println("\tname: " + user.getName());
                System.out.println("\tphone: " + user.getPhoneNumber());
                System.out.println("\temail: " + user.getEmailAddress());
                System.out.println(")");
                break;
            case CREATE_INTERNET:
                p = Pattern.compile(Command.CREATE_INTERNET.getRegex());
                m = p.matcher(line);
                m.find();
                String name = m.group(1);
                Date date = new Date();
                int speed = Integer.parseInt(m.group(2));
                boolean antivirus = Boolean.parseBoolean(m.group(3));
                Internet.ConnectionType connectionType;
                String connection = m.group(4);
                if (connection.equals("ADSL")) {
                    connectionType = Internet.ConnectionType.ADSL;
                } else if (connection.equals("DialUp")) {
                    connectionType = Internet.ConnectionType.Dial_up;
                } else if (connection.equals("Cable")) {
                    connectionType = Internet.ConnectionType.Cable;
                } else if (connection.equals("Fiber")) {
                    connectionType = Internet.ConnectionType.Fiber;
                } else {
                    connectionType = Internet.ConnectionType.ISDN;
                }
                Tariff tariff = controller.createTariff(new Internet(name, date, 1, speed, antivirus, connectionType));
                System.out.println(tariff.getService().toString());
                break;
            case CREATE_PHONE:
                p = Pattern.compile(Command.CREATE_PHONE.getRegex());
                m = p.matcher(line);
                m.find();
                name = m.group(1);
                date = new Date();
                int callsMinCount = Integer.parseInt(m.group(2));
                int smsCount = Integer.parseInt(m.group(3));
                tariff = controller.createTariff(new Phone(name, date, 1, callsMinCount, smsCount));
                System.out.println(tariff.getService().toString());
                break;
            case CREATE_TELEVISION:
                p = Pattern.compile(Command.CREATE_TELEVISION.getRegex());
                m = p.matcher(line);
                m.find();
                name = m.group(1);
                date = new Date();
                int numberOfChannels = Integer.parseInt(m.group(2));
                tariff = controller.createTariff(new Television(name, date, 1, numberOfChannels));
                System.out.println(tariff.getService().toString());
                break;
            case CHANGE_USER:
                p = Pattern.compile(Command.CHANGE_USER.getRegex());
                m = p.matcher(line);
                m.find();
                params = new HashMap<>();
                if (m.group(3) != null) {
                    params.put("name", m.group(3));
                }
                if (m.group(5) != null) {
                    params.put("phone", m.group(5));
                }
                if (m.group(7) != null) {
                    params.put("email", m.group(7));
                }
                user = controller.changeUserData(Integer.parseInt(m.group(1)), params);
                System.out.println("User (");
                System.out.println("\tname: " + user.getName());
                System.out.println("\tphone: " + user.getPhoneNumber());
                System.out.println("\temail: " + user.getEmailAddress());
                System.out.println(")");
                break;
            case CHANGE_INTERNET: // Необходимо дописать
                p = Pattern.compile(Command.CHANGE_INTERNET.getRegex());
                m = p.matcher(line);
                m.find();
                Internet oldInternet = (Internet)(controller.getTariff("Internet", Integer.parseInt(m.group(1))).getService());
                date = new Date();
                if (m.group(3) != null) {
                    name = m.group(3);
                } else {
                    name = oldInternet.getName();
                }
                if (m.group(5) != null) {
                    speed = Integer.parseInt(m.group(5));
                } else {
                    speed = oldInternet.getSpeed();
                }
                if (m.group(7) != null) {
                    antivirus = Boolean.parseBoolean(m.group(7));
                } else {
                    antivirus = oldInternet.isAntivirus();
                }
                if (m.group(9) != null) {
                    connection = m.group(9);
                    if (connection.equals("ADSL")) {
                        connectionType = Internet.ConnectionType.ADSL;
                    } else if (connection.equals("DialUp")) {
                        connectionType = Internet.ConnectionType.Dial_up;
                    } else if (connection.equals("Cable")) {
                        connectionType = Internet.ConnectionType.Cable;
                    } else if (connection.equals("Fiber")) {
                        connectionType = Internet.ConnectionType.Fiber;
                    } else {
                        connectionType = Internet.ConnectionType.ISDN;
                    }
                } else {
                    connectionType = oldInternet.getConnectionType();
                }
                //Service newInternet = new Internet(name, date, 1, speed, antivirus, connectionType);
                // Изменение тарифа
                break;
            case CHANGE_PHONE:
                p = Pattern.compile(Command.CHANGE_PHONE.getRegex());
                m = p.matcher(line);
                m.find();
                Phone oldPhone = (Phone)(controller.getTariff("Phone", Integer.parseInt(m.group(1))).getService());
                date = new Date();
                if (m.group(3) != null) {
                    name = m.group(3);
                } else {
                    name = oldPhone.getName();
                }
                if (m.group(5) != null) {
                    callsMinCount = Integer.parseInt(m.group(5));
                } else {
                    callsMinCount = oldPhone.getCallsMinCount();
                }
                if (m.group(7) != null) {
                    smsCount = Integer.parseInt(m.group(7));
                } else {
                    smsCount = oldPhone.getSmsCount();
                }
                // Service newPhone = new Phone(name, date, 1, callsMinCount, smsCount);
                // Изменение тарифа
                break;
            case CHANGE_TELEVISION:
                p = Pattern.compile(Command.CHANGE_TELEVISION.getRegex());
                m = p.matcher(line);
                m.find();
                Television oldTelevision = (Television)(controller.getTariff("Television", Integer.parseInt(m.group(1))).getService());
                date = new Date();
                if (m.group(3) != null) {
                    name = m.group(3);
                } else {
                    name = oldTelevision.getName();
                }
                if (m.group(5) != null) {
                    numberOfChannels = Integer.parseInt(m.group(5));
                } else {
                    numberOfChannels = oldTelevision.getNumberOfChannels();
                }
                // Service newTelevision = new Television(name, date, 1, numberOfChannels);
                // Изменение тарифа
                break;
            case SET_USER_TARIFF:
                p = Pattern.compile(Command.SET_USER_TARIFF.getRegex());
                m = p.matcher(line);
                m.find();
                // Изменение пользовательского тарифа
                break;
            case DELETE_USER:
                p = Pattern.compile(Command.DELETE_USER.getRegex());
                m = p.matcher(line);
                m.find();
                controller.deleteUser(Integer.parseInt(m.group(1)));
                System.out.println("User id=" + m.group(1) + " has been deleted");
                break;
            case DELETE_TARIFF:
                p = Pattern.compile(Command.DELETE_TARIFF.getRegex());
                m = p.matcher(line);
                m.find();
                controller.deleteTariff(m.group(1), Integer.parseInt(m.group(2)));
                System.out.println(m.group(1) + " id=" + m.group(2) + " has been deleted");
                break;
            case DELETE_TARIFF_FROM_USER:
                p = Pattern.compile(Command.DELETE_TARIFF_FROM_USER.getRegex());
                m = p.matcher(line);
                m.find();
                controller.removeTariffFromUser(Integer.parseInt(m.group(1)), m.group(2));
                System.out.println(m.group(2) + " has been deleted from User id=" + m.group(1));
                break;
        }
    }
}

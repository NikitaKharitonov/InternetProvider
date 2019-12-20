package view;

import controller.Controller;
import controller.FailedOperation;

import java.io.*;
import java.util.HashMap;
import java.util.regex.Matcher;

public class ConsoleView implements View {

    Helper helper;
    private BufferedReader reader;
    private BufferedWriter writer;

    public ConsoleView(Controller controller) {
        this.helper = new Helper(controller, System.out);
        this.writer = new BufferedWriter( new OutputStreamWriter(System.out));
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }
    public ConsoleView(Controller controller, InputStream inputStream, OutputStream outputStream) {
        this.helper = new Helper(controller, outputStream);
        this.writer = new BufferedWriter( new OutputStreamWriter(outputStream));
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    @Override
    public void run() throws IOException {
        println("\"help\" for information, \"exit\" for exit.");
        println("Waiting for input.");

        while (true) {
            String line = reader.readLine();
            if (line.equals("")) {
                println("It was an empty string. Try again please.");
                continue;
            }
            else if (line.toLowerCase().equals("exit")){
                break;
            }
            Command command = Command.parseCommand(line);
            if (command != null) {
                try {
                    processCommand(command, line);
                } catch (FailedOperation e) {
                    println(e.getMessage());
                    println("Try again please.");
                }
            } else {
                println("The given command is not a valid. Try again please.");
                println("\"help\" for information.");
            }
        }
    }

    private void processCommand(Command command, String line)
            throws FailedOperation {
        Matcher matcher;
        HashMap<String, String> params = new HashMap<>();
        switch (command) {
            case GET_USER:
                matcher = Command.getMatcher(command, line);
                helper.printUser(Integer.parseInt(matcher.group(1)));
                break;

            case GET_USERS:
                helper.printUsers();
                break;

            case GET_USER_SERVICE:
                matcher = Command.getMatcher(command, line);
                helper.printUserService(Integer.parseInt(matcher.group(1)), matcher.group(2));
                break;

            case GET_SERVICE:
                matcher = Command.getMatcher(command, line);
                helper.printService(Long.parseLong(matcher.group(1)));
                break;

            case GET_SERVICES:
                helper.printServices();
                break;

            case CREATE_USER:
                matcher = Command.getMatcher(command, line);
                helper.createUser(matcher.group(1), matcher.group(2), matcher.group(3));
                break;

            case CREATE_INTERNET:
                matcher = Command.getMatcher(command, line);
                helper.createInternet(matcher.group(1), Integer.parseInt(matcher.group(2)),
                        Boolean.parseBoolean(matcher.group(3)), matcher.group(4));
                break;

            case CREATE_PHONE:
                matcher = Command.getMatcher(command, line);
                helper.createPhone(matcher.group(1), Integer.parseInt(matcher.group(2)),
                        Integer.parseInt(matcher.group(3)));
                break;

            case CREATE_TELEVISION:
                matcher = Command.getMatcher(command, line);
                helper.createTelevision(matcher.group(1), Integer.parseInt(matcher.group(2)));
                break;

            case CHANGE_USER:
                matcher = Command.getMatcher(command, line);
                if (matcher.group(3) != null) {
                    params.put("name", matcher.group(3));
                }
                if (matcher.group(5) != null) {
                    params.put("phone", matcher.group(5));
                }
                if (matcher.group(7) != null) {
                    params.put("email", matcher.group(7));
                }
                helper.changeUser(Integer.parseInt(matcher.group(1)), params);
                break;

            case CHANGE_INTERNET:
                matcher = Command.getMatcher(command, line);
                if (matcher.group(3) != null) {
                    params.put("name", matcher.group(3));
                }
                if (matcher.group(5) != null) {
                    params.put("speed", matcher.group(5));
                }
                if (matcher.group(7) != null) {
                    params.put("antivirus", matcher.group(7));
                }
                if (matcher.group(9) != null) {
                    params.put("connectionType", matcher.group(9));
                }
                helper.changeInternet(Integer.parseInt(matcher.group(1)), params);
                break;

            case CHANGE_PHONE:
                matcher = Command.getMatcher(command, line);
                if (matcher.group(3) != null) {
                    params.put("name", matcher.group(3));
                }
                if (matcher.group(5) != null) {
                    params.put("callsMinCount", matcher.group(5));
                }
                if (matcher.group(7) != null) {
                    params.put("smsCount", matcher.group(7));
                }
                helper.changePhone(Integer.parseInt(matcher.group(1)), params);
                break;

            case CHANGE_TELEVISION:
                matcher = Command.getMatcher(command, line);
                if (matcher.group(3) != null) {
                    params.put("name", matcher.group(3));
                }
                if (matcher.group(5) != null) {
                    params.put("numberOfChannels", matcher.group(3));
                }
                helper.changeTelevision(Integer.parseInt(matcher.group(1)), params);
                break;

            case SET_USER_SERVICE:
                matcher = Command.getMatcher(command, line);
                helper.setUserService(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
                break;

            case DELETE_USER:
                matcher = Command.getMatcher(command, line);
                helper.deleteUser(Integer.parseInt(matcher.group(1)));
                break;

            case DELETE_SERVICE:
                matcher = Command.getMatcher(command, line);
                helper.deleteService(Integer.parseInt(matcher.group(1)));
                break;

            case DELETE_USER_SERVICE:
                matcher = Command.getMatcher(command, line);
                helper.deleteUserService(Integer.parseInt(matcher.group(1)), matcher.group(2));
                break;

            case HELP:
                for (Command curCommand : Command.values()) {
                    println(curCommand.name() + " \"" + curCommand.getRegex() + "\"");
                }
                break;
        }
    }

    private void println(String msg){
        try {
            writer.write(msg);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

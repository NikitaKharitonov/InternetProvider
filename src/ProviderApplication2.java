import controller.BaseController;
import controller.Controller;
import controller.FailedOperation;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.exceptions.ServiceNotFoundException;
import model.exceptions.UserNotFoundException;
import model.models.BaseModel;
import model.models.Model;
import model.services.Internet;
import model.services.Phone;
import model.services.Service;
import model.services.Television;
import model.users.User;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class ProviderApplication2 extends Application
{

    static void display(String title, String message) {
        Stage stage = new Stage();

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(250);
        stage.setMinHeight(200);

        Label label = new Label(message);
        Button button = new Button("OK");
        button.setOnAction(e -> stage.close());

        VBox box = new VBox(10);
        box.getChildren().addAll(label, button);
        box.setAlignment(Pos.CENTER);

        Scene scene = new Scene(box);
        stage.setScene(scene);
        stage.showAndWait();
    }

    Scene mainScene;
    Scene signUpScene;
    Scene loginScene;

    Scene userScene;
    Scene userServicesScene;
    Scene userInternetScene;
    Scene userTvScene;
    Scene userPhoneScene;

    Scene userPersonalInfoScene;
    Scene changePersonalInfo;

    Scene addInternetScene;
    Scene addPhoneScene;
    Scene addTvScene;

    Scene createScene(Node...nodes) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(20, 20, 20, 20));
        box.getChildren().addAll(nodes);
        ScrollPane scrollPane = new ScrollPane(box);
        return new Scene(scrollPane, 300, 350);
    }

    void createUserScene(Stage primaryStage) {
        Label label = new Label();
        label.setText("Hello, " + user.getName() + "!");
        Button personalInfo = new Button("Personal info");
        personalInfo.setOnAction(e -> primaryStage.setScene(userPersonalInfoScene));
        Button services = new Button("Services");
        services.setOnAction(e -> primaryStage.setScene(userServicesScene));
        Button logout = new Button("Log out");
        logout.setOnAction(e -> primaryStage.setScene(loginScene));
        userScene =  createScene(label, personalInfo, services, logout);
    }

    void createUserPersonalInfoScene(Stage stage) {
        Label label = new Label("Personal info");
        Label name = new Label("Name: " + user.getName());
        Label phone = new Label("Phone number: " + user.getPhoneNumber());
        Label email = new Label("Email address: " + user.getEmailAddress());
        Button change = new Button("Change");
        change.setOnAction(e -> {
            createChangePersonalInfo(stage);
            stage.setScene(changePersonalInfo);
        });
        Button back = new Button("Back");
        back.setOnAction(e -> stage.setScene(userScene));
        userPersonalInfoScene = createScene(label, name, phone, email, change, back);
    }

    void createChangePersonalInfo(Stage stage) {
        Label label = new Label("Name");
        TextField name = new TextField(user.getName());
        Label label1 = new Label("Phone number");
        TextField phone = new TextField(user.getPhoneNumber());
        Label label2 = new Label("Email address");
        TextField address = new TextField(user.getEmailAddress());
        Button confirm = new Button("Confirm");
        confirm.setOnAction(e -> {
            try {
                if (name.getText().equals(""))
                    throw new IllegalArgumentException("Enter name");
                user.setName(name.getText());
                user.setPhoneNumber(phone.getText());
                user.setEmailAddress(address.getText());
                createLoginScene(stage);
                createAllUserScenes(stage);
                stage.setScene(userPersonalInfoScene);
            } catch (IllegalArgumentException ex) {
                display("Error", ex.getMessage());
            }
        });
        Button back = new Button("Back");
        back.setOnAction(e -> stage.setScene(userPersonalInfoScene));
        changePersonalInfo = createScene(label, name, label1, phone, label2, address, confirm, back);
    }

    void createUserServicesScene(Stage primaryStage) {
        Label label = new Label("Your services");
        Button internet = new Button("Internet");
        internet.setOnAction(e -> {
            createAllUserScenes(primaryStage);
            primaryStage.setScene(userInternetScene);
        });
        Button tv = new Button("Television");
        tv.setOnAction(e -> {
            createAllUserScenes(primaryStage);
            primaryStage.setScene(userTvScene);
        });
        Button phone = new Button("Phone");
        phone.setOnAction(e -> {
            createAllUserScenes(primaryStage);
            primaryStage.setScene(userPhoneScene);
        });
        Button back = new Button("Back");
        back.setOnAction(e -> primaryStage.setScene(userScene));
        userServicesScene = createScene(label, internet, tv, phone, back);
    }

    void createUserInternetScene(Stage primaryStage) throws FailedOperation {
        Button back = new Button("Back");
        back.setOnAction(e -> primaryStage.setScene(userServicesScene));
        Label label = new Label("Your Internet\n————————————");
        LinkedList<Node> nodes = new LinkedList<>();
        nodes.add(label);
        String serviceType = Internet.class.getSimpleName();
        int count = user.getUserServiceCount(serviceType);
        for (int i = 0; i < count; ++i) {
            Internet service = (Internet) controller.getService(user.getUserService(serviceType, i).getServiceId());
            Label name = new Label("Name: " + service.getName());
            Label status = new Label("Status: " + user.getUserService(serviceType, i).getStatus());
            Label activationDate = new Label("Activation date: " + user.getUserService(serviceType, i).getActivationDate());
            Label speed = new Label("Speed: " + service.getSpeed());
            Label connectionType = new Label("Connection Type: " + service.getConnectionType());
            Label antivirus = new Label("Antivirus: " + service.isAntivirus());
            Button deactivate = new Button("Deactivate");
            nodes.add(name);
            nodes.add(status);
            nodes.add(activationDate);
            nodes.add(speed);
            nodes.add(connectionType);
            nodes.add(antivirus);
            nodes.add(new Label("————————————"));
        }

        Button add = new Button("Add");
        add.setOnAction(e -> primaryStage.setScene(addInternetScene));
        nodes.add(add);
        nodes.add(back);
        Node[] nodesArray = new Node[nodes.size()];
        nodes.toArray(nodesArray);
        userInternetScene = createScene(nodesArray);
    }

    void createUserTelevisionScene(Stage primaryStage) throws FailedOperation {
        Button back = new Button("Back");
        back.setOnAction(e -> primaryStage.setScene(userServicesScene));
        Label label = new Label("Your Television\n————————————");
        LinkedList<Node> nodes = new LinkedList<>();
        String serviceType = Television.class.getSimpleName();
        int count = user.getUserServiceCount(serviceType);
        nodes.add(label);
        for (int i = 0; i < count; ++i) {
            Television service = (Television) controller.getService(user.getUserService(serviceType, i).getServiceId());
            Label name = new Label("Name: " + service.getName());
            Label status = new Label("Status: " + user.getUserService(serviceType, i).getStatus());
            Label activationDate = new Label("Activation date: " + user.getUserService(serviceType, i).getActivationDate());
            Label numberOfChannels = new Label("Number of channels: " + service.getNumberOfChannels());
            Button deactivate = new Button("Deactivate");
            nodes.add(name);
            nodes.add(status);
            nodes.add(activationDate);
            nodes.add(numberOfChannels);
            nodes.add(new Label("————————————"));
        }

        Button add = new Button("Add");
        add.setOnAction(e -> primaryStage.setScene(addTvScene));
        nodes.add(add);
        nodes.add(back);
        Node[] nodesArray = new Node[nodes.size()];
        nodes.toArray(nodesArray);
        userTvScene = createScene(nodesArray);
    }

    // todo get rid of duplicated code by implementing toString method in services
    void createUserPhoneScene(Stage primaryStage) throws FailedOperation {
        Button back = new Button("Back");
        back.setOnAction(e -> primaryStage.setScene(userServicesScene));
        Label label = new Label("Your Phone\n————————————");
        LinkedList<Node> nodes = new LinkedList<>();
        String serviceType = Phone.class.getSimpleName();
        int count = user.getUserServiceCount(serviceType);
        nodes.add(label);
        for (int i = 0; i < count; ++i) {
            Phone service = (Phone) controller.getService(user.getUserService(serviceType, i).getServiceId());
            Label name = new Label("Name: " + service.getName());
            Label status = new Label("Status: " + user.getUserService(serviceType, i).getStatus());
            Label activationDate = new Label("Activation date: " + user.getUserService(serviceType, i).getActivationDate());
            Label callsMinCount = new Label("Number of minutes: " + service.getCallsMinCount());
            Label smsCount = new Label("Number of SMS: " + service.getSmsCount());
            Button deactivate = new Button("Deactivate");
            nodes.add(name);
            nodes.add(status);
            nodes.add(activationDate);
            nodes.add(callsMinCount);
            nodes.add(smsCount);
            nodes.add(new Label("————————————"));
        }

        Button add = new Button("Add");
        add.setOnAction(e -> primaryStage.setScene(addPhoneScene));
        nodes.add(add);
        nodes.add(back);
        Node[] nodesArray = new Node[nodes.size()];
        nodes.toArray(nodesArray);
        userPhoneScene = createScene(nodesArray);
    }

    void createAddInternetScene(Stage stage) {
        Button back = new Button("Back");
        back.setOnAction(e -> stage.setScene(userInternetScene));

        Label nameLabel = new Label("Name:");
        TextField nameTextField = new TextField();
        nameTextField.setText("int" + (model.getServiceMaxId() + 1));

        Label speedLabel = new Label("Speed:");
        TextField textFieldSpeed = new TextField();

        Label connectionTypeLabel = new Label("Connection type:");
        ChoiceBox<String> connectionType = new ChoiceBox<>();
        String[] connectionTypes = Internet.getConnectionTypes();
        for (String type: connectionTypes)
            connectionType.getItems().add(type);
        connectionType.setValue(connectionTypes[0]);

        CheckBox antivirus = new CheckBox("Antivirus");

        DateTimePicker dateTimePicker = new DateTimePicker();

        Button add = new Button("Confirm");
        add.setOnAction(e -> {
            try {
                String name = nameTextField.getText();
                String strSpeed = textFieldSpeed.getText();
                if (name.equals("") || strSpeed.equals(""))
                    throw new IllegalArgumentException("Enter name and speed");
                for (int i = 0; i < strSpeed.length(); ++i)
                    if (strSpeed.charAt(i) < '0' || strSpeed.charAt(i) > '9')
                        throw new IllegalArgumentException("Invalid speed");
                int speed = Integer.parseInt(strSpeed);
                boolean bAntivirus = antivirus.isSelected();
                Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateTimePicker.getEditor().getText());
                controller.setServiceToUser(user.getId(),
                        new Internet(controller.getNextServiceId(), name, speed, bAntivirus, Internet.ConnectionType.valueOf(connectionType.getValue())),
                        date);
                createAllUserScenes(stage);
                stage.setScene(userInternetScene);
            } catch (FailedOperation | IllegalArgumentException | ParseException ex) {
                display("Error", ex.getMessage());
            }
        });
        addInternetScene = createScene(nameLabel, nameTextField, speedLabel, textFieldSpeed, connectionTypeLabel, connectionType, antivirus, new Label("Activation date and time"), dateTimePicker, add, back);

    }

    void createAddPhoneScene(Stage stage) {
        TextField name = new TextField();
        name.setText("phone" + (model.getServiceMaxId() + 1));
        TextField callsMinCount = new TextField();
        TextField smsCount = new TextField();
        DateTimePicker dateTimePicker = new DateTimePicker();
        Button add = new Button("Confirm");
        Button back = new Button("Back");
        back.setOnAction(e -> stage.setScene(userPhoneScene));
        add.setOnAction(e -> {
            try {
                String nameText = name.getText();
                String callsMinCountText = callsMinCount.getText();
                String smsCountText = smsCount.getText();
                if (nameText.equals("") || callsMinCountText.equals("") || smsCountText.equals(""))
                    throw new IllegalArgumentException("Enter data");
                for (int i = 0; i < callsMinCountText.length(); ++i)
                    if (callsMinCountText.charAt(i) < '0' || callsMinCountText.charAt(i) > '9')
                        throw new IllegalArgumentException("Invalid number of minutes");
                for (int i = 0; i < smsCountText.length(); ++i)
                    if (smsCountText.charAt(i) < '0' || smsCountText.charAt(i) > '9')
                        throw new IllegalArgumentException("Invalid number of SMS");
                Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateTimePicker.getEditor().getText());
                controller.setServiceToUser(user.getId(),
                        new Phone(controller.getNextServiceId(), nameText, Integer.parseInt(callsMinCountText), Integer.parseInt(smsCountText)),
                        date);
                createAllUserScenes(stage);
                stage.setScene(userPhoneScene);
            } catch (FailedOperation | IllegalArgumentException | ParseException ex) {
                display("Error", ex.getMessage());
            }
        });
        addPhoneScene = createScene(new Label("Name:"), name, new Label("Number of minutes:"), callsMinCount, new Label("Number of SMS:"), smsCount, new Label("Activation date and time:"), dateTimePicker, add, back);

    }

    void createAddTvScene(Stage stage) {
        TextField name = new TextField();
        name.setText("tv" + (model.getServiceMaxId() + 1));
        TextField numberOfChannels = new TextField();
        DateTimePicker dateTimePicker = new DateTimePicker();
        Button back = new Button("Back");
        back.setOnAction(e -> stage.setScene(userTvScene));
        Button add = new Button("Confirm");
        add.setOnAction(e -> {
            try {
                String nameText = name.getText();
                String numberOfChannelsText = numberOfChannels.getText();
                if (nameText.equals("") || numberOfChannelsText.equals(""))
                    throw new IllegalArgumentException("Enter data");
                for (int i = 0; i < numberOfChannelsText.length(); ++i)
                    if (numberOfChannelsText.charAt(i) < '0' || numberOfChannelsText.charAt(i) > '9')
                        throw new IllegalArgumentException("Invalid number of channels");
                Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateTimePicker.getEditor().getText());
                controller.setServiceToUser(user.getId(),
                        new Television(controller.getNextServiceId(), name.getText(), Integer.parseInt(numberOfChannels.getText())),
                        date);
                createAllUserScenes(stage);
                stage.setScene(userTvScene);
            } catch (FailedOperation | IllegalArgumentException | ParseException ex) {
                display("Error", ex.getMessage());
            }
        });
        addTvScene = createScene(new Label("Name:"), name, new Label("Number of channels:"), numberOfChannels, new Label("Activation date and time:"), dateTimePicker, add, back);
    } // todo do we need to write name


    void createAllUserScenes(Stage primaryStage) {
        createUserScene(primaryStage);
        createUserServicesScene(primaryStage);
        createUserPersonalInfoScene(primaryStage);
        try {
            createUserInternetScene(primaryStage);
            createUserPhoneScene(primaryStage);
            createUserTelevisionScene(primaryStage);

            createAddInternetScene(primaryStage);
            createAddPhoneScene(primaryStage);
            createAddTvScene(primaryStage);
        } catch (FailedOperation e) {
            display("Error", e.getMessage());
        }
    }

    void createLoginScene(Stage primaryStage) {
        TextField username = new TextField();
        username.setPromptText("Username");
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPromptText("Name");
        comboBox.setEditable(true);
        for (int i = 0; i <= model.getUserMaxId(); ++i) {
            try {
                User user = model.getUserById(i);
                comboBox.getItems().add(user.getName());
            } catch (UserNotFoundException e) {
                //comboBox.getItems().add(String.valueOf(i));
            }
        }
        TextField password = new TextField();
        password.setPromptText("Password");
        CheckBox checkBox = new CheckBox("Remember me");
        Button login = new Button("Log in");
        login.setOnAction(e -> {
            try {
                System.out.println(comboBox.getValue());
                String value = comboBox.getValue();
                if (value.equals(""))
                    throw new IllegalArgumentException("Enter name");
                user = model.getUserByName(comboBox.getValue());
                createAllUserScenes(primaryStage);
                primaryStage.setScene(userScene);
            } catch (UserNotFoundException | IllegalArgumentException ex) {
                display("Error", ex.getMessage());
            }
        });
        Button back = new Button("Back");
        back.setOnAction(e -> primaryStage.setScene(mainScene));
        loginScene = createScene(comboBox, password, checkBox, login, back);
    }

    void createSignupScene(Stage stage)
    {
        Label label = new Label("Name");
        TextField name = new TextField();
        Label label1 = new Label("Phone number");
        TextField phone = new TextField();
        Label label2 = new Label("Email address");
        TextField address = new TextField();
        Button signup = new Button("Sign up");
        signup.setOnAction(e -> {
            try {
                if (name.getText().equals(""))
                    throw new IllegalArgumentException("Enter name");
                user = new User(controller.getNextUserId(), name.getText(), phone.getText(), address.getText(), null);
                controller.createUser(user);
                createLoginScene(stage);
                createAllUserScenes(stage);
                stage.setScene(userScene);
            } catch (FailedOperation | IllegalArgumentException ex) {
                display("Error", ex.getMessage());
            }
        });
        Button back = new Button("Back");
        back.setOnAction(e -> stage.setScene(mainScene));
        signUpScene = createScene(label, name, label1, phone, label2, address, signup, back);
    }

    Controller controller;
    Model model;
    User user;

    @Override
    public void start(Stage primaryStage) throws IOException, FailedOperation {
        model = new BaseModel();
        model.read();
        controller = new BaseController(model);
        primaryStage.setTitle("Provider");
        primaryStage.setIconified(false);
        primaryStage.setOnCloseRequest(e -> {
            try {
                model.save();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });

        createLoginScene(primaryStage);

        // main scene
        {
            Button login = new Button("Log in");
            login.setOnAction(e -> primaryStage.setScene(loginScene));
            Button signup = new Button("Sign up");
            signup.setOnAction(e -> {
                createSignupScene(primaryStage);
                primaryStage.setScene(signUpScene);
            });
            mainScene = createScene(login, signup);
        }

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
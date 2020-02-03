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
import java.util.ArrayList;
import java.util.LinkedList;

public class ProviderApplication extends Application
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

    Scene startScene;

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

    Scene adminScene;
    Scene internetAdminScene;
    Scene tvAdminScene;
    Scene phoneAdminScene;

    Scene addInternetAdminScene;
    Scene addPhoneAdminScene;
    Scene addTvAdminScene;

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
        Label label = new Label("Your Internet");
        Button back = new Button("Back");
        back.setOnAction(e -> primaryStage.setScene(userServicesScene));
        try {
            Internet service = (Internet)controller.getService(user.getServiceIdByType(Internet.class.getSimpleName()));
            Label name = new Label("Name: " + service.getName());
            Label speed = new Label("Speed: " + service.getSpeed());
            Label connectionType = new Label("Antivirus: " + service.getConnectionType());
            Label antivirus = new Label("Antivirus: " + service.isAntivirus());
            Button deactivate = new Button("Deactivate");
            Button change = new Button("Change");
            change.setOnAction(e -> primaryStage.setScene(addInternetScene));
            userInternetScene = createScene(label, name, speed, connectionType, antivirus, change, back);
        } catch (ServiceNotFoundException ex) {
            Label label1 = new Label("You have no " + Internet.class.getSimpleName() + " service");
            Button button = new Button("Add");
            button.setOnAction(e -> primaryStage.setScene(addInternetScene));
            userInternetScene = createScene(label1, button, back);
        }
    }

    void createUserTelevisionScene(Stage primaryStage) throws FailedOperation {
        Button back = new Button("Back");
        back.setOnAction(e -> primaryStage.setScene(userServicesScene));
        try {
            Label label = new Label("Your Television");
            Television service = (Television)controller.getService(user.getServiceIdByType(Television.class.getSimpleName()));
            Label name = new Label("Name: " + service.getName());
            Label numberOfChannels = new Label("Number of channels: " + service.getNumberOfChannels());
            Button deactivate = new Button("Deactivate");
            Button change = new Button("Change");
            change.setOnAction(e -> primaryStage.setScene(addTvScene));
            userTvScene = createScene(label, name, numberOfChannels, change, back);
        } catch (ServiceNotFoundException ex) {
            Label label = new Label("You have no " + Television.class.getSimpleName() + " service");
            Button button = new Button("Add");
            button.setOnAction(e -> primaryStage.setScene(addTvScene));
            userTvScene = createScene(label, button, back);
        }
    }

    void createUserPhoneScene(Stage primaryStage) throws FailedOperation {
        Button back = new Button("Back");
        back.setOnAction(e -> primaryStage.setScene(userServicesScene));
        try {
            Label label = new Label("Your Phone");
            Phone service = (Phone) controller.getService(user.getServiceIdByType(Phone.class.getSimpleName()));
            Label name = new Label("Name: " + service.getName());
            Label callsMinCount = new Label("Number of minutes: " + service.getCallsMinCount());
            Label smsCount = new Label("Number of SMS: " + service.getSmsCount());
            Button deactivate = new Button("Deactivate");
            Button change = new Button("Change");
            change.setOnAction(e -> primaryStage.setScene(addPhoneScene));
            userPhoneScene = createScene(label, name, callsMinCount, smsCount, change, back);
        } catch (ServiceNotFoundException ex) {
            Label label = new Label("You have no " + Phone.class.getSimpleName() + " service");
            Button button = new Button("Add");
            button.setOnAction(e -> primaryStage.setScene(addPhoneScene));
            userPhoneScene = createScene(label, button, back);
        }
    }

    void createAddInternetScene(Stage stage) throws FailedOperation {
        Button back = new Button("Back");
        back.setOnAction(e -> stage.setScene(userInternetScene));

        ArrayList<Service> services = controller.getAllServices(Internet.class.getSimpleName());
        LinkedList<Node> nodes = new LinkedList<>();
        nodes.add(back);
        nodes.add(new Label("————————————"));
        int i;
        for (i = 0; i < services.size(); ++i) {
            Internet internet = (Internet)services.get(i);
            Label name = new Label("Name: " + internet.getName());
            Label speed = new Label("Speed: " + internet.getSpeed());
            Label connectionType = new Label("Antivirus: " + internet.getConnectionType());
            Label antivirus = new Label("Antivirus: " + internet.isAntivirus());
            Button add = new Button("Add");
            add.setOnAction(e -> {
                Label name1 = new Label(internet.getName());
                Label label = new Label("Enter date and time");
                DateTimePicker dateTimePicker = new DateTimePicker();
                Button add1 = new Button("Add");
                add1.setOnAction(e1 -> {
                    //System.out.println(dateTimePicker.getEditor().getText());
                    try {
                        model.setServiceToUser(user.getId(), internet.getId(), dateTimePicker.getEditor().getText());
                        model.save();
                    } catch (ServiceNotFoundException | UserNotFoundException | ParseException | IOException ex) {
                        display("Error", ex.getMessage());
                    }
                    createAllUserScenes(stage);
                    stage.setScene(userInternetScene);
                });
                Scene scene = createScene(name1, label, dateTimePicker, add1);
                stage.setScene(scene);
            });
            Label label = new Label("————————————");
            nodes.add(name);
            nodes.add(speed);
            nodes.add(connectionType);
            nodes.add(antivirus);
            nodes.add(add);
            nodes.add(label);
        }
        if (services.size() == 0)
            nodes.add(new Label("No Internet"));
        Node[] nodeArray = new Node[nodes.size()];
        nodes.toArray(nodeArray);
        addInternetScene = createScene(nodeArray);
    }

    void createAddPhoneScene(Stage stage) throws FailedOperation {
        Button back = new Button("Back");
        back.setOnAction(e -> stage.setScene(userPhoneScene));

        ArrayList<Service> services = controller.getAllServices(Phone.class.getSimpleName());
        LinkedList<Node> nodes = new LinkedList<>();
        nodes.add(back);
        nodes.add(new Label("————————————"));
        int i;
        for (i = 0; i < services.size(); ++i) {
            Phone phone = (Phone) services.get(i);
            Label name = new Label("Name: " + phone.getName());
            Label callsMinCount = new Label("Number of Minutes: " + phone.getCallsMinCount());
            Label smsCount = new Label("Number of SMS: " + phone.getSmsCount());
            Button add = new Button("Add");
            add.setOnAction(e -> {
                Label name1 = new Label(phone.getName());
                Label label = new Label("Enter date and time");
                DateTimePicker dateTimePicker = new DateTimePicker();
                Button add1 = new Button("Add");
                add1.setOnAction(e1 -> {
                    try {
                        model.setServiceToUser(user.getId(), phone.getId(), dateTimePicker.getEditor().getText());
                        model.save();
                    } catch (ServiceNotFoundException | UserNotFoundException | ParseException | IOException ex) {
                        display("Error", ex.getMessage());
                    }
                    createAllUserScenes(stage);
                    stage.setScene(userPhoneScene);
                });
                Scene scene = createScene(name1, label, dateTimePicker, add1);
                stage.setScene(scene);
            });
            Label label = new Label("————————————");
            nodes.add(name);
            nodes.add(callsMinCount);
            nodes.add(smsCount);
            nodes.add(add);
            nodes.add(label);
        }
        if (services.size() == 0)
            nodes.add(new Label("No Phone"));
        Node[] nodeArray = new Node[nodes.size()];
        nodes.toArray(nodeArray);
        addPhoneScene = createScene(nodeArray);
    }

    void createAddTvScene(Stage stage) throws FailedOperation {
        Button back = new Button("Back");
        back.setOnAction(e -> stage.setScene(userTvScene));

        ArrayList<Service> services = controller.getAllServices(Television.class.getSimpleName());
        LinkedList<Node> nodes = new LinkedList<>();
        nodes.add(back);
        nodes.add(new Label("————————————"));
        int i;
        for (i = 0; i < services.size(); ++i) {
            Television television = (Television) services.get(i);
            Label name = new Label("Name: " + television.getName());
            Label numberOfChannels = new Label("Number of Channels: " + television.getNumberOfChannels());
            Button add = new Button("Add");
            add.setOnAction(e -> {
                Label name1 = new Label(television.getName());
                Label label = new Label("Enter date and time");
                DateTimePicker dateTimePicker = new DateTimePicker();
                Button add1 = new Button("Add");
                add1.setOnAction(e1 -> {
                    try {
                        model.setServiceToUser(user.getId(), television.getId(), dateTimePicker.getEditor().getText());
                        model.save();
                    } catch (ServiceNotFoundException | UserNotFoundException | ParseException | IOException ex) {
                        display("Error", ex.getMessage());
                    }
                    createAllUserScenes(stage);
                    stage.setScene(userTvScene);
                });
                Scene scene = createScene(name1, label, dateTimePicker, add1);
                stage.setScene(scene);
            });
            Label label = new Label("————————————");
            nodes.add(name);
            nodes.add(numberOfChannels);
            nodes.add(add);
            nodes.add(label);
        }
        if (services.size() == 0)
            nodes.add(new Label("No TV"));
        Node[] nodeArray = new Node[nodes.size()];
        nodes.toArray(nodeArray);
        addTvScene = createScene(nodeArray);
    }

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

    void createInternetAdminScene(Stage primaryStage) throws FailedOperation {
        Label label = new Label("All " + Internet.class.getSimpleName() + " services");

        Button back1 = new Button("Back");
        back1.setOnAction(e -> primaryStage.setScene(adminScene));
        Button add = new Button("Create new");
        add.setOnAction(e -> {
            createAddInternetAdminScene(primaryStage);
            primaryStage.setScene(addInternetAdminScene);
        });

        ArrayList<Service> services = controller.getAllServices(Internet.class.getSimpleName());
        LinkedList<Node> nodes = new LinkedList<>();
        nodes.add(label);
        int i;
        for (i = 0; i < services.size(); ++i) {
            Internet internet = (Internet)services.get(i);
            Button button = new Button(internet.getName());
            button.setOnAction(e -> {
                Label name = new Label("Name: " + internet.getName());
                Label speed = new Label("Speed: " + internet.getSpeed());
                Label connectionType = new Label("Antivirus: " + internet.getConnectionType());
                Label antivirus = new Label("Antivirus: " + internet.isAntivirus());
                Button back = new Button("Back");
                back.setOnAction(e1 -> primaryStage.setScene(internetAdminScene));
                primaryStage.setScene(createScene(name, speed, connectionType, antivirus, back));
            });
            nodes.add(button);
        }
        if (services.size() == 0)
            nodes.add(new Label("Not found"));
        nodes.add(new Label("————————————"));
        nodes.add(add);
        nodes.add(back1);
        Node[] nodes1 = new Node[nodes.size()];
        nodes.toArray(nodes1);
        internetAdminScene = createScene(nodes1);
    }

    void createPhoneAdminScene(Stage primaryStage) throws FailedOperation {
        Label label = new Label("All " + Phone.class.getSimpleName() + " services");
        Button back1 = new Button("Back");
        back1.setOnAction(e -> primaryStage.setScene(adminScene));
        Button add = new Button("Create new");
        add.setOnAction(e -> {
            createAddPhoneAdminScene(primaryStage);
            primaryStage.setScene(addPhoneAdminScene);
        });

        ArrayList<Service> services = controller.getAllServices(Phone.class.getSimpleName());
        LinkedList<Node> nodes = new LinkedList<>();
        nodes.add(label);
        int i;
        for (i = 0; i < services.size(); ++i) {
            Phone phone = (Phone) services.get(i);
            Button button = new Button(phone.getName());
            button.setOnAction(e -> {
                Label name = new Label("Name: " + phone.getName());
                Label callsMinCount = new Label("Number of Minutes: " + phone.getCallsMinCount());
                Label smsCount = new Label("Number of SMS: " + phone.getSmsCount());
                Button back = new Button("Back");
                back.setOnAction(e1 -> primaryStage.setScene(phoneAdminScene));
                primaryStage.setScene(createScene(name, callsMinCount, smsCount, back));
            });
            nodes.add(button);
        }
        if (services.size() == 0)
            nodes.add(new Label("Not found"));
        nodes.add(new Label("————————————"));
        nodes.add(add);
        nodes.add(back1);
        Node[] nodeArray = new Node[nodes.size()];
        nodes.toArray(nodeArray);
        phoneAdminScene = createScene(nodeArray);
    }

    void createTvAdminScene(Stage primaryStage) throws FailedOperation {
        Label label = new Label("All " + Television.class.getSimpleName() + " services");
        Button back1 = new Button("Back");
        back1.setOnAction(e -> primaryStage.setScene(adminScene));
        Button add = new Button("Create new");
        add.setOnAction(e -> {
            createAddTvAdminScene(primaryStage);
            primaryStage.setScene(addTvAdminScene);
        });

        ArrayList<Service> services = controller.getAllServices(Television.class.getSimpleName());
        LinkedList<Node> nodes = new LinkedList<>();
        nodes.add(label);
        int i;
        for (i = 0; i < services.size(); ++i) {
            Television television = (Television) services.get(i);
            Button button = new Button(television.getName());
            button.setOnAction(e -> {
                Label name = new Label("Name: " + television.getName());
                Label numberOfChannels = new Label("Number of channels: " + television.getNumberOfChannels());
                Button back = new Button("Back");
                back.setOnAction(e1 -> primaryStage.setScene(tvAdminScene));
                primaryStage.setScene(createScene(name, numberOfChannels, back));
            });
            nodes.add(button);
        }
        if (services.size() == 0)
            nodes.add(new Label("Not found"));
        nodes.add(new Label("————————————"));
        nodes.add(add);
        nodes.add(back1);
        Node[] nodeArray = new Node[nodes.size()];
        nodes.toArray(nodeArray);
        tvAdminScene = createScene(nodeArray);
    }

    void createAddInternetAdminScene(Stage stage) {
        Label nameLabel = new Label("Name");
        TextField nameTextField = new TextField();

        Label speedLabel = new Label("Speed");
        TextField textFieldSpeed = new TextField();

        Label connectionTypeLabel = new Label("Connection type");
        ChoiceBox<String> connectionType = new ChoiceBox<>();
        String[] connectionTypes = Internet.getConnectionTypes();
        for (String type: connectionTypes)
            connectionType.getItems().add(type);
        connectionType.setValue(connectionTypes[0]);

        CheckBox antivirus = new CheckBox("Antivirus");

        Button back = new Button("Back");
        back.setOnAction(e -> stage.setScene(internetAdminScene));

        Button add = new Button("Create");
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
                controller.createService(new Internet(controller.getNextServiceId(), name, speed, bAntivirus, Internet.ConnectionType.valueOf(connectionType.getValue())));
                createAllAdminScenes(stage);
                stage.setScene(internetAdminScene);
            } catch (FailedOperation | IllegalArgumentException ex) {
                display("Error", ex.getMessage());
            }
        });
        addInternetAdminScene = createScene(nameLabel, nameTextField, speedLabel, textFieldSpeed, connectionTypeLabel, connectionType, antivirus, add, back);
    }

    void createAddPhoneAdminScene(Stage stage) {

        TextField name = new TextField();
        TextField callsMinCount = new TextField();
        TextField smsCount = new TextField();
        Button add = new Button("Create");
        Button back = new Button("Back");
        back.setOnAction(e -> stage.setScene(phoneAdminScene));
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
                controller.createService(new Phone(controller.getNextServiceId(), nameText, Integer.parseInt(callsMinCountText), Integer.parseInt(smsCountText)));
                createAllAdminScenes(stage);
                stage.setScene(phoneAdminScene);
            } catch (FailedOperation | IllegalArgumentException ex) {
                display("Error", ex.getMessage());
            }
        });
        addPhoneAdminScene = createScene(new Label("Name"), name, new Label("Number of minutes"), callsMinCount, new Label("Number of SMS"), smsCount, add, back);
    }

    void createAddTvAdminScene(Stage stage) {

        TextField name = new TextField();
        TextField numberOfChannels = new TextField();
        Button back = new Button("Back");
        back.setOnAction(e -> stage.setScene(tvAdminScene));
        Button add = new Button("Create");
        add.setOnAction(e -> {
            try {
                String nameText = name.getText();
                String numberOfChannelsText = numberOfChannels.getText();
                if (nameText.equals("") || numberOfChannelsText.equals(""))
                    throw new IllegalArgumentException("Enter data");
                for (int i = 0; i < numberOfChannelsText.length(); ++i)
                    if (numberOfChannelsText.charAt(i) < '0' || numberOfChannelsText.charAt(i) > '9')
                        throw new IllegalArgumentException("Invalid number of channels");
                controller.createService(new Television(controller.getNextServiceId(), name.getText(), Integer.parseInt(numberOfChannels.getText())));
                createAllAdminScenes(stage);
                stage.setScene(tvAdminScene);
            } catch (FailedOperation | IllegalArgumentException ex) {
                display("Error", ex.getMessage());
            }
        });
        addTvAdminScene = createScene(new Label("Name"), name, new Label("Number of channels"), numberOfChannels, add, back);
    }

    void createAllAdminScenes(Stage stage) throws FailedOperation {
        createInternetAdminScene(stage);
        createPhoneAdminScene(stage);
        createTvAdminScene(stage);
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
                user = new User(controller.getNextUserId(), name.getText(), phone.getText(), address.getText());
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

        // start scene
        {
            Button enterAsUser = new Button("User");
            enterAsUser.setOnAction(e -> primaryStage.setScene(mainScene));
            Button enterAsAdmin = new Button("Admin");
            enterAsAdmin.setOnAction(e -> primaryStage.setScene(adminScene));
            startScene = createScene(enterAsAdmin, enterAsUser);
        }

        // admin scene
        {
            Button internet = new Button("Internet");
            internet.setOnAction(e -> primaryStage.setScene(internetAdminScene));
            Button phone = new Button("Phone");
            phone.setOnAction(e -> primaryStage.setScene(phoneAdminScene));
            Button tv = new Button("Television");
            tv.setOnAction(e -> primaryStage.setScene(tvAdminScene));
            Button back = new Button("Back");
            back.setOnAction(e -> primaryStage.setScene(startScene));
            adminScene = createScene(internet, phone, tv, back);
            createInternetAdminScene(primaryStage);
            createPhoneAdminScene(primaryStage);
            createTvAdminScene(primaryStage);
        }

        // main scene
        {
            Button login = new Button("Log in");
            login.setOnAction(e -> primaryStage.setScene(loginScene));
            Button signup = new Button("Sign up");
            signup.setOnAction(e -> {
                createSignupScene(primaryStage);
                primaryStage.setScene(signUpScene);
            });
            Button back = new Button("Back");
            back.setOnAction(e -> primaryStage.setScene(startScene));
            mainScene = createScene(login, signup, back);
        }



        primaryStage.setScene(startScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
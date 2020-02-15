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
import model.exceptions.UserNotFoundException;
import model.models.BaseModel;
import model.models.Model;
import model.services.Internet;
import model.services.Phone;
import model.services.Services;
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

    static void display(String message) {
        Stage stage = new Stage();

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Error");
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
        label.setText("Hello, " + user.getUsername() + "!");
        Button personalInfo = new Button("Personal info");
        personalInfo.setOnAction(e -> primaryStage.setScene(userPersonalInfoScene));
        Button services = new Button("Services");
        services.setOnAction(e -> primaryStage.setScene(userServicesScene));
        Button logout = new Button("Log out");
        logout.setOnAction(e -> primaryStage.setScene(loginScene));
        userScene =  createScene(label, personalInfo, services, logout);
    }

    void createUserPersonalInfoScene(Stage stage) {
        Label label = new Label("Personal info:\n" + user.toString());
       Button change = new Button("Change");
        change.setOnAction(e -> {
            createChangePersonalInfo(stage);
            stage.setScene(changePersonalInfo);
        });
        Button back = new Button("Back");
        back.setOnAction(e -> stage.setScene(userScene));
        userPersonalInfoScene = createScene(label, change, back);
    }

    void createChangePersonalInfo(Stage stage) {
        Label label = new Label("Name");
        TextField name = new TextField(user.getUsername());
        Label label1 = new Label("Phone number");
        TextField phone = new TextField(user.getPhoneNumber());
        Label label2 = new Label("Email address");
        TextField address = new TextField(user.getEmailAddress());
        Button confirm = new Button("Confirm");
        confirm.setOnAction(e -> {
            try {
                if (name.getText().equals(""))
                    throw new IllegalArgumentException("Enter name");
                user.setUsername(name.getText());
                user.setPhoneNumber(phone.getText());
                user.setEmailAddress(address.getText());
                createLoginScene(stage);
                createAllUserScenes(stage);
                stage.setScene(userPersonalInfoScene);
            } catch (IllegalArgumentException ex) {
                display(ex.getMessage());
            }
        });
        Button back = new Button("Back");
        back.setOnAction(e -> stage.setScene(userPersonalInfoScene));
        changePersonalInfo = createScene(label, name, label1, phone, label2, address, confirm, back);
    }

    void createUserServicesScene(Stage primaryStage) {
        String[] serviceTypes = Services.serviceTypes;
        ArrayList<Node> nodeArrayList = new ArrayList<>();
        nodeArrayList.add(new Label("Your services"));
        for (int i = 0; i < serviceTypes.length; ++i) {
            Button button = new Button(serviceTypes[i]);
            int finalI = i;
            button.setOnAction(e -> {
                try {
                    primaryStage.setScene(getServiceScene(primaryStage, serviceTypes[finalI]));
                } catch (FailedOperation ex) {
                    display(ex.getMessage());
                }
            });
            nodeArrayList.add(button);
        }
        Button back = new Button("Back");
        back.setOnAction(e -> primaryStage.setScene(userScene));
        nodeArrayList.add(back);
        Node[] nodes = new Node[nodeArrayList.size()];
        nodeArrayList.toArray(nodes);
        userServicesScene = createScene(nodes);
    }

    private Scene getServiceScene(Stage primaryStage, String serviceType) throws FailedOperation {
        Button back = new Button("Back");
        back.setOnAction(e -> primaryStage.setScene(userServicesScene));
        Label label = new Label("Your " + serviceType + "\n————————————");
        LinkedList<Node> nodes = new LinkedList<>();
        nodes.add(label);
        int count = user.getUserServiceCount(serviceType);
        for (int i = 0; i < count; ++i) {
            Label serviceString = new Label(Services.serviceToString(controller.getService(user.getUserService(serviceType, i).getServiceId())) + "\n" +
                    user.getUserService(serviceType, i).toString());
     //      Button deactivate = new Button("Deactivate");
            nodes.add(serviceString);
            nodes.add(new Label("————————————"));
        }
        Button add = new Button("Add");
        add.setOnAction(e -> {
            if (serviceType.equals(Internet.class.getSimpleName())) {
                createAddInternetScene(primaryStage);
                primaryStage.setScene(addInternetScene);
            }
            else if (serviceType.equals(Phone.class.getSimpleName())) {
                createAddPhoneScene(primaryStage);
                primaryStage.setScene(addPhoneScene);
            }
            else if (serviceType.equals(Television.class.getSimpleName())) {
                createAddTvScene(primaryStage);
                primaryStage.setScene(addTvScene);
            }
        });
        nodes.add(add);
        nodes.add(back);
        Node[] nodesArray = new Node[nodes.size()];
        nodes.toArray(nodesArray);
        return createScene(nodesArray);
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
                stage.setScene(getServiceScene(stage, Internet.class.getSimpleName()));
            } catch (FailedOperation | IllegalArgumentException | ParseException ex) {
                display(ex.getMessage());
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
                stage.setScene(getServiceScene(stage, Phone.class.getSimpleName()));
            } catch (FailedOperation | IllegalArgumentException | ParseException ex) {
                display(ex.getMessage());
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
                stage.setScene(getServiceScene(stage, Television.class.getSimpleName()));
            } catch (FailedOperation | IllegalArgumentException | ParseException ex) {
                display(ex.getMessage());
            }
        });
        addTvScene = createScene(new Label("Name:"), name, new Label("Number of channels:"), numberOfChannels, new Label("Activation date and time:"), dateTimePicker, add, back);
    } // todo do we need to write name


    void createAllUserScenes(Stage primaryStage) {
        createUserScene(primaryStage);
        createUserServicesScene(primaryStage);
        createUserPersonalInfoScene(primaryStage);
    }

    void createLoginScene(Stage primaryStage) {
        TextField username = new TextField();
        username.setPromptText("Username");
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPromptText("username");
        comboBox.setEditable(true);
        for (int i = 0; i <= model.getUserMaxId(); ++i) {
            try {
                User user = model.getUserById(i);
                comboBox.getItems().add(user.getUsername());
            } catch (UserNotFoundException e) {
                //comboBox.getItems().add(String.valueOf(i));
            }
        }
        TextField password = new TextField();
        password.setPromptText("password");
        //CheckBox checkBox = new CheckBox("Remember me");
        Button login = new Button("Log in");
        login.setOnAction(e -> {
            try {
                System.out.println(comboBox.getValue());
                String value = comboBox.getValue();
                if (value.equals(""))
                    throw new IllegalArgumentException("Enter name");
                user = model.getUserByUsername(comboBox.getValue());
                createAllUserScenes(primaryStage);
                primaryStage.setScene(userScene);
            } catch (UserNotFoundException | IllegalArgumentException ex) {
                display(ex.getMessage());
            }
        });
        Button back = new Button("Back");
        back.setOnAction(e -> primaryStage.setScene(mainScene));
        loginScene = createScene(comboBox, password, login, back);
    }

    void createSignUpScene(Stage stage)
    {
        TextField username = new TextField();
        TextField firstName = new TextField();
        TextField phone = new TextField();
        TextField address = new TextField();
        Button signUp = new Button("Sign up");
        signUp.setOnAction(e -> {
            try {
                if (username.getText().equals(""))
                    throw new IllegalArgumentException("Enter username");
                user = new User(controller.getNextUserId(), username.getText(), firstName.getText(), phone.getText(), address.getText(), null);
                controller.createUser(user);
                createLoginScene(stage);
                createAllUserScenes(stage);
                stage.setScene(userScene);
            } catch (FailedOperation | IllegalArgumentException ex) {
                display(ex.getMessage());
            }
        });
        Button back = new Button("Back");
        back.setOnAction(e -> stage.setScene(mainScene));
        signUpScene = createScene(new Label("Username:"), username, new Label("First name:"), firstName, new Label("Phone number:"), phone, new Label("Email address:"), address, signUp, back);
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
                createSignUpScene(primaryStage);
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
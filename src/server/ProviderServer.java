package server;

import controller.Controller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ProviderServer {

    private ConnectionManager manager;
    private Controller controller;
    private ServerSocket serverSocket;
    private int PORT;

    public ProviderServer(Controller controller, int port) throws IOException {
        PORT = port;
        this.controller = controller;
        // This one use to manage connections
        this.manager = new ConnectionManager(controller);
        this.serverSocket = new ServerSocket(PORT);
    }

    public void start() throws IOException {
        System.out.println("Server start");
        System.out.println("To stop server enter \"stop\"");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        //Accepting connections in other thread
        new Thread(() -> {
            while (true){
                try {
                    Socket clientSocket = serverSocket.accept();
                    manager.addConnection(clientSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //Reading console commands
        while(true){
            if (reader.ready()){
                if (reader.readLine().toLowerCase().equals("stop")){
                    break;
                }
            }
        }

        stop();
    }

    public void stop() throws IOException {
        manager.stop();
        serverSocket.close();
        System.out.println("Server was stopped");
    }
}

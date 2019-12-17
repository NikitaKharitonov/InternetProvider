package server;

import controller.Controller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ProviderServer {

    private ConnectionManager manager;
    private Controller controller;

    public ProviderServer(Controller controller){
        this.controller = controller;
        this.manager = new ConnectionManager(controller);
    }

    public void start() throws IOException {

        ServerSocket serverSocket = new ServerSocket(8080);

        while (true){
            Socket clientSocket = serverSocket.accept();
            manager.addConnection(clientSocket);
        }
    }
}

package server;

import controller.Controller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class ProviderServer {

    private LinkedList<Thread> activeConnections = new LinkedList<>();
    private Controller controller;

    public ProviderServer(){}

    public ProviderServer(Controller controller){
        this.controller = controller;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);

        while (true){
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client accepted!");
            activeConnections.add(new Thread(new clientConnection(clientSocket, controller)));
            activeConnections.getLast().start();

            for (int i = 0; i < activeConnections.size(); i++) {
                if (!activeConnections.get(i).isAlive()){
                    activeConnections.remove(i);
                }
            }
        }
    }
}

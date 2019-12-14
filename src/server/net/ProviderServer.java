package server.net;

import server.controller.Controller;
import server.model.services.Internet;

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

    private class clientConnection implements Runnable{

        Socket socket;
        InputStream in;
        OutputStream out;

        clientConnection(Socket clientSocket) throws IOException {
            socket = clientSocket;
            in = socket.getInputStream();
            out = socket.getOutputStream();
        }

        @Override
        public void run() {
            System.out.println("processed");
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(in);
                String temp = (String) objectInputStream.readObject();
                while(!temp.equals("<END>")){
                    System.out.println(temp);
                    temp = (String) objectInputStream.readObject();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);

        while (true){
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client accepted!");
            try {
                activeConnections.add(new Thread(new clientConnection(clientSocket)));
                activeConnections.getLast().start();
            } catch (IOException ex){
                System.out.println("Client was rejected");
            }
        }
    }
}

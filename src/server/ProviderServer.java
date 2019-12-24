package server;

import controller.Controller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


public class ProviderServer {

    private ConnectionManager manager;
    private ServerSocket serverSocket;
    private Thread listener;
    private int PORT;

    public ProviderServer(Controller controller, int port) throws IOException {
        PORT = port;
        // This one use to manage connections
        this.manager = new ConnectionManager(controller);
        // Create server socket
        this.serverSocket = new ServerSocket(PORT);
        //Accepting connections in other thread
        this.listener = new Thread(()->{
            try {
                while(true){
                    Socket clientSocket = serverSocket.accept();
                    manager.addConnection(clientSocket);
                }
            } catch (SocketException ignored){
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void start() throws IOException {
        System.out.println("Server start");
        System.out.println("To stop server enter \"stop\"");
        // read console
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // start accepting connections
        listener.start();
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
        listener.interrupt();
        manager.stop();
        serverSocket.close();
        System.out.println("Server was stopped");
    }
}

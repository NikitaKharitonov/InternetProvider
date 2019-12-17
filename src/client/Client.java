package client;
import com.sun.jdi.request.ThreadDeathRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    public static void main(String[] args) {
        clientSocket a = new clientSocket();
        a.run();
    }

    static void testManyClients(){
        clientSocket[] sockets = new clientSocket[10];
        for (int i = 0; i < sockets.length; i++) {
            sockets[i] = new clientSocket();
        }
        for (int i = 0; i < sockets.length; i++) {
            new Thread(sockets[i]).start();
        }
    }
}

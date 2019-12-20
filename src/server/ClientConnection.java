package server;

import controller.Controller;
import controller.FailedOperation;
import view.ConsoleView;
import view.View;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;


class ClientConnection{

    private Socket socket;
    private Controller controller;

    ClientConnection(Socket clientSocket, Controller controller){
        socket = clientSocket;
        this.controller = controller;
    }

    private class Processor implements Runnable{

        @Override
        public void run() {
            System.out.println("Client " + socket.getInetAddress().toString() + " connected");
            try{

                View console = new ConsoleView(
                        controller,
                        socket.getInputStream(),
                        socket.getOutputStream()
                );

                console.run();

                socket.close();

            } catch (SocketException e){
                System.out.println(e.getMessage());
                System.out.println("Connections isn't working well. Client probably closed program");
            } catch (IOException | FailedOperation e){
                System.out.println(e.getMessage());
            }

            System.out.println("Client " + socket.getInetAddress().toString() + " disconnected");
        }
    }

    void start(){
        Thread processor = new Thread(new Processor());
        processor.start();
    }

    boolean isStopped(){
        return socket.isClosed();
    }

    void stop() throws IOException {
        socket.close();
    }
}

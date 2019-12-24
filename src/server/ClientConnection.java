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
    private Thread processor;

    ClientConnection(Socket clientSocket, Controller controller){
        socket = clientSocket;
        this.controller = controller;
        processor = new Thread(new Processor());
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
                System.out.println(e.getMessage() + "[client" + socket.getInetAddress() + "]");
            } catch (IOException | FailedOperation e){
                System.out.println(e.getMessage());
            }

            System.out.println("Client " + socket.getInetAddress().toString() + " disconnected");
        }
    }

    void start() {
        processor.start();
    }

    boolean isStopped(){
        return !processor.isAlive();
    }

    void stop() throws IOException {
        socket.close();
        processor.interrupt();
        try {
            processor.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

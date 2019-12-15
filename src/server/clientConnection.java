package server;

import controller.Controller;
import view.ConsoleView;
import view.View;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

class clientConnection implements Runnable{

    private Socket socket;
    private Controller controller;

    clientConnection(Socket clientSocket, Controller controller){
        socket = clientSocket;
        this.controller = controller;
    }

    @Override
    public void run() {
        System.out.println("Start client " + socket.getInetAddress().toString());
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {

            View console = new ConsoleView(controller);
            /*
             * Сделать так, что бы consoleView принимала потоки в конструктор*/
            String temp;
            while (!socket.isClosed()) {
                temp = (String) objectInputStream.readObject();
                if (temp.equals("<END>")){
                    break;
                }
                System.out.println(temp);
            }

            objectOutputStream.writeObject("No, Fuck you");

            socket.close();
        } catch (EOFException e){
            System.out.println("Client socket was close");
        } catch( SocketException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("End client " + socket.getInetAddress().toString());
    }
}

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

    private class Processor implements Runnable{

        @Override
        public void run() {
            System.out.println("Start client " + socket.getInetAddress().toString());
            try{

                View console = new ConsoleView(
                        controller,
                        socket.getInputStream(),
                        socket.getOutputStream()
                );
                console.run();

                socket.close();
            } catch (EOFException e){
                System.out.println("Client socket was close");
            } catch( SocketException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FailedOperation failedOperation) {
                failedOperation.printStackTrace();
            }
            System.out.println("End client " + socket.getInetAddress().toString());
        }
    }

    ClientConnection(Socket clientSocket, Controller controller){
        socket = clientSocket;
        this.controller = controller;
    }

    public void start(){
        Thread processor = new Thread(new Processor());
        processor.start();
    }

    public boolean isStopped(){
        return socket.isClosed();
    }

    public void stop() throws IOException {
        socket.close();
    }
}

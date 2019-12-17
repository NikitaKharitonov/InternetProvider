package server;

import controller.Controller;

import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

class ConnectionManager {
    private List<ClientConnection> connections;
    Controller controller;

    ConnectionManager(Controller controller) {
        this.controller = controller;
        connections = new LinkedList<>();
    }

    void addConnection(Socket socket){
        ClientConnection temp = new ClientConnection(socket, this.controller);
        connections.add(temp);
        temp.start();
    }

    private void validate(){
        for (ClientConnection connection : connections){
            if (connection.isStopped()){
                connections.remove(connection);
            }
        }
    }
}

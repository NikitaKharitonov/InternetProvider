package server;

import controller.Controller;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;


class ConnectionManager {
    private List<ClientConnection> connections;
    private Controller controller;

    ConnectionManager(Controller controller) {
        this.controller = controller;
        connections = new LinkedList<>();
    }

    void addConnection(Socket socket){
        ClientConnection clientConnection = new ClientConnection(socket, this.controller);
        connections.add(clientConnection);
        clientConnection.start();
        validate();
    }

    List<ClientConnection> getConnections(){
        validate();
        return connections;
    }

    private void validate(){
        for (ClientConnection connection : connections){
            if (connection.isStopped()){
                connections.remove(connection);
            }
        }
    }

    public void stop() throws IOException {
        for (ClientConnection connection : connections){
            if (connection.isStopped()){
                connections.remove(connection);
            }
            else{
                connection.stop();
                connections.remove(connection);
            }
        }
    }

    public int getConnectionsNumber(){
        return connections.size();
    }
}

package client;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    public static void main(String[] args) {
        // TODO socket for connecting to server
        Scanner scanner = new Scanner(System.in);
        Socket socket = null;
        try {
            socket = new Socket("localhost", 8080);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            for (int i = 0; i < 10; i++) {
                out.writeObject("" + i);
            }
            out.writeObject("FUCK YOU");
            out.writeObject("<END>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

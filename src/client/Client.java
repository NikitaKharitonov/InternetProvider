package client;
import java.io.IOException;
import java.io.ObjectInputStream;
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
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            for (int i = 0; i < 10; i++) {
                out.writeObject("" + i);
            }
            out.writeObject("FUCK YOU");
            out.writeObject("<END>");
            Thread.sleep(1000);
            String temp = (String) in.readObject();
            System.out.println(temp);
            socket.close();
            //System.out.println(new ObjectInputStream(socket.getInputStream()).readObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

package server;

import java.io.IOException;

public class ServerTest {
    public static void main(String[] args){
        ProviderServer providerServer = new ProviderServer();
        try {
            providerServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

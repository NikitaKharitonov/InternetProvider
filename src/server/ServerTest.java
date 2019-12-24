package server;

import controller.BaseController;
import model.data_storage_factories.XMLFileDataStorageFactory;
import model.models.BaseModel;
import model.models.Model;

import java.io.IOException;

public class ServerTest {
    public static void main(String[] args){
        try {
            // Creating main component and start off server
            Model model = new BaseModel();
            model.setDataStorageFactory(new XMLFileDataStorageFactory());
            model.read();
            ProviderServer providerServer = new ProviderServer(new BaseController(model), 8080);
            providerServer.start();
            System.out.println("App closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package server;

import controller.BaseController;
import model.data_storage_factories.XMLFileDataStorageFactory;
import model.models.BaseModel;
import model.models.Model;

import java.io.IOException;

public class ServerTest {
    public static void main(String[] args){
        try {
            Model model = new BaseModel();
            model.setDataStorageFactory(new XMLFileDataStorageFactory());
            model.read();
            ProviderServer providerServer = new ProviderServer(new BaseController(model));
            providerServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

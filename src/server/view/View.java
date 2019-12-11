package server.view;

import server.controller.*;
import java.io.IOException;


public interface View {

    void run() throws IOException, FailedOperation;
}


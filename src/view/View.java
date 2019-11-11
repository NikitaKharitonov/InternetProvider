package view;

import util.Procedure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class View {

    private String[] request;
    private Procedure handler;

    public void start() throws IOException {
        String newRequest = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (!(newRequest = reader.readLine()).equals("exit")) {
            request = newRequest.split(" ");
        }
    }

    public void setHandler(Procedure handler) {
        this.handler = handler;
    }

    public String getRequestType() {
        return request[0];
    }

    public String getObject() {
        return request[1];
    }

    public Integer getSubject() {
        return Integer.parseInt(request[2]);
    }

    public Map<String, String> getParameters() {
        if (this.getRequestType().equals("set")) {
            Map<String, String> parameters = new HashMap<>();
            for (int i = 3; i < request.length; i++) {
                String[] keyAndValue = request[i].split("=");
                parameters.put(keyAndValue[0], keyAndValue[1]);
            }
            return parameters;
        } else {
            return null;
        }
    }

    public void printResponse(String status, String[] response) {
        System.out.println("Status: " + status);
        for (String str : response) {
            System.out.println(str);
        }
    }
}

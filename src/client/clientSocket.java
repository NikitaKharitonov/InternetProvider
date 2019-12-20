package client;

import java.io.*;
import java.net.Socket;

public class clientSocket implements Runnable{
    private Socket socket;

    public void run(){
        try {
            socket = new Socket("localhost", 8080);

            BufferedWriter socketOut = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            Thread.sleep(40);
            String tempLine;
            if (socketIn.ready()){
                System.out.println(readResponse(socketIn));
            }
            while (true) {
                tempLine = console.readLine();

                if (socket.isClosed()){
                    break;
                }

                socketOut.write(tempLine);
                socketOut.newLine();

                socketOut.flush();

                Thread.sleep(40);

                if (tempLine.toLowerCase().equals("exit")){
                    break;
                }

                if (socket.isClosed()){
                    break;
                }
                System.out.println(readResponse(socketIn));
            }
            socket.close();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String readResponse(BufferedReader bufferedReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String temp;
        while (bufferedReader.ready()){
            temp = bufferedReader.readLine();
            if (!temp.equals("")){
                stringBuilder.append(temp);
            }
            if (bufferedReader.ready()){
                stringBuilder.append( System.lineSeparator() );
            }
        }

        return stringBuilder.toString();
    }
}

import java.io.*;
import java.net.*;

public class myTCPClient{
  public static void main(String args[]){
    BufferedReader inFromUser =
            new BufferedReader(
                    new InputStreamReader(System.in));

    Integer port = 1446;

    try{
      Socket clientSocket = new Socket("localhost",port);

      BufferedReader inFromServer =
              new BufferedReader(
                      new InputStreamReader(clientSocket.getInputStream()));

      PrintWriter printToServer =
              new PrintWriter(clientSocket.getOutputStream(), true);


        // TODO: adjustable size array?
        String[] input = new String[128];
        String userInput = "";

        int i = 0;
        while(true) {
            System.out.print("Type in a line: ");
            userInput = inFromUser.readLine();
            if(userInput == null) break;
            input[i] = userInput;
            i++;
        }

        for(int j = 0; j < i; j++) {
            System.out.println("Sending to server ");
            printToServer.println(input[j]);
        }

            clientSocket.shutdownOutput();
            System.out.println("waiting for response");
        String response = inFromServer.readLine();
        System.out.println("Server's response:\n" + response);

        clientSocket.close();
    } catch (Exception e){e.printStackTrace();}

  }
}

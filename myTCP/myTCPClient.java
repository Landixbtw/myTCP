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



      //int i = 0;
      String[] input = new String[128];
      String userInput = "";

      //do {
      //  System.out.print("Type in a line: ");
      //userInput = inFromUser.readLine();
      //input[i++] = userInput;
      //} while(inFromUser.readLine() != null);
      for(int i = 0; i < 3; i++) {
        System.out.print("Type in a line: ");
        userInput = inFromUser.readLine();
        input[i] = userInput;
        System.out.println(input[i]);
      }

      for(int i = 0; i < 3; i++) {
        System.out.println("Sending to server ");
        printToServer.println(input[i]);
      }

      String response = inFromServer.readLine();
      System.out.println("Server's response:\n" + response);

      clientSocket.close();
    } catch (Exception e){e.printStackTrace();}

  }
}

import java.io.*;
import java.net.*;

public class myTCPClient{
  public static void main(String args[]){
    BufferedReader inFromUser = 
      new BufferedReader(
        new InputStreamReader(System.in));	

    Integer port = 1444;

    try{
      Socket clientSocket = new Socket("localhost",port);
	
      BufferedReader inFromServer = 
        new BufferedReader(
          new InputStreamReader(clientSocket.getInputStream()));

      PrintWriter printToServer = 
        new PrintWriter(clientSocket.getOutputStream(), true);

      System.out.print("Type in a line:\n");
      String userinput = inFromUser.readLine();
      printToServer.println(userinput);

      String response = inFromServer.readLine();
      System.out.println("Server's response:\n" + response);
      clientSocket.close();
    } catch (Exception e){e.printStackTrace();}

  }
}

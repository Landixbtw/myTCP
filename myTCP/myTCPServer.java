import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;

public class myTCPServer{
  public static void main(String args[]){
    Integer port=1446;
    try{
      if (args.length >= 2 && args[0].equals("-port"))
        port=Integer.parseInt(args[1]);

      System.out.println("Listen to Port: " + port);
      ServerSocket welcomeSocket = new ServerSocket(port);
        
        ArrayList<String> list = new ArrayList<>();
        Iterator it = list.iterator();
      while(true) {
        System.out.println("Waiting for Client to connect...");
        Socket connectionSocket = welcomeSocket.accept();

        BufferedReader inFromClient = new BufferedReader(
        new InputStreamReader(connectionSocket.getInputStream()));
        
            String request ;
        while((request = inFromClient.readLine()) != null) {
            System.out.println(request);
            list.add(request.toUpperCase());
        }

        System.out.println("list.size(): " + list.size());
        for(int i = 0; i < list.size(); i++) {
            OutputStream os = connectionSocket.getOutputStream();
            os.write(list.get(i).getBytes("UTF-8"));
        }
            
        connectionSocket.close();
      }
    } catch (IOException e){
      e.printStackTrace();
    }
  }
}

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class myTCPServer{
    public static void main(String args[]){
        Integer port=1446;
        try{
            if (args.length >= 2 && args[0].equals("-port"))
              port=Integer.parseInt(args[1]);

            System.out.println("Listen to Port: " + port);
            ServerSocket welcomeSocket = new ServerSocket(port);

            ArrayList<String> list = new ArrayList<>();
            while(true) {

                System.out.println("Waiting for Client to connect...");
                Socket connectionSocket = welcomeSocket.accept();

                BufferedReader inFromClient = new BufferedReader(
                new InputStreamReader(connectionSocket.getInputStream()));

                String request;
                while((request = inFromClient.readLine()) != null && !request.isEmpty()) {
                    System.out.println(request);
                    list.add(request.toUpperCase());
                }

                if(list.get(0).split("\\s+")[0].equals("GET")) {
                    String[] strarray = list.get(0).split("\\s+");
                    for(String l : strarray) {
                        System.out.println(l);
                    }
                    if((strarray[1]) != null) System.out.println("\nFILE: " + strarray[1]);

                }

                System.out.println("list.size(): " + list.size());

                OutputStream os = connectionSocket.getOutputStream();

                // tcm / html response
                String response = "HTTP/1.1 200 OK \nContent-Length: 100\nContent-Type: text/html\n\n <HTML> <BODY>Hallo</BODY></HTML>";
                System.out.println(response);
                os.write(response.getBytes("UTF-8"));
                // for(int i = 0; i < list.size(); i++) {
                //     os.write(list.get(i).getBytes("UTF-8"));
                // }
                connectionSocket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

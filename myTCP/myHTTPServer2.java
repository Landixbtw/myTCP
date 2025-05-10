import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class myHTTPServer2 {

    public static String htmlToString(String filename) throws IOException {

        StringBuilder output = new StringBuilder();

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line);
        }
        reader.close();

        return output.toString();
    }

    public static byte[] combine(byte[] a, byte[] b){
        byte[] result = new byte[a.length + b.length];

        for(int i = 0; i < a.length; i++)
            result[i] = a[i];

        for(int i = 0; i < b.length; i++)
            result[a.length + i] = b[i];

        return result;

    }

    public static void main(String args[]) {

        Integer port = 1444;

        try {
            if (args.length >= 2 && args[0].equals("-port"))
                port = Integer.parseInt(args[1]);

            System.out.println("Listen to Port: " + port);
            ServerSocket welcomeSocket = new ServerSocket(port);

            while (true) {

                System.out.println("Waiting for Client to connect...");
                Socket connectionSocket = welcomeSocket.accept();
                System.out.println("Connected");

                BufferedReader inFromClient = new BufferedReader(
                        new InputStreamReader(connectionSocket.getInputStream()));

                ArrayList<String> requests = new ArrayList<>();

                //Request lesen, abbrechen wenn leer oder gleich null (für TCP-Client und Browser)
                String line;
                while(((line = inFromClient.readLine()) != null) && !line.isEmpty()) {
                    requests.add(line);
                }

                //filename aus Request herausfiltern
                String fileName;

                try {   //try block weil es Probleme mit leeren bzw. nicht-HTTP Requests geben kann
                    String[] firstLine = requests.get(0).split(" ");
                        fileName = firstLine[1].substring(1);
                }
                catch (Exception e){
                    System.out.println("Empty Request");
                    fileName = "NotFound";
                    //wenn Fehler auftritt, später im Code den Fehler 404 senden
                }

                //Output String, HTTP Header
                String status;
                String contentLength;
                String contentType;
                byte[] data;


                //gesuchtes File herausfinden und mit Header senden

                //default
                if (fileName.isEmpty()) {
                    status = "HTTP/1.1 200 OK\r\n";
                    contentType = "Content-Type: text/html\r\n\r\n";
                    data = htmlToString("index.html").getBytes("UTF-8");
                    contentLength = "Content-Length: " + data.length + "\r\n";
                }
                //html files
                else if(fileName.endsWith(".html")){
                    contentType = "Content-Type: text/html\r\n\r\n";

                    //falls angegebenes HTML file nicht existiert
                    try {
                        status = "HTTP/1.1 200 OK\r\n";
                        data = htmlToString(fileName).getBytes("UTF-8");
                        contentLength = "Content-Length: " + data.length + "\r\n";
                    }
                    catch (IOException e){
                        status = "HTTP/1.1 404 Not Found\r\n";
                        data = htmlToString("fileNotFound.html").getBytes("UTF-8");
                        contentLength = "Content-Length: " + data.length + "\r\n";
                    }

                }
                //images
                else if(fileName.endsWith(".png")){
                    try {
                        status = "HTTP/1.1 200 OK\r\n";
                        contentType = "Content-Type: image/png\r\n\r\n";

                        //Bild als Byte Array
                        BufferedImage pic = ImageIO.read(new File(fileName));
                        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                        ImageIO.write(pic, "png", byteStream);
                        data = byteStream.toByteArray();
                        contentLength = "Content-Length: " + data.length + "\r\n";
                    }
                    catch (IOException e){
                        status = "HTTP/1.1 404 Not Found\r\n";
                        contentType = "Content-Type: text/html\r\n\r\n";
                        data = htmlToString("fileNotFound.html").getBytes("UTF-8");
                        contentLength = "Content-Length: " + data.length + "\r\n";
                    }
                }
                //file not found
                else{
                    status = "HTTP/1.1 404 Not Found\r\n";
                    contentType = "Content-Type: text/html\r\n\r\n";
                    data = htmlToString("fileNotFound.html").getBytes("UTF-8");
                    contentLength = "Content-Length: " + data.length + "\r\n";
                }

                //Output stream
                OutputStream os = connectionSocket.getOutputStream();

                //Header und Body kombinieren und senden
                byte[] header = (status + contentLength + contentType).getBytes("UTF-8");
                byte[] send = combine(header, data);

                os.write(send);


                //Close connection
                connectionSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ftpclient;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;
import sun.misc.IOUtils;
/**
 *
 * @author ceosa
 */
public class Client {
    Socket socket;
    DataInputStream in;
    BufferedReader reader;
    OutputStream out;
    PrintWriter sendToServer;
    String serverName;
    String userName = "demo"; 
    String password = "demopass";
    Client dataChannel = null;

    public Client(String serverName, int port) throws IOException {
        this.serverName = serverName;
        this.socket = new Socket(serverName, port);
        this.in = new DataInputStream(socket.getInputStream());;
        this.reader = new BufferedReader(new InputStreamReader(in));

        this.out = socket.getOutputStream();
        this.sendToServer = new PrintWriter(out, true);
    }
    
    public String authenticate(String userName, String password) throws IOException{
        boolean authenticated = false;
        sendToServer.print("USER "+ userName + "\n");
        sendToServer.flush();
        getResponse();
        sendToServer.print("PASS " + password + "\n");
        sendToServer.flush();
        while(!authenticated){
            String response = getResponse();
            if(response.endsWith("230 Login successful.")){
                authenticated = true;
                return response;
            }
            if(response.endsWith("530 Login incorrect."))
                return response;
            
        }
        return null;
    } 
    
    public String getResponse() throws IOException{
        while(true){
            String response = this.reader.readLine();
            if(response!= null)
                return response;
            else
                return null;
        }
    }
    
    public ArrayList directory() throws IOException{
        ArrayList<String> directory = new ArrayList<>();
        String response = "";
        boolean endOfDirectory = false;
        while(!endOfDirectory){
            response = getResponse();
            if(response != null)
                directory.add(response);
            else
                endOfDirectory = true;
        }
        return directory; 
    }
    
    public void close() throws IOException{
        this.reader.close();
        this.socket.close();
    }

    public BufferedReader getReader() {
        return reader;
    }

    public PrintWriter getSendToServer() {
        return sendToServer;
    }
    
    public void sendRequest(String request) throws IOException{
        sendToServer.print(request);
        sendToServer.flush();
    }

    public String getServerName() {
        return serverName;
    }
    
    public int cdCommand(String command) throws IOException{
        command = command.replaceFirst("cd", "CWD");
        sendRequest(command + "\n");
        String response = getResponse();
        if(response.startsWith("250"))
            return 0;
        return -1;
    }
    
    public int deleteCommand(String command) throws IOException{
        command = command.replaceFirst("delete", "DELE");
        sendRequest(command + "\n");
        String response = getResponse();
        if(response.startsWith("250"))
            return 0;
        return -1;
    }
    
    public int putCommand(Client data, String command) throws IOException{
        sendRequest("STOR " + command.substring(command.lastIndexOf("\\")+1) 
                + "\n");
        getResponse();
        String inputFile = command.substring(4);
        byte[] allBytes = Files.readAllBytes(Paths.get(inputFile));
        data.out.write(allBytes);
        data.close();
        getResponse();
        return 1;
    }
    
    
}

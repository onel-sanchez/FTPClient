/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ftpclient;
import java.io.*;
import java.net.*;
import java.util.Scanner;
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

    public Client(String serverName, int port) throws IOException {
        this.serverName = serverName;
        this.socket = new Socket(serverName, port);
        this.in = new DataInputStream(socket.getInputStream());;
        this.reader = new BufferedReader(new InputStreamReader(in));
        this.out = socket.getOutputStream();
        this.sendToServer = new PrintWriter(out, true);
    }
    
    public String getResponse() throws IOException{
        while(true){
            String response = this.reader.readLine();
            if(!response.isEmpty())
                return response;
        }
    }

    public BufferedReader getReader() {
        return reader;
    }

    public PrintWriter getSendToServer() {
        return sendToServer;
    }
    
    public void sendRequest(String request){
        sendToServer.print(request);
        sendToServer.flush();
    }

    public String getServerName() {
        return serverName;
    }
    
    
}

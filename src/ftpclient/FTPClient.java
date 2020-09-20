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
public class FTPClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
      
        System.out.println("Specify the Hostname");
        Client client = new Client("inet.cs.fiu.edu", 21 );
        if(client.getResponse().startsWith("220")){
            client.sendToServer.print("OPTS UTF8 ON\n");
            client.sendToServer.flush();
            System.out.println("Connecting to " + client.getServerName() + "...");
            System.out.print("SERVER: Username: ");
            //Scanner user = new Scanner(System.in);           
            //username = user.nextLine();
            client.sendToServer.print("USER "+ username + "\n");
            client.sendToServer.flush();
        }
        
        
        
       /*
        String serverName = "inet.cs.fiu.edu";
        int port = 21;
        String username = "demo"; 
        String pass = "demopass";
        Socket socket;
        socket = createSocket(serverName, port);
        
        DataInputStream in = new DataInputStream(socket.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        OutputStream out = socket.getOutputStream();
        PrintWriter sendToServer = new PrintWriter(out, true);
        
        if(reader.readLine().startsWith("220")){
            sendToServer.print("OPTS UTF8 ON\n");
            sendToServer.flush();
            System.out.println("Connecting to " + serverName + "...");
            System.out.print("SERVER: Username: ");
            //Scanner user = new Scanner(System.in);           
            //username = user.nextLine();
            sendToServer.print("USER "+ username + "\n");
            sendToServer.flush();

            System.out.print("SERVER: Password: ");
            //Scanner password = new Scanner(System.in);
           // pass = password.nextLine();

            sendToServer.print("PASS " + pass + "\n");
            sendToServer.flush();
            boolean quit = false;
            boolean responded = false;
            boolean connected = false;
            boolean pasvResponded =false;
            String temp = "";
            while(!quit){
                if(reader.readLine().endsWith("230 Login successful.") && !connected ){
                    System.out.print("SERVER: Login Successful\n");
                    connected =true;
                }
                if(connected){
                    sendToServer.print("PASV\n");
                    sendToServer.flush();
                    
                    while(!pasvResponded){
                        temp = reader.readLine();
                        if(temp.startsWith("227")){
                            pasvResponded =true;
                            port = calculatePort(temp);
                            
                            
                            //reader.close();
                            //sendToServer.close();
                            //socket.close();
                            String csv = temp.substring(temp.indexOf("(")+1, temp.indexOf(")"));
                            String[] elems = csv.split(",");
                            socket = createSocket(elems[0].concat("."+elems[1]).concat("."+elems[2]).concat("."+elems[3]), port);
                            
                            in = new DataInputStream(socket.getInputStream());
                            reader = new BufferedReader(new InputStreamReader(in));
                            out = socket.getOutputStream();
                            sendToServer = new PrintWriter(out, true);

                            if(reader.readLine().startsWith("220")){
                                System.out.print("SERVER: Waiting for a command\n");
                                Scanner command = new Scanner(System.in);
                                if(command.nextLine().equalsIgnoreCase("ls")){
                                    sendToServer.print("NLST\n");
                                    sendToServer.flush();

                                    while (!responded){
                                        if(!reader.readLine().isEmpty()){
                                            responded =true;
                                            System.out.print((String)reader.readLine());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
    }
    public static int calculatePort(String pasvResponse){
        String csv = pasvResponse.substring(pasvResponse.indexOf("(")+1, pasvResponse.indexOf(")"));
        String[] elements = csv.split(",");
        int p1 = Integer.parseInt(elements[4]);
        int p2 = Integer.parseInt(elements[5]);
        return (p1*256)+ p2;
    }
    
    public static Socket createSocket(String serverName, int port){
        Socket socket = new Socket();
        try{
              socket = new Socket(serverName, port);
              return socket;
          }
          catch(Exception e){
              System.out.println("Unknown host");
          }
         return null;*/
    }
}

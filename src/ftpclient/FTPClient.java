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
        String serverName = "inet.cs.fiu.edu";
        int port = 21;
        String username = "demo"; 
        String pass = "demopass";
        String line = "";
        Socket socket;
        while(true){
            
            try{
                socket = new Socket(serverName, port);
                break;
            }
            catch(Exception e){
                System.out.println("Unknown host");
            }
        }
       
        
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
            
            while(!quit){
                if(reader.readLine().endsWith("230 Login successful.") && !connected ){
                    System.out.print("SERVER: Login Successful\n");
                    connected =true;
                }
                if(connected){
                    System.out.print("SERVER: Waiting for a command\n");
                    sendToServer.print("PASV\n");
                    sendToServer.flush();
                    Scanner command = new Scanner(System.in);
                    
                    
                    if(command.nextLine().equalsIgnoreCase("ls")){
                        reader = new BufferedReader(new InputStreamReader(in));
                        sendToServer.print("NLST\n");
                        sendToServer.flush();
                        
                        
                        while (!responded){
                            if(!reader.readLine().isEmpty() && !reader.readLine().startsWith("227")){
                                responded =true;
                                System.out.print((String)reader.readLine());
                            }
                             reader = new BufferedReader(new InputStreamReader(in));
                        }
                    }
                }
            }
        }
        
    }
    public static int port(String port){
        String csv = port.substring(port.indexOf("(")+1, port.indexOf(")")-1);
        String[] elements = port.split(",");
        int p1 = Integer.parseInt(elements[4]);
        int p2 = Integer.parseInt(elements[5]);
        return (p1*256)+ p2;
    }
    
}

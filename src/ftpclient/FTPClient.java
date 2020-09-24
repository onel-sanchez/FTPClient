/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ftpclient;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
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
        boolean authenticated = false;
        String username;
        String pass;
        Client client = null;
        Client dataConnection = null;
        int port;
        
        
        while(!authenticated){
            System.out.println("myftp> Specify the Hostname:");
            client = new Client("inet.cs.fiu.edu", 21 );
            if(client.getResponse().startsWith("220")){
                client.sendRequest("OPTS UTF8 ON\n");
                client.getResponse();
                System.out.println("myftp> Connecting to " + client.getServerName() + "...");
                System.out.print("myftp> Username: ");
                //Scanner user = new Scanner(System.in);           
                //username = user.nextLine();
                System.out.print("myftp> Password: ");
                //Scanner password = new Scanner(System.in);
                //pass = password.nextLine();
                if(client.authenticate("demo", "demopass").endsWith("230 Login successful.")){      
                    System.out.print("myftp> Login Successful.\n");
                    authenticated = true;
                }
                else{
                    System.out.print("myftp> Login Incorrect.\n");
                    client.close();
                }
            }         
        }
        
        boolean quit = false;
        while(!quit){
            System.out.print("myftp> ");
            Scanner userInput = new Scanner(System.in);
            String command = userInput.nextLine();
            if(command.equalsIgnoreCase("quit")){
                quit = true;
                client.sendRequest("QUIT\n");
                client.close();
                System.out.print("myftp> Good Bye!!\n");
            }
            if(command.equalsIgnoreCase("ls")){
                dataConnection = dataChannel(client);
                client.sendRequest("NLST\n");
                client.getResponse();
                ArrayList<String> directory = dataConnection.directory();
                client.getResponse();
                System.out.println("myftp> List of Directories:");
                for (int i = 0; i < directory.size(); i++){
                    System.out.println(directory.get(i));
                }
            }
            if(command.startsWith("cd ")){
                if(client.cdCommand(command)==0)
                    System.out.println("myftp> Directory successfully changed.");
                else
                    System.out.println("myftp> Failed to change directory.");
            }
            if(command.startsWith("delete ")){
                if(client.deleteCommand(command)==0)
                    System.out.println("myftp> Delete operation successful.");
                else
                    System.out.println("myftp> Delete operation failed.");
            }
            if(command.startsWith("put ")){
                dataConnection = dataChannel(client);
                client.putCommand(dataConnection, command);
                dataConnection.close();
                System.out.println("myftp> File Transfered.");
            }
            if(command.startsWith("get ")){
                dataConnection = dataChannel(client);
                if(client.getCommand(dataConnection, command)== -1)
                    System.out.println("myftp> File Transfered failed. ");
                else
                    System.out.println("myftp> Transfer complete.");
                dataConnection.close();
            }
            
            
        }   
            
    }
    
    public static Client dataChannel(Client client) throws IOException{
        client.sendRequest("PASV\n");
        String response ="";
        do{
            response = client.getResponse();
        }
        while(!response.startsWith("227"));
            if(response.startsWith("227")){
                int port = calculatePort(response);
                Client dataConnection = new Client(client.getServerName(), port);
                client.sendRequest("TYPE I\n");
                client.getResponse();
                return dataConnection;
            }
        return null;
    }
    
    public static int calculatePort(String pasvResponse){
        String csv = pasvResponse.substring(pasvResponse.indexOf("(")+1, pasvResponse.indexOf(")"));
        String[] elements = csv.split(",");
        int p1 = Integer.parseInt(elements[4]);
        int p2 = Integer.parseInt(elements[5]);
        return (p1*256)+ p2;
    }
    
    public static String hostPort(String response){
        String hostPort = response.substring(response.indexOf("(")+1, response.indexOf(")"));
        return hostPort;
    }   
}

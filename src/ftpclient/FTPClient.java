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
                System.out.println("myftp> Connecting to " + client.getServerName() + "...");
                System.out.print("myftp> Username: ");
                Scanner user = new Scanner(System.in);           
                username = user.nextLine();
                System.out.print("myftp> Password: ");
                Scanner password = new Scanner(System.in);
                pass = password.nextLine();
                if(client.authenticate(username, pass).endsWith("230 Login successful.")){
                    System.out.print("myftp> Login Successful.\n");
                    authenticated = true;
                }
            }         
        }
        client.sendRequest("PASV\n");
        String response = client.getResponse();
        if(response.startsWith("227")){
            port = calculatePort(response);
            dataConnection = new Client(client.getServerName(), port);
            client.sendRequest("TYPE I\n");
        }
        boolean quit = false;
        while(!quit){
            System.out.print("myftp> ");
            Scanner userInput = new Scanner(System.in);
            String command = userInput.nextLine();
            if(command.equalsIgnoreCase("quit")){
                quit = true;
                client.sendRequest("QUIT\n");
                dataConnection.close();
                client.close();
                System.out.print("myftp> Good Bye!!");
            }
            if(command.equalsIgnoreCase("ls")){
                client.sendRequest("NLST\n");
                ArrayList<String> directory = dataConnection.directory();
                System.out.println("myftp> List of Directories:");
                for (int i = 0; i < directory.size(); i++){
                    System.out.println(directory.get(i));
                }
            }
            if(command.substring(0, 2).equalsIgnoreCase("cd ")){
                
            }
            if(command.substring(0, 2).equalsIgnoreCase("get ")){
                
            }
            if(command.substring(0, 2).equalsIgnoreCase("put ")){
                
            }
            if(command.substring(0, 2).equalsIgnoreCase("delete ")){
                
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
    
    public static String hostPort(String response){
        String hostPort = response.substring(response.indexOf("(")+1, response.indexOf(")"));
        return hostPort;
    }   
}

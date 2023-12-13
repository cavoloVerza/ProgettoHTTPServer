package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class App {
    public static void main( String[] args ){
        // Server

        try {

            ServerSocket server = new ServerSocket(3000);

            Socket s = server.accept();  
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            String line;
            line = in.readLine();

            String[] stringhe = line.split(" ");
            String path = stringhe[1].substring(1);
                
            do{

                line = in.readLine();
                System.out.println("La string è: " + line);

            }while(!line.isEmpty());

            File file = new File(path);
            boolean flag = file.exists();

            if(flag) {

                System.out.println("File trovato");
            }

            else {

                System.out.println("File non trovato");
            }
            
            s.close();
            server.close();

        } 

        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("errore durante l'istanza del server");
            System.exit(1);
        }

    }

}
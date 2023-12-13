package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class App {
    public static void main( String[] args ){
        // Server

        try {

            ServerSocket server = new ServerSocket(3000);

            while(true) {

                Socket socket = server.accept();  
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                String line;
                line = in.readLine();
                System.out.println("La string è: " + line);

                String[] stringhe = line.split(" ");
                String path = stringhe[1].substring(1);
                    
                do{

                    line = in.readLine();
                    System.out.println("La string è: " + line);

                }while(!line.isEmpty());

                File file = new File(path);
                boolean flag = file.exists();

                if(flag) {

                    String msg = "File trovato";
                    System.out.println("msg");

                    out.writeBytes("HTTP/1.1 200 OK\n");
                    out.writeBytes("Content-Lenght: " + msg.length() + '\n');
                    out.writeBytes("\n");
                    out.writeBytes(msg);

                }

                else {

                    String msg = "File non trovato";
                    System.out.println("msg");

                    out.writeBytes("HTTP/1.1 400 Not found\n");
                    out.writeBytes("Content-Lenght: " + msg.length() + '\n');
                    out.writeBytes("\n");
                    out.writeBytes(msg);

                }

                socket.close();
            }

        } 

        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("errore durante l'istanza del server");
            System.exit(1);
        }

    }

    private static void sendBinaryFile(Socket socket, File file) throws IOException{

        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        output.writeBytes("HTTP/1.1 200 OK\n");
        output.writeBytes("Contenet-Lenght: " + file.length() + "\n");

        output.writeBytes("Content-Type: " + getContentType(file) + "\n");

        output.writeBytes("\n");
        InputStream input = new FileInputStream(file);
        byte[] buf = new byte[8192];
        int n;

        while((n = input.read(buf)) != -1) {

            output.write(buf, 0, n);;
        }
        input.close();
    
    }

    private static String getContentType(File file) {
        return null;
    }

}
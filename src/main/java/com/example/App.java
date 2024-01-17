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

                String line = in.readLine();
                System.out.println("La string è: " + line);

                String[] stringhe = line.split(" ");
                System.out.println("La string è: " + stringhe[1]);
                    
                do{

                    line = in.readLine();
                    System.out.println("La string è: " + line);

                }while(!line.isEmpty());

                File file = null;

                if(stringhe[1].substring(stringhe[1].length()-1).equals("/")) {

                    file = new File("hatdocs/index.html"); //se vuoto metto index di default

                }

                else {

                    if(stringhe[1].substring(1).equals("test")) {

                        out.writeBytes("HTTP/1.1 301 Moved Permanently\n");
                        out.writeBytes("Location: https://www.google.com\n");
                        out.writeBytes("\n");

                    }

                    else {

                        file = new File("htdocs/"+stringhe[1].substring(1)); //cerco il file con quell'indirizzo

                    }

                }

                String rawResponse = "";

                if(file.exists()) {

                    sendBinaryFile(socket, file);
                }

                else {

                    String response = "Il file non esiste.";
                    int responseLength = response.length();

                    rawResponse += "HTTP/1.1 404 Not Found\n";
                    rawResponse += "Content-Length: " + responseLength + "\n";
                    rawResponse += "Content-Type: text/plain\n";
                    rawResponse += "\n";
                    rawResponse += response;
                    
                }

                out.writeBytes(rawResponse);
                socket.close();
            }

        } 

        catch (Exception e) {
            System.err.println(e.getMessage());
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
        String filename = file.getName();
        String[] temp = filename.split("\\.");
        String extension = temp[temp.length - 1];
        switch (extension) {
            case "html":
                return "Content-Type: text/html";
            case "png":
                return "Content-Type: image/png";
            case "jpeg":
            case "jpg":
                return "Content-Type: image/jpeg";
            case "css":
                return "Content-Type: text/css";
            default:
                return "Content-Type: text/plain";
        }
    }

}
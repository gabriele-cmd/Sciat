package it.client;

import java.io.*;
import java.net.*;

public class ClientListener extends Thread{
    String stringaInDalServer; //stringa ricevuta dal SERVER
    BufferedReader inDalServer; //stream di INPUT dal SERVER
    Socket socket; //canale di comunicazione
    
    public ClientListener(Socket socket){
        try {
            this.socket = socket;
            inDalServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));   
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public void run(){
        while(!socket.isClosed()){ //finché il socket risulta CONNESSO e APERTO, il client continuerà a RICEVERE messaggi
            try {
                stringaInDalServer = inDalServer.readLine(); //LEGGO la stringa ricevuta
                System.out.println(stringaInDalServer);
            } catch (Exception e) {
                System.out.println("Hai abbandonato il gruppo!");
                System.exit(1);
            }
        }
    }
}

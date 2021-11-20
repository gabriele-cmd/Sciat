package it.client;

import java.io.BufferedReader;
import java.net.Socket;

public class ClientListener implements Runnable{
    String stringaInDalServer; //stringa ricevuta dal SERVER
    BufferedReader inDalServer; //stream di INPUT dal SERVER
    Socket socket; //canale di comunicazione
    
    public ClientListener(BufferedReader inDalServer, Socket socket){
        this.inDalServer = inDalServer;
        this.socket = socket;
    }

    public void run(){
        while(!socket.isClosed()){ //finché il socket risulta CONNESSO  e APERTO, il client continuerà a RICEVERE messaggi
            try {
                stringaInDalServer = inDalServer.readLine(); //LEGGO la stringa ricevuta
            } catch (Exception e) {
                System.out.println("Hai abbandonato il gruppo!");
                System.exit(1);
            }
        }
    }
}

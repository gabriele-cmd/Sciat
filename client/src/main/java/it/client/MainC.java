package it.client;

import java.io.IOException;

public class MainC {
    public static void main( String[] args ) throws IOException{
        Client utente = new Client();
        utente.connetti();
        utente.comunica();
        while(!utente.socket.isClosed()){
            utente.invia();
        }
    }
}

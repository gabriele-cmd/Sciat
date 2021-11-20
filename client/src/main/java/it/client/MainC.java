package it.client;

public class MainC {
    public static void main( String[] args ){
        Client utente = new Client();
        utente.connetti();
        utente.comunica();
    }
}

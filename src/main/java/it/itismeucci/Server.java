package it.itismeucci;

import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    public void start(){
        try{
            ServerSocket serverSocket = new ServerSocket(6789);
            for(;;){
                Socket socket = serverSocket.accept(); //In ATTESA di comunicazione
                System.out.println("Connessione di " + socket);
                ServerThread serverThread = new ServerThread(socket); //Apro il THREAD per il client
                serverThread.start();
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("Errore durante l'istanza dels erver !");
            System.exit(1);
        }
    }
}
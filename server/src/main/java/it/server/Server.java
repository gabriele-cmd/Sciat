package it.server;

import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    public void start(){
        ThreadMap threadMap = new ThreadMap();//creazione dell'HashMap per gestire i thread del server
        try{
            ServerSocket serverSocket = new ServerSocket(6789);//BIND della porta
            for(;;){
                Socket socket = serverSocket.accept(); //In ATTESA di comunicazione con il CLIENT
                System.out.println("Connessione di " + socket);
                ServerThread serverThread = new ServerThread(socket, serverSocket, threadMap); //Apro il THREAD per il client
                serverThread.start();//avvio thread
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("Errore durante l'istanza del server !");
            System.exit(1);
        }
    }
}
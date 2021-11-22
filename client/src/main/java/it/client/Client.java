package it.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private String nomeServer = "localhost"; //nome del SERVER a cui il client si connette
    private int portaServer = 3456; //PORTA del SERVER a cui il Client si connette
    String stringaInDalServer;
    DataOutputStream outputVersoServer;
    BufferedReader inputDalServer;
    BufferedReader tastiera; //INPUT da tastiera
    private Thread listener;//THREAD che gestisce la RICEZIONE dei messaggi
    Socket socket; //canale di comunicazione

    public Socket connetti(){//CONNESSIONE alla PORTA del SERVER
        try {
            socket = new Socket(nomeServer, portaServer);//APERTURA canale di comunicazione
            outputVersoServer = new DataOutputStream(socket.getOutputStream());
            tastiera = new BufferedReader(new InputStreamReader(System.in));

        } catch (UnknownHostException e) {
            System.err.println("Host sconosciuto");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("Errore durante la connessione");
            System.exit(1);
        }

        return socket;
    }

    public void comunica() throws IOException{//gestisco la CONNESSIONE con il SERVER
        listener = new ClientListener(socket);
        listener.start();//utilizzo il Thread per inviare messaggi
        invia();
    }

    public String setUsername (String username) throws IOException{
        outputVersoServer.writeBytes(username + '\n');//INVIO l'username scelto
        stringaInDalServer = inputDalServer.readLine(); //LEGGO la RISPOSTA del server

        return stringaInDalServer;
    }

    public void invia() throws IOException{//INVIO messaggi al SERVER
        String mexInvio = tastiera.readLine();
        if(mexInvio.equals("/exit")){
            outputVersoServer.writeBytes("/exit" + "\n");
            socket.close();
        }else{
            outputVersoServer.writeBytes(mexInvio + '\n');
        }
    }
}

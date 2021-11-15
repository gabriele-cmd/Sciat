package it.itismeucci;

import java.net.*;
import java.io.*;

public class ServerThread extends Thread{
    private ServerSocket server;
    private Socket client; //socket che si connette al CLIENT
    private String stringaRicevuta; //stringa ricevuta dal CLIENT
    private BufferedReader inDalClient;
    private DataOutputStream outVersoClient;
    private String usernameClient;

    public ServerThread(Socket socket, ServerSocket serverSocket) {
        this.client = socket;
        this.server = serverSocket;

    }

    public void run(){  //Il metodo RUN del THREAD si limita solo a richiamare il metodo COMUNICA() che avvia la comunicazione con il CLIENT

        try{
            comunica();
            connetti();
        }
        catch(Exception e){
            e.printStackTrace(System.out);
        }
    }

    public void connetti() throws Exception{

        try {
            outVersoClient = new DataOutputStream(client.getOutputStream()); //Inizializzo flusso VERSO client
            inDalClient = new BufferedReader(new InputStreamReader(client.getInputStream())); //Inizializzo flusso DAL client
            String controllo;
            do{
                usernameClient = inDalClient.readLine(); //Leggo il NOME inserito da TASTIERA con readLine()

            }while(!controllo.equals("success"));
        } 
        
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void comunica(){

    }
}

package it.itismeucci;

import java.io.*;
import java.net.Socket;
import java.util.List;

import javax.swing.JOptionPane;

public class Client {
    private String nomeServer = "localhost";
    private int portaServer = 3456;
    private List<String> listaClient;
    private BufferedReader tastiera;
    private BufferedReader inputDalServer;
    private DataOutputStream outputVersoServer;
    private Socket socket; 


    public void Client(List lista, String serverIP, int serverPort){
        listaClient = lista;
        try {
            socket = new Socket(serverIP, serverPort);
            inputDalServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //output = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile connettersi al Server!");
        }
    }

    public Socket connetti(){
        try {
            tastiera = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socket = new Socket(nomeServer, portaServer);
            inputDalServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputVersoServer = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {

            e.printStackTrace();
        }
        return socket;
    }
}
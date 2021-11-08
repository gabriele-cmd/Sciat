package it.itismeucci;
import java.io.*;
import java.net.Socket;
import java.util.List;

import javax.swing.JOptionPane;

public class Client {
    private List<String> listaClient;
    private BufferedReader input = null;
    private PrintWriter output = null;
    private Socket socket;
    private String username;


    public void Client(List lista, String serverIP, int serverPort){
        listaClient = lista;
        try {
            socket = new Socket(serverIP, serverPort);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile connettersi al Server!");
        }
    }
}
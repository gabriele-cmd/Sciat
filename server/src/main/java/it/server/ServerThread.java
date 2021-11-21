package it.server;

import java.net.*;
import java.io.*;

public class ServerThread extends Thread{
    private ThreadMap threadMap;
    ServerSocket server;//porta SERVER
    private Socket client; //socket che si connette al CLIENT
    private String mexRicevuto; //stringa ricevuta dal CLIENT
    private BufferedReader inDalClient;
    private DataOutputStream outVersoClient;
    private String usernameClient;

    public ServerThread(Socket socket, ServerSocket serverSocket, ThreadMap threadMap) {
        this.client = socket;
        this.server = serverSocket;
        this.threadMap = threadMap;
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
            outVersoClient.writeBytes("Inserire un Username: (caratteri validi: A-Z/a-z/0-9)");
            String controllo;
            do{
                usernameClient = inDalClient.readLine(); //Leggo il NOME inserito da TASTIERA con readLine()
                controllo = threadMap.connessioneClient(usernameClient, this);

                if(controllo.equals("esistente")){//controllo se l'Username scelto è GIA PRESENTE tra i client connessi
                    System.out.println("SERVER: Errore! l'Username inserito è già in utilizzo, riprova..." + '\n');

                }else if(controllo.equals("simboli presenti")){//controllo non siano presenti simboli
                    System.out.println("SERVER: Errore! L'Username NON può contenere caratteri speciali. Si prega di Scegliere un Username contenente solo caratteri da A-Z/a-z o numeri compresi tra 0-9");

                }else if(controllo.equals("vuoto")){//controllo che il nome scelto non sia vuoto
                    System.out.println("SERVER: Errore! L'Username scelto non presenta caratteri, è VUOTO! Riprova...");
                }
            }while(!controllo.equals("ok"));

            outVersoClient.writeBytes("SERVER: Username scelto!" + '\n');
            threadMap.globale(usernameClient, "SERVER: " + usernameClient + "si è unito al gruppo!");
            threadMap.lista(); //eseguo aggiornamento sulla LISTA
            threadMap.invioLista(usernameClient); //Invio la LISTA SOLO al Client che si è APPENA CONNESSO per informarlo sugli utenti al momento connessi alla chat
        } 
        
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void comunica() throws Exception{
        do{
            mexRicevuto = inDalClient.readLine(); //LEGGO il messaggio del Client
            switch(mexRicevuto.charAt(0)){

                case '@'://messaggio PRIVATO
                    String mexPrivato = mexRicevuto.substring(1); //Rimuovo il carattere @ per avere direttamente la forma "destinatario messaggio"
                    String[] parole = mexPrivato.split(" ");
                    threadMap.privato(usernameClient, parole[0], parole[1]);
                    break;
                
                case '/': //comando specifico
                    switch(mexRicevuto){
                        case "/list": //richiesta della LISTA dei Client
                            threadMap.invioLista(usernameClient);
                            break;
                        case "/exit":
                            outVersoClient.close();
                            inDalClient.close();
                            client.close();
                            break;
                        default: //in caso un utente inserisca "//" il messaggio dovrà comunque essere mandato in GLOBALE
                            String str = mexRicevuto.replace(" ", ""); //mi assicuro di rimuovere tutti gli spazi vuoti per controllare che l'utente non abbia inserito un messaggio del tipo "      "
                            if(str.isEmpty()){
                                outVersoClient.writeBytes("SERVER: Errore, messsaggio vuoto!" + '\n');
                                break;
                            }

                            threadMap.globale(usernameClient, mexRicevuto);
                            break;
                    }
                    default: //I messaggi GLOBALI non richiedono nessun inserimento specifico perciò qualsiasi messaggio che non sia un comando specifico o che non abbia la @ davanti verrà mandato a tutti i Client
                    String str = mexRicevuto.replace(" ", ""); //mi assicuro di rimuovere tutti gli spazi vuoti per controllare che l'utente non abbia inserito un messaggio del tipo "      "
                    if(str.isEmpty()){
                        outVersoClient.writeBytes("SERVER: Errore, messsaggio vuoto!" + '\n');
                        break;
                    }

                    threadMap.globale(usernameClient, mexRicevuto);
                    break;
            }
        }while(!mexRicevuto.equals("/exit"));
    }

    public void invia(String messaggio) throws Exception{ //INVIO di un messaggio al Client
        outVersoClient.writeBytes(messaggio + '\n');
    }
}

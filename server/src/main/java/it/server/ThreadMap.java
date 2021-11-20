package it.server;

import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ThreadMap {

    HashMap<String, ServerThread> threadMap = new HashMap<String, ServerThread>();

    public ThreadMap(){

    }

    public String connessioneClient (String username, ServerThread server){//connessione e aggiunta di un client
        Pattern pattern = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(username);

        if(matcher.find()){
            return "simboli presenti";
        }

        if(threadMap.putIfAbsent(username, server) != null){
            return "esistente";
        }
        
        return "ok";
        
    }

    public void disconnessioneClient(String username, ServerThread server) throws Exception{//disconnessione e rimozione di un client con conseguente aggiornamento della lista
        threadMap.remove(username, server);
        globale (username, "SERVER: " + username + " ha abbandonato il gruppo");
        //aggLista(); //lista che dovrebbe essere inviata a ogni connessione/disconnessione
        lista();
    }

    public void invioLista(String username) throws Exception{//messaggio di invio LISTA al Client richiedente (attraverso COMANDO apposito)
        String lista;
        for(String client: threadMap.keySet()){
            if(client.equals(username)){
                lista = lista();
                threadMap.get(client).invia(lista);
            }
        }
    }

    public void notificaConnessione(){//notifica TUTTI i Client che un nuovo utente si è CONNESSO

    }

    public String lista(){//creazione necessaria e aggiornamento della lista di utenti connessi
        String lista = "LISTA: " ;

        for(String i : threadMap.keySet()){
            lista += i + ", ";//separo ogni client con una virgola (,)
        }

        return lista;
    }

    public void globale(String username, String messaggio) throws Exception{//invio messaggio globale
        for(String i : threadMap.keySet()){
            if (i == username){//il client che invia il messaggio globale non deve ricevere a sua volta il messaggio stesso!
                continue;
            }else{
                threadMap.get(i).invia(messaggio);
            }
        }
    }

    public void privato(String clientMittente, String clientDestinatario, String messaggio) throws Exception{//invio messaggio privato
        if(threadMap.containsKey(clientDestinatario)){
            threadMap.get(clientDestinatario).invia("@" + clientMittente + ": " + messaggio);
        }else{ //se non trova nessun utente col nome fornito come Destinatario...
            threadMap.get(clientMittente).invia("SERVER: Utente inesistente o Sintassi Errata");
        }
    }
}

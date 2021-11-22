package it.server;

import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ThreadMap {

    HashMap<String, ServerThread> threadMap = new HashMap<String, ServerThread>();

    public String connessioneClient (String username, ServerThread server){//connessione e aggiunta di un client
        Pattern pattern = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(username);

        if(matcher.find()){
            return "simboli presenti";
        }

        if(threadMap.putIfAbsent(username, server) != null){
            return "esistente";
        }

        username.replace(" ", ""); //mi assicuro di rimuovere tutti gli spazi vuoti per controllare che l'utente non abbia inserito un username del tipo "      "
            if(username.isEmpty()){
                return "vuoto";
            }
        
        return "ok";
        
    }

    public void disconnessioneClient(String username, ServerThread server) throws Exception{//disconnessione e rimozione di un client con conseguente aggiornamento della lista
        threadMap.remove(username, server);
        globale (username, "SERVER: " + username + " ha abbandonato il gruppo");
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

    public String lista(){//creazione necessaria e aggiornamento della lista di utenti connessi
        String lista = "LISTA: " ;

        for(String i : threadMap.keySet()){
            lista += i + ", ";//separo ogni client con una virgola (,)
        }

        return lista;
    }

    public void globale(String username, String messaggio) throws Exception{//invio messaggio globale
        if(threadMap.size() == 1 && threadMap.containsKey(username)){//In caso l'utente che invia il messaggio è SOLO, il messaggio NON verrà INOLTRATO
            threadMap.get(username).invia("SERVER: Sei l'unico utente connesso!");
            return;
        }
        for(String i : threadMap.keySet()){
            if (i == username){//il client che invia il messaggio globale non deve ricevere a sua volta il messaggio stesso!
                continue;
            }else{
                String[] controllo = messaggio.split(" ");
                if(controllo[0].equals("SERVER:")){
                    threadMap.get(i).invia(messaggio);
                }else{
                    threadMap.get(i).invia(username + ": " + messaggio);
                }
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

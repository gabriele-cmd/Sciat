package it.itismeucci;

import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class HashMap {

    HashMap<String, ServerThread> threadMap = new HashMap(String, ServerThread);

    public String connessioneClient (String username, ServerThread server){
        Pattern pattern = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(username);

        if(matcher.find()){
            return "errore: simboli presenti";
        }
        
    }
}

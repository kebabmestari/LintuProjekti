package lib;

import java.util.ArrayList;

import databaseconnection.DB_connection;
import databaseobjects.Json;
import databaseobjects.Paivamaara;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;

public class Operations {
    
    /**
     * Muuntaa taulukon JSONiksi
     * @param array
     * @param connection
     * @return 
     */
    public static String arrayToJSON(ArrayList<?> array, DB_connection connection) {
            String json;
            int pituus=array.size();
            if(pituus>0){
                    json="[\n"+((Json)array.get(0)).toJSON(connection);
            }else{
                    return "[]";
            }
            for(int i=1;i<pituus;i++){
                    json += ",\n"+((Json)array.get(i)).toJSON(connection);
            }
            json+="\n]";
            return json;
    }
        
    /**
     * Palauttaa tämänhetkisen ajan MySQL:n ymmärtämänä stringinä
     * @return 
     */
    public static String getMySQLTime(){
        java.util.Date dt = new java.util.Date();

        java.text.SimpleDateFormat sdf = 
             new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sdf.format(dt);        
    }
    
    /**
     * Palauttaa tämänhetkisen ajan Paivamaara-oliona
     * @return 
     */
    public static Paivamaara getMySQLPvm(){
        
        Calendar cal = Calendar.getInstance();
        Paivamaara pvm = null;
        try{
            pvm = new Paivamaara(
                    cal.get(Calendar.DAY_OF_MONTH),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.YEAR));
        } catch(Exception e){
            e.printStackTrace();
        }
        return pvm;
    }
    
    public static String readQuery(String queryname) throws Exception{
        File f = new File("kyselyt.txt");
        if(!f.exists()){
            System.err.println("Tiedostoa kyselyt.txt ei löydy");
            throw new Exception("Virhe tiedostonluvussa");
        }
        try{
            BufferedReader cin = new BufferedReader(new FileReader(f));
            if(scrollPastLine(cin, queryname)){
                return readQueryString(cin);
            }
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
        
        throw new Exception("Virhe tiedostonluvussa");
    }

    private static boolean scrollPastLine(BufferedReader cin, String line){
        try{
            while(true){
                String s = cin.readLine();
                if(s == null){
                    System.err.println("Kyselyä " + line + " ei löydy tiedostosta!");
                    return false;
                }
                if(s.length() > 1){
                    if(s.substring(1).equals(line) && s.charAt(0) == '>'){
                        return true;
                    }
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }
    
    private static String readQueryString(BufferedReader cin){
        StringBuilder result = new StringBuilder("");
        try{
            String s = null;
            while((s = cin.readLine()) != null){
                s = s.trim();
                if(s.isEmpty()) break;
                if(s.charAt(0) != '#'){
                    result.append(" " + s);
                }   
            }
            if(result == null){
                System.err.println("Virhe kyselyä luettaessa");
            }
            return result.substring(1).toString();
        } catch(IOException e){
            e.printStackTrace();
        }
        System.err.println("SQL-kyselyä ei voitu lukea");
        return "";
    }
            
}

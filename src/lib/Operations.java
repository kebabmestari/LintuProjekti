package lib;

import java.util.ArrayList;

import databaseconnection.DB_connection;
import databaseobjects.Json;
import databaseobjects.Paivamaara;
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

}

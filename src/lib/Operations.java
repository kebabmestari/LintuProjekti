package lib;

import java.util.ArrayList;

import databaseconnection.DB_connection;
import databaseobjects.Json;
import databaseobjects.Kalahavainto;
import databaseobjects.Lintuhavainto;
import databaseobjects.Paivamaara;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
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
    
    /**
     * Muuntaa resultsetin ensimmäisen jäsenen Lintuhavainto-objektiksi
     * @param rs Resultset joka saatu tietokannalta
     * @return Lintuhavainto-objekti
     */
    public static Lintuhavainto RStoLintuHavainto(ResultSet rs){
        try{
        if(rs.next())
            return new Lintuhavainto(rs.getInt("lintuid"), rs.getString("paikka"),
                    new Paivamaara(rs.getString("paivamaara")), rs.getInt("id"),
                    rs.getInt("havaitsija"), rs.getBoolean("eko"), rs.getBoolean("sponde"));
        } catch(Exception e){
            e.printStackTrace();
        }
        System.err.println("Muuntaminen Lintuhavainto-objektiksi ei toiminut");
        return null;
    }
    
    /**
     * Muuntaa resultsetin kokonaan Lintuhavaintolistaksi
     * @param rs Resultset joka saatu tietokannalta
     * @return Lintuhavaintolista
     */
    public static ArrayList<Lintuhavainto> wholeRStoLintuHavainto(ResultSet rs){
        ArrayList<Lintuhavainto> birdWatchArray=new ArrayList<>();
        try{
            while(rs.next()){
                birdWatchArray.add(new Lintuhavainto(rs.getInt("lintuid"), rs.getString("paikka"),
                        new Paivamaara(rs.getString("paivamaara")), rs.getInt("id"),
                        rs.getInt("havaitsija"), rs.getBoolean("eko"), rs.getBoolean("sponde")));
            }
            return birdWatchArray;
        } catch(Exception e){
            e.printStackTrace();
        }
        System.err.println("Muuntaminen Lintuhavainto-objektiksi ei toiminut");
        return null;
    }
    
    /**
     * Muuntaa resultsetin ensimmäisen jäsenen Kalahavainto-objektiksi
     * @param rs Resultset joka saatu tietokannalta
     * @return Kalahavainto-objekti
     */
    public static Kalahavainto RStoKalaHavainto(ResultSet rs){
        try{
        if(rs.next())
            return new Kalahavainto(rs.getString("paikka"), rs.getInt("pituus"), rs.getInt("kalaid"),
                        rs.getInt("havaitsija"), new Paivamaara(rs.getString("paivamaara")));
        } catch(Exception e){
            e.printStackTrace();
        }
        System.err.println("Muuntaminen Kalahavainto-objektiksi ei toiminut");
        return null;
    }
    
    public static String readQuery(String queryname) throws Exception{
        return readQuery(queryname, false);
    }
    
    public static String readQuery(String queryname, boolean debug) throws Exception{
       // System.out.println("Etsitään kysely " + queryname);
        File f = new File("sql/kyselyt.txt");
        if(!f.exists()){
            System.err.println("Tiedostoa kyselyt.txt ei löydy");
            throw new Exception("Virhe tiedostonluvussa");
        }
        try{
            BufferedReader cin = new BufferedReader(new FileReader(f));
            if(scrollPastLine(cin, queryname, debug)){
                return readQueryString(cin, queryname, debug);
            }
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
        
        throw new Exception("Virhe tiedostonluvussa");
    }
    
    /**
     * Palauttaa uuden lintuhavaintolistan,
     * jossa on vain kunkin lajin ensimmainen havainto
     * Saman lajin duplikaatit ignoorataan
     * @param birdWatchArray
     * @return Lintuhavaintolista, jossa ei esiiny samaa lajia kahteen kertaan
     */
    public static ArrayList<Lintuhavainto> birdWatchArrayToDistinct(ArrayList<Lintuhavainto> birdWatchArray) {
        ArrayList<Lintuhavainto> distinct=new ArrayList<>();
        ArrayList<Integer> birdIdArray=new ArrayList<>();
        boolean olluJo;
        for(int i=0;i<birdWatchArray.size();i++){
            Lintuhavainto birdWatch=birdWatchArray.get(i);
            olluJo=false;
            for(int id:birdIdArray){
                if(birdWatch.getLintuId()==id){
                    olluJo=true;
                    break;
                }
            }
            if(!olluJo){
                distinct.add(birdWatch);
                birdIdArray.add(birdWatch.getLintuId());
            }
        }
        return distinct;
    }

    private static boolean scrollPastLine(BufferedReader cin, String line, boolean debug){
        try{
            int rivi = 0;
            String s = null;
            while(true){
                s = cin.readLine();
                rivi++;
                if(debug) System.out.println(rivi + " " + s);
//                System.out.println(s);
                if(s == null){
                    System.err.println("Kyselyä " + line + " ei löydy tiedostosta!");
                    return false;
                }
                if(s.length() > 1){
                    if(s.substring(1).equals(line) && s.charAt(0) == '>'){
                        if(debug)
                            System.out.println(line + " rivi " + rivi);
                        return true;
                    }
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }
    
    private static String readQueryString(BufferedReader cin, String query, boolean debug){
        StringBuilder result = new StringBuilder("");
        try{
            String s = null;
            while((s = cin.readLine()) != null){
                s = s.trim();
                if(s.isEmpty() || s.length() <= 1) break;
                if(s.charAt(0) != '#'){
                    result.append(" " + s);
                }   
            }
            if(result == null){
                System.err.println("Virhe kyselyä " + query + " luettaessa");
                return "";
            }
//            System.out.println(result.toString());
            if(!(result.length() > 1)){
                System.err.println("Virhe kyselyä " + query + " lukiessa, pituus: " + result.length());
                return "";
            }
            return result.substring(1).toString();
        } catch(IOException e){
            e.printStackTrace();
        }
        System.err.println("SQL-kyselyä ei voitu lukea");
        return "";
    }
            
}

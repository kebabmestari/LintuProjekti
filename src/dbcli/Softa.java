package dbcli;

import databaseconnection.DB_connection;
import databaseobjects.Kala;
import databaseobjects.Kalahavainto;
import databaseobjects.Kayttaja;
import databaseobjects.Lintu;
import databaseobjects.Lintuhavainto;
import databaseobjects.Paivamaara;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Hallinnointi CLI-softa
 * @author Samuel
 */
public class Softa {
    
    private static DB_connection dbConn = null;
    
    private static final String defHost = "86.50.101.164";
    private static final String defDB = "tk2";
    private static final String defUser = "user";
    private static final String defPwd = "salasana";
    
    private static BufferedReader inputReader;
    private static Scanner inScanner;
    
    private static final String appName = "Tietokannan hallinnointisofta";
    private static final String welcomeString =
            "-------------------\n" +
            ""+ appName + "\n" +
            "Versio 1.0";
    
    private static String [][] valikko = {
                                        {"Muokkaa käyttäjiä", "Lisää käyttäjä", "Poista käyttäjä"},
                                        {"Muokkaa havaintoja", "Lisää havainto", "Poista havainto", "Hae havainto"},
                                        {"Muokkaa lajeja", "Lisää laji", "Poista laji", "Hae laji"},
                                        {"Yleinen SQL-kysely"},
                                        {"Hae tiedokannan tiedot"}};
    
    private static int valikkosyvyys = 0;
    private static int valinta = 0;
            
    public static void main(String [] args){
        inputReader = new BufferedReader(new InputStreamReader(System.in));
        inScanner = new Scanner(System.in);
        dbConn = promptConnection();
        if(dbConn == null || !dbConn.isConnected()){
            virhe("Ei saatu yhteyttä tietokantaan");
        }
        tulostaRivi(welcomeString);
        menu();
        return;
    }
    
    public static void menu(){
        while(true){
            tulostaVali("-");
            tulostaValinnat();
            tulostaVali("-");
            int foo = 0;
            foo = inScanner.nextInt();
            if(valikkosyvyys == 0){
                if(foo > 0 && foo <= valikko.length){
                    switch(foo){
                        case 1:
                        case 2:
                        case 3:
                            valikkosyvyys = 1;
                            valinta = foo;
                            continue;
                        case 4:
                            toteutaYleinenKysely();
                            continue;
                        case 5:
                            dbConn.printInfo();
                            continue;
                        default:
                            tulostaRivi("Virheellinen input");
                    }
                }
            } else {
                //Paluu
                if(foo == 0){
                    valikkosyvyys = 0;
                    continue;
                } else if(valinta == 1){ //Käyttäjät
                    switch(foo){
                        case 1:
                            lisaaKayttaja();
                            continue;
                        case 2:
                            poistaKayttaja();
                            continue;
                    }
                    continue;
                } else if(valinta == 2){ //Havainnot
                    switch(foo){
                        case 1:
                            lisaaHavainto();
                            continue;
                        case 2:
//                            poistaHavainto();
                            continue;
                        case 3:
//                            haeHavainto();
                    }
                    continue;
                } else if(valinta == 3){ //Lajit
                    switch(foo){
                        case 1:
                            lisaaLaji();
                            continue;
                        case 2:
                            poistaLaji();
                            continue;
                        case 3:
                            haeLaji();
                    }
                    continue;
                } else {
                    tulostaRivi("Virheellinen input");
                    continue;
                }
            }
        }
    }
    
    private static String getMySQLTime(){
        java.util.Date dt = new java.util.Date();

        java.text.SimpleDateFormat sdf = 
             new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sdf.format(dt);        
    }
    
    private static Paivamaara getMySQLPvm(){
        
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
    
    private static void lisaaHavainto(){
        tulostaRivi("Lisää (k)ala- vai (l)intuhavainto");
        while(true){
            String valinta = getInput();
            if(valinta.toLowerCase().equals("k")){
                String lajinimi = getInput("Lajin nimi");
                int lajiId = 0;
                try{
                    ArrayList<Kala> kl = dbConn.searchFish(lajinimi);
                    if(kl.size() > 1 || kl.size() == 0){
                        throw new SQLException("Ei yksiselitteistä lajia");
                    }
                    lajiId = kl.get(0).getId();
                } catch(SQLException e){
                    tulosta("Virhe lajia etsittäessä", true, false);
                    return;
                }
                dbConn.insertFishCatch(
                        new Kalahavainto(getInput("Havaintopaikka"),
                                Integer.parseInt(getInput("Pituus")),
                                lajiId,
                                dbConn.getUserID(getInput("Anna havaitsijan kayttajanimi")),
                                getMySQLPvm())
                        );
                break;
            }
            if(valinta.toLowerCase().equals("l")) {
                String lajinimi = getInput("Lajin nimi");
                int lajiId = 0;
                try{
                    ArrayList<Lintu> kl = dbConn.searchBird(lajinimi);
                    if(kl.size() > 1 || kl.size() == 0){
                        throw new SQLException("Ei yksiselitteistä lajia");
                    }
                    lajiId = kl.get(0).getId();
                } catch(SQLException e){
                    tulosta("Virhe lajia etsittäessä", true, false);
                    return;
                }
                dbConn.insertBirdWatch(
                        new Lintuhavainto(
                                lajiId,
                                getInput("Havaintopaikka"),
                                dbConn.getUserID(getInput("Anna havaitsijan kayttajanimi")),
                                getMySQLPvm())
                        );
                break;
            }
        }
        tulostaRivi("Lisays onnistui");
    }
    
    private static void lisaaLaji(){
        tulostaRivi("Lisää (k)ala vai (l)intu");
        while(true){
            String valinta = getInput();
            if(valinta.toLowerCase().equals("k")){
                dbConn.insertFish(new Kala(getInput("Anna kalan nimi")));
                break;
            }
            if(valinta.toLowerCase().equals("l")) {
                dbConn.insertBird(new Lintu(getInput("Anna linnun nimi")));
                break;
            }
        }
    }
    private static void haeLaji(){
        tulostaRivi("Hae (k)ala vai (l)intu");
        String nimitulos = "";
        int yleisyys = 0, id = 0;
        String valinta = "";
        while(true){
            valinta = getInput();
            if(valinta.toLowerCase().equals("k")){
                ArrayList<Kala> tulos = dbConn.searchFish(getInput("Nimi tai nimen alkuosa"));
                nimitulos = tulos.get(0).getNimi();
                id = tulos.get(0).getId();
                break;
            }
            if(valinta.toLowerCase().equals("l")) {
                ArrayList<Lintu> tulos = dbConn.searchBird(getInput("Nimi tai nimen alkuosa"));
                nimitulos = tulos.get(0).getNimi();
                id = tulos.get(0).getId();
                yleisyys = tulos.get(0).getYleisyys();
                break;
            }
        }
        tulosta("Nimi: " + nimitulos + " Id: " + id, false, true);
        if(valinta == "l")
            tulosta(" Yleisyys: " + yleisyys, false, false);
        tulosta("\r\n", false, true);
        String valittu = valinta.equals("l")?"lintuhavainto":"kalahavainto";
        String valittu2 = valinta.equals("l")?"lintuid":"kalaid";
        String sql = "SELECT * FROM " + valittu + " WHERE " + valittu2 + "=" + id + " ORDER BY paivamaara ASC LIMIT 5";
        ResultSet rs = dbConn.commitGeneralQuery(sql);
        if(rs == null) return;
        try{
            while(rs.next()){
                tulostaRivi("\t" + rs.getString("paivamaara") + " " + rs.getString("havaitsija"));
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    private static void poistaLaji(){
        String lajiNimi = getInput("Anna poistettavan lajin nimi");
        tulostaRivi("Laji " + lajiNimi + " poistetaan. Oletko varma?");
        String confirm = null;
        while(true){
            confirm = getInput();
            if(confirm.toLowerCase().equals("n")) return;
            if(confirm.toLowerCase().equals("y")) {
                break;
            }
        }
        dbConn.removeSpecies(lajiNimi);
    }
    
    private static void lisaaKayttaja(){
        Kayttaja newUser = new Kayttaja(getInput("Kayttajanimi"), getInput("Salasana"));
        dbConn.insertUser(newUser);
        tulostaRivi("Käyttäjä lisätty onnistuneesti");
    }
    
    public static void poistaKayttaja(){
        String kayttajaNimi = getInput("Anna kayttajan nimi");
        tulostaRivi("Kayttaja " + kayttajaNimi + " poistetaan. Oletko varma?");
        String confirm = null;
        while(true){
            confirm = getInput();
            if(confirm.toLowerCase().equals("n")) return;
            if(confirm.toLowerCase().equals("y")) {
                break;
            }
        }
        dbConn.removeUser(kayttajaNimi);
    }
    
    public static void toteutaYleinenKysely(){
        tulostaRivi("Syötä SQL-kysely");
        tulostaRivi("Jätä tyhjäksi käyttääksesi clipboardin sisältöä");
        
        String sqlQuery = getInput();
        if(sqlQuery == "" || sqlQuery == null){
            try{
                sqlQuery = (String) Toolkit.getDefaultToolkit()
                            .getSystemClipboard().getData(DataFlavor.stringFlavor); 
            } catch(UnsupportedFlavorException | IOException e){
                e.printStackTrace();
                virhe("Virhe, leikepöydän sisältöä ei saatu haettua");
            }
        }
        
        ResultSet rs = dbConn.commitGeneralQuery(sqlQuery);
        if(rs == null) return;
        try{
            if(!rs.next()){
                tulostaRivi("Kysely tehty, ei palautusjoukkoa.");
                return;
            }
            ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
            int columns = rsmd.getColumnCount();
            tulostaVali("<");
            do{
                for(int i = 1; i <= columns; i++){
                    System.out.print(rsmd.getColumnName(i) + ": " + rs.getString(i));
                    System.out.print(" # ");
                }
                System.out.print("\n");
            } while(rs.next());
            tulostaVali(">");
            if(promptYN("Tallennetaanko tiedostoon?")){
                String tiednimi = getInput("Tiedoston nimi");
                File fil = new File(tiednimi);
                if(!fil.exists()){
                    try {
                        fil.createNewFile();
                    } catch (IOException ex) {
                        Logger.getLogger(Softa.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                try {
                    rs.first();
                    FileWriter fout = new FileWriter(fil);
                    do{
                        for(int i = 1; i <= columns; i++){
                            fout.write(rsmd.getColumnName(i) + ": " + rs.getString(i));
                            fout.write(" # ");
                        }
                        fout.write("\r\n");
                    } while(rs.next());
                    fout.flush();
                    fout.close();
                } catch (IOException ex) {
                    Logger.getLogger(Softa.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        
    }
    
    public static boolean promptYN(String str){
        tulostaRivi(str);
        Scanner cin = new Scanner(System.in);
        while(true){
            String in = cin.next().toLowerCase();
            if(in.equals("y")){
                return true;
            } else if(in.equals("n")){
                return false;
            }
        }
    }
    
    public static void tulostaVali(String mark){
        for(int i = 0; i < (appName.length() + 2) / mark.length(); i++)
            System.out.print(mark);
        System.out.print("\n");
    }
    
    public static void tulostaValinnat(){
        if(valikkosyvyys == 0){
            for(int i = 0; i < valikko.length; i++){
                tulostaRivi(i+1 + " " + valikko[i][0]);
            }
        } else{
            for(int i = 1; i < valikko[valinta-1].length; i++){
                tulostaRivi(i + " " + valikko[valinta-1][i]);
            }
        }
    }
    
    /**
     * Form a database connection with inserted values
     * @return A DB_Connection object which encloses all database methods
     */
    public static DB_connection promptConnection(){
        
        String host = "", db = "", user = "", pasw = "";
        
        tulostaRivi("Anna tietokantaserverin host");
        tulostaRivi("Jätä tyhjäksi käyttääksesi oletusserveriä");
        host = getInput();
        
        tulostaRivi("Anna käytettävä tietokanta");
        tulostaRivi("Jätä tyhjäksi käyttääksesi oletustietokantaa");
        db = getInput();
        
        tulostaRivi("Anna käyttäjä");
        user = getInput();
        
        tulostaRivi("Anna salasana");

        pasw = getInput();
        
        //Defaultit
        if(host.length() == 0)
            host = defHost;
        if(db.length() == 0)
            db = defDB;
        if(user.length() == 0)
            user = defUser;
        if(pasw.length() == 0)
            pasw = defPwd;
        
        //Create new connection handler

        return new DB_connection(host, db, user, pasw);
        
    }
    
    public static String getInput(String str){
        tulostaRivi(str);
        return getInput();
    }
    
    /**
     * Prompts user a string from standard input
     * @return input string from console
     */
    public static String getInput(){
        String input = null;
        try{
            input = inputReader.readLine().trim();
        } catch(IOException e){
            tulostaRivi("Error with input");
            e.printStackTrace();
        }
        return input;
    }
    
    public static void tulosta(String str, boolean error, boolean append){
        if(append)
            System.out.print(str);
        else if(!error)
            System.out.println(str);
        else
            System.err.println(str);
    }
    
    public static void tulosta(String str){
        tulosta(str, false, true);
    }
    
    public static void tulostaRivi(String str){
        tulosta(str, false, false);
    }
    
    public static void virhe(String str){
        tulosta(str, true, false);
    }
}

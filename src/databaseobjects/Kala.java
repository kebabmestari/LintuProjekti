package databaseobjects;

import databaseconnection.DB_connection;

public class Kala implements Insertable, Json {
    /**
     * Kalan id
     */
    private int id;
    /**
     * Kalan lajinimi
     */
    private String nimi;

    /**
     * Konstruktori, jossa kalan id tunnetaan
     * Kun luodaan olio tietokannasta
     * @param id
     * @param nimi 
     */
    public Kala(int id, String nimi) {
        this.id = id;
        this.nimi = nimi;
    }
    
    /**
     * Konstruktori, jossa vain kalan nimi tunnetaan
     * @param nimi 
     */
    public Kala(String nimi) {
        this.nimi = nimi;
    }
    
    /**
     * Palauttaa kalan nimen
     * @return kalan nimi
     */
    @Override
    public String getNimi() {
        return nimi;
    }
    
    /**
     * Asettaa kalalle uuden nimen
     * @param nimi 
     */
    public void setNimi(String nimi) {
        this.nimi = nimi;
    }
    
    /**
     * Palauttaa kalan id:n
     * @return id
     */
    public int getId() {
        return id;
    }
    
    /**
     * Palauttaa kalan nimee vastaavan id:n tietokantahaun perusteella
     * Asettaa id:n olion id:ksi
     * @param connection
     * @return id
     */
    public int getId(DB_connection connection){
        this.id=connection.searchFishId(nimi);
        return this.id;
    }
    
    /**
     * Asettaa kalalle id:n
     * @param id 
     */
    public void setId(int id){
        this.id=id;
    }

    /**
     * Palauttaa olion tiedot SQL:n vaatimassa muodossa
     * @return muotoa ('nimi')
     */
    @Override
    public String toInsertableString() {
        return "('" + nimi + "')";
    }

    /**
     * Palauttaa taulun nimen ja attribuuttilistan
     * @return kala(nimi)
     */
    @Override
    public String toInsertHeader() {
        return "kala(nimi)";
    }

    /**
     * Palauttaa taulun nimen
     * @return kala
     */
    @Override
    public String getTableName() {
        return "kala";
    }
    
    /**
     * Palauttaa olion tiedot JSON-formaatissa
     * @param connection
     * @return muodossa {"id":"id","nimi":"nimi"}
     */
    @Override
    public String toJSON(DB_connection connection) {
        return "{\"id\":\""+id+"\",\"nimi\":\""+nimi+"\"}";
    }
}

package databaseobjects;

import databaseconnection.DB_connection;

public class Lintu implements Insertable, Json {

    /**
     * Linnun id
     */
    private int id;
    /**
     * Linnun lajinimi
     */
    private String nimi;
    /**
     * Linnun yleisyys, mita suurempi, sen yleisempi
     */
    private int yleisyys;

    /**
     * Luo linnun esim. tietokannasta
     * @param id
     * @param nimi
     * @param yleisyys 
     */
    public Lintu(int id, String nimi, int yleisyys) {
        this.id = id;
        this.nimi = nimi;
        this.yleisyys = yleisyys;
    }

    /**
     * Luo linnun nimen perusteella
     * Yleisyyden oletusarvo 1
     * @param linnunNimi 
     */
    public Lintu(String linnunNimi) {
        nimi = linnunNimi;
        yleisyys = 1;
    }

    /**
     * Palauttaa linnun nimen
     * @return lajinimi
     */
    @Override
    public String getNimi() {
        return nimi;
    }

    /**
     * Asettaa linnulle uuden nimen
     * @param nimi 
     */
    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    /**
     * Palauttaa linnun yleisyyden
     * @return yleisyys
     */
    public int getYleisyys() {
        return yleisyys;
    }

    /**
     * Asettaa linnulle uuden yleisyyden
     * @param yleisyys 
     */
    public void setYleisyys(int yleisyys) {
        this.yleisyys = yleisyys;
    }

    /**
     * Palauttaa linnun id:n
     * @return id
     */
    public int getId() {
        return id;
    }
    
    /**
     * Palauttaa linnun nimee vastaavan id:n tietokantahaun perusteella
     * Asettaa id:n olion id:ksi
     * @param connection
     * @return id
     */
    public int getId(DB_connection connection){
        this.id=connection.searchBirdId(nimi);
        return this.id;
    }

    /**
     * Palauttaa olion tiedot SQL-muodossa
     * @return muodossa ('nimi','yleisyys')
     */
    @Override
    public String toInsertableString() {
        return "('" + nimi + "','" + yleisyys + "')";
    }

    /**
     * Palauttaa taulun nimen ja attribuuttilistan
     * @return lintu(nimi,yleisyys)
     */
    @Override
    public String toInsertHeader() {
        return "lintu(nimi,yleisyys)";
    }

    /**
     * Palauttaa taulun nimen
     * @return lintu
     */
    @Override
    public String getTableName() {
        return "lintu";
    }

    /**
     * Palauttaa olion tiedot JSON-formaatissa
     * @param connection
     * @return muodossa {"id":"id","nimi":"nimi","yleisyys":"yleisyys"}
     */
    @Override
    public String toJSON(DB_connection connection) {
        return "{\"id\":\""+id+"\",\"nimi\":\""+nimi+"\",\"yleisyys\":\""+yleisyys+"\"}";
    }
}

package databaseobjects;

import databaseconnection.DB_connection;

public class Kunta implements Insertable, Json {

    /**
     * Kunnan nimi
     */
    private String nimi;

    /**
     * Luo Kunnan annetulla nimellä
     * Muuttaa nimen alkukirjaimen isoksi ja loput pieneksi
     * @param nimi 
     */
    public Kunta(String nimi) {
        nimi = nimi.trim();
        this.nimi = nimi.substring(0, 1).toUpperCase() + nimi.substring(1).toLowerCase();
    }

    /**
     * Palauttaa kunnan nimen
     * @return kunnanNimi
     */
    @Override
    public String getNimi() {
        return nimi;
    }

    /**
     * Palauttaa olion tiedot SQL-muodossa
     * @return muotoa ('kunnanNimi')
     */
    @Override
    public String toInsertableString() {
        return "('" + nimi + "')";
    }

    /**
     * Palauttaa taulun ja attribuuttien nimet
     * @return kunta(nimi)
     */
    @Override
    public String toInsertHeader() {
        return "kunta(nimi)";
    }

    /**
     * Palauttaa taulun nimen
     * @return kunta
     */
    @Override
    public String getTableName() {
        return "kunta";
    }

    /**
     * Palauttaa kunnan nimen merkkijonona
     * @return kunnanNimi
     */
    @Override
    public String toString() {
        return nimi;
    }

    /**
     * Palauttaa olion tiedot JSON-formaatissa
     * @param connection
     * @return muotoa {"nimi":"kunnanNimi"}
     */
    @Override
    public String toJSON(DB_connection connection) {
        return "{\"nimi\":\""+nimi+ "\"}";
    }
}

package databaseobjects;

import databaseconnection.DB_connection;

public class Kuva implements Json{
    /**
     * Kuvan osoite
     */
    private String filename;
    /**
     * Linnun id
     */
    private int lintuid;
    /**
     * Kalan id
     */
    private int kalaid;

    /**
     * Luo kuvaolion.
     * Ei suositella, koska ei samaan aikaan linu- ja kalaid:ta
     * @param filename
     * @param lintuid
     * @param kalaid
     */
    @Deprecated
    public Kuva(String filename, int lintuid, int kalaid) {
        this.filename = filename;
        this.lintuid = lintuid;
        this.kalaid = kalaid;
    }

    /**
     * Luo kuvaolion, joka viittaa annettuun osoitteeeseen ja lintuun
     * @param filename
     * @param lintu
     * @param connection 
     */
    public Kuva(String filename, Lintu lintu, DB_connection connection) {
        this.filename = filename;
        this.lintuid = lintu.getId(connection);
    }

    /**
     * Luo kuvaolion, joka viittaa annettuun osoitteeeseen ja kalaan
     * @param filename
     * @param kala
     * @param connection 
     */
    public Kuva(String filename, Kala kala, DB_connection connection) {
        this.filename = filename;
        this.lintuid = kala.getId(connection);
    }
    
    /**
     * Palauttaa kuvan osoittaan
     * @return filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Palauttaa linnun id:n
     * @return lintuid
     */
    public int getLintuid() {
        return lintuid;
    }

    /**
     * Palauttaa kalan id:n
     * @return kalaid
     */
    public int getKalaid() {
        return kalaid;
    }

    /**
     * Asettaa uuden osoitteen
     * @param filename 
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Asettaa uuden linnun id:n
     * @param lintuid 
     */
    public void setLintuid(int lintuid) {
        this.lintuid = lintuid;
    }

    /**
     * Asettaa uuden kalan id:n
     * @param kalaid 
     */
    public void setKalaid(int kalaid) {
        this.kalaid = kalaid;
    }

    /**
     * Palauttaa kuvaolion JSON-formaatissa
     * @param connection
     * @return muotoa {"filename":"url","lintuidTaiKalaid":"id"}
     */
    @Override
    public String toJSON(DB_connection connection) {
        if(lintuid==0){
            return "{\"filename\":\""+filename+"\",\"kalaid\":\""+kalaid+"\"}";
        }else{
            return "{\"filename\":\""+filename+"\",\"kalaid\":\""+lintuid+"\"}";
        }
    }

}

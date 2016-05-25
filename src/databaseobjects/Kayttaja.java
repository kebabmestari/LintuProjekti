package databaseobjects;

public class Kayttaja implements Insertable{
        /**
         * Kayttajan id
         * Id autogeneroidaan rekisteroityessa
         */
	private int id;
        /**
         * Kayttajan antama nimi
         */
	private String nimi;
        /**
         * Kayttajan antama salasana
         */
	private String salasana;
	
        /**
         * Konstruktori, jolla vodaan luoda Kayttaja suoraan tietokannasta
         * @param id
         * @param nimi
         * @param salasana 
         */
	public Kayttaja(int id, String nimi, String salasana) {
		this.id = id;
		this.nimi = nimi;
		this.salasana = kryptaa(salasana);
	}
        /**
         * Salasanan voisi kryptata luonnin yhteydessa
         * Silloin tietokannasta ei nakisi salasanoja
         * @param kryptattava
         * @return kryptattu
         */
	private String kryptaa(String kryptattava){
		//TODO toteuta kryptaus ja muista pakettien salaus my#s verkossa
		return kryptattava;
	}
        /**
         * Konstruktori, jota kaytetaan eniten
         * Kirjautumisen ja rekisteroitymisen yhteydessa, kun kayttaja ei tieda id:ta
         * @param nimi, kayttajanini
         * @param salasana 
         */
	public Kayttaja(String nimi, String salasana) {
		this.nimi = nimi;
		this.salasana = salasana;
	}
        /**
         * Paluttaa Kayttajan id:n
         * @return 
         */
	public int getId() {
		return id;
	}
	
        /**
         * Asettaa Kayttajalle id:n
         * @param id 
         */
	public void setId(int id){
		this.id=id;
	}
	
        /**
         * Palauttaa Kayttajan salasanan
         * @return salasana
         */
	public String getSalasana() {
		return salasana;
	}
	/**
	 * Palauttaa Kayttajan nimen
	 * @return
	 */
	@Override
	public String getNimi() {
		return nimi;
	}
        
        /**
         * Palauttaa olion tiedot SQL:n VALUES-muodossa
         * Lisattaessa Kayttajaa tietokantaan
         * @return muodossa ('nimi','salasana')
         */
	@Override
	public String toInsertableString() {
		return "('"+nimi+"','"+salasana+"')";
	}
        
        /**
         * Palauttaa taulun nimen ja attribuutit,
         * joita aiotaan lisata tietokantaan
         * @return kayttaja(nimi,salasana)
         */
	@Override
	public String toInsertHeader() {
		return "kayttaja(nimi,salasana)";
	}
        
        /**
         * Palauttaa taulun nimen
         * @return kayttaja
         */
	@Override
	public String getTableName() {
		return "kayttaja";
	}
	
}

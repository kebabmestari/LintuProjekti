package databaseobjects;

public class Kayttaja {
	private int id;
	private String nimi;
	private String salasana;
	
	public Kayttaja(int id, String nimi, String salasana) {
		this.id = id;
		this.nimi = nimi;
		this.salasana = kryptaa(salasana);
	}
	private String kryptaa(String kryptattava){
		//TODO toteuta kryptaus ja muista pakettien salaus myös verkossa
		return kryptattava;
	}
	public Kayttaja(String nimi, String salasana) {
		this.nimi = nimi;
		this.salasana = salasana;
	}
	public int getId() {
		return id;
	}
	public String getNimi() {
		return nimi;
	}
	public String getSalasana() {
		return salasana;
	}
	
}

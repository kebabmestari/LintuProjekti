package databaseobjects;

public class Kayttaja implements Insertable{
	private int id;
	private String nimi;
	private String salasana;
	
	public Kayttaja(int id, String nimi, String salasana) {
		this.id = id;
		this.nimi = nimi;
		this.salasana = kryptaa(salasana);
	}
	private String kryptaa(String kryptattava){
		//TODO toteuta kryptaus ja muista pakettien salaus my#s verkossa
		return kryptattava;
	}
	public Kayttaja(String nimi, String salasana) {
		this.nimi = nimi;
		this.salasana = salasana;
	}
	public int getId() {
		return id;
	}
	
	public void setId(int id){
		this.id=id;
	}
	
	public String getSalasana() {
		return salasana;
	}
	/**
	 * 
	 * @return
	 */
	@Override
	public String getNimi() {
		return nimi;
	}
	@Override
	public String toInsertableString() {
		return "('"+nimi+"','"+salasana+"')";
	}
	@Override
	public String toInsertHeader() {
		return "kayttaja(nimi,salasana)";
	}
	@Override
	public String getTableName() {
		return "kayttaja";
	}
	
}

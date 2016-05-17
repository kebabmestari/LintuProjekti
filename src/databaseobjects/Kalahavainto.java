package databaseobjects;

import databaseconnection.DB_connection;

public class Kalahavainto implements Havainto, Json {
	private int id;
	private Kunta paikka;
	private int pituus;
	private int kalaid;
	private int havaitsija;
	private Paivamaara pvm;
	
	public Kalahavainto(String paikka, int pituus, String kala, int havaitsija, Paivamaara pvm, DB_connection conection){
		this.paikka=new Kunta(paikka);
		this.pituus=pituus;
		this.kalaid=conection.searchFishId(kala);
		this.havaitsija=havaitsija;
		this.pvm=pvm;
	}
	
	public Kalahavainto(int id, String paikka, int pituus, int kalaid, int havaitsija, Paivamaara pvm) {
		this.id = id;
		this.paikka = new Kunta(paikka);
		this.pituus = pituus;
		this.kalaid = kalaid;
		this.havaitsija = havaitsija;
		this.pvm=pvm;
	}

	public Kalahavainto(String paikka, int pituus, int kalaid, int havaitsija, Paivamaara pvm) {
		this.paikka = new Kunta(paikka);
		this.pituus = pituus;
		this.kalaid = kalaid;
		this.havaitsija = havaitsija;
		this.pvm=pvm;
	}

	public String getPaikka() {
		return paikka.toString();
	}

	public void setPaikka(String paikka) {
		this.paikka = new Kunta(paikka);
	}

	public int getPituus() {
		return pituus;
	}

	public void setPituus(int pituus) {
		this.pituus = pituus;
	}

	public int getKalaid() {
		return kalaid;
	}

	public void setKalaid(int kalaid) {
		this.kalaid = kalaid;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id){
		this.id=id;
	}

	public int getHavaitsija() {
		return havaitsija;
	}

	public Paivamaara getPvm() {
		return pvm;
	}

	public void setPvm(Paivamaara pvm) {
		this.pvm = pvm;
	}

	@Override
	public String toInsertableString() {
		return "('"+paikka.toString()+"','"+pituus+"','"+kalaid+"','"+pvm.toString()+"','"+havaitsija+"')";
	}

	@Override
	public String toInsertHeader() {
		return "kalahavainto(paikka,pituus,kalaid,paivamaara,havaitsija)";
	}

	@Override
	public String getUniqueAttributesWithValues() {
		return "kalaid='"+kalaid+"' AND havaitsija='"+havaitsija+"' AND YEAR(paivamaara)='"+pvm.getVuosi()+"'";
	}

	@Override
	public String getTable() {
		return "kalahavainto";
	}

	@Override
	public String getAllUpdatableAttributesWithValues() {
		return "paikka='"+paikka.toString()+"', pituus='"+pituus+"', paivamaara='"+pvm+"'";
	}
	
	/**
	 * Palauttaa olion JSON-formaatissa,
	 * kalaid:n tilalla on kalan nimi, eik� havaitsijaa ole
	 * @param connection
	 * @return olio JSON-formaatissa
	 */
	@Override
	public String toJSON(DB_connection connection){
		if(pvm==null){
			System.out.println("Päivämäärä null");
			return "{}";
		}
		return "{\n"+
				"\"id\":"+"\""+id+"\",\n"+
				"\"kala\":"+"\""+connection.getFishNameById(kalaid)+"\",\n"+
				"\"pituus\":\""+pituus+"\",\n"+
				"\"paikka\":\""+paikka.toString()+"\",\n"+
				"\"paivamaara\":\""+pvm.toJSON()+"\"\n}";
	}

	
}

package databaseobjects;

import databaseconnection.DB_connection;

public class Kalahavainto implements Havainto {
	private int id;
	private String paikka;
	private int pituus;
	private int kalaid;
	private int havaitsija;
	private Paivamaara pvm;
	
	public Kalahavainto(String paikka, int pituus, String kala, int havaitsija, Paivamaara pvm, DB_connection con){
		this.paikka=paikka;
		this.pituus=pituus;
		this.kalaid=con.searchFishId(kala);
		this.havaitsija=havaitsija;
		this.pvm=pvm;
	}
	
	public Kalahavainto(int id, String paikka, int pituus, int kalaid, int havaitsija, Paivamaara pvm) {
		this.id = id;
		this.paikka = paikka;
		this.pituus = pituus;
		this.kalaid = kalaid;
		this.havaitsija = havaitsija;
	}

	public Kalahavainto(String paikka, int pituus, int kalaid, int havaitsija, Paivamaara pvm) {
		this.paikka = paikka;
		this.pituus = pituus;
		this.kalaid = kalaid;
		this.havaitsija = havaitsija;
	}

	public String getPaikka() {
		return paikka;
	}

	public void setPaikka(String paikka) {
		this.paikka = paikka;
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
		return "('"+paikka+"','"+pituus+"','"+kalaid+"','"+pvm.toString()+"','"+havaitsija+"')";
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
	public String getAllAttributesWithValues() {
		// TODO Auto-generated method stub
		return null;
	}
	
}

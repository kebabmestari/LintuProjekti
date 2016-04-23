package databaseobjects;

public class Kalahavainto {
	private int id;
	private String paikka;
	private int pituus;
	private int kalaid;
	private int havaitsija;
	private Paivamaara pvm;
	
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
	
}

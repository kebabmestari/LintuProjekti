package databaseobjects;

public class Lintuhavainto {
	private int lintu;
	private String paikka;
	private int id;
	private int havaitsija;
	
	public Lintuhavainto(int lintu, String paikka, int havaitsija) {
		this.lintu = lintu;
		this.paikka = paikka;
		this.havaitsija = havaitsija;
	}
	public Lintuhavainto(int lintu, String paikka, int id, int havaitsija) {
		this.lintu = lintu;
		this.paikka = paikka;
		this.id = id;
		this.havaitsija = havaitsija;
	}
	public int getLintu() {
		return lintu;
	}
	public String getPaikka() {
		return paikka;
	}
	public int getId() {
		return id;
	}
	public int getHavaitsija() {
		return havaitsija;
	}
}

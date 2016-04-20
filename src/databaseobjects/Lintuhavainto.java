package databaseobjects;

public class Lintuhavainto {
	private int lintu;
	private String paikka;
	private int id;
	private int havaitsija;
	private boolean eko;
	private boolean sponde;
	
	public Lintuhavainto(int lintu, String paikka, int havaitsija) {
		this.lintu = lintu;
		this.paikka = paikka;
		this.havaitsija = havaitsija;
		eko=sponde=false;
	}
	
	public Lintuhavainto(int lintu, String paikka, int id, int havaitsija) {
		this.lintu = lintu;
		this.paikka = paikka;
		this.id = id;
		this.havaitsija = havaitsija;
		eko=sponde=false;
	}
	
	public Lintuhavainto(int lintu, String paikka, int id, int havaitsija, boolean eko, boolean sponde) {
		this.lintu = lintu;
		this.paikka = paikka;
		this.id = id;
		this.havaitsija = havaitsija;
		this.eko = eko;
		this.sponde = sponde;
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
	public boolean isEko() {
		return eko;
	}
	public void setEko(boolean eko) {
		this.eko = eko;
	}
	public boolean isSponde() {
		return sponde;
	}
	public void setSponde(boolean sponde) {
		this.sponde = sponde;
	}
	public void setLintu(int lintu) {
		this.lintu = lintu;
	}
	public void setPaikka(String paikka) {
		this.paikka = paikka;
	}
	public String toInsertableString(){
		return "('"+lintu+"','"+paikka+"','"+havaitsija+"','"+eko+"','"+sponde+"')";
	}
}

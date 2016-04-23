package databaseobjects;

import databaseconnection.DB_connection;

public class Lintuhavainto implements Insertable{
	private int lintu;
	private String paikka;
	private int id;
	private int havaitsija;
	private Paivamaara pvm;
	private boolean eko;
	private boolean sponde;
	
	public Lintuhavainto(int lintu, String paikka, int havaitsija, Paivamaara pvm) {
		this.lintu = lintu;
		this.paikka = paikka;
		this.havaitsija = havaitsija;
		this.setPvm(pvm);
		eko=sponde=false;
	}
	
	public Lintuhavainto(int lintu, String paikka, int id, int havaitsija) {
		this.lintu = lintu;
		this.paikka = paikka;
		this.id = id;
		this.havaitsija = havaitsija;
		eko=sponde=false;
	}
	
	public Lintuhavainto(String lintu, String paikka, Paivamaara pvm, 
			int havaitsija, Boolean eko, Boolean sponde, DB_connection con){
		this.lintu=con.searchBirdId(lintu);
		this.paikka=paikka;
		this.setPvm(pvm);
		this.havaitsija=havaitsija;
		this.eko=eko;
		this.sponde=sponde;
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
	
	@Override
	public String toInsertableString(){
		return "('"+lintu+"','"+paikka+"','"+pvm.toString()+"','"+havaitsija+"','"+(eko?1:0)+"','"+(sponde?1:0)+"')";
	}

	@Override
	public String toInsertHeader() {
		return "lintuhavainto(lintu,paikka,paivamaara,havaitsija,eko,sponde)";
	}

	@Override
	public String getNimi() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	public Paivamaara getPvm() {
		return pvm;
	}

	public void setPvm(Paivamaara pvm) {
		this.pvm = pvm;
	}
}

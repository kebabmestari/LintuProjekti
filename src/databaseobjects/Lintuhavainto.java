package databaseobjects;

import databaseconnection.DB_connection;

public class Lintuhavainto implements Havainto, Json{
	private int lintuid;
	private String paikka; //TODO kunta
	private int id;
	private int havaitsija;
	private Paivamaara pvm;
	private boolean eko;
	private boolean sponde;
	
	public Lintuhavainto(int lintu, String paikka, int havaitsija, Paivamaara pvm) {
		this.lintuid = lintu;
		this.paikka = paikka;
		this.havaitsija = havaitsija;
		this.setPvm(pvm);
		eko=sponde=false;
	}
	
	public Lintuhavainto(int lintu, String paikka, int id, int havaitsija) {
		this.lintuid = lintu;
		this.paikka = paikka;
		this.id = id;
		this.havaitsija = havaitsija;
		eko=sponde=false;
	}
	
	public Lintuhavainto(String lintu, String paikka, Paivamaara pvm, 
			int havaitsija, Boolean eko, Boolean sponde, DB_connection con){
		this.lintuid=con.searchBirdId(lintu);
		this.paikka=paikka;
		this.setPvm(pvm);
		this.havaitsija=havaitsija;
		this.eko=eko;
		this.sponde=sponde;
	}
	
	public Lintuhavainto(int lintu, String paikka, int id, int havaitsija, boolean eko, boolean sponde) {
		this.lintuid = lintu;
		this.paikka = paikka;
		this.id = id;
		this.havaitsija = havaitsija;
		this.eko = eko;
		this.sponde = sponde;
	}

	public int getLintu() {
		return lintuid;
	}
	public String getPaikka() {
		return paikka;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
		this.lintuid = lintu;
	}
	public void setPaikka(String paikka) {
		this.paikka = paikka;
	}
	
	public Paivamaara getPvm() {
		return pvm;
	}

	public void setPvm(Paivamaara pvm) {
		this.pvm = pvm;
	}

	@Override
	public String toInsertableString(){
		return "('"+lintuid+"','"+paikka+"','"+pvm.toString()+"','"+havaitsija+"','"+(eko?1:0)+"','"+(sponde?1:0)+"')";
	}

	@Override
	public String toInsertHeader() {
		return "lintuhavainto(lintuid,paikka,paivamaara,havaitsija,eko,sponde)";
	}


	@Override
	public String getUniqueAttributesWithValues() {
		return "lintuid='"+lintuid+"' AND paivamaara='"+pvm.toString()+"' AND havaitsija='"+havaitsija+"'";
	}

	@Override
	public String getTable() {
		return "lintuhavainto";
	}

	@Override
	public String getAllUpdatableAttributesWithValues() {
		return "paikka='"+paikka+"', eko='"+(eko?1:0)+"', sponde='"+(sponde?1:0)+"'";
	}
	
	/**
	 * Palauttaa havainnoon loppukäyttäjän tarvimassa muodossa
	 * @return JSON
	 */
	@Override
	public String toJSON(DB_connection connection) {
		return "{\n"+
				"\"id\":\""+id+"\",\n"+
				"\"lintu\":\""+connection.getBirdNameById(lintuid)+"\",\n"+
				"\"paikka\":\""+paikka+"\",\n"+
				"\"paivamaara\":\""+pvm.toJSON()+"\",\n"+
				"\"eko\":\""+eko+"\",\n"+
				"\"sponde\":\""+sponde+"\"\n"+
				"}";
	}
}

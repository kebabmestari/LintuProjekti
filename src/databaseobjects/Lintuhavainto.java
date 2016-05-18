package databaseobjects;

import databaseconnection.DB_connection;

public class Lintuhavainto implements Havainto, Json{
	private int lintuid;
	private Kunta paikka; //TODO kunta
	private int id;
	private int havaitsija;
	private Paivamaara pvm;
	private boolean eko;
	private boolean sponde;
	
	public Lintuhavainto(int lintuid, String paikka, int havaitsija, Paivamaara pvm) {
		this.lintuid = lintuid;
		this.paikka = new Kunta(paikka);
		this.havaitsija = havaitsija;
		this.setPvm(pvm);
		eko=sponde=false;
	}
	
	public Lintuhavainto(int lintuid, String paikka, Paivamaara paivamaara, int id, int havaitsija) {
		this.lintuid = lintuid;
		this.paikka = new Kunta(paikka);
		this.pvm=paivamaara;
		this.id = id;
		this.havaitsija = havaitsija;
		eko=sponde=false;
	}
	
	public Lintuhavainto(String lintu, String paikka, Paivamaara pvm, 
			int havaitsija, Boolean eko, Boolean sponde, DB_connection con){
		this.lintuid=con.searchBirdId(lintu);
		this.paikka=new Kunta(paikka);
		this.setPvm(pvm);
		this.havaitsija=havaitsija;
		this.eko=eko;
		this.sponde=sponde;
	}
	
	public Lintuhavainto(int lintuid, String paikka, Paivamaara paivamaara, int id, int havaitsija, boolean eko, boolean sponde) {
		this.lintuid = lintuid;
		this.paikka = new Kunta(paikka);
		this.id = id;
		this.havaitsija = havaitsija;
		this.eko = eko;
		this.sponde = sponde;
		this.pvm=paivamaara;
	}

	public int getLintuId() {
		return lintuid;
	}
	
	public void setLintuId(int lintuid) {
		this.lintuid = lintuid;
	}

	public String getPaikka() {
		return paikka.getNimi();
	}
	
	public void setPaikka(String paikka) {
		this.paikka = new Kunta(paikka);
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public Paivamaara getPvm() {
		return pvm;
	}

	public void setPvm(Paivamaara pvm) {
		this.pvm = pvm;
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

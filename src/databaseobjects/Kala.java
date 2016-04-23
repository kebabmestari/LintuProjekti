package databaseobjects;

public class Kala implements Insertable{
	private int id;
	private String nimi;
	
	public Kala(int id, String nimi) {
		this.id = id;
		this.nimi = nimi;
	}
	
	public Kala(String nimi) {
		this.nimi = nimi;
	}

	@Override
	public String getNimi() {
		return nimi;
	}
	public void setNimi(String nimi) {
		this.nimi = nimi;
	}
	
	public int getId(){
		return id;
	}

	@Override
	public String toInsertableString() {
		return "('"+nimi+"')";
	}

	@Override
	public String toInsertHeader() {
		return "kala(nimi)";
	}

	@Override
	public String getTableName() {
		return "kala";
	}
}

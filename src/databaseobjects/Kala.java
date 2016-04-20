package databaseobjects;

public class Kala {
	private int id;
	private String nimi;
	
	public Kala(int id, String nimi) {
		this.id = id;
		this.nimi = nimi;
	}
	
	public Kala(String nimi) {
		this.nimi = nimi;
	}

	public String getNimi() {
		return nimi;
	}
	public void setNimi(String nimi) {
		this.nimi = nimi;
	}
	
	public int getId(){
		return id;
	}
}

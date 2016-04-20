package databaseobjects;

public class Kunta {
	private String nimi;
	
	public Kunta(String nimi) {
		this.nimi = nimi.trim();
	}

	public String getNimi() {
		return nimi;
	}
}

package databaseobjects;

public class Kunta implements Insertable{
	private String nimi;
	
	public Kunta(String nimi) {
		this.nimi = nimi.trim();
	}

	@Override
	public String getNimi() {
		return nimi;
	}

	@Override
	public String toInsertableString() {
		return "('"+nimi+"')";
	}

	@Override
	public String toInsertHeader() {
		return "kunta(nimi)";
	}

	@Override
	public String getTableName() {
		return "kunta";
	}
}

package databaseobjects;

public class Kunta implements Insertable{
	private String nimi;
	
	public Kunta(String nimi) {
		nimi=nimi.trim();
		this.nimi = nimi.substring(0,1).toUpperCase()+nimi.substring(1).toLowerCase();
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
	
	@Override
	public String toString(){
		return nimi;
	}
}

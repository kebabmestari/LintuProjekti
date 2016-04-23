package databaseobjects;

public class Lintu implements Insertable{
	private int id;
	private String nimi;
	private int yleisyys;
	
	public Lintu(int id, String nimi, int yleisyys) {
		this.id = id;
		this.nimi = nimi;
		this.yleisyys = yleisyys;
	}

	public Lintu(String linnunNimi){
		nimi=linnunNimi;
		yleisyys=1;
	}

	@Override
	public String getNimi() {
		return nimi;
	}

	public void setNimi(String nimi) {
		this.nimi = nimi;
	}

	public int getYleisyys() {
		return yleisyys;
	}

	public void setYleisyys(int yleisyys) {
		this.yleisyys = yleisyys;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toInsertableString() {
		return "('"+nimi+"','"+yleisyys+"')";
	}

	@Override
	public String toInsertHeader() {
		return "lintu(nimi,yleisyys)";
	}

	@Override
	public String getTableName() {
		return "lintu";
	}
}

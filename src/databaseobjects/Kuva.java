package databaseobjects;

public class Kuva {
	private String filename;
	private int lintuid;
	private int kalaid;
	
	public Kuva(String filename, int lintuid, int kalaid) {
		this.filename = filename;
		this.lintuid = lintuid;
		this.kalaid = kalaid;
	}

	public String getFilename() {
		return filename;
	}

	public int getLintuid() {
		return lintuid;
	}

	public int getKalaid() {
		return kalaid;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setLintuid(int lintuid) {
		this.lintuid = lintuid;
	}

	public void setKalaid(int kalaid) {
		this.kalaid = kalaid;
	}
	

}

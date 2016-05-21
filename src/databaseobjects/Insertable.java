package databaseobjects;

public interface Insertable {
	/**
	 * Palauttaa olion tiedot SQL:n vaatimassa muodossa
	 * @return muotoa ('arvo1','arvo2',...)
	 */
	public String toInsertableString();
	
	/**
	 * Palauttaa lis#tt#v#n taulun nimen ja attribuuttilistan
	 * @return muotoa taulu(attr1,attr2,...)
	 */
	public String toInsertHeader();
	
	/**
	 * Palauttaa olion nimen
	 * @return nimi
	 */
	public String getNimi();
	
	/**
	 * Palauttaa taulun nimen
	 * @return taulun nimi
	 */
	public String getTableName();

}

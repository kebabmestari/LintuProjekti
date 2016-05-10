package databaseobjects;

public interface Havainto {
	/**
	 * Palauttaa olion tiedot SQL:n vaatimassa muodossa
	 * @return muotoa ('arvo1','arvo2',...)
	 */
	public String toInsertableString();
	
	/**
	 * Palauttaa lisättävän taulun nimen ja attribuuttilistan
	 * @return muotoa taulu(attr1,attr2,...)
	 */
	public String toInsertHeader();
	
	/**
	 * Palauttaa ne attribuuttivertailut,
	 * jotka määräävät järkevät havainnot yksikäsitteisesti.
	 * @return muodossa "lintu='12' AND paivamaara='2012-04-20' AND havaitsija='1'"
	 */
	public String getUniqueAttributesWithValues();

	/**
	 * Palauttaa tietokannan taulun nimen
	 * @return taulun nimi
	 */
	public String getTable();

	/**
	 * Palauttaa päivitystä varten kaikki arrtibuutit ja arvot
	 * @return
	 */
	public String getAllAttributesWithValues();
}

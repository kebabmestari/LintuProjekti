package databaseobjects;

public interface Havainto {
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
	 * Palauttaa ne attribuuttivertailut,
	 * jotka m##r##v#t j#rkev#t havainnot yksik#sitteisesti.
	 * @return muodossa "lintu='12' AND paivamaara='2012-04-20' AND havaitsija='1'"
	 */
	public String getUniqueAttributesWithValues();

	/**
	 * Palauttaa tietokannan taulun nimen
	 * @return taulun nimi
	 */
	public String getTable();

	/**
	 * Palauttaa p#ivityst# varten kaikki arrtibuutit ja arvot
	 * @return
	 */
	public String getAllUpdatableAttributesWithValues();
}

package databaseobjects;

import databaseconnection.DB_connection;

public interface Json {
	
	/**
	 * Palauttaa olion JSON-formaatissa
	 * @param connection
	 * @return JSON
	 */
	public String toJSON(DB_connection connection);

}

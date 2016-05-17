package lib;

import java.util.ArrayList;

import databaseconnection.DB_connection;
import databaseobjects.Json;

public class Operations {
	public static String arrayToJSON(ArrayList<?> array, DB_connection connection) {
		String json;
		int pituus=array.size();
		if(pituus>0){
			json="[\n"+((Json)array.get(0)).toJSON(connection);
		}else{
			return "[]";
		}
		for(int i=1;i<pituus;i++){
			json += ",\n"+((Json)array.get(i)).toJSON(connection);
		}
		json+="\n]";
		return json;
	}

}

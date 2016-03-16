package databaseconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB_connection {
	private final String DB_URI;
	private final String user; 				//"root"
	private final String password;			//"mysli"
	
	private Connection con = null;
	private Statement stm=null;
	
	public DB_connection(String host, String db_name, String user, String password){
		DB_URI="jdbc:mysql://"+host.trim()+"/"+db_name.trim();
		this.user=user;
		this.password=password;
		if (!createConnection()){
			System.err.println("Yhteyttä ei voitu muodostaa");
			//TODO Mitä sitten?
		}
	}
	
	public boolean createConnection(){
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			
			con=DriverManager.getConnection(DB_URI, user, password);
			
			stm=con.createStatement();
			
			return true;
		}catch (ClassNotFoundException | SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public ResultSet searchBird(String wordBegin){
		String sql="SELECT nimi FROM lintu WHERE nimi LIKE '"+wordBegin+"%';";
		try {
			return stm.executeQuery(sql);
		} catch (SQLException e) {
			System.err.println("Kysely lajeista ei toiminut");
			e.printStackTrace();
			return null;
		}
	}
}

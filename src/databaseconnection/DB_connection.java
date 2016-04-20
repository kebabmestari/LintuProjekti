package databaseconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import databaseobjects.Lintu;

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
			System.err.println("Yhteytt� ei voitu muodostaa");
			//TODO Mit� sitten?
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
	
	/**
	 * Etsii linnuista vaihtoehdot nimen alun perusteella,
	 * palauttaa lajit yleisyysj�rjestyksess�
	 * @param wordBegin, linnun nimen alku
	 * @return lista sopivista linnuista, pelk�t nimet
	 */
	public ResultSet searchBird(String wordBegin){	
		return SQLOperations.searchBird(wordBegin, con);
	}
	
	/**
	 * Lintujen lis�ys, id autogeneroidaan
	 * Lis�t��n vain ne linnut, jotka puuttuivat tietokannasta
	 * @param birdArray on lista lis�tt�vist� linnuista
	 */
	public void addBird(ArrayList<Lintu> birdArray){
		SQLOperations.addBird(birdArray, stm, con);
	}
	
	/**
	 * Poista ehdottomasti, kun kaikki toimii!!!!!!!
	 * Vain testausta varten
	 * @return
	 */
	public Connection getConnection(){
		return con;
	}
}
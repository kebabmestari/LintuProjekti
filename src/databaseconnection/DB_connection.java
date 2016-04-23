package databaseconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import databaseobjects.Insertable;
import databaseobjects.Kala;
import databaseobjects.Kayttaja;
import databaseobjects.Kunta;
import databaseobjects.Lintu;

public class DB_connection {
	private final String DB_URI;
	private final String user; 				//"root"
	private final String password;			//"mysli"
	
	private Connection con = null;
	private PreparedStatement preparedBirdSearch=null;
	
	public DB_connection(String host, String db_name, String user, String password){
		DB_URI="jdbc:mysql://"+host.trim()+"/"+db_name.trim();
		
		this.user=user;
		this.password=password;
		
		if (!createConnection()){
			System.err.println("Yhteytt‰ ei voitu muodostaa");
			//TODO Mit‰ sitten?
		}
	}
	
	/**
	 * Poista ehdottomasti, kun kaikki toimii!!!!!!!
	 * Vain testausta varten
	 * @return
	 */
	public Connection getConnection(){
		//TODO poista kun kaikki toimii
		return con;
	}

	public boolean createConnection(){
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			
			con=DriverManager.getConnection(DB_URI, user, password);
			
			String prepareBirdSearchString="SELECT nimi FROM lintu WHERE nimi LIKE ? ORDER BY yleisyys;";
			
			preparedBirdSearch=con.prepareStatement(prepareBirdSearchString);
			
			return true;
			
		}catch (ClassNotFoundException | SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean disconnect(){
		try {
			con.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Etsii linnuista vaihtoehdot nimen alun perusteella,
	 * palauttaa lajit yleisyysj‰rjestyksess‰
	 * @param wordBegin, linnun nimen alku
	 * @return lista sopivista linnuista, pelk‰t nimet
	 */
	public ResultSet searchBird(String wordBegin){	
		return SQLOperations.searchBird(wordBegin, preparedBirdSearch);
	}
	
	/**
	 * Lintujen lis‰ys, id autogeneroidaan
	 * Lis‰t‰‰n vain ne linnut, jotka puuttuivat tietokannasta
	 * @param birdArray on lista lis‰tt‰vist‰ linnuista
	 */
	public void insertBird(ArrayList<Lintu> birdArray){
		SQLOperations.insertObject(convertToInsertable(birdArray), con);
	}
	
	/**
	 * Lis‰‰ parametrina annetun linnun,
	 * mik‰li sit‰ ei ole jo tietokannassa
	 * @param lintu, joka aiotaan lis‰t‰
	 */
	public void insertBird(Lintu bird){
		ArrayList<Insertable> birdArray = new ArrayList<>();
		birdArray.add(bird);
		SQLOperations.insertObject(birdArray, con);
	}
	
	/**
	 * Lis‰‰ parametrina annetun k‰ytt‰j‰n,
	 * mik‰li sit‰ ei ole jo tietokannassa
	 * @param user, joka aiotaan lis‰t‰
	 */
	public void insertUser(Kayttaja user){
		ArrayList<Insertable> userArray = new ArrayList<>();
		userArray.add(user);
		SQLOperations.insertObject(userArray, con);
	}
	
	/**
	 * Lis‰‰ parametrina annetun kunnan,
	 * mik‰li sit‰ ei viel‰ ole tietokannassa
	 * @param town
	 */
	public void insertTown(Kunta town) {
		ArrayList<Insertable> townArray=new ArrayList<>();
		townArray.add(town);
		SQLOperations.insertObject(townArray, con);
	}
	
	public void insertTown(ArrayList<Kunta> towns) {
		SQLOperations.insertObject(convertToInsertable(towns), con);
	}

	public void insertFish(Kala fish){
		ArrayList<Insertable> fishArray=new ArrayList<>();
		fishArray.add(fish);
		SQLOperations.insertObject(fishArray, con);
	}
	
	public void insertFish(ArrayList<Kala> fishArray) {
		SQLOperations.insertObject(convertToInsertable(fishArray), con);
	}
	
	/**
	 * Muuttaa annetun listan tyypin Insertable mukaisesti,
	 * mik‰li alkuper‰inen tyyppi toteuttaa rajapinnan.
	 * @param array
	 * @return insertable array
	 */
	public ArrayList<Insertable> convertToInsertable(ArrayList<?> array){
		ArrayList<Insertable> insertableArray=new ArrayList<>();
		try{
			for(int i=0;i<array.size();i++){
				insertableArray.add((Insertable)array.get(i));
			}
			return insertableArray;
		} catch (Exception e){
			return null;
		}
	}
}

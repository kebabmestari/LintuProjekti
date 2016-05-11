package databaseconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import databaseobjects.Insertable;
import databaseobjects.Kala;
import databaseobjects.Kalahavainto;
import databaseobjects.Kayttaja;
import databaseobjects.Kunta;
import databaseobjects.Lintu;
import databaseobjects.Lintuhavainto;

public class DB_connection {
	private final String DB_URI;
	private final String user; 				//"root"
	private final String password;			//"mysli"
	
	private Connection con = null;
	
	private PreparedStatement preparedBirdNameSearch=null;
	private PreparedStatement preparedBirdIdSearch=null;
	
	private PreparedStatement preparedFishNameSearch=null;
	private PreparedStatement preparedFishIdSearch=null;
	
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

	private boolean createConnection(){
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			
			con=DriverManager.getConnection(DB_URI, user, password);
			
			String prepareBirdSearchString="SELECT nimi FROM lintu WHERE nimi LIKE ? ORDER BY yleisyys;";
			preparedBirdNameSearch=con.prepareStatement(prepareBirdSearchString);
			
			prepareBirdSearchString="SELECT id FROM lintu WHERE nimi=? ;";
			preparedBirdIdSearch=con.prepareStatement(prepareBirdSearchString);
			
			String fishSearch="SELECT nimi FROM kala WHERE nimi LIKE ?;";
			preparedFishNameSearch=con.prepareStatement(fishSearch);
			
			fishSearch="SELECT id FROM kala WHERE nimi=? ;";
			preparedFishIdSearch=con.prepareStatement(fishSearch);
			
			return true;
			
		}catch (ClassNotFoundException | SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Sulkee tietokantayhteyden
	 * @return onnistuiko sulkeminen
	 */
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
		return SQLOperations.searchBird(wordBegin, preparedBirdNameSearch);
	}
	
	/**
	 * Etsii linnun koko nime‰ vastaavan id:n
	 * @param bird koko nimi
	 * @return id tai -5 jos ei lˆydy
	 */
	public int searchBirdId(String bird){
		return SQLOperations.searchBirdId(bird, preparedBirdIdSearch);
	}
	
	/**
	 * Etsii kalojen nimet, jotka alkavat annetulla merkkijonolla
	 * @param wordBegin
	 * @return
	 */
	public ResultSet searchFish(String wordBegin){	
		return SQLOperations.searchFish(wordBegin, preparedFishNameSearch);
	}
	
	/**
	 * Etsii kalan id:n
	 * Kun lis‰t‰‰n kalahavainto, k‰ytt‰j‰ kirjoittaaa
	 * kalan nimen, joten tarvitaan myˆs id.
	 * @param fish
	 * @return palauttaa kalan nime‰ vastaavan id:n
	 */
	public int searchFishId(String fish){
		return SQLOperations.searchBirdId(fish, preparedFishIdSearch);
	}
	
	/**
	 * Lis‰‰ parametrina annetun fongatun kalan tiedot tietokantaan.
	 * Jos sin‰ vuonna on jo kyseinen laji saatu, havainnot p‰ivitet‰‰n
	 * @param fishCatch Kalahavainto, joka halutaan lis‰t‰
	 */
	public void insertFishCatch(Kalahavainto fishCatch){
		SQLOperations.insertHavainto(fishCatch, con);
	}
	
	/**
	 * Lis‰‰ lintuhavainnon tietokantaan. Mik‰li oli n‰hty jo laji sin‰ p‰iv‰n‰,
	 * havainto vain p‰ivitet‰‰n uusilla tiedoilla.
	 */
	public void insertBirdWatch(Lintuhavainto birdWatch){
		SQLOperations.insertHavainto(birdWatch, con);
	}
	
	/**
	 * P‰ivitt‰‰ havainnon annetulla havainnolla
	 * @param birdWatch
	 */
	public void updateBirdWatch(Lintuhavainto birdWatch){
		int id=SQLOperations.havaintoIdIfAlreadyInTable(birdWatch, con);
		if(id>0){
			SQLOperations.updateHavainto(birdWatch, id, con);
		}else{
			//TODO ei voida p‰ivitt‰‰, koska ei ole alunperin havaintoa
		}
	}
	
	/**
	 * P‰ivitt‰‰ havainnon annetulla havainnolla
	 * @param fishCatch uudet tiedot
	 */
	public void updateFichCatch(Kalahavainto fishCatch){
		int id=SQLOperations.havaintoIdIfAlreadyInTable(fishCatch, con);
		if(id>0){
			SQLOperations.updateHavainto(fishCatch, id, con);
		}else{
			//TODO ei voida p‰ivitt‰‰, koska ei ole alunperin havaintoa
		}
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
	
	/**
	 * Lis‰‰ parametrina annetut kunnat,
	 * mik‰li niit‰ ei viel‰ ole tietokannassa
	 * @param towns
	 */
	public void insertTown(ArrayList<Kunta> towns) {
		SQLOperations.insertObject(convertToInsertable(towns), con);
	}

	/**
	 * Lis‰‰ parametrina annetun kalan,
	 * mik‰li sit‰ ei viel‰ ole tietokannassa
	 * @param fish
	 */
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

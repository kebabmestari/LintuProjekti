package databaseconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import databaseobjects.Havainto;
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
	
	private PreparedStatement insertUser=null;
	private PreparedStatement getUserId=null;
	
	private PreparedStatement preparedBirdNameSearch=null;
	private PreparedStatement preparedBirdIdSearch=null;
	
	private PreparedStatement preparedFishNameSearch=null;
	private PreparedStatement preparedFishIdSearch=null;
	private PreparedStatement preparedGetFishName=null;
	
	/**
	 * Kalahavainnon preparaatit
	 */
	private PreparedStatement preparedFishCatchDataSearch=null;
	private PreparedStatement preparedFishIndexCheck=null;
	private PreparedStatement preparedFishIndexSearch=null;
	private PreparedStatement preparedFishMaxLengthSearch=null;
	private PreparedStatement preparedFishCatchDuplicateDelete=null;
	private PreparedStatement preparedFishCatchDeleteById=null;
	
	private PreparedStatement preparedTownSearch=null;
	
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
			
			String getFishName="SELECT nimi FROM kala WHERE id=?;";
			preparedGetFishName=con.prepareStatement(getFishName);
			
			String townSearch="SELECT nimi FROM kunta WHERE nimi LIKE ?;";
			preparedTownSearch=con.prepareStatement(townSearch);
			
			String fishCatchDataSearch="SELECT * "+
					"FROM kalahavainto "+
					"WHERE havaitsija=? AND YEAR(paivamaara)=?;";
			preparedFishCatchDataSearch=con.prepareStatement(fishCatchDataSearch);
			
			String fishIndexCheck="SELECT COUNT(*) AS lkm, kalaid "+
								"FROM kalahavainto "+
								"WHERE havaitsija=? AND YEAR(paivamaara)=? "+
								"GROUP BY kalaid "+
								"HAVING COUNT(*)>1;";
			preparedFishIndexCheck=con.prepareStatement(fishIndexCheck);
			
			String fishIndexSearch="SELECT 	SUM(pituus) AS pituus, COUNT(kalaid) as lkm "+
								"FROM 	kalahavainto "+
								"WHERE 	havaitsija=? AND YEAR(paivamaara)=?;";
			preparedFishIndexSearch=con.prepareStatement(fishIndexSearch);
			
			String fishMaxLenghtIdSearch="SELECT k.id "+
			"FROM kalahavainto AS k "+
			"WHERE k.havaitsija=? AND k.kalaid=? AND YEAR(k.paivamaara)=? "+
			" AND k.pituus=(SELECT MAX(pituus)"+
						" FROM kalahavainto"+
						" WHERE havaitsija=? AND kalaid=? AND YEAR(paivamaara)=?)"+
			"ORDER BY paivamaara desc;";
			preparedFishMaxLengthSearch=con.prepareStatement(fishMaxLenghtIdSearch);
			
			String deleteduplicateFishCatch="DELETE FROM kalahavainto"+
			" WHERE havaitsija=? AND kalaid=? AND YEAR(paivamaara)=? AND id<>?";
			preparedFishCatchDuplicateDelete=con.prepareStatement(deleteduplicateFishCatch);
			
			String deleteFishCatchBiId="DELETE FROM kalahavainto WHERE id=? AND havaitsija=?;";
			preparedFishCatchDeleteById=con.prepareStatement(deleteFishCatchBiId);
			
			String insertUserSql="INSERT INTO kayttaja(nimi,salasana) VALUES (?,?);";
			insertUser=con.prepareStatement(insertUserSql);
			
			String getUserIdSql="SELECT id FROM kayttaja WHERE nimi=? AND salasana=?;";
			getUserId=con.prepareStatement(getUserIdSql);
			
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
	public ArrayList<Lintu> searchBird(String wordBegin){
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
	public ArrayList<Kala> searchFish(String wordBegin){	
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
	
	public ArrayList<Kunta> searchTown(String townBegin) {
		return SQLOperations.searchTown(townBegin, preparedTownSearch);
	}
	
	/**
	 * Lis‰‰ parametrina annetun fongatun kalan tiedot tietokantaan.
	 * Jos sin‰ vuonna on jo kyseinen laji saatu, havainnot p‰ivitet‰‰n.
	 * Palauttaa havainnon id:n
	 * @param fishCatch Kalahavainto, joka halutaan lis‰t‰
	 * @return havainnon id
	 */
	public int insertFishCatch(Kalahavainto fishCatch){
		return SQLOperations.insertHavainto(fishCatch, con);
	}
	
	/**
	 * Lis‰‰ lintuhavainnon tietokantaan. Mik‰li oli n‰hty jo laji sin‰ p‰iv‰n‰,
	 * havainto vain p‰ivitet‰‰n uusilla tiedoilla.
	 * Palauttaa havainnon id:n
	 * @param birdWatch, joka halutaan lis‰t‰
	 * @return havainnon id
	 */
	public int insertBirdWatch(Lintuhavainto birdWatch){
		return SQLOperations.insertHavainto(birdWatch, con);
	}
	
	/**
	 * Etsii havainnon id:n.
	 * Kalahavainnoissa riitt‰‰, ett‰ sin‰ vuonna on saatu kyseinen laji.
	 * Lintuhavainnoissa lintu pit‰‰ olla havaittu samana p‰iv‰n‰,
	 * koska voidaa myˆs p‰iv‰npinnailla.
	 * @param havainto lintuhavainto tai kalahavainto
	 * @return 0 jos ei ole havaintoa, -1 jos error, id > 0 jos lˆytyi
	 */
	public int getHavaintoId(Havainto havainto){
		return SQLOperations.havaintoIdIfAlreadyInTable(havainto, con);
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
			//TODO ei voida p‰ivitt‰‰, koska ei ole alunperin havaintoa tai error
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
	 * @return k‰ytt‰j‰n id tai -2 jos nimi jo k‰ytˆss‰ tai -1 jos poikkeus
	 */
	public int insertUser(Kayttaja user){
		return SQLOperations.insertUser(user, insertUser, getUserId, con);
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
	 * @param fish, joka lis‰t‰‰n
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
	
	/**
	 * Palauttaa fongausindeksin
	 * Mik‰li tietokannassa on virheellisi‰ monikoita, ne poistetaan.
	 * Yht‰ k‰ytt‰j‰‰, kalalajia ja vuotta kohti saa olla vain yksi havainto.
	 * Pisin ja pisimmist‰ uusin j‰‰ tietokantaan.
	 * @param user, huom id oltava oikea id
	 * @param vuosi, vuosi, jolta haetaan indeksi‰
	 * @return fongausindeksi
	 */
	public int[] getFishCatchIndex(Kayttaja user, int vuosi){
		return SQLOperations.getFongoIndex(user, vuosi, preparedFishIndexSearch,
				preparedFishIndexCheck,preparedFishMaxLengthSearch, preparedFishCatchDuplicateDelete);
	}
	
	/**
	 * Palauttaa parametrina annetun vuoden ja k‰ytt‰j‰n havainnot
	 * loppuk‰ytt‰j‰n tarvimassa JSON-formaatissa
	 * KESKEN!!
	 * @param user
	 * @param vuosi
	 * @return
	 */
	public String getFishCatchData(Kayttaja user, int vuosi){
		ArrayList<Kalahavainto> catchArray=SQLOperations.getFishCatchData(user, vuosi, preparedFishCatchDataSearch);
		//TODO Array -> JSON
		return null;
	}
	
	/**
	 * Poistaa kalahavainnon, jonka id on parametrina annettu.
	 * Palauttaa tiedon, montako rivi‰ poistettiin tai poikkeustapauksessa -1
	 * @param id kalahavainnon id
	 * @return poistettujen rivien m‰‰r‰ tai -1 poikkeustapauksessa
	 */
	public int deleteFishCatch(int id, Kayttaja user) {
		return SQLOperations.deleteFishCatch(id, user, preparedFishCatchDeleteById);
	}
	
	/**
	 * Palauttaa kalan id:t‰ vastaavan nimen
	 * Null, jos exception tai ei kalaa
	 * @param kalaid
	 * @return kalan nimi tai null
	 */
	public String getFishNameById(int kalaid) {
		return SQLOperations.getFishNameById(kalaid, preparedGetFishName);
	}
}

package databaseconnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Pattern;

import databaseobjects.Havainto;
import databaseobjects.Insertable;
import databaseobjects.Kala;
import databaseobjects.Kayttaja;
import databaseobjects.Kunta;
import databaseobjects.Lintu;
import databaseobjects.Lintuhavainto;

import java.sql.PreparedStatement;

public class SQLOperations {
	
	/**
	 * Onko akkosellinen merkkijono
	 * @param s, tutkittava merkkijono
	 * @return sis‰lt‰‰kˆ vain aakkosia
	 */
	public static boolean isAlphabetic(String s){
		if(Pattern.matches("[a-ˆA-÷]+", s)){
			return true;
		}
		else{
			return false;
		} 
	}
	/**
	 * Onko annettu merkkijono akkosnumeerinen
	 * @param s
	 * @return onko aakkosnumeerinen
	 */
	public static boolean isAlphaNumeric(String s){
		if(Pattern.matches("[a-ˆA-÷0-9]+", s)){
			return true;
		}
		else{
			return false;
		} 
	}
	
	public static ResultSet searchBird(String wordBegin, PreparedStatement pstm){
		try {
			pstm.setString(1, wordBegin+"%");
			try {
				ResultSet rs=pstm.executeQuery();
				return rs;
			} catch (SQLException e) {
				pstm.close();
				System.err.println("Kysely lajeista ei toiminut");
				e.printStackTrace();
				return null;
			}	
		} catch (SQLException e1) {
			System.out.println("virheellinen SQL");
			e1.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * Palauttaa linnun id:n
	 * @param bird
	 * @param con
	 * @return Linnun id
	 */
	public static int searchBirdId(String bird, PreparedStatement pstm){
		try {
			pstm.setString(1, bird);
			ResultSet rs=pstm.executeQuery();
			if(rs.next()){
				return rs.getInt("id");
			}else{
				return -5;
				//TODO heitet‰‰nkˆ poikkeus kun lintua ei lˆydy????? Kyll‰!!!!
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Lis‰‰ lintu- tai kalahavainnon tietokantaan.
	 * @param havainto
	 * @param con
	 * @return lis‰ttyjen rivien m‰‰r‰
	 */
	public static int insertHavainto(Havainto havainto, Connection con){
		int id=havaintoIdIfAlreadyInTable(havainto, con);
		if(id>0){
			updateHavainto(havainto, id);
			return 0;
		}else{
			String sql="INSERT INTO "+havainto.toInsertHeader()+" VALUES "+havainto.toInsertableString()+";";
			try {
				Statement stm=con.createStatement();
				return stm.executeUpdate(sql);
			} catch (SQLException e) {
				System.err.println("Havainnon lis‰ys ei onnistu");
				e.printStackTrace();
				return 0;
			}
		}
	}
	
	/**
	 * P‰ivitt‰‰ havainnon tiedot parametrina annettun havainnon tietoihin
	 * @param havainto
	 * @param id
	 */
	private static void updateHavainto(Havainto havainto, int id) {
		// TODO Auto-generated method stub
		
	}
	
	private static int havaintoIdIfAlreadyInTable(Havainto havainto, Connection con){
		String sql="SELECT * FROM "+havainto.getTable()+
				"WHERE "+havainto.getUniqueAttributesWithValues()+";";
		try {
			Statement stm=con.createStatement();
			ResultSet rs=stm.executeQuery(sql);
			if(rs.next()){
				int id=rs.getInt("id");
				rs.close();stm.close();
				return id;
			}else{
				rs.close();stm.close();
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		
	}
	
	/**
	 * Lis‰‰ kaikki ne listan alkiot sit‰ vastaan tauluun,
	 * joita ei viel‰ ole taulussa
	 * Lis‰ksi poistaa listasta sivuvaikutuksena ne alkiot,
	 * jotka olivat jo tietokannassa.
	 * @param insertableArray
	 * @param con
	 */
	public static void insertObject(ArrayList<Insertable> insertableArray, Connection con) {
		if(insertableArray.size()>0){
			insertableArray=removeDuplicateInsertables(insertableArray, con);
			if(insertableArray.size()>0){
				Insertable first=insertableArray.get(0);
				String insertables=first.toInsertableString();
				for (int i=1;i<insertableArray.size();i++){
					insertables=insertables+","+insertableArray.get(i).toInsertableString();
				}
				try {
					String sql="INSERT INTO "+insertableArray.get(0).toInsertHeader()+" VALUES "+insertables+";";
					System.out.println(sql);
					Statement stm=con.createStatement();
					stm.executeUpdate(sql);
				} catch (SQLException e) {
					System.out.println("SQL-ongelma yritett‰ess‰ lis‰t‰ tyyppi‰ "+insertableArray.get(0).getTableName());
					e.printStackTrace();
				}
			}else{
				System.out.println("Kaikki esiintym‰t olivat jo tietokannassa, ei mit‰‰n lis‰tt‰v‰‰");
			}
		}else{
			System.out.println("Tyhj‰ lista, ei mit‰‰n lis‰tt‰v‰‰");
		}
	}
	
	private static ArrayList<Insertable> removeDuplicateInsertables(ArrayList<Insertable> insertableArray, Connection con) {
		for(int i=0;i<insertableArray.size();i++){
			Insertable insertable=insertableArray.get(i);
			if(isInsrtableAlreadyInTable(insertable, con)){
				insertableArray.remove(i);
				i--;
			}
		}
		return insertableArray;
	}
	private static boolean isInsrtableAlreadyInTable(Insertable insertable, Connection con) {
		try{
			String sql="SELECT * FROM "+insertable.getTableName()+" WHERE nimi=?;";
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setString(1, insertable.getNimi());
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				rs.close();
				pstmt.close();
				return true;
			}
			rs.close();
			pstmt.close();
			return false;
		} catch (SQLException e1) {
			System.out.println("SQL-ongelma etsiess‰ tyyppi‰ "+insertable.getTableName());
			e1.printStackTrace();
			System.exit(-1);
		}
		return true;
	}
	
	/**
	 * Etsii kunnan sen alkuosan perusteella
	 * Ennustavaa syˆttˆ‰ varten
	 * @param townBegin
	 * @param con
	 * @return lista kunnista, jotka alkoivat parametrina annetulla merkkijonolla
	 */
	public static ResultSet searchTown(String townBegin, Connection con) {
		PreparedStatement psql=null;
		String psqlStatement="SELECT nimi FROM kunta WHERE nimi LIKE ?;";
		try {
			psql=con.prepareStatement(psqlStatement);
			psql.setString(1, townBegin+"%");
			try {
				ResultSet rs=psql.executeQuery();
				return rs;
			} catch (SQLException e) {
				psql.close();
				System.err.println("Kysely kunnista ei toiminut");
				e.printStackTrace();
				return null;
			}	
		} catch (SQLException e1) {
			System.out.println("virheellinen SQL");
			e1.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Etsii kalan nimen sen alkukirjaimen perusteella
	 * Ennustavaa syˆttˆ‰ varten
	 * @param fishBegin
	 * @param con
	 * @return ResultSet kaikista kaloista, jotka alkoivat annetulla merkkijonolla
	 */
	public static ResultSet searchFish(String fishBegin, PreparedStatement pstm) {
		try {
			pstm.setString(1, fishBegin+"%");
			try {
				ResultSet rs=pstm.executeQuery();
				return rs;
			} catch (SQLException e) {
				pstm.close();
				System.err.println("Kysely kaloista ei toiminut");
				e.printStackTrace();
				return null;
			}	
		} catch (SQLException e1) {
			System.out.println("virheellinen SQL");
			e1.printStackTrace();
			return null;
		}
	}
	/**
	 * Palauttaa kalan id:n
	 * @param fish
	 * @param con
	 * @return Kalan id
	 */
	public static int searchFishId(String fish, PreparedStatement pstm){
		try {
			pstm.setString(1, fish);
			ResultSet rs=pstm.executeQuery();
			if(rs.next()){
				return rs.getInt("id");
			}else{
				return -5;
				//TODO heitet‰‰nkˆ poikkeus kun kalaa ei lˆydy?????
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
}

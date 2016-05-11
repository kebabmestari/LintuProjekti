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

import java.sql.PreparedStatement;

public class SQLOperations {
	
	/**
	 * Onko akkosellinen merkkijono
	 * @param s, tutkittava merkkijono
	 * @return sisältääkö vain aakkosia
	 */
	public static boolean isAlphabetic(String s){
		if(Pattern.matches("[a-öA-Ö]+", s)){
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
		if(Pattern.matches("[a-öA-Ö0-9]+", s)){
			return true;
		}
		else{
			return false;
		} 
	}
	
	/**
	 * Etsii listan linnuista, jotka alkavat ennetulla merkkijonolla
	 * @param wordBegin
	 * @param pstm
	 * @return lista linnuista
	 */
	public static ArrayList<Lintu> searchBird(String wordBegin, PreparedStatement pstm){
		ArrayList<Lintu> birdList=new ArrayList<>();
		try {
			pstm.setString(1, wordBegin+"%");
			try {
				ResultSet rs=pstm.executeQuery();
				while(rs.next()){
					String nimi=rs.getString("nimi");
					birdList.add(new Lintu(nimi));
				}
				return birdList;
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
				//TODO heitetäänkö poikkeus kun lintua ei löydy????? Kyllä!!!!
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Etsii kalan nimen sen alkukirjaimen perusteella
	 * Ennustavaa syöttöä varten
	 * @param fishBegin
	 * @param con
	 * @return ResultSet kaikista kaloista, jotka alkoivat annetulla merkkijonolla
	 */
	public static ArrayList<Kala> searchFish(String fishBegin, PreparedStatement pstm) {
		ArrayList<Kala> fishlist=new ArrayList<>();
		try {
			pstm.setString(1, fishBegin+"%");
			try {
				ResultSet rs=pstm.executeQuery();
				while(rs.next()){
					String fishName=rs.getString("nimi");
					fishlist.add(new Kala(fishName));
				}
				return fishlist;
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
	 * @return Kalan id tai -5 jos ei löyty tai -1 jos error
	 */
	public static int searchFishId(String fish, PreparedStatement pstm){
		try {
			pstm.setString(1, fish);
			ResultSet rs=pstm.executeQuery();
			if(rs.next()){
				return rs.getInt("id");
			}else{
				return -5;
				//TODO heitetäänkö poikkeus kun kalaa ei löydy?????
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	/**
	 * Etsii kunnan sen alkuosan perusteella
	 * Ennustavaa syöttöä varten
	 * @param townBegin kunnan alkuosa
	 * @param townSearch kuntahaku
	 * @return lista kunnista, jotka alkoivat parametrina annetulla merkkijonolla
	 */
	public static ArrayList<Kunta> searchTown(String townBegin, PreparedStatement townSearch) {
		ArrayList<Kunta> townList=new ArrayList<>();
		try {
			townSearch.setString(1, townBegin+"%");
			try {
				ResultSet rs=townSearch.executeQuery();
				while(rs.next()){
					String town=rs.getString("nimi");
					townList.add(new Kunta(town));
				}
				return townList;
			} catch (SQLException e) {
				townSearch.close();
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
	 * Lisää lintu- tai kalahavainnon tietokantaan.
	 * @param havainto
	 * @param con
	 * @return lisättyjen rivien määrä, jos error -1, jos päivitys 0
	 */
	public static int insertHavainto(Havainto havainto, Connection con){
		int id=havaintoIdIfAlreadyInTable(havainto, con);
		if(id>0){
			System.out.println("Päivitetään");
			updateHavainto(havainto, id, con);
			return 0;
		}else if(id==-1){
			return -1;
		}else{	
			System.out.println("Lisätään");
			String sql="INSERT INTO "+havainto.toInsertHeader()+" VALUES "+havainto.toInsertableString()+";";
			try {
				Statement stm=con.createStatement();
				return stm.executeUpdate(sql);
			} catch (SQLException e) {
				System.err.println("Havainnon lisäys ei onnistu");
				e.printStackTrace();
				return -1;
			}
		}
	}
	
	/**
	 * Päivittää havainnon tiedot parametrina annettun havainnon tietoihin
	 * @param havainto
	 * @param id
	 */
	public static void updateHavainto(Havainto havainto, int id, Connection con) {
		String sql="UPDATE "+havainto.getTable()+
				" SET "+havainto.getAllUpdatableAttributesWithValues()+
				"WHERE id='"+id+"';";
		try {
			Statement stm=con.createStatement();
			stm.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static int havaintoIdIfAlreadyInTable(Havainto havainto, Connection con){
		String sql="SELECT id FROM "+havainto.getTable()+
				" WHERE "+havainto.getUniqueAttributesWithValues()+";";
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
	 * Lisää kaikki ne listan alkiot sitä vastaan tauluun,
	 * joita ei vielä ole taulussa
	 * Lisäksi poistaa listasta sivuvaikutuksena ne alkiot,
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
					System.out.println("SQL-ongelma yritettäessä lisätä tyyppiä "+insertableArray.get(0).getTableName());
					e.printStackTrace();
				}
			}else{
				System.out.println("Kaikki esiintymät olivat jo tietokannassa, ei mitään lisättävää");
			}
		}else{
			System.out.println("Tyhjä lista, ei mitään lisättävää");
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
			System.out.println("SQL-ongelma etsiessä tyyppiä "+insertable.getTableName());
			e1.printStackTrace();
			System.exit(-1);
		}
		return true;
	}
	
	public int getFongoIndex(Kayttaja user){
		return 0;
	}
}

package databaseconnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Pattern;

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
	
	public static ResultSet searchBird(String wordBegin, Connection conn){
		PreparedStatement psql=null;
		String psqlStatement="SELECT nimi FROM lintu WHERE nimi LIKE ? ORDER BY yleisyys;";
		try {
			psql=conn.prepareStatement(psqlStatement);
			psql.setString(1, wordBegin+"%");
			try {
				ResultSet rs=psql.executeQuery();
				return rs;
			} catch (SQLException e) {
				psql.close();
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
	
	public static int insertBirdWatch(String bird, String town, String user, Boolean sponde, Boolean eko, Statement stm){
		String sql="INSERT INTO lintuhavainto VALUES("+bird+", "+town+", "+user+", "+sponde+", "+eko+")";
		try {
			return stm.executeUpdate(sql);
		} catch (SQLException e) {
			System.err.println("Päivitys ei onnistu");
			e.printStackTrace();
			return 0;
		}
	}
	/**
	 * Lisää vain ne linnut, joita ei vielä ole lisätty
	 * @param birdArray, lintulista
	 * @param stm sql Statement
	 */
	public static void insertBird(ArrayList<Lintu> birdArray, Statement stm, Connection con){
		if(birdArray.size()>0){
			birdArray=removeDuplicateBirds(birdArray, con);
			if(birdArray.size()>0){
				Lintu first=birdArray.get(0);
				String birds="('"+first.getNimi()+"','"+first.getYleisyys()+"')";
				for (int i=1;i<birdArray.size();i++){
					birds=birds+",('"+birdArray.get(i).getNimi()+"','"+birdArray.get(i).getYleisyys()+"')";
				}
				try {
					String sql="INSERT INTO lintu(nimi, yleisyys) VALUES "+birds+";";
					System.out.println(sql);
					stm.executeUpdate(sql);
				} catch (SQLException e) {
					System.out.println("SQL-ongelma yritettäessä lisätä lintuja");
					e.printStackTrace();
				}
			}else{
				System.out.println("Kaikki linnut olivat jo tietokannassa, ei mitään lisättävää");
			}
		}else{
			System.out.println("Lintulista oli tyhjä, ei mitään lisättävää");
		}
	}
	
	/**
	 * Tutkitaan, onko taulukossa jo
	 * @param bird, jota tutkitaan
	 * @param con, tietokannan yhteysolio 
	 * @return löytyikö lintua tietokannasta
	 */
	public static boolean isBirdAlreadyInTable(Lintu bird, Connection con){
		ResultSet rs=searchBird(bird.getNimi(), con);
		try {
			while(rs.next()){
				String nimi  = rs.getString("nimi");
				if(nimi.equals(bird.getNimi())){
					rs.close();
					return true;
				}
			}
			rs.close();
			return false;
		} catch (SQLException e1) {
			System.out.println("SQL-ongelma");
			e1.printStackTrace();
		}
		return false;
	}
	/**
	 * Poistetaan lintulistasta kaikki ne niment, jotka esiintyvät jo tietokannassa
	 * AE: birdArray<>null
	 * Loppuehto: lintulistasta ei löydy yhtään lintua, joka olisi jo tietokannassa
	 * @param birdArray
	 * @param con
	 * @return putsattu lintulista
	 */
	public static ArrayList<Lintu> removeDuplicateBirds(ArrayList<Lintu> birdArray, Connection con){
		for(int i=0;i<birdArray.size();i++){
			Lintu bird=birdArray.get(i);
			if(isBirdAlreadyInTable(bird, con)){
				birdArray.remove(i);
				i--;
			}
		}
		return birdArray;
	}
	
	/**
	 * Lisää parametrina annetun käyttäjän
	 * @param user, joka aiotaan lisätä
	 * @param stm
	 */
	public static void insertUser(Kayttaja user, Statement stm){
		if (isAlphabetic(user.getNimi()) && isAlphaNumeric(user.getSalasana())){
			//TODO tarkista, onko käyttäjänimi kelvollinen eli uniikki
			String sql="INSERT INTO kayttaja(nimi,salasana) VALUES ('"+user.getNimi()+"','"+user.getSalasana()+"');";
			try {
				stm.executeUpdate(sql);
			} catch (SQLException e) {
				System.out.println("Ainaki tulee mieleen et käyttäjänimi voi olla jo varattu...");
				e.printStackTrace();
			}
		}
	}
	
	public static void insertKunta(ArrayList<Kunta> townArray, Statement stm, Connection con) {
		if(townArray.size()>0){
			townArray=removeDuplicateTowns(townArray, con);
			if(townArray.size()>0){
				Kunta first=townArray.get(0);
				String towns="('"+first.getNimi()+"')";
				for (int i=1;i<townArray.size();i++){
					towns=towns+",('"+townArray.get(i).getNimi()+"')";
				}
				try {
					String sql="INSERT INTO kunta(nimi) VALUES "+towns+";";
					System.out.println(sql);
					stm.executeUpdate(sql);
				} catch (SQLException e) {
					System.out.println("SQL-ongelma yritettäessä lisätä kuntia");
					e.printStackTrace();
				}
			}else{
				System.out.println("Kaikki kunnat olivat jo tietokannassa, ei mitään lisättävää");
			}
		}else{
			System.out.println("Kuntalista oli tyhjä, ei mitään lisättävää");
		}
	}
	
	private static ArrayList<Kunta> removeDuplicateTowns(ArrayList<Kunta> townArray, Connection con) {
		for(int i=0;i<townArray.size();i++){
			Kunta town=townArray.get(i);
			if(isTownAlreadyInTable(town, con)){
				townArray.remove(i);
				i--;
			}
		}
		return townArray;
	}
	private static boolean isTownAlreadyInTable(Kunta town, Connection con) {
		ResultSet rs=searchTown(town.getNimi(), con);
		try {
			while(rs.next()){
				String nimi  = rs.getString("nimi");
				if(nimi.equals(town.getNimi())){
					rs.close();
					return true;
				}
			}
			rs.close();
			return false;
		} catch (SQLException e1) {
			System.out.println("SQL-ongelma kuntaa etsiessä");
			e1.printStackTrace();
		}
		return false;
	}
	
	private static ResultSet searchTown(String townBegin, Connection con) {
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
	public static void insertFish(ArrayList<Kala> fishArray, Statement stm, Connection con) {
		if(fishArray.size()>0){
			fishArray=removeDuplicateFish(fishArray, con);
			if(fishArray.size()>0){
				Kala first=fishArray.get(0);
				String fish="('"+first.getNimi()+"')";
				for (int i=1;i<fishArray.size();i++){
					fish=fish+",('"+fishArray.get(i).getNimi()+"')";
				}
				try {
					String sql="INSERT INTO kala(nimi) VALUES "+fish+";";
					System.out.println(sql);
					stm.executeUpdate(sql);
				} catch (SQLException e) {
					System.out.println("SQL-ongelma yritettäessä lisätä kuntia");
					e.printStackTrace();
				}
			}else{
				System.out.println("Kaikki kalat olivat jo tietokannassa, ei mitään lisättävää");
			}
		}else{
			System.out.println("Kalalista oli tyhjä, ei mitään lisättävää");
		}
	}
	private static ArrayList<Kala> removeDuplicateFish(ArrayList<Kala> fishArray, Connection con) {
		for(int i=0;i<fishArray.size();i++){
			Kala fish=fishArray.get(i);
			if(isFishAlreadyInTable(fish, con)){
				fishArray.remove(i);
				i--;
			}
		}
		return fishArray;
	}
	private static boolean isFishAlreadyInTable(Kala fish, Connection con) {
		ResultSet rs=searchFish(fish.getNimi(), con);
		try {
			while(rs.next()){
				String nimi  = rs.getString("nimi");
				if(nimi.equals(fish.getNimi())){
					rs.close();
					return true;
				}
			}
			rs.close();
			return false;
		} catch (SQLException e1) {
			System.out.println("SQL-ongelma kuntaa etsiessä");
			e1.printStackTrace();
		}
		return false;
	}
	private static ResultSet searchFish(String fishBegin, Connection con) {
		PreparedStatement psql=null;
		String psqlStatement="SELECT nimi FROM kala WHERE nimi LIKE ?;";
		try {
			psql=con.prepareStatement(psqlStatement);
			psql.setString(1, fishBegin+"%");
			try {
				ResultSet rs=psql.executeQuery();
				return rs;
			} catch (SQLException e) {
				psql.close();
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
}

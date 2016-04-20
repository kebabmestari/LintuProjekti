package databaseconnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Pattern;

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
	 * Toimii
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
				return psql.executeQuery();
			} catch (SQLException e) {
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
	public static void addBird(ArrayList<Lintu> birdArray, Statement stm, Connection con){
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
}

package test;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import databaseconnection.DB_connection;
import databaseobjects.Kayttaja;
import databaseobjects.Kunta;
import databaseobjects.Lintu;
import databaseobjects.Lintuhavainto;
import databaseobjects.Paivamaara;
import databaseobjects.Havainto;
import databaseobjects.Insertable;
import databaseobjects.Kala;
import databaseobjects.Kalahavainto;

public class Test {

	private static String user="root";
	private static String password="mysli";
	
	public static void main(String[] args) {
		DB_connection connection=new DB_connection("localhost", "tk2", user, password);
	//	testikunta(connection); toimii
	//	testikäyttäjä(connection); toimii
	
	//	lisaaLinnut(lueLinnut(true), connection); //toimii
	//	arrayTest(connection.getConnection()); //toimii
	//	lisaaKunnat(lueKunnat(true), connection); //toimii
	//	lisaaKalat(lueKalat(true),connection);
		lintuhavaintoTesti(connection);
	//	kalahavaintotesti(connection);
		connection.disconnect();
	}

	public static void kalahavaintotesti(DB_connection connection) {
		int id=connection.searchFishId("hauki");
		System.out.println("hauki "+id);
		try {
			Kalahavainto kh=new Kalahavainto("Kaarina", 25, "ahven",1,new Paivamaara("3.5.2016"), connection);
			System.out.println("Kalaid: "+kh.getKalaid()+" Päivämäärä: "+kh.getPvm().toString());
			System.out.println(((Havainto)kh).getUniqueAttributesWithValues());
			connection.insertFishCatch(kh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		id=connection.searchFishId("ahven");
		System.out.println("ahven "+id);
	}

	public static void lintuhavaintoTesti(DB_connection connection){
		Lintuhavainto lintuhavainto = null;
		try {
			lintuhavainto = new Lintuhavainto("kiuru", "Turku", new Paivamaara(22,4,2016),1,false,true, connection);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Otsikko "+lintuhavainto.toInsertHeader()+"ja arvot"+ lintuhavainto.toInsertableString());
		connection.insertBirdWatch(lintuhavainto);
	}
	
	public static ArrayList<Kala> lueKalat(boolean debug){
		ArrayList<Kala> kalat=new ArrayList<>();
		try {
			InputStream inp=Test.class.getResourceAsStream("kalat.csv");
			Scanner scan=new Scanner(inp);
			scan.useDelimiter(";");
			scan.nextLine();
			while(scan.hasNextLine()){
				kalat.add(new Kala(scan.next().toLowerCase()));
				scan.nextLine();
			}
			scan.close();
			inp.close();
			if(debug){
				System.out.println("ArrayListan pituus: "+kalat.size());
				printArray(kalat);
			}
			return kalat;
			
		} catch (Exception e) {
			System.out.println("Tiedostoa ei löydy tai");
			e.printStackTrace();
			return null;
		}
	}
	
	public static ArrayList<Lintu> lueLinnut(boolean debug){
		ArrayList<Lintu> linnut=new ArrayList<>();
		try {
			InputStream inp=Test.class.getResourceAsStream("linnut.csv");
			Scanner scan=new Scanner(inp);
			while(scan.hasNextLine()){
				linnut.add(new Lintu(scan.nextLine()));
			}
			scan.close();
			inp.close();
			if(debug)System.out.println("ArrayListan pituus: "+linnut.size());
			return linnut;
			
		} catch (Exception e) {
			System.out.println("Tiedostoa ei löydy tai");
			e.printStackTrace();
			return null;
		}
	}
	
	public static ArrayList<Kunta> lueKunnat(boolean debug){
		ArrayList<Kunta> kunnat=new ArrayList<>();
		try {
			InputStream inp=Test.class.getResourceAsStream("kunnat.csv");
			Scanner scan=new Scanner(inp);
			scan.useDelimiter(";");
			while(scan.hasNextLine()){
				kunnat.add(new Kunta(scan.next()));
				scan.nextLine();
			}
			scan.close();
			inp.close();
			if(debug){
				System.out.println("ArrayListan pituus: "+kunnat.size());
				printArray(kunnat);
			}
			return kunnat;
			
		} catch (Exception e) {
			System.out.println("Tiedostoa ei löydy tai");
			e.printStackTrace();
			return null;
		}
	}
	
	public static void lisaaKalat(ArrayList<Kala> kalalista, DB_connection connection){
		connection.insertFish(kalalista);
	}

	public static void lisaaLinnut(ArrayList<Lintu> lintulista, DB_connection connection){
		connection.insertBird(lintulista);
	}

	public static void lisaaKunnat(ArrayList<Kunta> kunnat, DB_connection connection){
		connection.insertTown(kunnat);
	}
	
	public static void etsiLinnut(DB_connection connection, String alkuosa) {
		try{
			ResultSet rs = connection.searchBird(alkuosa);
			while(rs.next()){
				String nimi  = rs.getString("nimi");
				//int id=rs.getInt("id");
				System.out.println("Nimi: " +nimi+" id tuntematon");
			}
			rs.close();		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void testikäyttäjä(DB_connection connection) {
		Kayttaja josia=new Kayttaja("Josia", "kovinSalainen");
		connection.insertUser(josia);
	}
	
	public static void testikunta(DB_connection connection) {
		ArrayList<Kunta> kunnat=new ArrayList<>();
		kunnat.add(new Kunta("Raisio"));
		kunnat.add(new Kunta("Helsinki"));
//		connection.insertKunta(kunnat);
	}
	

	public static void printBirdArray(ArrayList<Lintu> lista) {
		System.out.print("[");
		for(Lintu l: lista){
			System.out.print("("+l.getNimi()+","+l.getYleisyys()+"),");
		}
		System.out.println("] Size:"+lista.size());
	}
	
	public static void printArray(ArrayList<?> lista) {
		System.out.print("[");
		for(Object l: lista){
			System.out.print("("+((Insertable) l).getNimi()+"), ");
		}
		System.out.println("] Size:"+lista.size());
	}
}

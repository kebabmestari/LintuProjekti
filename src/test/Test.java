package test;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import databaseconnection.DB_connection;
import databaseconnection.SQLOperations;
import databaseobjects.Kayttaja;
import databaseobjects.Kunta;
import databaseobjects.Lintu;
import databaseobjects.Kala;

public class Test {

	private static String user="root";
	private static String password="mysli";
	
	public static void main(String[] args) {
		DB_connection connection=new DB_connection("localhost", "tk2", user, password);
	//	testikunta(connection); toimii
	//	testikäyttäjä(connection); toimii
	
	//	lisaaLinnut(lueLinnut(true), connection); toimii
	//	arrayTest(connection.getConnection()); toimii
	//	lisaaKunnat(lueKunnat(true), connection); toimii
		lisaaKalat(lueKalat(true),connection);
		connection.disconnect();
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
	
	public static void lisaaKunnat(ArrayList<Kunta> kunnat, DB_connection connection){
		connection.insertKunta(kunnat);
	}
	
	public static void lisaaLinnut(ArrayList<Lintu> lintulista, DB_connection connection){
		connection.insertBird(lintulista);
	}
	
	public static void lisaaKalat(ArrayList<Kala> kalalista, DB_connection connection){
		connection.insertFish(kalalista);
	}
	
	public static void etsiLinnut(DB_connection connection, String alkuosa) {
		try{
			//	Scanner scan =new Scanner(System.in);
			//	while (scan.hasNext()){
			//		String lintu= scan.next();
					ResultSet rs = connection.searchBird(alkuosa);
						
					while(rs.next()){
							//Retrieve by column name
						String nimi  = rs.getString("nimi");
						//int id=rs.getInt("id");
						System.out.println("Nimi: " +nimi+" id tuntematon");
					}

					rs.close();
				//	}
				//	scan.close();		
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
		connection.insertKunta(kunnat);
	}
	
	public static void arrayTest(Connection con) {
		ArrayList<Lintu> lista=lueLinnut(true);
	//	lista.add(new Lintu("tilhi"));
		printArray(lista);
	//	lista.add(new Lintu("talitiainen"));
	//	lista.add(new Lintu("varis"));
	//	lista.add(new Lintu("kalahuuhkaja"));
		printArray(lista);
		printIfExsists(lista, con);
		lista=SQLOperations.removeDuplicateBirds(lista, con);
		printIfExsists(lista, con);
		printArray(lista);
	}
	
	public static void printIfExsists(ArrayList<Lintu> lista, Connection con) {
		for(Lintu l: lista){
			System.out.println(SQLOperations.isBirdAlreadyInTable(l, con));
		}
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
			System.out.print("("+((Kala) l).getNimi()+"), "); // vaihda vain castattavaa oliota tarpeen mukaan
		}
		System.out.println("] Size:"+lista.size());
	}
}

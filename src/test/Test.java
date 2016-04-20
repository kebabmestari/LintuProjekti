package test;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import databaseconnection.DB_connection;
import databaseconnection.SQLOperations;
import databaseobjects.Lintu;

public class Test {

	private static String user="root";
	private static String password="mysli";
	
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
	
	public static void lisaaLinnut(ArrayList<Lintu> lintulista, DB_connection connection){
		connection.addBird(lintulista);
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
	
	public static void main(String[] args) {
		DB_connection connection=new DB_connection("localhost", "tk2", user, password);
		
	//	lisaaLinnut(lueLinnut(true), connection);
		arrayTest(connection.getConnection());
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

	public static void printArray(ArrayList<Lintu> lista) {
		System.out.print("[");
		for(Lintu l: lista){
			System.out.print("( "+l.getNimi()+","+l.getYleisyys()+"),");
		}
		System.out.println("] Size:"+lista.size());
	}
}

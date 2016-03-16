package test;
import java.io.InputStream;
import java.sql.*;
import java.util.Scanner;

import databaseconnection.DB_connection;

public class Test {

	
	private static final String DB_URI="jdbc:mysql://localhost/testi";
	private static String user="root";
	private static String password="mysli";
	
	public static void main(String[] args) {
			DB_connection connection=new DB_connection("localhost", "testi", user, password);
			
			try{
				
				ResultSet rs = connection.searchBird("k");
			    
			    while(rs.next()){
			         //Retrieve by column name
			         String nimi  = rs.getString("nimi");
			         
			         System.out.println("Nimi: " +nimi);
			    }

			    rs.close();
			  
		
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		/*	String sql;
			String linnut="";
			try {
				InputStream inp=Test.class.getResourceAsStream("linnut.csv");
				Scanner scan=new Scanner(inp);
				linnut="('"+scan.nextLine()+"')";
				while (scan.hasNextLine()){
					linnut=linnut+", ('"+scan.nextLine()+"')";
				}
				scan.close();
				inp.close();
				System.out.println(linnut);
				
				
			} catch (Exception e) {
				System.out.println("Tiedostoa ei löydy tai");
				e.printStackTrace();
			}
			
			
			sql="DELETE FROM lintu;";
	//		stm.addBatch(sql);
			
			sql="INSERT INTO lintu VALUES "+linnut+";";
	//		stm.addBatch(sql);
			
	/*		int[] tulos= stm.executeBatch();
			for (int i:tulos){
				System.out.println(i);
			}
			
		*/	
	}
	

}

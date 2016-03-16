package test;
import java.io.InputStream;
import java.sql.*;
import java.util.Scanner;

public class Test {

	
	private static final String DB_URI="jdbc:mysql://localhost/testi";
	private static String user="root";
	private static String password="mysli";
	
	public static void main(String[] args) {
		Connection con =null;
		Statement stm=null;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			
			con=DriverManager.getConnection(DB_URI, user, password);
			
			stm=con.createStatement();
			
			String sql;
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
			stm.addBatch(sql);
			
			sql="INSERT INTO lintu VALUES "+linnut+";";
			stm.addBatch(sql);
			
			int[] tulos= stm.executeBatch();
			for (int i:tulos){
				System.out.println(i);
			}
			
			sql = "SELECT * FROM lintu";
			ResultSet rs = stm.executeQuery(sql);
		    
		    while(rs.next()){
		         //Retrieve by column name
		         String nimi  = rs.getString("nimi");
		         
		         
		         System.out.println("Nimi: " +nimi);
		    }

		    rs.close();
		    stm.close();
		    con.close();
		}
		catch (ClassNotFoundException e){
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}

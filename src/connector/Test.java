package connector;
import java.sql.*;

public class Test {

	private static final String DB_URI="jdbc:mysql://localhost/testi";
	private static String user="root";
	private static String password="mysli";
	
	public static void main(String[] args) {
		Connection con =null;
		Statement stm=null;
		
		// TODO Auto-generated method stub
		try{
			Class.forName("com.mysql.jdbc.Driver");
			
			con=DriverManager.getConnection(DB_URI, user, password);
			
			stm=con.createStatement();
			
			String sql;
			sql = "SELECT nimi FROM Lintu";
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

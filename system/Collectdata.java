import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.ini4j.Wini;

/*
 * Collects data from input arguments
 * First argument: time (integer value, seconds since Jan 1. 1970)
 * Second argument: val (integer value)
 */
public class Collectdata {

	public static void main(String[] args) {
		/*
		 * Retrieve data from INI file
		 */
		Wini ini = null; 
		File inifile; 
		try{
			inifile = new File("constants.ini"); 
			ini = new Wini(inifile);
		}catch (Exception e){
			System.err.println("Unable to load ini file");
			e.printStackTrace(); 
		}
		String host = ini.get("DBinfo", "host");
		String dbname = ini.get("DBinfo", "dbname");
		String port = ini.get("DBinfo", "port");
		String user = ini.get("DBinfo", "user");
		String password = ini.get("DBinfo", "password");
		/*
		 * Do some error checking on arguments
		 */
		if (args.length != 2){
			throw new IllegalArgumentException("You must supply 2 arguments"); 
		}

		String arg0 = args[0];
		String arg1 = args[1];

		double time = 0; 
		try{
			time = Double.parseDouble(arg0); 
		}catch (NumberFormatException e){
			System.out.println("First argument must be an double value");
		}
		if(!(time>0)){
			System.out.println("time must be greater than 0");
		}

		int value = 0; 
		try{
			value = Integer.parseInt(arg1); 
		}catch (NumberFormatException e){
			System.out.println("second argument must be an integer value");
		}


		/*
		 * Create a new connection to the database
		 */
		Connection con = null;
		PreparedStatement pst = null;
		String url = "jdbc:mysql://" + host + ":" + port + "/" + dbname;

		/*
		 * Insert values into the 'datapoint' table
		 */
		try {
			con = DriverManager.getConnection(url, user, password);
			pst = con.prepareStatement("INSERT INTO datapoint(time,val) VALUES(?,?)");
			pst.setString(1, Double.toString(time));
			pst.setString(2, Integer.toString(value));
			pst.executeUpdate();
		} catch (SQLException e) {
			System.err.println("SQL error during connection or insertion");
			e.printStackTrace(); 
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				System.err.println("SQL closing of connection");
				e.printStackTrace(); 
			}
		}
	}
}

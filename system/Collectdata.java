import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
 * Collects data from input arguments
 * First argument: time (integer value, seconds since Jan 1. 1970)
 * Second argument: val (integer value)
 */
public class Collectdata {

	public static void main(String[] args) {
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
		String url = "jdbc:mysql://localhost:8889/hackerati";
		String user = "emil";
		String password = "mrmemorex";

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

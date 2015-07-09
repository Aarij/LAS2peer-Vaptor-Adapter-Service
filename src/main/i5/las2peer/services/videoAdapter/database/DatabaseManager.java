package i5.las2peer.services.videoAdapter.database;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import i5.las2peer.services.videoAdapter.util.GetProperty;


//import de.dbis.util.GetProperty;

/**
 * 
 * Establishes connection to the MySql database.
 * Uses 'dbconnection.properties' file for configuration.
 *
 */
public class DatabaseManager
{
	private final static String INPUT_FILE = "etc/i5.las2peer.services.videoAdapter.dbconnection.properties";
	//private final static String BASE_INPUT_FILE = "base";
	private static String url;
	private static String host;
	private static String port;
	private static String dbName;
	private static String driver;
	private static String userName;
	private static String password;
	private static String databaseServer;
	/*private static String useUniCode;
	private static String charEncoding;
	private static String charSet;
	private static String collation;*/
	
	public void init(String driver, String databaseServer, String port, String dbName, String userName, String password, String host) {

		//System.out.println("Inputfile: "+INPUT_FILE);
		System.out.println("DB CHECK: ");
		/*this.driver = GetProperty.getParam("driverName", INPUT_FILE);
		this.databaseServer = GetProperty.getParam("databaseServer", INPUT_FILE);
		//base = GetProperty.getParam("uri", BASE_INPUT_FILE);
		this.port = GetProperty.getParam("port", INPUT_FILE);
		this.dbName = GetProperty.getParam("database", INPUT_FILE);
		this.userName = GetProperty.getParam("username", INPUT_FILE);
		this.password = GetProperty.getParam("password", INPUT_FILE);
		this.useUniCode = GetProperty.getParam("useUniCode", INPUT_FILE);
		this.charEncoding = GetProperty.getParam("charEncoding", INPUT_FILE);
		this.charSet = GetProperty.getParam("charSet", INPUT_FILE);
		this.collation = GetProperty.getParam("collation", INPUT_FILE);*/
		
		this.driver = driver;
		this.databaseServer = databaseServer;
		this.port = port;
		this.dbName = dbName;
		this.userName = userName;
		this.password = password;
		this.host=host;
		
		//this.useUniCode = useUniCode;
		//this.charEncoding = charEncoding;
		//this.charSet = charSet;
		//this.collation = collation;
		
		url = "jdbc:" + this.databaseServer + "://" + this.host +":"+ this.port + "/";
		
		System.out.println("DB URL: "+url);
	}

	public void setPreferences(String [] preferences){
		
		int rowCount = 0;
		try {
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(url+dbName,userName,password);
			
			String insertQuery = " insert into profile (username, location, language, duration) values (?, ?, ?, ?)";
			
			PreparedStatement pstmt = conn.prepareStatement(insertQuery);
			pstmt.setString(1, preferences[0]);
			pstmt.setString(2, preferences[1]);
			pstmt.setString(3, preferences[2]);
			pstmt.setString(4, preferences[3]);
			rowCount = pstmt.executeUpdate();
			
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public String[] getPreferences(String username){
		
		//init();
		System.out.println("in getPreferences, "+ username);
		ResultSet res = null;
		String preferences[]=new String[5];
		String interests, language, domain, duration, location;
		try {
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(url+dbName,userName,password);
			
			String insertQuery = "SELECT * FROM profile WHERE username = ?";
			
			
			PreparedStatement pstmt = conn.prepareStatement(insertQuery);
			pstmt.setString(1, username);
			res = pstmt.executeQuery();

			if(res.next()){
				
				interests = res.getString("interests");
				language = res.getString("language");
				domain = res.getString("domain");
				duration = res.getString("duration");
				location = res.getString("location");
				
				preferences[0]=interests;
				preferences[1]=language;
				preferences[2]=domain;
				preferences[3]=duration;
				preferences[4]=location;
			}
			
			else{
				System.out.println("User Not Found!");
				//return false;
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return preferences;
	}
	
	
	public void userExists(String username){
		
		System.out.println("in update, "+ username);
		ResultSet res = null;
		
		try {
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(url+dbName,userName,password);
			
			String insertQuery = "SELECT * FROM profile WHERE username = ?";
			
			
			PreparedStatement pstmt = conn.prepareStatement(insertQuery);
			pstmt.setString(1, username);
			res = pstmt.executeQuery();

			if(!res.next()){
				
				String[] preferences = {username,"aachen","english","5"};
				setPreferences(preferences);
				
			}
			
			else{
				System.out.println("User Exists!");
				//return false;
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
public void update(String [] preferences){
		
		System.out.println("in update, "+ preferences[0]);
		ResultSet res = null;
		
		String interests, language, domain, duration, location;
		try {
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(url+dbName,userName,password);
			
			String insertQuery = "SELECT * FROM profile WHERE username = ?";
			
			
			PreparedStatement pstmt = conn.prepareStatement(insertQuery);
			pstmt.setString(1, preferences[0]);
			res = pstmt.executeQuery();

			if(res.next()){
				
							
				
				String updateQuery = " update profile set location=?, language=?, duration=? where username=?";
				//String query = "update users set num_points = ? where first_name = ?";
				//"update budgetreport set sales= ? , cogs= ? where quarter="+ qTracker;
				PreparedStatement pstmt_update = conn.prepareStatement(updateQuery);
				
				pstmt_update.setString(1, preferences[1]);
				pstmt_update.setString(2, preferences[2]);
				pstmt_update.setString(3, preferences[3]);
				pstmt_update.setString(4, preferences[0]);
				pstmt_update.executeUpdate();
				
			}
			
			else{
				System.out.println("User Exists!");
				//return false;
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
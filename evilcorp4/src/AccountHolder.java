import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;


public class AccountHolder {
	private int customer_id;
	private String customer_name;
	private String customer_birth;
	private String id;
	private Connection conn;
	public int getCustomer_id() {
		return customer_id;
	}
public AccountHolder(){
	
		}
		
	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setid(String id) {
		this.id = id;
	}
	public String getid() {
		return id;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getCustomer_birth() {
		return customer_birth;
	}
	public void setCustomer_birth(String customer_birth) {
		this.customer_birth = customer_birth;
	}
    
	public AccountHolder(int customer_id,String customer_name,String customer_birth,String id){
		this.customer_id=customer_id;
		this.customer_name = customer_name;
		this.customer_birth=customer_birth;
		this.id = id;
	}
	
	//SEARCH FOR AN ACCOUNT USING ID IN ORACLE
		public ResultSet getAccountTypeByAccountHolderID(int type) throws SQLException{
			String url = "jdbc:oracle:thin:system/password@localhost"; 
		      
	        //properties for creating connection to Oracle database
	        Properties props = new Properties();
	        props.setProperty("user", "TESTUSERDB");
	        props.setProperty("password", "password");
	      
	        //creating connection to Oracle database using JDBC
	         conn = DriverManager.getConnection(url,props);
			 String sql = "select  * from  ACCOUNT where CUSTOMER_ID ='" + customer_id +"' AND type = " + type ;

		        //creating PreparedStatement object to execute query
		        PreparedStatement preStatement = conn.prepareStatement(sql);
		    
		        ResultSet result = preStatement.executeQuery();
		        
		        return result;
		      
		}
	//SEARCH FOR AN ACCOUNT WITHOUT TYPE USING ID IN ORACLE	
		public ResultSet getAccountByAccountHolderID() throws SQLException{
			String url = "jdbc:oracle:thin:system/password@localhost"; 
		      
	        //properties for creating connection to Oracle database
	        Properties props = new Properties();
	        props.setProperty("user", "TESTUSERDB");
	        props.setProperty("password", "password");
	      
	        //creating connection to Oracle database using JDBC
	         conn = DriverManager.getConnection(url,props);
			 String sql = "select  * from  ACCOUNT where CUSTOMER_ID =" + customer_id ;

		        //creating PreparedStatement object to execute query
		        PreparedStatement preStatement = conn.prepareStatement(sql);
		    
		        ResultSet result = preStatement.executeQuery();
		        
		        return result;
		      
		}
	
	//SEARCH FOR AN CUSTOMER IN ORACLE
		public ResultSet searchAccountHolder() throws SQLException{
			String url = "jdbc:oracle:thin:system/password@localhost"; 
		      
	        //properties for creating connection to Oracle database
	        Properties props = new Properties();
	        props.setProperty("user", "TESTUSERDB");
	        props.setProperty("password", "password");
	      
	        //creating connection to Oracle database using JDBC
	         conn = DriverManager.getConnection(url,props);
			 String sql = "select  * from  ACCOUNTHOLDER where ID = '" + id +"'";

		        //creating PreparedStatement object to execute query
		        PreparedStatement preStatement = conn.prepareStatement(sql);
		    
		        ResultSet result = preStatement.executeQuery();
		        
		        return result;
		      
		}
	//ADD NEW CUSOTMER TO DATABASE
		public boolean addNewAccountHolder() throws SQLException {
			boolean isAdded = false;
			String url = "jdbc:oracle:thin:system/password@localhost";

			// properties for creating connection to Oracle database
			Properties props = new Properties();
			props.setProperty("user", "TESTUSERDB");
			props.setProperty("password", "password");

			// creating connection to Oracle database using JDBC
			Connection conn = DriverManager.getConnection(url, props);

			String sqlIdentifier = "SELECT SEQ_ACCOUNTHOLDER.NEXTVAL FROM DUAL";
			PreparedStatement preStatement = conn.prepareStatement(sqlIdentifier);
			ResultSet result = preStatement.executeQuery();

			if (result.next()) {
				int customerID = result.getInt("NEXTVAL");

				String sql = "INSERT INTO ACCOUNTHOLDER (CUSTOMER_ID,CUSTOMER_NAME,CUSTOMER_BIRTH,ID) VALUES ("
						+ customerID
						+ ",'"
						+ customer_name
						+ "', '"
						+ customer_birth
						+"', '"
						+ id 
						+ "')"; // 1 for open status
				preStatement = conn.prepareStatement(sql);

				result = preStatement.executeQuery();
				isAdded = true;

			}
			
			return isAdded;
		}
		
		
}

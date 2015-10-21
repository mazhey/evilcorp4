import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Properties;

public class Transcation {
	private int type;
	private double amount;
	private String date;

	public Transcation(int type, double amount, String date) {
		this.setType(type);
		this.setAmount(amount);
		this.setDate(date);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFormattedPrice() {
		NumberFormat currency = NumberFormat.getCurrencyInstance();
		return currency.format(amount);
	}

	public String history() {
		String str = "";
		str += "Transcation type: " + type + "\n";
		str += "Transaction amount: " + getFormattedPrice() + "\n";
		str += "Transaction date: " + date;
		return str;

	}

	public boolean addNewTransaction(int accountID) throws SQLException {
		boolean isAdded = false;
		String url = "jdbc:oracle:thin:system/password@localhost";

		// properties for creating connection to Oracle database
		Properties props = new Properties();
		props.setProperty("user", "TESTUSERDB");
		props.setProperty("password", "password");

		// creating connection to Oracle database using JDBC
		Connection conn = DriverManager.getConnection(url, props);

		String sqlIdentifier = "SELECT SEQ_TRANSACTION.NEXTVAL FROM DUAL";
		PreparedStatement preStatement = conn.prepareStatement(sqlIdentifier);
		ResultSet result = preStatement.executeQuery();

		if (result.next()) {
			int transactionID = result.getInt("NEXTVAL");

			String sql = "INSERT INTO TRANSACTION (TRANSACTION_ID, TYPE, ACCOUNT_ID, AMOUNT, TRANSACTION_DATE) VALUES ("
					+ transactionID
					+ ","
					+ this.type
					+ ","
					+ accountID
					+ ","
					+ this.amount + ",'" + this.date + "')";
			preStatement = conn.prepareStatement(sql);

			result = preStatement.executeQuery();
			isAdded = true;

		}
		conn.close();
		return isAdded;
	}
}



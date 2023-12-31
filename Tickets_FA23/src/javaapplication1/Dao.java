package javaapplication1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dao {
	// instance fields
	static Connection connect = null;
	Statement statement = null;

	// constructor
	public Dao() {
	  
	}

	public Connection getConnection() {
		// Setup the connection with the DB
		try {
			connect = DriverManager
					.getConnection("jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false"
							+ "&user=fp411&password=411");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connect;
	}

	// CRUD implementation

	public void createTables() {
		// variables for SQL Query table creations
		final String createTicketsTable = "CREATE TABLE bwardhana_tickets(ticket_id INT AUTO_INCREMENT PRIMARY KEY, ticket_issuer VARCHAR(30), ticket_description VARCHAR(200), ticket_status VARCHAR(8))";
		final String createUsersTable = "CREATE TABLE bwardhana_users(uid INT AUTO_INCREMENT PRIMARY KEY, uname VARCHAR(30), upass VARCHAR(30), admin int)";

		try {

			// execute queries to create tables

			statement = getConnection().createStatement();

			statement.executeUpdate(createTicketsTable);
			statement.executeUpdate(createUsersTable);
			System.out.println("Created tables in given database...");

			// end create table
			// close connection/statement object
			statement.close();
			connect.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// add users to user table
		addUsers();
	}

	public void addUsers() {
		// add list of users from userlist.csv file to users table

		// variables for SQL Query inserts
		String sql;

		Statement statement;
		BufferedReader br;
		List<List<String>> array = new ArrayList<>(); // list to hold (rows & cols)

		// read data from file
		try {
			br = new BufferedReader(new FileReader(new File("./userlist.csv")));

			String line;
			while ((line = br.readLine()) != null) {
				array.add(Arrays.asList(line.split(",")));
			}
		} catch (Exception e) {
			System.out.println("There was a problem loading the file");
		}

		try {

			// Setup the connection with the DB

			statement = getConnection().createStatement();

			// create loop to grab each array index containing a list of values
			// and PASS (insert) that data into your User table
			for (List<String> rowData : array) {

				sql = "insert into bwardhana_users(uname,upass,admin) " + "values('" + rowData.get(0) + "'," + " '"
						+ rowData.get(1) + "','" + rowData.get(2) + "');";
				statement.executeUpdate(sql);
			}
			System.out.println("Inserts completed in the given database...");

			// close statement object
			statement.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public int insertRecords(String ticketName, String ticketDesc) {
		int id = 0;
		try {
			statement = getConnection().createStatement();
			statement.executeUpdate("Insert into bwardhana_tickets" + "(ticket_issuer, ticket_description, ticket_status) values(" + " '"
					+ ticketName + "','" + ticketDesc + "', 'OPEN')", Statement.RETURN_GENERATED_KEYS);

			// retrieve ticket id number newly auto generated upon record insertion
			ResultSet resultSet = null;
			resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				// retrieve first field in table
				id = resultSet.getInt(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;

	}

	public ResultSet readRecords() {

		ResultSet results = null;
		try {
			statement = connect.createStatement();
			results = statement.executeQuery("SELECT * FROM bwardhana_tickets");
			//connect.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return results;
	}

    // Find function based on ticket id
    public ResultSet findRecords(String id) {

		ResultSet results = null;
		try {
			statement = connect.createStatement();
			results = statement.executeQuery("SELECT * FROM bwardhana_tickets WHERE ticket_id=" + id );
			//connect.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return results;
	}

    // Find function based on ticket id
    public ResultSet userTicket(String uname) {

		ResultSet results = null;
		try {
			statement = connect.createStatement();
			results = statement.executeQuery("SELECT * FROM bwardhana_tickets WHERE ticket_issuer='" + uname + "'");
			//connect.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return results;
	}

	// continue coding for updateRecords implementation
    public void updateRecords(String ticketID, String ticketName, String ticketDesc) throws SQLException {
        // SQL statement to update a ticket
        statement = getConnection().createStatement();
        String updateStatement = "UPDATE bwardhana_tickets SET ticket_issuer='" + ticketName + "', " + "ticket_description='" + ticketDesc + "' WHERE ticket_id=" + ticketID;
        statement.executeUpdate(updateStatement);

        // Print success message
        System.out.println("Ticked has been updated successfully.");
    } 

	// continue coding for deleteRecords implementation
    public void deleteRecords(String ticketID) throws SQLException {
        // SQL statement to delete a ticket based on the id
        statement = getConnection().createStatement();
        String deleteStatement = "DELETE FROM bwardhana_tickets WHERE ticket_id=" + ticketID;
        statement.executeUpdate(deleteStatement);

        // Print success message
        System.out.println("Ticked has been deleted successfully.");
    }

    public void closeTicket(String ticketID) throws SQLException {
        // SQL statement to change the status of a ticket (close)
        statement = getConnection().createStatement();
        String updateStatement = "UPDATE bwardhana_tickets SET ticket_status='CLOSE' WHERE ticket_id=" + ticketID;
        statement.executeUpdate(updateStatement);
    }
    /*

    public void openTicket(String ticketID) throws SQLException {
        // SQL statement to change the status of a ticket (open)
        statement = getConnection().createStatement();
        String updateStatement = "UPDATE bwardhana_tickets SET ticket_status='OPEN' WHERE ticket_id=" + ticketID;
        statement.executeUpdate(updateStatement);
    }

    */


}

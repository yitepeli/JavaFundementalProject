/**
 * 
 */
package fr.epita.iam.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.exceptions.DaoInitializationException;
import fr.epita.iam.launcher.ConsoleLauncher;

/**
 * @author tbrou
 *
 */
public class IdentityJDBCDAO {

	

	private static final Logger logger = Logger.getLogger(ConsoleLauncher.class.getName());
	private Connection currentConnection;

	/**
	 * 
	 */
	public IdentityJDBCDAO() throws DaoInitializationException {
			
		try {
			getConnection("TBR");
		} catch (SQLException e) {
			DaoInitializationException die = new DaoInitializationException();
			die.initCause(e);
			throw die;
		}
	}

	/**
	 * @throws SQLException
	 */
	private Connection getConnection(String pass) throws SQLException {
		try {
			this.currentConnection.getSchema();
		} catch (Exception e) {
			logger.log(Level.FINE, "Creating", e);
			String user = "TBR";
			String connectionString = "jdbc:derby://localhost:1527/sample;create=true";
			this.currentConnection = DriverManager.getConnection(connectionString, user, pass);
		}
		return this.currentConnection;
	}

	
	private void releaseResources() {
		try {
			this.currentConnection.close();
		} catch (Exception e) {
			logger.log(Level.WARNING, "Warning", e);
		}
	}

	/**
	 * Read all the identities from the database
	 * @return
	 * @throws SQLException
	 */
	public List<Identity> readAllIdentities() throws SQLException {
		List<Identity> identities = new ArrayList();

		Connection connection = getConnection("TBR");

		PreparedStatement statement = connection.prepareStatement("select * from IDENTITIES");
		ResultSet rs = statement.executeQuery();

		while (rs.next()) {
			int uid = rs.getInt("IDENTITY_ID");
			String displayName = rs.getString("IDENTITY_DISPLAYNAME");
			String email = rs.getString("IDENTITY_EMAIL");
			Identity identity = new Identity(String.valueOf(uid), displayName, email, "***");
			identities.add(identity);
		}
		statement.close();
		return identities;
	}
	
	public boolean isAdmin(String login, String pass) throws SQLException {
		Connection connection = getConnection("TBR");

		PreparedStatement statement = connection.prepareStatement("select * from IDENTITIES WHERE IDENTITY_DISPLAYNAME LIKE ? AND IDENTITY_PASSWORD LIKE ?");
		statement.setString(1, login);
		statement.setString(2, pass);
		ResultSet rs = statement.executeQuery();

		if (rs.next()) {
			statement.close();
			return true;
		}
		else {
			statement.close();
			return false;
		}
	}

	/**
	 * write an identity in the database
	 * @param identity
	 * @throws SQLException
	 */
	public void write(Identity identity) throws SQLException {
		Connection connection = getConnection("TBR");

		String sqlInstruction = "INSERT INTO IDENTITIES(IDENTITY_DISPLAYNAME, IDENTITY_EMAIL, IDENTITY_BIRTHDATE, IDENTITY_PASSWORD) VALUES(?,?,?,?)";
		PreparedStatement pstmt = connection.prepareStatement(sqlInstruction);
		pstmt.setString(1, identity.getDisplayName());
		pstmt.setString(2, identity.getEmail());
		// TODO implement date for identity
		pstmt.setString(3, null);
		pstmt.setString(4, identity.getPass());

		pstmt.execute();
		pstmt.close();
	}
	
	public void delete(String email) throws SQLException {
		Connection connection = getConnection("TBR");

		String sqlInstruction = "DELETE FROM IDENTITIES WHERE IDENTITY_EMAIL LIKE ?";
		PreparedStatement pstmt = connection.prepareStatement(sqlInstruction);
		pstmt.setString(1, email);

		pstmt.execute();
		pstmt.close();
	}
	
	public void update(String email, Identity identity) throws SQLException {
		Connection connection = getConnection("TBR");

		String sqlInstruction = "UPDATE IDENTITIES SET IDENTITY_DISPLAYNAME=?, IDENTITY_EMAIL=? WHERE IDENTITY_EMAIL LIKE ?";
		PreparedStatement pstmt = connection.prepareStatement(sqlInstruction);
		pstmt.setString(1, identity.getDisplayName());
		pstmt.setString(2, identity.getEmail());
		pstmt.setString(3, email);

		pstmt.execute();
		pstmt.close();
	}

}

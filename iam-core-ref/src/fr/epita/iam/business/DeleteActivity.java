/**
 * 
 */
package fr.epita.iam.business;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.epita.iam.launcher.ConsoleLauncher;
import fr.epita.iam.services.IdentityJDBCDAO;

/**
 * @author tbrou
 *
 */
public class DeleteActivity {
	
	private static final Logger logger = Logger.getLogger(ConsoleLauncher.class.getName());
	
	private DeleteActivity(){}

	
	
	public static void execute(Scanner scanner){
		System.out.println("Identity Deletion");
		System.out.println("please enter the mail address of user who you want to delete");
		String displayEmail = scanner.nextLine();
		
		IdentityJDBCDAO dao = new IdentityJDBCDAO();
		try {
			dao.delete(displayEmail);
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Warning", e);
		}
		
		System.out.println("deletion Done");
		
	}
	

}

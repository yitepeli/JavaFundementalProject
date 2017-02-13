/**
 * 
 */
package fr.epita.iam.business;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.launcher.ConsoleLauncher;
import fr.epita.iam.services.IdentityJDBCDAO;

/**
 * @author tbrou
 *
 */
public class UpdateActivity {
	
	private static final Logger logger = Logger.getLogger(ConsoleLauncher.class.getName());
	
	private UpdateActivity(){}
	
	public static void execute(Scanner scanner){
		System.out.println("Identity Update");
		System.out.println("please enter the mail of the user which you want to update:");
		String displayEmail = scanner.nextLine();
		
		
		System.out.println("please input the name");
		String name = scanner.nextLine();
		System.out.println("please input the email address");
		String email = scanner.nextLine();
		System.out.println("please input the passwrod (Leave blank for non-admins)");
		String pass = scanner.nextLine();
		Identity identity = new Identity("",name, email, pass);
		
		IdentityJDBCDAO dao = new IdentityJDBCDAO();
		try {
			dao.update(displayEmail, identity);
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Warning", e);
		}
		
		System.out.println("update Done");
		
	}
	

}

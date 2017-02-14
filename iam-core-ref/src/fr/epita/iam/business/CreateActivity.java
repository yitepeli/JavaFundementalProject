/**
 * 
 */
package fr.epita.iam.business;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.*;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.launcher.ConsoleLauncher;
import fr.epita.iam.services.FileIdentityDAO;
import fr.epita.iam.services.IdentityJDBCDAO;

/**
 * @author tbrou
 *
 */
public class CreateActivity {
	

	private static final Logger logger = Logger.getLogger(ConsoleLauncher.class.getName());
	
	private CreateActivity(){}

	
	
	public static void execute(Scanner scanner){

		System.out.println("Identity Creation");
		String isAdmin;
		String pass="";
		do{
			System.out.println("Do you want to create Identity as Admin? (y/n)");
			isAdmin = scanner.nextLine();
		}
		while(!(isAdmin.equals("y")) && !(isAdmin.equals("n")));
		System.out.println("please input the displayName");
		String displayName = scanner.nextLine();
		System.out.println("please input the email address");
		String email = scanner.nextLine();
		if(isAdmin.equals("y")){
			System.out.println("please input the password");
			pass = scanner.nextLine();
		}
		Identity identity = new Identity("",displayName, email, pass);
		
		FileIdentityDAO identityWriter = new FileIdentityDAO("tests.txt");
		identityWriter.write(identity);
		IdentityJDBCDAO dao = new IdentityJDBCDAO();
		try {
			dao.write(identity);
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Warning", e);		
		}
		
		System.out.println("creation Done");
		
	}
	

}

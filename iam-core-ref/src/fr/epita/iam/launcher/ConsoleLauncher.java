/**
 * 
 */
package fr.epita.iam.launcher;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.*;

import fr.epita.iam.business.*;
import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.services.IdentityJDBCDAO;

/**
 * @author tbrou
 *
 */
public class ConsoleLauncher {

	/**
	 * @param args
	 */
	private static final Logger logger = Logger.getLogger(ConsoleLauncher.class.getName());
	
	
	private ConsoleLauncher(){}
	
	public static void main(String[] args) {
		System.out.println("Welcome to the IAM software");
		Scanner scanner = new Scanner(System.in);
		
		//authentication
		if (!authenticate(scanner)){
			System.out.println("Athentication was not successful");
			end(scanner);
			return;
		}
		String choice;
		do{
		//menu
		System.out.println("Please select an action :");
		System.out.println("a. create an Identity");
		System.out.println("b. modify an Identity");
		System.out.println("c. delete an Identity");
		System.out.println("d. quit");
		choice = scanner.nextLine();
		switch (choice) {
		case "a":
			//Create
			CreateActivity.execute(scanner);
			break;
			
		case "b":
			UpdateActivity.execute(scanner);
			break;
			
		case "c":
			DeleteActivity.execute(scanner);
			break;
			
		case "d":
			//Quit
			IdentityJDBCDAO dao = new IdentityJDBCDAO();
			try {
				for(Identity i:dao.readAllIdentities()){
					System.out.println(i);
				}
			} catch (SQLException e) {
				logger.log(Level.WARNING, "Warning", e);
			}
			end(scanner);
			break;
			
		default:
			System.out.println("Your choice is not recognized");
			break;
		}

		}
		while(!"d".equals(choice));
		
	}

	/**
	 * @param scanner
	 */
	private static void end(Scanner scanner) {
		System.out.println("Thanks for using this application, good bye!");
		scanner.close();
	}

	/**
	 * @param scanner
	 */
	private static boolean authenticate(Scanner scanner) {
		System.out.println("Please type your login : ");
		String login = scanner.nextLine();
		
		System.out.println("Please type your password : ");
		String password = scanner.nextLine();
		
		IdentityJDBCDAO dao = new IdentityJDBCDAO();
		if ("adm".equals(login) && "pwd".equals(password)){
			System.out.println("Athentication was successful");
			return true;
		} else{
			try {
				return dao.isAdmin(login, password);
			} catch (SQLException e) {
				logger.log(Level.WARNING, "Warning", e);	
				return false;
			}
		}
	}

}

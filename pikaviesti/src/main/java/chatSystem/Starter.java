package chatSystem;


/* 
 * TODO list for GUI:
 * _UML
 * _Tests
 */

/* TODO list for back-end:
 * endSession marche pas
 * vérifier envoyer image puis message
 * Ne pas créer d'erreur pour fermer
 * remoteSessionStarted marche pas
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Starter {

	public static Connection con = null; // We test if we can connect to the database from the get go

	public static void main(String[] args) {

		try {
			con = DriverManager.getConnection("jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/tp_servlet_005",
					"tp_servlet_005", "aoh8Naij");
			System.out.println("Success");
		} catch (SQLException e) {
			e.printStackTrace();
			ChatSystemGUI.showPopup("Impossible d'atteindre la base de données");
			System.exit(-1);
		}


		ChatSystemGUI GUI = new ChatSystemGUI(con); //A déplacer au dessus
		// Launch GUI
		GUI.openLoginWindow();
	}
}

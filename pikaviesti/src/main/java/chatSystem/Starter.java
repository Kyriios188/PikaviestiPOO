package chatSystem;

//function

/* 
 * TODO list for GUI:
 * _Quand ya un utilisateur distant qui initie une conenction j'appel "remoteSessionStarted" dans ChatSystemGUI, faut que ça enlève le nom envoyé de USerList pour qu'on puisse
 * pas commencer la même session deux fois. ça devrait aussi ouvrir une fenêtre pour envoyer des messages
 * _Si on ouvre plusieurs sessions yaura le bouton username pour chaque session (et user list), je pense qu'il faudrait une fenêtre d'où on établie la connection et on change de
 * nom et ensuite une fenêtre pas ession de chat.
 *
 */

/* TODO list for back-end:
 * _faire fonctionner closeApp
 * _tester messages TCP
 * _parcourir les messages avant de les envoyer pour pas qu'ils pêtent le système en arrivant
 * _BDD pour comptes utilisateurs et historique des messages
 *
 *
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
		}



		ChatSystemGUI GUI = new ChatSystemGUI(con);
		// Launch GUI
		GUI.openLoginWindow();
	}
}

package chatSystem;


/* 
 * TODO list for GUI:
 * _Quand ya un utilisateur distant qui initie une connection j'appel "remoteSessionStarted" dans ChatSystemGUI, faut que ça enlève le nom envoyé de USerList pour qu'on puisse
 * pas commencer la même session deux fois. ça devrait aussi ouvrir une fenêtre pour envoyer des messages
 * _Si on ouvre plusieurs sessions yaura le bouton username pour chaque session (et user list), je pense qu'il faudrait une fenêtre d'où on établie la connection et on change de
 * nom et ensuite une fenêtre pas session de chat.
 * _Permettre de finir une session
 * _Implémenter remoteSessionStarted (c'est quand qq distant lance la session, il faut qu'une fenêtre s'affiche pour montrer qu'une nouvelle session a commencé)
 * _Alignement messages selon l'envoyeur/l'envoyé (pour bien voir qui a envoyé quoi)
 * _Utiliser controller.getChatHistory, controller.getNameFromId et message.getFormattedTime pour afficher l'historique des messages
 * _Supprimer user quand fermer fenêtre
 *
 *
 */

/* TODO list for back-end:
 * _faire fonctionner closeApp
 * _parcourir les messages avant de les envoyer pour pas qu'ils pêtent le système en arrivant
 * _failure log dans target
 * _click on user should only startSession once, after it only shows the history -> menfou ?
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

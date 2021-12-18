package chatSystem;


/* 
 * TODO list for GUI:
 * _Quand ya un utilisateur distant qui initie une connection j'appel "remoteSessionStarted" dans ChatSystemGUI, faut que ça enlève le nom envoyé de USerList pour qu'on puisse
 * pas commencer la même session deux fois. ça devrait aussi ouvrir une fenêtre pour envoyer des messages
 * _Si on ouvre plusieurs sessions yaura le bouton username pour chaque session (et user list), je pense qu'il faudrait une fenêtre d'où on établie la connection et on change de
 * nom et ensuite une fenêtre pas session de chat.
 * _Permettre de finir une session
 * _Faut pas qu'on voit un message d'un mec qui est pas dans ta session, faut séparer les sessions
 * _Implémenter remoteSessionStarted (c'est quand qq distant lance la session, il faut qu'une fenêtre s'affiche pour montrer qu'une nouvelle session a commencé)
 *
 * _Quand je rechange le nom pour remettre le premier ça me met qu'il est déjà pris
 * _Problème quand je me connecte avec 3 personnes "java.net.BindException: Address already in use: bind"
 *
 */

/* TODO list for back-end:
 * _failure log dans target
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

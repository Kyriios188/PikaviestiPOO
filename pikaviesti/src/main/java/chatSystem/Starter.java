package chatSystem;


/* 
 * TODO list for GUI:
 * _Permettre de finir une session
 * _Faut pas qu'on voit un message d'un mec qui est pas dans ta session, faut séparer les sessions
 * _Implémenter remoteSessionStarted (c'est quand qq distant lance la session, il faut avertir l'utilisateur
 * _Implémenter GUI.receiveImage
 *
 * _Quand je rechange le nom pour remettre le premier ça me met qu'il est déjà pris <-- réglé
 *
 */

/* TODO list for back-end:
 * _failure log dans target
 * _les images ne sont pas stockées dans la bdd donc si on est pas en train de sélectionner le mec qui l'envoie, l'image se perd.
 * -> on ne charge pas l'image quand on recommence une session -> elles sont perdues
 * -> du coup pour l'instant je les affiche à chaque fois
 * --> stocker dans le dossier et afficher ?
 * _images trop grosses meurent
 * _les images trop grosses s'envoient en plusieurs fois ce qui est pas géré pour l'instant
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

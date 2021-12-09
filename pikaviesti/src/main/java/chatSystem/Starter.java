package chatSystem;

//function

/* 
 * TODO list for GUI:
 * _j'ai besoin d'une méthode pour que quand je trouve un nouvel utilisateur, je puisse dire de l'ajouter à la liste des utilisateurs qui sont montrés dans UserList
 * (c'est la méthode addUser de UserList.java) - OK
 * _Ce serait mieux si au lieu d'un bouton "change username" yai un bouton avec le nom actuel de l'utilisateur. Et que ça ressemble pas à un bouton (c'est intuitif de cliquer sur
 * son nom pour le changer, comme discord)
 * _Quand ya un utilisateur distant qui initie une conenction j'appel "remoteSessionStarted" dans ChatSystemGUI, faut que ça enlève le nom envoyé de USerList pour qu'on puisse
 * pas commencer la même session deux fois. ça devrait aussi ouvrir une fenêtre pour envoyer des messages
 * _Si on ouvre plusieurs sessions yaura le bouton username pour chaque session (et user list), je pense qu'il faudrait une fenêtre d'où on établie la connection et on change de
 * nom et ensuite une fenêtre pas ession de chat.
 * _Quand tu veux envoyer un message, appelle sendChatMessage du controller (je garantit pas que ça marche mais elle existe) - OK
 * _Utiliser ça : getStrUserList pour mettre à jour la liste la première fois
 *
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

public class Starter {
	public static void main(String[] args) {
		ChatSystemGUI GUI = new ChatSystemGUI();
		// Launch GUI
		GUI.openLoginWindow();
	}
}

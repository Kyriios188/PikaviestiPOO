package chatSystem;

import java.util.Random;

/* 
 * TODO list for GUI:
 * _changeDistantUsername has to be public (controller calls it) - OK
 * _has to call ChatSystemController.closeApp() when we quit the application (to stop servers listening on ports) - Je veux bien mais closeApp() n'a pas l'air de fonctionner
 * _needs method to add user to user list - Pas sûr d'avoir compris celle-la
 */


public class Starter {
	public static void main(String[] args) {
		ChatSystemGUI GUI = new ChatSystemGUI();
		// Launch GUI
		GUI.openLoginWindow();
	}
}

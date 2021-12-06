package chatSystem;

import java.util.Random;

/* 
 * TODO list for GUI:
 * _changeDistantUsername has to be public (controller calls it)
 * _has to call ChatSystemController.closeApp() when we quit the application (to stop servers listening on ports)
 * _needs method to add user to user list
 */


public class Starter {
	public static void main(String[] args) {
		//TODO: uncomment for production
		ChatSystemGUI GUI = new ChatSystemGUI();
		// Launch GUI
		GUI.openLoginWindow();
		
		// GUI needs to listen the CSModel to enact changes
		// GUI does not have access to the CSModel, so controller has to call the GUI manually
		//ChatSystemController controller = new ChatSystemController(GUI);
		//int rand = new Random().nextInt(1000);
		//String random_name = Integer.toString(rand);
		//controller.setLocalUser("jean"); //TODO: remove and uncomment checkNameUnique
		//controller.checkNameUnique(random_name);
	}
}

package chatSystem;

import java.util.Random;

import chatSystem.ChatSystemGUI;

public class Starter {
	public static void main(String args[]) {
		//TODO: uncomment for production
		ChatSystemGUI GUI = new ChatSystemGUI();
		// Launch GUI
		GUI.openLoginWindow();
		
		/*
		ChatSystemController controller = new ChatSystemController(GUI);
		int rand = new Random().nextInt(1000);
		String random_name = Integer.toString(rand);
		controller.setLocalUser(random_name);
		controller.checkNameUnique(random_name);
		*/
	}
}

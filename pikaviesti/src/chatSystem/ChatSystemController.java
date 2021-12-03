package chatSystem;// attributes random id to user
import communication.CommunicationSystem;
import objects.User;

import java.util.Random; //TODO: remove once login is implemented



public class ChatSystemController {

    private User local_user;


    private ChatSystemGUI gui;


    private CommunicationSystem com_sys;


    private ChatSystemModel cs_model;
    
    //TODO: Remove the ChatSystemGUI?
    public ChatSystemController(ChatSystemGUI gui) {
    	this.gui = gui;
    	this.cs_model = new ChatSystemModel();
    }

    //public chat_history getChatHistory(user target_user) {
    //}
    
    
    // We instantiate the CommunicationSystem when we check if the name is unique
    public boolean checkNameUnique(String name) {

    	this.com_sys = new CommunicationSystem(this);
    	this.com_sys.whatsYourNameBroadcast(name);
    	
    	//setLocalUser(name);
    	return true;
    }


    //public void notifyNameChange(user new_user) {
    //}


    //public void stampMessage(message message) {
    //}


    //public void updateGUI(message new_message) {
    //}


    //public void updateChatHistory(message new_message) {
    //}


    // Updates the CSModel by either changing the name or adding a user
    public void updateCSModel(User user) {
    	if (this.cs_model.checkUserExistence(user)) {
    		try {
				this.cs_model.changeUserName(user);
			} catch (Exception e) {
				e.printStackTrace(); // Cannot happen
			}
    	}
    	else {
    		this.cs_model.addUser(user);
    	}
    }
    
    public User getLocalUser() {
    	return this.local_user;
    }
    
    // Gives the local user a unique id (unless we're really unlucky)
    public void setLocalUser(String local_name) {
    	int id = new Random().nextInt(1000);
    	System.out.println(local_name + " : " + id);
    	this.local_user = new User(local_name, id);
    }


    public void showUserList() {
    }

    // If it exists, it uses message_code too
    //public void createMessage(String input, String username) {
    //}

}
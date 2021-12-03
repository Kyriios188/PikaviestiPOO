package chatSystem;// attributes random id to user
import communication.CommunicationSystem;
import objects.User;

import java.util.Random; //TODO: remove once login is implemented



public class ChatSystemController {

    private User local_user;


    private ChatSystemGUI gui;


    private CommunicationSystem com_sys;


    private ChatSystemModel cs_model;

    public ChatSystemController(ChatSystemGUI gui) {
    	this.gui = gui;
    	this.cs_model = new ChatSystemModel();
    }

    //public chat_history getChatHistory(user target_user) {
    //}

    public boolean checkNameUnique(String name) {

    	this.com_sys = new CommunicationSystem(this);
    	//TODO: create message -> constructor or custom method?
    	this.com_sys.UDPMessage("TODO", null);
    	
    	setLocalUser(name);
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


    // Updates the CSModel
    // We test if the user exists, literally cannot throw an exception :)
    public void updateCSModel(User user) throws Exception {
    	if (this.cs_model.checkUserExistence(user)) {
    		this.cs_model.changeUserName(user);
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
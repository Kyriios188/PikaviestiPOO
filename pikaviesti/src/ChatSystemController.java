// attributes random id to user
import java.util.Random; //TODO: remove once login is implemented



public class ChatSystemController {

    private User local_user;


    private ChatSystemGUI gui;


    private CommunicationSystem com_sys;


    private ChatSystemModel cs_model;

    public ChatSystemController(ChatSystemGUI gui) {
    	this.gui = gui;
    }

    //public chat_history getChatHistory(user target_user) {
    //}


    public boolean checkNameUnique(String name) {

    	this.com_sys = new CommunicationSystem(this);
    	//TODO: create message -> constructor or custom method?
    	this.com_sys.UDPBroadcast("TODO");
    	
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


    //public user findUser(String name) {
    //}


    //public void receiveNotification(user user) {
    //}
    
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
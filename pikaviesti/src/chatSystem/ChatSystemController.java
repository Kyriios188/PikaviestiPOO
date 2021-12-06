package chatSystem;// attributes random id to user
import communication.CommunicationSystem;
import objects.Message;
import objects.User;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;



public class ChatSystemController {

    private User local_user;


    private ChatSystemGUI gui;

    private CommunicationSystem com_sys;


    private ChatSystemModel cs_model;
    
    // We need the GUI here so when a distant user changes we can tell the GUI
    public ChatSystemController(ChatSystemGUI gui) {
    	this.gui = gui;
    	this.cs_model = new ChatSystemModel();
    }

    //public chat_history getChatHistory(user target_user) {
    //}
    
    // Called by GUI to see the active users
    // GUI only needs the string objects
    public ArrayList<String> getStrUserList() {
    	ArrayList<User> user_list = this.cs_model.getUserList();
    	ArrayList<String> str_user_list = new ArrayList<>();
    	for (int i=0; i<user_list.size(); i++) {
    		str_user_list.add(user_list.get(i).getName());
    	}
    	return str_user_list;
    }
    
    // Starts a session and gives the corresponding socket
    // GUI shouldn't call it if the connection is already on
    public void startSession(String target_username) {
    	InetAddress host_addr = this.cs_model.getAddressFromName(target_username);
    	Socket sock = null;
    	try {
    		// Connect to foreign host
			sock = this.com_sys.TCPConnect(host_addr);
			// Store the socket created to send messages later on
			this.com_sys.addSenderSocket(this.cs_model.getIdFromName(target_username), sock);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void sendChatMessage(String target_username, String content) {
    	try {
			int target_id = this.cs_model.getIdFromName(target_username);
			Message m = new Message(this.local_user.getId(), target_id, 0, content); //TODO: Parse the content for safety
			this.com_sys.sendChatMessage(m);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    // We instantiate the CommunicationSystem when we check if the name is unique
    public boolean checkNameUnique(String name) {

    	this.com_sys = new CommunicationSystem(this);
    	this.com_sys.whatsYourNameBroadcast();
    	
    	try {
			Thread.sleep(500); // Wait for the UDP answers to come
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	this.com_sys.closePorts();
    	// If you find the name in this list, your name isn't unique
    	if (this.cs_model.checkNameExistence(name)) {
    		return false;
    	}
    	
    	// Your name is unique, notify users of name change
    	else {
    		//setLocalUser(name);
    		this.com_sys.nameChangeNotificationBroadcast(name);
        	return true;
    	}
    	
    }
    
    public void updateModelAddressTable(int user_id, InetAddress addr) {
    	this.cs_model.updateAddressTable(user_id, addr);
    }
    
    public void closeApp() {
    	this.com_sys.closePorts();
    }


    //public void updateGUI(message new_message) {
    //}


    //public void updateChatHistory(message new_message) {
    //}


    // Updates the CSModel by either changing the name or adding a user
    public void updateCSModel(User new_user) {
    	if (this.cs_model.checkUserExistence(new_user)) {
    		
        	String new_name = new_user.getName();
    		
    		try {
    			// the new_user kept their id when changing their name
    			String old_name = this.cs_model.getNameFromId(new_user.getId());
				this.cs_model.changeUserName(new_user);
			} catch (Exception e) {
				e.printStackTrace(); // Cannot happen
			}
    		//TODO: uncomment
    		//this.gui.changeDistantUsername(old_name, new_name);
    	}
    	else {
    		this.cs_model.addUser(new_user);
    		//TODO: call GUI method to add user to user list
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
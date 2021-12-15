package chatSystem;// attributes random id to user
import communication.CommunicationSystem;
import objects.Message;
import objects.User;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
import java.time.format.DateTimeFormatter;



public class ChatSystemController {

    private final User local_user;


    private final ChatSystemGUI GUI;

    private CommunicationSystem com_sys;

	// We can't answer to some messages until the local_user is set
	private boolean local_user_defined;


    private final ChatSystemModel cs_model;
    
    // We need the GUI here so when a distant user changes we can tell the GUI
    public ChatSystemController(ChatSystemGUI gui) {
    	this.GUI = gui;
    	this.cs_model = new ChatSystemModel();
		this.local_user = new User();
		this.local_user_defined = false;

	}

    //public chat_history getChatHistory(user target_user) {
    //}
    
    // Called by GUI to see the active users
    // GUI only needs the string objects
    public ArrayList<String> getStrUserList() {
    	ArrayList<User> user_list = this.cs_model.getUserList();
    	ArrayList<String> str_user_list = new ArrayList<>();
		for (User user : user_list) {
			str_user_list.add(user.getName());
		}
    	return str_user_list;
    }
    
    // Starts a session and gives the corresponding socket
    // GUI shouldn't call it if the connection is already on
    public void startSessionFromLocal(String target_username) {
    	InetAddress host_addr = this.cs_model.getAddressFromName(target_username);
    	Socket sock = null;
    	try {
    		// Connect to foreign host
			sock = this.com_sys.TCPConnect(host_addr);
			// Store the socket created to send messages later on
			this.com_sys.addSenderSocket(this.cs_model.getIdFromName(target_username), sock);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int startSessionFromRemote(InetAddress address) {
		if (this.cs_model.checkAddressExistence(address)) {
			try {
				int id = this.cs_model.getIdFromAddress(address);
				this.GUI.remoteSessionStarted(this.cs_model.getNameFromId(id));
				return id;
			} catch (Exception e) {/* cannot happen */}
		}
		return -1;
	}

	public void endSession(String target_username) {
		try {
			int target_id = this.cs_model.getIdFromName(target_username);
			this.com_sys.endTCPSession(this.com_sys.getSocketFromId(target_id));
		}
		catch (Exception e) {
			System.out.println(e);
			System.out.println("Failure in endSession.");
		}
	}
    
    public void sendChatMessage(String target_username, String content) {
    	try {
			int target_id = this.cs_model.getIdFromName(target_username);
			Message m = new Message(this.local_user.getId(), target_id, 0, content); //TODO: Parse the content for safety
			this.com_sys.sendChatMessage(m);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failure in sendMessage.");
		}
    }

	public void initCommunicationSystem(int id) {
		this.com_sys = new CommunicationSystem(this, id);
	}

	public void enableTCPMessaging() {
		this.com_sys.startTCPServer();
	}

    
    // We instantiate the CommunicationSystem when we check if the name is unique
    public boolean checkNameUnique(String name) {

    	this.com_sys.whatsYourNameBroadcast();

    	try {
			Thread.sleep(500); // Wait for the UDP answers to come
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

    	// If you find the name in this list, your name isn't unique
    	if (this.cs_model.checkNameExistence(name)) {
			System.out.println("Username isn't unique");
    		return false;
    	}
    	
    	// Your name is unique, notify users of name change
    	else {
			System.out.println("Username is valid");
    		this.setLocalUsername(name);
			this.local_user_defined = true;
    		this.com_sys.nameChangeNotificationBroadcast(name);
        	return true;
    	}
    	
    }
    
    public void updateModelAddressTable(int user_id, InetAddress addr) {
    	this.cs_model.updateAddressTable(user_id, addr);
    }

	// TODO change into close before name change and after name change
	// TODO send disconnect broadcast
    public void closeApp() {
		this.com_sys.closeTCPServer();
		this.com_sys.closeUDPServer();
    }


    public void updateGUI(Message received_message) {
		try {
			String source_name = this.cs_model.getNameFromId(received_message.getSrcId());
			String content = received_message.getContent();
			LocalTime time = received_message.getTimeStamp();
			DateTimeFormatter formatter = DateTimeFormatter.ISO_TIME;
			String formatted_time = time.format(formatter);
			this.GUI.updateGUIMessageReceived(content, source_name, formatted_time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


    // Updates the CSModel by either changing the name or adding a user
    public void updateCSModel(User new_user, boolean update_gui) {
    	if (this.cs_model.checkUserExistence(new_user)) {
    		
        	String new_name = new_user.getName();
    		
    		try {
    			// the new_user kept their id when changing their name
    			String old_name = this.cs_model.getNameFromId(new_user.getId());
				this.cs_model.changeUserName(new_user);
				this.GUI.changeDistantUsername(old_name, new_name);
			} catch (Exception e) {
				e.printStackTrace(); // Cannot happen
			}
    	}
    	else {
    		this.cs_model.addUser(new_user);
			if (update_gui) {
				this.GUI.addUserToUserlist(new_user.getName());
			}
    	}
    }
    
    public User getLocalUser() {
    	return this.local_user;
    }
    
    // Gives the local user a unique id (unless we're really unlucky)
	// Ignore messages before local_user is set


	// Uses the DB to get the user_id associated with the login
	// TODO: add user to DB if not present already
	public void setLocalId(int id) {
		this.local_user.setId(id);
	}

    public void setLocalUsername(String local_name) {
    	this.local_user.setName(local_name);
    }

	public boolean isLocalUserDefined() {
		return this.local_user_defined;
	}

    // If it exists, it uses message_code too
    //public void createMessage(String input, String username) {
    //}

}
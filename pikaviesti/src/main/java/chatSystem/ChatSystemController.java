package chatSystem;// attributes random id to user
import communication.CommunicationSystem;
import objects.Message;
import objects.User;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;


public class ChatSystemController {

    private final User local_user;


    private final ChatSystemGUI GUI;

    private CommunicationSystem com_sys;

	private final Connection con;

	// We can't answer to some messages until the local_user is set
	private boolean local_user_defined;


    private final ChatSystemModel cs_model;
    
    // We need the GUI here so when a distant user changes we can tell the GUI
    public ChatSystemController(ChatSystemGUI gui, Connection con) {
    	this.GUI = gui;
    	this.cs_model = new ChatSystemModel(this);
		this.local_user = new User();
		this.local_user_defined = false;
		this.con = con;

	}


    public ArrayList<Message> getChatHistory(String target_str) {
		ArrayList<Message> chat_history = new ArrayList<>();
		int target_id = -1;
		try {
			target_id = this.cs_model.getIdFromName(target_str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int local_id = this.local_user.getId();

		try {
			Statement statement = this.con.createStatement();
			String query = "SELECT src_user, dest_user, time, content FROM message_history " +
					"WHERE (src_user='"+local_id+"' AND dest_user='"+target_id+"') " +
					"OR (src_user='"+target_id+"' AND dest_user='"+local_id+"')" +
					"ORDER BY time ASC";
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				chat_history.add(new Message(rs.getInt("src_user"), rs.getInt("dest_user"),
						rs.getTimestamp("time").toLocalDateTime(),
						0,
						rs.getString("content"))
				);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return chat_history;
    }

	// The GUI gets an array of messages, so it needs a method to translate it
	public String getControllerNameFromId(int id) throws Exception {
		return this.cs_model.getNameFromId(id);
	}

    // Called by GUI to see the active users
    // GUI only needs the string objects
    public ArrayList<String> getStrUserListWithoutSelf() {
    	ArrayList<User> user_list = this.cs_model.getUserListWithoutSelf();
    	ArrayList<String> str_user_list = new ArrayList<>();
		for (User user : user_list) {
			str_user_list.add(user.getName());
		}
    	return str_user_list;
    }
    
    // Starts a session and gives the corresponding socket
    // GUI shouldn't call it if the connection is already on
    public void startSessionFromLocal(String target_username) {
		if (target_username == null) {return;}

    	InetAddress host_addr = this.cs_model.getAddressFromName(target_username);
    	Socket sock;
    	try {
    		// Connect to foreign host
			sock = this.com_sys.TCPConnect(host_addr, this.cs_model.getIdFromName(target_username));
			// Store the socket created to send messages later on
			this.com_sys.addSenderSocket(this.cs_model.getIdFromName(target_username), sock);
		} catch (IOException ioe) {
			System.out.println("Failure to connect to distant host at address " + host_addr);
			System.out.println(ioe);
		}
		catch (Exception e) {/**/}
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
			Message m = new Message(this.local_user.getId(), target_id, 0, content);
			this.com_sys.sendChatMessage(m);
			this.updateChatHistory(m);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failure in sendMessage.");
		}
    }

	public void updateChatHistory(Message m) {

		try {

			String query = "INSERT INTO message_history (src_user, dest_user, time, content) " +
					"VALUES (?, ?, ?, ?)";
			PreparedStatement preparedStatement = con.prepareStatement(query);
						preparedStatement.setInt(1, m.getSrcId());
			preparedStatement.setInt(2, m.getDestId());
			preparedStatement.setTimestamp(3, Timestamp.valueOf(m.getTimeStamp()));
			preparedStatement.setString(4, m.getContent());

			preparedStatement.executeUpdate();

			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
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

		// We add him to the list of users so the old names count too
		// We have to clean the old names
		// There is no old name if it's the first time we choose it
		if (this.local_user_defined) {
			this.cs_model.delUser(this.local_user.getId());
		}


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
			this.cs_model.addUser(new User(name, this.local_user.getId()));
			this.local_user_defined = true;
    		this.com_sys.nameChangeNotificationBroadcast(name);
        	return true;
    	}
    	
    }
    
    public void updateModelAddressTable(int user_id, InetAddress addr) {
    	this.cs_model.updateAddressTable(user_id, addr);
    }

	public void postLoginClose() {
		this.com_sys.deathNotificationBroadcast();
		try {
			this.con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.com_sys.closeUDPServer();
	}

	public void postNameClose() {
		this.com_sys.deathNotificationBroadcast();
		try {
			this.con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.com_sys.closeUDPServer();
		this.com_sys.closeTCPServer();
	}

    public void updateGUI(Message received_message) {

		// We only update the GUI if the person who sent is the one currently selected
		try {
			if (this.cs_model.getIdFromName(this.GUI.getGUISelected()) != received_message.getSrcId()) { return; }
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			String source_name = this.cs_model.getNameFromId(received_message.getSrcId());
			String content = received_message.getContent();
			String formatted_time = Message.getFormattedTime(received_message.getTimeStamp());
			System.out.println("updateGUIMessageReceived");
			this.GUI.updateGUIMessageReceived(content, source_name, formatted_time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ------------------ Image handling -------------------


	public void sendImage(File f) {

		// Check that the file is a jpg file
		boolean ext_ok = this.checkExtension(f.getName());
		long size_kb = this.getFileSize(f.toPath().toString());

		if (!ext_ok) {
			ChatSystemGUI.showPopup("Veuillez envoyer une image .jpg");
			System.out.println("Image extension incorrect");
			return;
		}

		if (size_kb > 125) {
			ChatSystemGUI.showPopup("L'image est trop volumineuse");
			System.out.println("Image too large to be sent");
			return;
		}

		BufferedImage image = null;
		try {
			image = ImageIO.read(f);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			this.com_sys.sendImage(image, this.cs_model.getIdFromName(this.GUI.getGUISelected()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Optional<String> getExtension(String filename) {
		// ofNullable allows filename to be null
		// but the return can't be null
		return Optional.ofNullable(filename)
				.filter(f -> f.contains("."))
				.map(f -> f.substring(filename.lastIndexOf(".") + 1));
	}

	public boolean checkExtension(String filename) {
		boolean ok = true;
		Optional<String> opt_ext = getExtension(filename);
		if (opt_ext.isPresent()) {
			String extension = opt_ext.get();

			// Wrong extension
			if (!(extension.equals("jpg"))) {
				ok = false;
			}
		}
		// No extension
		else {
			ok = false;
		}
		return ok;
	}

	public long getFileSize(String filepath) {

		long length_kb = 0;
		Path path = Paths.get(filepath);

		try {

			// size of a file (in bytes)
			length_kb = Files.size(path)/1024;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return length_kb;
	}

	// -------------------- End of image handling ------------------------


	public void removeUser(int dead_user_id) {
		String name = null;
		try {
			name = this.cs_model.getNameFromId(dead_user_id);
		} catch (Exception e) {/**/}

		// remove from gui (not shown anymore)
		this.GUI.delGUIUser(name);

		// Remove from model
		this.cs_model.delUser(dead_user_id);
	}


    // Updates the CSModel by either changing the name or adding a user
    public void updateCSModel(User new_user, boolean update_gui) {

		int r;
		// User existence returns -1 if user doesn't exist, index otherwise
    	if ((r = this.cs_model.checkUserExistence(new_user)) != -1) {
    		
        	String new_name = new_user.getName();
    		
    		try {
    			// the new_user kept their id when changing their name
    			String old_name = this.cs_model.getNameFromId(new_user.getId());
				System.out.println("Name change from " + old_name + " to " + new_name);
				this.cs_model.changeUserName(new_user, r);
				this.GUI.changeDistantUsername(old_name, new_name);
				if (Objects.equals(this.GUI.getGUISelected(), old_name)) {
					// Tell the GUI the name of the session target user has changed
					this.GUI.setGUISelected(new_name);
				}
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
	public void setLocalId(int id) {
		this.local_user.setId(id);
	}

    public void setLocalUsername(String local_name) {
    	this.local_user.setName(local_name);
    }

	public boolean isLocalUserDefined() {
		return this.local_user_defined;
	}
}
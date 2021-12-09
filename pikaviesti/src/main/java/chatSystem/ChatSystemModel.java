package chatSystem;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;

import objects.User;



public class ChatSystemModel {

	private final ArrayList<User> user_list;
	/*
	We need to keep the address associated with the user to be able to send messages
	Is checked and updated whenever we receive a message
	We can access the addresses through the sockets, but we need this to be able to identify
	to which host a socket belongs.
	 */

	private final Hashtable<Integer, InetAddress> addresses = new Hashtable<>();

		
	public ChatSystemModel() {
		this.user_list = new ArrayList<>();
		this.user_list.add(new User("e",2));
	}
	
	
    public ArrayList<User> getUserList() {
    	return this.user_list;
    }


    public void addUser(User new_user) {
    	this.user_list.add(new_user);
    }
    
    // Checks if the user's id is in the list
    public boolean checkUserExistence(User target_user) {
    	
    	int target_id = target_user.getId();
		for (User u : this.user_list) {
			if (u.getId() == target_id) {
				return true;
			}
		}
    	return false;
    }
    
    public boolean checkNameExistence(String name) {
		for (User u : this.user_list) {
			if (Objects.equals(u.getName(), name)) {
				return true;
			}
		}
    	return false;
    }
    
    public InetAddress getAddressFromName(String name) {
    	InetAddress matching_addr = null;
    	try {
			int id = this.getIdFromName(name);
			matching_addr = this.addresses.get(id);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Couldn't find user with name " + name + ", giving null address.");
		}
    	return matching_addr;
    	
    }

	public int getIdFromAddress(InetAddress address) throws Exception {
		int user_id;
		for (int i = 0; i < this.addresses.size(); i++) {
			user_id = this.user_list.get(i).getId();
			if( this.addresses.get(user_id) == address) {
				return user_id;
			}
		}
		throw new Exception("Couldn't find user with address " + address);
	}

	public boolean checkAddressExistence(InetAddress address) {
		return this.addresses.containsValue(address);
	}
    
    public void updateAddressTable(int user_id, InetAddress address) {
    	// If there is no user_id matching, add the corresponding address
    	// If there is one and the address is the same, does it and returns the value
    	// If there is one and the address is different, does it and returns previous value
    	// Literally does everything at once, why even hire a developer
    	this.addresses.put(user_id, address);
    }


    public void changeUserName(User u) throws Exception {
    	if (this.user_list.contains(u)) {
    		int user_index = this.user_list.indexOf(u);
        	this.user_list.set(user_index, u);
    	}
    	else {
    		throw new Exception("Couldn't find user " + u.getName());
    	}
    }

    public int getIdFromName(String name) throws Exception {
		for (User u : this.user_list) {
			if (Objects.equals(u.getName(), name)) {
				return u.getId();
			}
		}
    	throw new Exception("Couldn't find user " + name);
    }
    
    public String getNameFromId(int id) throws Exception {
		for (User u : this.user_list) {
			if (u.getId() == id) {
				return u.getName();
			}
		}
    	throw new Exception("Couldn't find user " + id);
    }
}

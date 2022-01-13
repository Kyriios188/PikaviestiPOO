package chatSystem;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Enumeration;
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

	private final ChatSystemController controller;

	private final Hashtable<Integer, InetAddress> addresses = new Hashtable<>();

		
	public ChatSystemModel(ChatSystemController controller) {
		this.user_list = new ArrayList<>();
		this.controller = controller;
	}
	
	
    public ArrayList<User> getUserListWithoutSelf() {

		ArrayList<User> list_without_self = new ArrayList<>(this.user_list);
		try {
			list_without_self.remove(this.getIndexFromId(this.controller.getLocalUser().getId()));

		} catch (Exception e) {e.printStackTrace();}

		return list_without_self;
    }

	public ArrayList<User> getUserList() {
		return this.user_list;
	}


    public void addUser(User new_user) {
		this.user_list.add(new_user);
    }

	public int getIndexFromId(int id) throws Exception {
		int index;
		for (index = 0; index < this.user_list.size(); index++) {
			if (this.user_list.get(index).getId() == id) {
				return index;
			}
		}
		throw new Exception("Couldn't find user with id " + id);
	}

	public void delUser(int id) {
		try {
			int i = getIndexFromId(id);
			this.user_list.remove(i);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Didn't do anything");
		}

	}
    
    // Checks if the user's id is in the list
    public int checkUserExistence(User target_user) {
    	
    	int target_id = target_user.getId();
		int i = 0;
		for (User u : this.user_list) {
			if (u.getId() == target_id) {
				return i;
			}
			i += 1;
		}
    	return -1;
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

		Enumeration<Integer> e = this.addresses.keys();

		while (e.hasMoreElements()) {

			// Getting the key of a particular entry
			int key = e.nextElement();

			if (this.addresses.get(key).equals(address) && key != this.controller.getLocalUser().getId()) {
				return key;
			}
		}
		throw new Exception("Couldn't find user with address " + address);
	}

	// If every session isn't on the same computer, returns whether
	// an address exists in the model
	public boolean checkAddressExistence(InetAddress address) {
		Enumeration<Integer> e = this.addresses.keys();
		boolean trouve = false;
		// We use the address to identify a user
		// We get the address of the user who starts a connection remotely
		// We use this address to alert the local user that a remote user
		// has started a connection. Problem : locally, every one has the same
		// address => locally, the feature is disabled.
		// all_the_same is true if all the addresses are the same i.e. every session
		// on the same computer
		boolean all_the_same = false;
		ArrayList<InetAddress> check_identical = new ArrayList<>();

		// Iterating through the Hashtable
		while (e.hasMoreElements()) {

			// Getting the key of a particular entry
			int key = e.nextElement();

			check_identical.add(this.addresses.get(key));
			if (this.addresses.get(key).equals(address)) {
				trouve = true;
			}

		}

		if (check_identical.size() > 1) {
			if (check_identical.get(0).equals(check_identical.get(1))) {
				all_the_same = true;
			}
		}

		if (!all_the_same) {
			return trouve;
		}
		else {
			return false;
		}
	}
    
    public void updateAddressTable(int user_id, InetAddress address) {
    	// If there is no user_id matching, add the corresponding address
    	// If there is one and the address is the same, does it and returns the value
    	// If there is one and the address is different, does it and returns previous value
    	// Literally does everything at once, why even hire a developer
    	this.addresses.put(user_id, address);
    }

	// it's called when we know it exists
    public void changeUserName(User u, int i) {
		this.user_list.set(i, u);
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

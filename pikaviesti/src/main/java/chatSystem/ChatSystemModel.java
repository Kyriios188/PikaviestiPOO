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

	private final ChatSystemController controller;

	private final Hashtable<Integer, InetAddress> addresses = new Hashtable<>();

		
	public ChatSystemModel(ChatSystemController controller) {
		this.user_list = new ArrayList<>();
		this.controller = controller;
	}
	
	
    public ArrayList<User> getUserListWithoutSelf() {

		ArrayList<User> list_without_self = new ArrayList<>(this.user_list);
		try {
			System.out.println("////////////////////////");
			System.out.println(this.user_list.size());
			list_without_self.remove(this.getIndexFromId(this.controller.getLocalUser().getId()));
			System.out.println(this.user_list.size());
		} catch (Exception e) {/**/}
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
				return id;
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
			System.out.println("Didn't do anything lol");
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

package chatSystem;

import java.util.ArrayList;
import java.util.List;

import objects.User;



public class ChatSystemModel {

	private ArrayList<User> user_list;

		
	public ChatSystemModel() {
		this.user_list = new ArrayList<>();
	}
	
	
    public ArrayList<User> getUserList() {
    	return this.user_list;
    }


    public void addUser(User new_user) {
    	this.user_list.add(new_user);
    }
    
    public boolean checkUserExistence(User u) {
    	return this.user_list.contains(u);
    }
    
    public boolean checkNameExistence(String name) {
    	for (int i = 0; i < this.user_list.size(); i++) {
    		User u = this.user_list.get(i);
    		if (u.getName() == name) {
    			return true;
    		}
    	}
    	return false;
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
    
    public String getUserFromName(String name) throws Exception {
    	for (int i = 0; i < this.user_list.size(); i++) {
    		User u = this.user_list.get(i);
    		if (u.getName() == name) {
    			return u.getName();
    		}
    	}
    	throw new Exception("Couldn't find user " + name);
    }

    public int getIdFromName(String name) throws Exception {
    	for (int i = 0; i < this.user_list.size(); i++) {
    		User u = this.user_list.get(i);
    		if (u.getName() == name) {
    			return u.getId();
    		}
    	}
    	throw new Exception("Couldn't find user " + name);
    }


    public void deleteUser(User u) throws Exception {
    	int i = this.user_list.indexOf(u);
    	if (i == -1) {
    		throw new Exception("Couldn't find user " + u.getName());
    	}
    }

}

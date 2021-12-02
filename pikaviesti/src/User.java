


public class User {

    private String name;


    private int id;


    public int getId() {
    	return this.id;
    }
    
    public void setId(int new_id) {
    	this.id = new_id;
    }


    public String getName() {
    	return this.name;
    }
    
    public void setName(String new_name) {
    	this.name = new_name;
    }
    
    public User(String name, int id) {
    	this.name = name;
    	this.id = id;
    }

}

import java.time.*;


public class Message {
	
	private String content;
	private LocalTime timestamp;
	private int src_user;
	private int dest_user;
	
	private int message_code; 
	// represents the type of message so it's dealt with appropriately
	/*
     *  Types of Message :
     *  _3 : Notification of a user who changed their name by themselves
     *  _2 : Answer to the question "whatsYourName?" (same treatment -> update Model)
     *  _1 : Question "whatsYourName?"
     *  _0 : Chat message
     */
	
	/*
	 * Message format :
	 * example : "45/587/15:23:47/0/arcane best show"
	 */

	
	//**************  CONSTRUCTORS  **************
	
	public Message(String content, int src, int dest, int m_code) {
		this.content = content;
		this.src_user = src;
		this.dest_user = dest;
		this.message_code = m_code;
	}
	
	public Message(int src, int dest, LocalTime time, String content, int m_code) {
		this.content = content;
		this.src_user = src;
		this.dest_user = dest;
		this.timestamp = time;
		this.message_code = m_code;
	}
	
	//**************  GETTERS  **************
	
	public int getSrcId() {
		return this.src_user;
	}
	
	public int getDestId() {
		return this.dest_user;
	}
	
	public LocalTime getTimeStamp() {
		return this.timestamp;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public int getMessageCode() {
		return this.message_code;
	}
	
	//**************  SETTERS  **************
	
	public void setSrcId(int new_id) {
		this.src_user = new_id;
	}
	
	public void setDestId(int new_id) {
		this.dest_user = new_id;
	}
	
	public void setTimeStamp(LocalTime timestamp) {
		this.timestamp = timestamp;
	}
	
	public void setContent(String new_content) {
		this.content = new_content;
	}
	
	public void setMessageCode(int new_code) {
		this.message_code = new_code;
	}
}

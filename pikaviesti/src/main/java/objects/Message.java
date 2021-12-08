package objects;

import java.time.*;


public class Message {
	
	//TODO: For internal testing, add a port number?
	private final String content;
	private final LocalTime timestamp;
	private final int src_user;
	private final int dest_user;
	
	private final int message_code;
	// represents the type of message so it's dealt with appropriately
	/*
     *  Types of objects.Message :
     *  _3 : Notification of a user who changed their name by themselves
     *  _2 : Answer to the question "whatsYourName?" (same treatment -> update Model)
     *  _1 : Question "whatsYourName?"
     *  _0 : Chat message
     */
	
	/*
	 * objects.Message format :
	 * 	src_user_id / dest_user_id / time_stamp / message_code / message_content
	 * example : "45/587/15:23:47/0/arcane best show"
	 */

	
	//**************  CONSTRUCTORS  **************
	
	public Message(int src, int dest, int m_code, String content) {
		this.content = content;
		this.src_user = src;
		this.dest_user = dest;
		this.message_code = m_code;
		this.timestamp = LocalTime.now();
	}
	
	public Message(int src, int dest, LocalTime time,  int m_code, String content) {
		this.content = content;
		this.src_user = src;
		this.dest_user = dest;
		this.timestamp = time;
		this.message_code = m_code;
	}
	
	public String toString() {
		return "From : " + this.src_user + "\nTo : " + this.dest_user + "\nAt : " + this.timestamp + "\nType : "+this.message_code + "\nContent : " + this.content;
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
}

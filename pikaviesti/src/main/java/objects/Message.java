package objects;

import java.time.*;
import java.time.format.DateTimeFormatter;


public class Message {

	private final String content;
	private final LocalTime timestamp;
	private final int src_user;
	private final int dest_user;
	
	private final int message_code;
	// represents the type of message so it's dealt with appropriately
	/*
     *  Types of objects.Message :
     *  _5 : Warning that the next line is an image
     *  _4 : Notification of death
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

	// Used when we fetch the chat history
	public Message(int src, int dest, LocalTime time,  int m_code, String content) {
		this.content = content;
		this.src_user = src;
		this.dest_user = dest;
		this.timestamp = time;
		this.message_code = m_code;
	}

	public String getMessageType(int code) {
		String message_type;
		switch (code) {
			case 0 -> message_type = "TCP message";
			case 1 -> message_type = "What's your name question";
			case 2 -> message_type = "What's your name answer";
			case 3 -> message_type = "Name change notification";
			case 4 -> message_type = "Notification of death";
			default -> message_type = "Unidentified message";
		}
		return message_type;
	}
	
	public String toString() {
		return "From : " + this.src_user + "\nTo : " + this.dest_user + "\nAt : " + this.timestamp + "\nType : "+this.message_code + "\nContent : " + this.content;
	}
	
	//**************  GETTERS  **************

	public static String getFormattedTime(LocalTime timestamp) {
		DateTimeFormatter formatter = DateTimeFormatter.ISO_TIME;
		return timestamp.format(formatter).substring(0, 8);
	}
	
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

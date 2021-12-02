import java.time.*;


public class Message {
	
	private String content;
	private LocalTime timestamp;
	private int src_user;
	private int dest_user;
	
	/*
	 * Format des messages :
	 * "src_user/dest_user/time/content"
	 * exemple : "45/587/15:23/ceci est un exemple"
	 */
	
	public Message(String content, int src, int dest) {
		this.content = content;
		this.src_user = src;
		this.dest_user = dest;
	}
	
	public int getSrcId() {
		return this.src_user;
	}
	
	public void stampMessage(LocalTime timestamp) {
		this.timestamp = timestamp;
	}
	
	public int getDestId() {
		return this.dest_user;
	}
	
	public String getContent() {
		return this.content;
	}
}

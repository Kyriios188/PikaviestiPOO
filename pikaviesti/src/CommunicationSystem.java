import java.io.IOException;
import java.lang.reflect.Array;
import java.net.*;
import java.time.LocalTime;

public class CommunicationSystem {

    public String src_address;
    
    private int UDP_PORT = 7071;


    // IOException because of the DatagramSocket.receive
    public CommunicationSystem() {
    	
    	// Launch UDP server listening on specific port
    	new UDPServerThread(this, UDP_PORT);
    	
    }
    
    // Takes a String of format "src_user/dest_user/time/code/content"
    // Returns the Message object associated with it
    public Message parseMessage(String raw_message) {
        String[] fields = raw_message.split("/");
        int src_id = Integer.parseInt(fields[0]);
        int dest_id = Integer.parseInt(fields[1]);
        LocalTime t = LocalTime.parse(fields[2]);
        int m_code = Integer.parseInt(fields[3]);
        String content = fields[4];
        return new Message(src_id, dest_id, t, content, m_code);
    }
    
    // Converts Message to String with format "src_user/dest_user/time/content"
    // So it can be sent via UDP or TCP
    public String createRawMessage(Message m) {
    	return m.getSrcId() + "/" + m.getDestId() + "/" + m.getTimeStamp() + "/" + m.getContent();
    }
    
    public void receiveMessage(String raw_message) {
    	Message m = parseMessage(raw_message);
    	
    }


    //public void sendInsertMessage(message m) {
    //}


    public void broadcastWhatsYourName(String name) {
    	
    }


    public void broadcastNotification(String new_name) {
    }


    //public void sendQueryChatHistory(user_id u1, user_id u2) {
    //}


    //public user receiveNotification() {
    //}


    //public void sendMessage(message m) {
    //}

}
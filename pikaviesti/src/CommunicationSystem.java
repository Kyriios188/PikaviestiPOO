import java.io.IOException;
import java.lang.reflect.Array;
import java.net.*;

public class CommunicationSystem {

    public String src_address;
    
    private int UDP_PORT = 7071;


    // IOException because of the DatagramSocket.receive
    public CommunicationSystem() {
    	
    	// Launch UDP server listening on specific port
    	new UDPServerThread(this, UDP_PORT);
    	
    }
    
    //TODO: implement this
    public Message parseMessage(String raw_message) {
        String[] fields = raw_message.split("/");
        //int src_id = Integer.parseInt(Array.get(fields, 0));
        return null;
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
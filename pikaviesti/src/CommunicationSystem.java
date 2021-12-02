import java.io.IOException;
import java.net.*;

public class CommunicationSystem {

    public String src_address;
    
    private int UDP_PORT = 7071;


    // IOException because of the DatagramSocket.receive
    public CommunicationSystem() throws IOException {
    	
    	// Launch UDP server listening on specific port
    	new UDPServerThread(UDP_PORT);
    	
    }
    
    //public message receiveMessage() {
    //}


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
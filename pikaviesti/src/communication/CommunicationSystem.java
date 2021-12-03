package communication;

import chatSystem.ChatSystemController;
import objects.Message;

import java.io.IOException;
import java.net.*;
import java.time.LocalTime;

public class CommunicationSystem {
    
    private int UDP_RCV_PORT = 7071;
    private int UDP_SND_PORT = 7070;
    public static String delimiter = "‡";
    
    // We keep the object around to be able to close it cleanly
    UDPServerThread udp_rcv_server;
    
    
    private ChatSystemController controller;
    
    private int local_id;
    private String local_name;


    public CommunicationSystem(ChatSystemController controller) {
    	
    	this.controller = controller;
    	this.local_id = this.controller.getLocalUser().getId();
    	this.local_name = this.controller.getLocalUser().getName();
    	// Launch UDP server listening on specific port
    	this.udp_rcv_server = new UDPServerThread(this, UDP_RCV_PORT);
    	this.udp_rcv_server.start();
    }
    
    // Takes a String of format "src_user‡dest_user‡time‡code‡content"
    // Returns the objects.Message object associated with it
    public Message parseMessage(String raw_message) {
        String[] fields = raw_message.split(this.delimiter);
        int src_id = Integer.parseInt(fields[0]);
        int dest_id = Integer.parseInt(fields[1]);
        LocalTime t = LocalTime.parse(fields[2]);
        int m_code = Integer.parseInt(fields[3]);
        String content = fields[4];
        return new Message(src_id, dest_id, t, content, m_code);
    }
    
    // Converts objects.Message to String with format "src_user/dest_user/time/content"
    // So it can be sent via UDP or TCP
    public String createRawMessage(Message m) {
    	return m.getSrcId() + this.delimiter + m.getDestId() + this.delimiter + m.getTimeStamp() + this.delimiter + m.getContent();
    }
    
    public void receiveMessage(String raw_message, InetAddress src_addr, int src_id) {
    	Message m = parseMessage(raw_message);
    	//TODO: finish this
    	
    	switch (m.getMessageCode()) {
    	
    	case 0:
    		// We received a chat message
    	
    	case 1:
    		// We received "whatsYourName?"
    		Message answer = new Message(this.local_name, this.local_id, m.getSrcId(), 2);
    		UDPMessage(createRawMessage(answer), src_addr);
    	
    	case 2:
    		// We received an answer to the question "whatsYourName?"
    		
    	
    	case 3:
    		// We received notification that distant user changed their name
    	}
    	
    }
    //TODO: Closing window has to call this method (and the TCP equivalent)
    public void closePorts() {
    	this.udp_rcv_server.stop_server();
    	//TODO: Add TCP to that
    	//TODO: Verify they are open before closing them
    }

    //public void sendInsertMessage(message m) {
    //}


    /*
     *  Broadcast use cases:
     *  _local host changes their name
     *  _local host asks names of connected hosts
     *  
     *  Non-broadcast use cases:
     *  _answer to "whatsYourName?"
     *  _send to the Database ?
     */
    
    // For broadcast, dest_addr = InetAddress.getByName("255.255.255.255")
    // TODO: simply it, enter string instead
    public void UDPMessage(String raw_message, InetAddress dest_addr) {

    	try {
			
			DatagramSocket dgramSocket = new DatagramSocket(UDP_SND_PORT);
	    	DatagramPacket outPacket = new DatagramPacket(raw_message.getBytes(), 0, raw_message.length(),
	    			dest_addr, UDP_RCV_PORT);
	    	dgramSocket.send(outPacket);
	    	dgramSocket.close();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }



    //public void sendQueryChatHistory(user_id u1, user_id u2) {
    //}


    //public void sendMessage(message m) {
    //}

}
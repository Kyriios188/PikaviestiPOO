package communication;

import chatSystem.ChatSystemController;
import objects.Message;
import objects.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.time.LocalTime;
import java.util.Hashtable;
import java.util.Scanner;

public class CommunicationSystem {
    
    private int UDP_RCV_PORT = 6071;
    private int UDP_SND_PORT = 6070;
    private int TCP_RCV_PORT = 5071;
    private int TCP_SND_PORT = 5070;
    public static String delimiter = "!";
    
    // We keep the object around to be able to close it cleanly
    private UDPServerThread udp_rcv_server;
    private TCPServerThread tcp_server;
    
    // When we send a message we have a name
    // We get the matching id and address through CSModel but we still need the session socket
    // Integer is distant user id
    // Updated when we create a new session
    private Hashtable<Integer, Socket> sender_sockets;
    // TODO: handle when we close a connection, remove the socket
    
    
    private ChatSystemController controller;
    
    private int local_id;
    private String local_name;


    public CommunicationSystem(ChatSystemController controller) {
    	
    	this.controller = controller;
		this.local_id = this.controller.getLocalUser().getId();
		//this.local_name = this.controller.getLocalUser().getName();

    	// Launch UDP server listening on specific port
    	this.udp_rcv_server = new UDPServerThread(this, UDP_RCV_PORT);
    	this.udp_rcv_server.start();
    	
    }
    
    // Takes a String of format "src_user‡dest_user‡time‡code‡content"
    // Returns the objects.Message object associated with it
    public Message parseMessage(String raw_message) {
        String[] fields = raw_message.split(CommunicationSystem.delimiter);
        int src_id = Integer.parseInt(fields[0]);
        int dest_id = Integer.parseInt(fields[1]);
        LocalTime t = LocalTime.parse(fields[2]);
        int m_code = Integer.parseInt(fields[3]);
        String content = fields[4];
        return new Message(src_id, dest_id, t, m_code, content);
    }
    
    public void whatsYourNameBroadcast() {
    	// It's broadcast so we put 0 in the dest_user field
    	Message whatsyourname = new Message(this.local_id, 0, 1, "fill");
    	String m = createRawMessage(whatsyourname);
    	UDPMessage(m, "255.255.255.255");
    }
    
    public void nameChangeNotificationBroadcast(String new_name) {
    	// It's broadcast so we put 0 in the dest_user field
    	Message whatsyourname = new Message(this.local_id, 0, 1, new_name);
    	String m = createRawMessage(whatsyourname);
    	UDPMessage(m, "255.255.255.255");
    }
    
    // Converts objects.Message to String with format "src_user/dest_user/time/content"
    // So it can be sent via UDP or TCP
    public String createRawMessage(Message m) {
    	return m.getSrcId() + CommunicationSystem.delimiter + 
    			m.getDestId() + CommunicationSystem.delimiter + 
    			m.getTimeStamp() + CommunicationSystem.delimiter +
    			m.getMessageCode() + CommunicationSystem.delimiter +
    			m.getContent();
    }
    
    public void receiveMessage(String raw_message, InetAddress src_addr) throws UnknownHostException {

    	Message m = parseMessage(raw_message);
		if (m.getSrcId() == local_id) {System.out.println("Ignore self-messaging");return;}
    	this.controller.updateModelAddressTable(m.getSrcId(), src_addr);
    	
    	System.out.println("Message code : " + m.getMessageCode());
    	switch (m.getMessageCode()) {
    	
    	case 0:
    		// We received a chat message
    		break;
    	
    	case 1:
    		// We received "whatsYourName?"
    		System.out.println("Received 'whatsYourName?' question");
    		Message answer = new Message(this.local_id, m.getSrcId(), 2, this.local_name);
    		System.out.println("Answered :\n"+answer);
    		// src_addr.toString() returns /10.10.40.246 -> substring(1) gives a string with the /
    		UDPMessage(createRawMessage(answer), src_addr.toString().substring(1));
    		break;
    	
    	case 2:
    		// We received an answer to the question "whatsYourName?"
    		User u1 = new User(m.getContent(), m.getSrcId());
    		this.controller.updateCSModel(u1);
    		break;
    	
    	case 3:
    		// We received notification that distant user changed their name
    		User u2 = new User(m.getContent(), m.getSrcId());
    		this.controller.updateCSModel(u2);
    		break;
    	}
    	
    }
    
    public void addSenderSocket(Integer id, Socket sock) {
    	this.sender_sockets.put(id, sock);
    }
    
    public void closePorts() {
    	this.udp_rcv_server.stop_server();
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
    
    
    public void UDPMessage(String raw_message, String dest_str) {
    	
    	try {
    		InetAddress dest_addr = InetAddress.getByName(dest_str);
    		
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
    
    /*
     * TCP receives messages with the TCPServerThread and TCPSessionThread
     * We send messages when the GUI asks us to.
     * There is no dedicated thread, we connect, keep the proof of connection (socket)
     * in a table (sender_sockets) and use it whenever we send a message
     * TODO: If we start a session with host1 and host2 messages first?
     * --> host2 doesn't have the sender_socket
     * --> host2 shouldn't call the TCPConnect method since connection exists already
     * ==> TCPServerThread needs to update the sender_sockets  when it gets a connection
     */
    
    public Socket TCPConnect(InetAddress distant_host) throws IOException {
    	Socket sock = new Socket(distant_host, this.TCP_SND_PORT);
    	return sock;
    }
    
    public void sendChatMessage(Message m) {
    	String raw_message = createRawMessage(m);
    	Socket sock = sender_sockets.get(m.getSrcId());
    	TCPMessage(raw_message, sock);
    }
    
    public void TCPMessage(String raw_message, Socket sock) {

		PrintWriter output;
		try {
			output = new PrintWriter(sock.getOutputStream());
			output.println(raw_message);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    }



    //public void sendQueryChatHistory(user_id u1, user_id u2) {
    //}

}
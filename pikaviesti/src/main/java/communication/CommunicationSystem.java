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

	// Receive broadcast and answers to broadcast
	// Used in UDPServerThread and UDPMessage
	// Socket is created in UDPServerThread.run()
	// Has a handler to close the socket and stopServer()
    private final int UDP_RCV_PORT = 6071;
	// Send broadcast and answers to broadcast
	// Used only in UDPMessage
	// Corresponding socket is closed immediately, does not need a handler
    private final int UDP_SND_PORT = 6070;

	// Receives connection attempts
	// Used in TCPServerThread only
	// Socket is created in TCPServerThread.run()
	// Has a handler to close the socket and stopServer() which doesn't work yet
    private final int TCP_RCV_PORT = 5071;

	// Send messages after establishing connection
	// Used in TCPConnect only
	// Socket is created in TCPConnect called by startSession, startSession adds it to sender_sockets
	// Has a handler to close the socket and TODO stopSession
    private final int TCP_SND_PORT = 5070;
    public static String delimiter = "!";


    // We keep the objects around to be able to close it cleanly
    private UDPServerThread udp_rcv_server;
    private TCPServerThread tcp_rcv_server;

    // When we send a message we have a name
    // We get the matching id and address through CSModel, but we still need the session socket
    // Integer is distant user id
    // Updated when we create a new session
	// They close if there's a problem
    private Hashtable<Integer, Socket> sender_sockets;


    private ChatSystemController controller;

    private int local_id;
    private String local_name;


    public CommunicationSystem(ChatSystemController controller, int local_user_id) {

    	this.controller = controller;
		this.local_id = local_user_id;


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
    	Message whatsyourname = new Message(this.local_id, 0, 2, new_name);
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
		String m_type = m.getMessageType(m.getMessageCode());
		System.out.println("Received " + m_type);
		// Ignore self-messaging
		if (m.getSrcId() == local_id) {
			System.out.println("Ignored");
			return;
		}
    	this.controller.updateModelAddressTable(m.getSrcId(), src_addr);


    	switch (m.getMessageCode()) {

    	case 0:
    		// We received a chat message
    		break;

    	case 1:
    		// We received "whatsYourName?"
			// Can only answer if we have a name
			if (!this.controller.isLocalUserDefined()) {return;}

    		Message answer = new Message(this.local_id, m.getSrcId(), 2, this.local_name);
    		System.out.println("Answered :\n"+answer);
    		// src_addr.toString() returns /10.10.40.246 -> substring(1) gives a string without the /
    		UDPMessage(createRawMessage(answer), src_addr.toString().substring(1));
    		break;

		// Both cases are treated the same, update the Model
    	case 2:
			// We received an answer to the question "whatsYourName?"
			case 3:
				// We received notification that distant user changed their name

    		User u1 = new User(m.getContent(), m.getSrcId());
    		this.controller.updateCSModel(u1);
    		break;
		}

    }

	public void startTCPServer() {
		this.tcp_rcv_server = new TCPServerThread(this, this.TCP_RCV_PORT);
	}

	// Every TCP session has its socket recorded
    public void addSenderSocket(Integer id, Socket sock) {
		this.sender_sockets.put(id, sock);
		this.addShutDownHook(sock); // Make it close gracefully if there's a problem
    }

    public void closeUDPServer() {
		// The server will verify if the socket is open before closing it
    	this.udp_rcv_server.stop_server();
    }
	// TODO: needs to know which sessions are ongoing
	// Use socket.getInetAddress().isReachable(int timeout) to get every remaining session
	// Or remove ended sessions from the list and use the list to close remaining ones

	public void closeTCPServer() {
		this.tcp_rcv_server.stop_server();
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
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getSocketFromId(int id) {
		return this.sender_sockets.get(id);
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
		return new Socket(distant_host, this.TCP_SND_PORT);
    }
    
    public void sendChatMessage(Message m) {
    	String raw_message = createRawMessage(m);
    	Socket sock = sender_sockets.get(m.getSrcId());
    	TCPMessage(raw_message, sock);
    }

	public void endTCPSession(Socket sock) {
		this.tcp_rcv_server.closeSession(sock);
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

	//************** SOCKET SHUT DOWN HANDLERS **************

	// If app has a problem, the socket won't be hogged

	// For Socket
	public void addShutDownHook(Socket socket) {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println(e);
			}
		}));
	}
	// For datagram
	public void addShutDownHookDatagram(DatagramSocket socket) {
		Runtime.getRuntime().addShutdownHook(new Thread(socket::close));
	}

	public void addShutDownHookServer(ServerSocket socket) {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println(e);
			}
		}));
	}

}
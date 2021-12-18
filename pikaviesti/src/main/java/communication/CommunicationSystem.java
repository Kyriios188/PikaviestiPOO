package communication;

import chatSystem.ChatSystemController;
import objects.Message;
import objects.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.nio.ByteBuffer;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Hashtable;

public class CommunicationSystem {

	// Receive broadcast and answers to broadcast
	// Used in UDPServerThread and UDPMessage
	// Socket is created in UDPServerThread.run()
	// Has a handler to close the socket and stopServer()
    private final int UDP_RCV_PORT = 6071;



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

	// Each session also has its own socket in the sender_sockets list

    public static String delimiter = "/";


    // We keep the objects around to be able to close it cleanly
    private final UDPServerThread udp_rcv_server;
    private TCPServerThread tcp_rcv_server;

    // When we send a message we have a name
    // We get the matching id and address through CSModel, but we still need the session socket
    // Integer is distant user id
    // Updated when we create a new session
	// They close if there's a problem
    private final Hashtable<Integer, Socket> sender_sockets;


    private final ChatSystemController controller;

    private final int local_id;

    public CommunicationSystem(ChatSystemController controller, int local_user_id) {

    	this.controller = controller;
		this.local_id = local_user_id;

		this.sender_sockets = new Hashtable<>();

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

	public void deathNotificationBroadcast() {
		Message imdead = new Message(this.local_id, 0, 4, "fill");
		String m = createRawMessage(imdead);
		UDPMessage(m, "255.255.255.255");
	}

    public void whatsYourNameBroadcast() {
		System.out.println("Sending what's your name question broadcast");
    	// It's broadcast so we put 0 in the dest_user field
    	Message whatsyourname = new Message(this.local_id, 0, 1, "fill");
    	String m = createRawMessage(whatsyourname);
    	UDPMessage(m, "255.255.255.255");
    }

    public void nameChangeNotificationBroadcast(String new_name) {
    	System.out.println("Sending name change notification broadcast");
    	// It's broadcast so we put 0 in the dest_user field
    	Message whatsyourname = new Message(this.local_id, 0, 3, new_name);
    	String m = createRawMessage(whatsyourname);
    	UDPMessage(m, "255.255.255.255");
    }

    // Converts Message to String with format "src_user/dest_user/time/content"
    // So it can be sent via UDP or TCP
    public String createRawMessage(Message m) {
    	return m.getSrcId() + CommunicationSystem.delimiter +
    			m.getDestId() + CommunicationSystem.delimiter +
    			m.getTimeStamp() + CommunicationSystem.delimiter +
    			m.getMessageCode() + CommunicationSystem.delimiter +
    			m.getContent();
    }

    public void receiveMessage(String raw_message, InetAddress src_addr, TCPSessionThread session) throws UnknownHostException {

    	Message m = parseMessage(raw_message);
		String m_type = m.getMessageType(m.getMessageCode());
		System.out.println("Received " + m_type);
		// Ignore self-messaging
		if (m.getSrcId() == local_id) {
			System.out.println("Ignored, comes from "+local_id);
			return;
		}
    	this.controller.updateModelAddressTable(m.getSrcId(), src_addr);


    	switch (m.getMessageCode()) {

    	case 0:
    		// We received a chat message
			this.controller.updateGUI(m);
    		break;

    	case 1:
    		// We received "whatsYourName?"
			// Can only answer if we have a name
			if (!this.controller.isLocalUserDefined()) {return;}

    		Message answer = new Message(this.local_id, m.getSrcId(), 2, this.controller.getLocalUser().getName());
    		System.out.println("Answered :\n"+answer);
    		// src_addr.toString() returns /10.10.40.246 -> substring(1) gives a string without the /
			// Used to be src_addr.toString().substring(1) but the machine doesn't see self UDP
    		UDPMessage(createRawMessage(answer), "255.255.255.255");
    		break;

    	case 2:
			// We received an answer to the question "whatsYourName?"
			// We don't update the GUI here, it doesn't exist yet
			User u1 = new User(m.getContent(), m.getSrcId());
			this.controller.updateCSModel(u1, false);
			break;

		case 3:
			// We received notification that distant user changed their name
			User u2 = new User(m.getContent(), m.getSrcId());
			this.controller.updateCSModel(u2, true);
			break;

		case 4:
			// We received notification that someone died
			int dead_user = m.getSrcId();
			this.controller.removeUser(dead_user);

		case 5:
			// We received warning that an image is coming
			session.receiveImage();
		}

    }

	public void sendImage(BufferedImage image, int rcv_id) {

		Socket sock = this.sender_sockets.get(rcv_id);

		try {
			OutputStream outputStream = sock.getOutputStream();
			PrintWriter output = new PrintWriter(outputStream);
			String raw_prepare_message = this.createRawMessage(new Message(this.local_id, rcv_id, 5, "fill"));
			output.println(raw_prepare_message);
			output.flush();

			// Wait that server processed gets ready
			Thread.sleep(500); // TODO check if this is needed




			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", byteArrayOutputStream);

			byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
			outputStream.write(size); // Send size first
			outputStream.write(byteArrayOutputStream.toByteArray()); // Then send image
			outputStream.flush();

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}


	}

	public void startTCPServer() {

		this.tcp_rcv_server = new TCPServerThread(this, this.TCP_RCV_PORT + this.local_id);
		this.tcp_rcv_server.start();
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

	// We remove ended sessions from the list
	// So we can use the list to close the remaining ones

	public void closeTCPServer() {
		this.tcp_rcv_server.stop_server();
	}


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

			DatagramSocket dgramSocket = new DatagramSocket();
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
     * --> host2 shouldn't call the TCPConnect method since connection exists already
     */

	// Need the id of the user we're trying to connect to
	// He set up his listener on the TCP_RCV_PORT + local_id, so we try to connect to that
    public Socket TCPConnect(InetAddress distant_host, int distant_id) throws IOException {
		return new Socket(distant_host, this.TCP_RCV_PORT + distant_id);
    }
    
    public void sendChatMessage(Message m) {
    	String raw_message = createRawMessage(m);
    	Socket sock = sender_sockets.get(m.getDestId());
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

	// Adds socket to sender_sockets, so we can message first even if remote host establishes connection
	// Tells GUI that a new session has started
	public void handleConnection(Socket socket) {
		InetAddress address = socket.getInetAddress();
		// Tell the GUI a session has started
		int id = this.controller.startSessionFromRemote(address);
		// id = -1 if address doesn't match any connected user
		if (id >= 0) {
			this.addSenderSocket(id, socket);
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
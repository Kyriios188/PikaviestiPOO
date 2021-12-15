package communication;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class TCPServerThread extends Thread {

	private int RCV_PORT;
	private ServerSocket socket;
	private CommunicationSystem com_sys;

	private boolean isOpen;
	
	// Needed to close sockets only
	private ArrayList<TCPSessionThread> session_list;
	
	
	// This thread accepts connections and dispatches them to other threads who will handle the session
	public TCPServerThread(CommunicationSystem com_sys, int RCV_PORT) {
		super();
		this.RCV_PORT = RCV_PORT;
		this.com_sys = com_sys;
		this.session_list = new ArrayList<>();
		this.isOpen = true;
	}

	// Remote user has ended the session, we update the list of active sessions
	public void removeSession(TCPSessionThread session) {
		this.session_list.remove(session);
	}

	// Local user wants to end the session
	public void closeSession(Socket sock) {
		int index = -1;
		for (int i = 0; i < this.session_list.size(); i++) {
			if (this.session_list.get(i).getSocket() == sock) {
				index = i;
				this.session_list.get(i).closeSession();
			}
		}
		if (index == -1) {
			System.out.println("ERROR : TRIED TO CLOSE SESSION NOT IN SESSION_LIST");
			return;
		}
		this.session_list.remove(index);
	}

	public void stop_server() {
		try {
			// Close the server socket
			this.socket.close();
			
			// Close every session
			for (TCPSessionThread tcpSessionThread : this.session_list) {
				tcpSessionThread.closeSession();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		System.out.println("Running TCP server");

		try {
			this.socket = new ServerSocket();
			this.socket.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			System.out.println("Reuse address: " + this.socket.getReuseAddress());
			this.socket.bind(new InetSocketAddress(this.RCV_PORT));
			this.com_sys.addShutDownHookServer(this.socket);
			System.out.println("Successful bind of ServerSocket");

		} catch (IOException e1) {
			// Socket couldn't be bound
			e1.printStackTrace();
		}

		// To accept multiple connections in a row
		// accept() blocks the thread
		while (this.isOpen) {
			try {
				System.out.println("Waiting for connection");
				// Accept incoming connection attempts
				Socket link = this.socket.accept();
				this.com_sys.addShutDownHook(link);
				System.out.println("Accepted session");
				// Dispatch a TCPSessionThread to handle sending and receiving messages
				TCPSessionThread new_session = new TCPSessionThread(link, this.com_sys, this);
				// Add the session to the list
				this.session_list.add(new_session);
				// Add the socket to the sender_sockets list so this host can message first
				this.com_sys.handleConnection(link);


				
				
			} catch (IOException e) {
				System.out.println(e);
				return;
			}
		}
	}
}
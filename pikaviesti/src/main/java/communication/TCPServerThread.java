package communication;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class TCPServerThread extends Thread {

	private final int RCV_PORT;
	private ServerSocket socket;
	private final CommunicationSystem com_sys;

	private final boolean isOpen;
	
	// Needed to close sockets only
	private final ArrayList<TCPSessionThread> session_list;
	
	
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
		System.out.println("On appelle removeSession");
		this.session_list.remove(session);
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

	@Override
	public void interrupt() {
		super.interrupt();
		if (!this.socket.isClosed()) {
			this.stop_server();
			System.out.println("Sockets closed by interrupt");
			System.exit(0);
		}
	}
	
	public void run() {

		try {
			this.socket = new ServerSocket(this.RCV_PORT);
			this.com_sys.addShutDownHookServer(this.socket);
		} catch (IOException e1) {
			// Socket couldn't be bound
			e1.printStackTrace();
		}

		// To accept multiple connections in a row
		// accept() blocks the thread
		while (this.isOpen) {
			try {
				// Accept incoming connection attempts
				Socket link = this.socket.accept();
				this.com_sys.addShutDownHook(link);
				System.out.println("Accepted session");
				// Dispatch a TCPSessionThread to handle sending and receiving messages
				TCPSessionThread new_session = new TCPSessionThread(link, this.com_sys, this);

				// Add the socket to the sender_sockets list so this host can message first
				this.com_sys.handleConnection(link);

				new_session.start();
				// Add the session to the list
				this.session_list.add(new_session);


			} catch (IOException e) {/* The 'receive' method will trigger this exception upon closing */}
		}
	}
}
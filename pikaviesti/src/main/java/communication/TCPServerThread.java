package communication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class TCPServerThread extends Thread {

	private int RCV_PORT;
	private ServerSocket socket;
	private CommunicationSystem com_sys;

	private boolean isOpen;
	
	// Needed to close sockets only
	// TODO: update when session ends
	private ArrayList<TCPSessionThread> session_list;
	
	
	// This thread accepts connections and dispatches them to other threads who will handle the session
	public TCPServerThread(CommunicationSystem com_sys, int RCV_PORT) {
		super();
		this.RCV_PORT = RCV_PORT;
		this.com_sys = com_sys;
		this.session_list = new ArrayList<>();
		this.isOpen = true;
	}
	
	// TODO: is this method even called during accept?
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
		
		try {
			this.socket = new ServerSocket(this.RCV_PORT);
			this.com_sys.addShutDownHookServer(this.socket);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// To accept multiple connections in a row
		// accept() blocks the thread
		while (this.isOpen) {
			try {
				
				// Accept incoming connection attempts
				Socket link = this.socket.accept();
				this.com_sys.addShutDownHook(link);
				
				// Dispatch a TCPSessionThread to handle sending and receiving messages
				TCPSessionThread new_session = new TCPSessionThread(link, this.com_sys);
				// Add the session to the list
				this.session_list.add(new_session);


				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
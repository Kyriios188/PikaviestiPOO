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
	
	private ArrayList<TCPSessionThread> session_list;
	
	
	// This thread accepts connections and dispatches them to other threads who will handle the session
	public TCPServerThread(CommunicationSystem com_sys, int RCV_PORT) {
		super();
		this.RCV_PORT = RCV_PORT;
		this.com_sys = com_sys;
		this.session_list = new ArrayList<>();
	}
	
	
	public void stop_server() {
		try {
			// Close the server socket
			this.socket.close();
			
			// Close every session
			for (int i=0; i<this.session_list.size(); i++) {
				this.session_list.get(i).closeSession();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		
		try {
			this.socket = new ServerSocket(this.RCV_PORT);
		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// To accept multiple connections in a row
		// The receive() method blocks the thread
		while (true) {
			try {
				
				// Accept incoming connection attempts
				Socket link = this.socket.accept();
				
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
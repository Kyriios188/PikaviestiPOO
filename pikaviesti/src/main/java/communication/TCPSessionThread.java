package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;


// TCPSessionThreads only wait for messages and receive them
// To send messages, the GUI calls the controller who calls the com_sys
public class TCPSessionThread extends Thread {
	
	private final Socket sock;
	private final CommunicationSystem com_sys;
	private final InetAddress distant_addr;
	private final TCPServerThread server;
	
	// Will call the receiveMessage method so it needs the com_sys
	public TCPSessionThread(Socket sock, CommunicationSystem com_sys, TCPServerThread server) {
		super();
		this.sock = sock;
		this.com_sys = com_sys;
		this.distant_addr = sock.getInetAddress();
		this.server = server;
	}
	
	public void closeSession() {
		try {
			this.sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		String raw_message;
		BufferedReader input = null;

		while (true) {
			try {

				// We accepted a connection
				// We read what the other session says
	         	input = new BufferedReader(new InputStreamReader(sock.getInputStream()));

				// Wait for the message?
				System.out.println("Waiting for message");
				raw_message = input.readLine();
				System.out.println("TCPSessionThread received message : " + raw_message);
				this.com_sys.receiveMessage(raw_message, this.distant_addr);


			} catch (IOException e) {
				System.out.println("Session ended");
				this.server.removeSession(this);
				assert input != null;
				try {
					input.close();
					return;
				} catch (IOException ex) {/* cannot happen */}
			}
		}

	}

	public Socket getSocket() {
		return this.sock;
	}
}

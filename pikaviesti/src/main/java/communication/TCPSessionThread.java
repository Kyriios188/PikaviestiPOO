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
	
	// Will call the receiveMessage method so it needs the com_sys
	public TCPSessionThread(Socket sock, CommunicationSystem com_sys) {
		super();
		this.sock = sock;
		this.com_sys = com_sys;
		this.distant_addr = sock.getInetAddress();
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
		try {
	        
			// We accepted a connection
			// We read what the other session says
	        BufferedReader input = new BufferedReader(new InputStreamReader(sock.getInputStream()));

			// Wait for the message?
	        while((raw_message = input.readLine()) != null) {
				System.out.println("TCPSessionThread received message : " + raw_message);
				this.com_sys.receiveMessage(raw_message, this.distant_addr);
			}


	        input.close();
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

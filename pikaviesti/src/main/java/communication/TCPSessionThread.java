package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;


// TCPSessionThreads only wait for messages and receive them
// To send messages, the GUI calls the controller who calls the com_sys
public class TCPSessionThread extends Thread {
	
	private Socket sock;
	private CommunicationSystem com_sys;
	private InetAddress distant_addr;
	
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
		
		try {
	        
			// We accepted a connection
			// We read what the other session says
	        BufferedReader input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	        String raw_message = input.readLine();
	        //TODO: wait for the distant host to send messages?
	        System.out.println("TCPSessionThread received message : " + raw_message);
	        this.com_sys.receiveMessage(raw_message, this.distant_addr);
	        
	        //TODO: loop
	        input.close();
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

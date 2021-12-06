package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServerThread extends Thread {

	private int UDP_PORT;
	private DatagramSocket socket;
	private CommunicationSystem com_sys;
	
	public UDPServerThread(CommunicationSystem com_sys, int UDP_PORT) {
		super();
		this.UDP_PORT = UDP_PORT;
		this.com_sys = com_sys;
	}
	
	
	public void stop_server() {
		this.socket.close();
	}
	
	public void run() {
		
		try {
			this.socket = new DatagramSocket(UDP_PORT);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		
		// To receive multiple messages in a row
		// The DatagramSocket.receive() method blocks the thread
		while (true) {
			try {
				
				
				byte[] buffer = new byte[256];
		    	DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
		    	this.socket.receive(inPacket); // Blocks thread
		    	
		    	// Have the communication.CommunicationSystem handle the message
				String raw_message = new String(inPacket.getData(), inPacket.getOffset(), inPacket.getLength());
				this.com_sys.receiveMessage(raw_message, inPacket.getAddress()); //inPacket.getPort()
				
				
			} catch (SocketException e) {
				e.printStackTrace();
				System.exit(-1);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		
		
		
	}
}

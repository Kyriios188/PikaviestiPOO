package communication;

import java.io.IOException;
import java.net.*;

public class UDPServerThread extends Thread {

	private final int UDP_PORT;
	private DatagramSocket socket;
	private final CommunicationSystem com_sys;
	
	public UDPServerThread(CommunicationSystem com_sys, int UDP_PORT) {
		super();
		this.UDP_PORT = UDP_PORT;
		this.com_sys = com_sys;
	}
	
	// We'd rather have the sockets close gracefully on our terms
	public void stop_server() {
		if (!this.socket.isClosed()) {this.socket.close();
		}
	}
	
	public void run() {
		
		try {
			this.socket = new DatagramSocket(null);
			this.socket.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			this.socket.bind(new InetSocketAddress(UDP_PORT));
			this.com_sys.addShutDownHookDatagram(this.socket);
		} catch (IOException e) {
			e.printStackTrace();
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
				this.com_sys.receiveMessage(raw_message, inPacket.getAddress(), null);
				
				
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		
		
		
	}
}

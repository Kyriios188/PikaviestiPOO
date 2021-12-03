import java.io.IOException;
import java.net.*;

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
		
		// To receive multiple messages in a row
		// The DatagramSocket.receive() method blocks the thread
		while (true) {
			try {
				
				this.socket = new DatagramSocket(UDP_PORT);
				byte[] buffer = new byte[256];
		    	DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
		    	this.socket.receive(inPacket); // Blocks thread
		    	
		    	// Have the CommunicationSystem handle the message
				String raw_message = new String(inPacket.getData(), 0, inPacket.getLength());
				this.com_sys.receiveMessage(raw_message);
				
				
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}
}

import java.io.IOException;
import java.net.*;

public class UDPServerThread extends Thread {

	private int UDP_PORT;
	private CommunicationSystem com_sys;
	
	public UDPServerThread(CommunicationSystem com_sys, int UDP_PORT) {
		super();
		this.UDP_PORT = UDP_PORT;
		this.com_sys = com_sys;
	}
	
	public void run() {
		try {
			
			
			DatagramSocket dgramSocket = new DatagramSocket(UDP_PORT);
			byte[] buffer = new byte[256];
	    	DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
	    	dgramSocket.receive(inPacket); // Blocks thread
	    	
	    	
	    	//InetAddress clientAddress = inPacket.getAddress();
			//int clientPort = inPacket.getPort();
			String raw_message = new String(inPacket.getData(), 0, inPacket.getLength());
			
			
			
			
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}

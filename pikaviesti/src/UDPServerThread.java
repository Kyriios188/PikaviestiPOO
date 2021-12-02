import java.io.IOException;
import java.net.*;

public class UDPServerThread extends Thread {

	private int UDP_PORT;
	
	public UDPServerThread(int UDP_PORT) {
		super();
		this.UDP_PORT = UDP_PORT;
	}
	
	public void run() {
		try {
			DatagramSocket dgramSocket = new DatagramSocket(UDP_PORT);
			byte[] buffer = new byte[256];
	    	DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
	    	dgramSocket.receive(inPacket); // Blocks thread
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

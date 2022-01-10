package communication;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;


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

	// Called after the receiveMessage, goes back to the loop once it's over
	public void receiveImage() {

		try {

			InputStream inputStream = sock.getInputStream();

			byte[] sizeAr = new byte[100];
			System.out.println("Waiting for image");
			if (inputStream.read(sizeAr) == -1) {
				System.out.println("End of stream 'error?'");
			}
			int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
			byte[] imageAr = new byte[size];
			if (inputStream.read(imageAr) == -1) {
				System.out.println("No image received");
			}
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));

			// TODO create folder to store images for every user (ex : Robin folder)
			// TODO if can't show image in GUI, use folder. Else, use DB to store.
			ImageIO.write(image, "jpg", new File(System.getProperty("user.dir") + "\\received.jpg"));
			System.out.println("Envoyé à " + System.getProperty("user.dir") + "\\received.jpg");
			// TODO alert user that he just received an image to his folder

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
				this.com_sys.receiveMessage(raw_message, this.distant_addr, this);


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

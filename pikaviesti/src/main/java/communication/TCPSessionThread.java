package communication;

import chatSystem.ChatSystemGUI;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


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
	public void receiveImage(String img_name, String distant_username) {

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

			// Create directory and get the path
			String str_path = System.getProperty("user.dir") + "\\"+ this.com_sys.getLocalId();
			Path path = Paths.get(str_path);
			try {
				Files.createDirectory(path);
			}
			// If the folder exists then cool
			catch (FileAlreadyExistsException e) {/**/}

			// Save the image
			ImageIO.write(image, "jpg", new File(str_path + "\\" + img_name));

			// Open the file
			File file = new File(str_path + "\\" + img_name);
			Desktop desktop = Desktop.getDesktop();
			if(file.exists()) {
				boolean decision = ChatSystemGUI.showConfirm(distant_username+" vous a envoyé une image ("+str_path + "\\" + img_name+"). Souhaitez-vous l'ouvrir ?");
				if (decision) {
					desktop.open(file);
				}
			}

		} catch (IOException ex) {
			ex.printStackTrace();
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
				if (!(raw_message == null)) {
					System.out.println("TCPSessionThread received message : " + raw_message);
					this.com_sys.receiveMessage(raw_message, this.distant_addr, this);
				}


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

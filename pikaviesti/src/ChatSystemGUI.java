

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.*;

public class ChatSystemGUI implements ActionListener {

    JFrame usernameFrame;
    JPanel usernamePanel;
    JTextField usernameAttempt;
    JLabel nameLabel;
    JButton testUsername;
    
    ChatSystemController cs_controller;

    //private GUI user_gui_window;
    private String local_message;


    private void showChatHistory(String target_name) {
    }


    private String getUserMessage() {
    return "";
    }


    public ChatSystemGUI() {
    	
    	// Launches the controller
    	this.cs_controller = new ChatSystemController(this);
    	
        //Create and set up the window.
        usernameFrame = new JFrame("Choose your username");
        usernameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        usernameFrame.setPreferredSize(new Dimension(600, 400));

        //Create and set up the panel.
        usernamePanel = new JPanel(new GridLayout(1, 1));

        //Add the widgets.
        addWidgets();

        //Set the default button.
        usernameFrame.getRootPane().setDefaultButton(testUsername);

        //Add the panel to the window.
        usernameFrame.getContentPane().add(usernamePanel, BorderLayout.EAST);

        //Display the window.
        usernameFrame.pack();
        usernameFrame.setVisible(true);

    }

    /**
     * Create and add the widgets.
     */
    private void addWidgets() {

        usernameAttempt = new JTextField("");

        nameLabel = new JLabel("Username", SwingConstants.LEFT);
        testUsername = new JButton("Choose this username");


        nameLabel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        //Listen to events from the Convert button and
        //the usernameAttempt text field.
        testUsername.addActionListener((ActionListener) this);
        usernameAttempt.addActionListener((ActionListener) this);


        //Add the widgets to the container.
        usernamePanel.add(usernameAttempt);
        usernamePanel.add(nameLabel);
        usernamePanel.add(testUsername);
    }

    public void actionPerformed(ActionEvent event) {
        String eventName = event.getActionCommand();
        
        String name_attempt = usernameAttempt.getText();
        System.out.println("Username : " + name_attempt + "\n");
        this.cs_controller.checkNameUnique(name_attempt);
        
        
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        new ChatSystemGUI();
    }


    private void startSession(String name) {
    }


    private void endSession(String name) {
    }


    private void changeDistantUsername(String old_name, String new_name) {
    }


    private void updateGUImessageReceived(String message_content, String sender_name, LocalTime time) {
    }


    private void updateGUImessageSent(String message_content, String dest_user, LocalTime time) {
    }


    private void showFailureToast() {
    }


    private void showSuccessToast() {
    }


    private void showUserList(String name_array) {
    }

}
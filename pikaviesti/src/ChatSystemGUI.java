import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.*;

public class ChatSystemGUI {

    JFrame usernameFrame;
    JPanel usernamePanel;
    JTextField usernameAttempt;
    JLabel nameLabel;
    JButton testUsername;

    //private GUI user_gui_window;
    private String local_message;


    private void showChatHistory(String target_name) {
    }


    private String getUserMessage() {
    return "";
    }


    private chooseUsername() {
        //Create and set up the window.
        usernameFrame = new JFrame("Choix du nom d'utilisateur");
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
package chatSystem;

import windows.InputLogin;
import windows.ChooseUsername;
import windows.HistoryMessageUserList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static javax.swing.JOptionPane.showMessageDialog;

public class ChatSystemGUI {

    ChatSystemController cs_controller;
    InputLogin login_window;

    private HistoryMessageUserList HstrMssgSrLst;
    private final Connection con;

    public ChatSystemGUI(Connection con) {

        // Launches the controller
        this.cs_controller = new ChatSystemController(this, con);
        this.con = con;

    }


    //************** Windows **************

    public void openLoginWindow() {
        this.login_window = new InputLogin(this, this.cs_controller);
    }

    // Returns the account id or -1 if no account is found/if there's an error
    public int findAccount(String login, String password) {
        return this.cs_controller.findAccount(login, password);
    }

    public boolean createAccount(String login, String password) {
        return this.cs_controller.createAccount(login, password);
    }


    public void openUsernameWindow(boolean state) {
        new ChooseUsername(this, this.cs_controller, state);
    }

    public void openHistoryMessage() {
        this.HstrMssgSrLst = new HistoryMessageUserList(this, this.cs_controller);
    }



    //**************         **************

    // When we start a session
    public void startSession(String name) {
        this.cs_controller.startSessionFromLocal(name);
    }

    // When remote user starts a session
    public void remoteSessionStarted(String username) {
        System.out.println(username + " souhaite clavarder");
    }

    public void endSession(String name) {
        this.cs_controller.endSession(name);
    }

    public void addUserToUserlist(String name) {
        this.HstrMssgSrLst.addUser(name);
    }
    
    public void changeDistantUsername(String old_name, String new_name) {this.HstrMssgSrLst.modifyUser(old_name, new_name);
    }

    public void changeOwnUsername(String new_name) {this.HstrMssgSrLst.setUsername(new_name);}

    public void updateGUIMessageReceived(String message_content, String sender_name, String time) {
        this.HstrMssgSrLst.addFormattedMessage(message_content, sender_name, time);
    }

    public void setGUISelected(String selected) {HstrMssgSrLst.setSelected(selected);}

    public String getGUISelected() {return HstrMssgSrLst.getSelected();}

    public void delGUIUser(String name) {HstrMssgSrLst.delUser(name);}

    public static void showPopup(String message) {showMessageDialog(null, message);}
}
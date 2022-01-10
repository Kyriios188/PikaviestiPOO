package chatSystem;

import windows.InputLogin;
import windows.ChooseUsername;
import windows.HistoryMessage;
import windows.HistoryMessageUserList;
import windows.UserList;

import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;

public class ChatSystemGUI {

    ChatSystemController cs_controller;
    InputLogin login_window;

    private ChooseUsername username_window;
    private UserList userlist_window;
    private HistoryMessageUserList HstrMssgSrLst;
    private Connection con;

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
        int account_id;
        try {
            Statement statement = this.con.createStatement();
            String query1 = "SELECT COUNT(id) AS n FROM accounts WHERE login='"+login+"' AND password='"+password+"'";
            ResultSet rs_number = statement.executeQuery(query1);
            // If we find a matching row in the database
            rs_number.next();
            if (rs_number.getInt("n") == 1) {
                String query2 = "SELECT id AS acc_id FROM accounts WHERE login='"+login+"' AND password='"+password+"'";
                ResultSet rs_id = statement.executeQuery(query2);
                rs_id.next();
                account_id = rs_id.getInt("acc_id");
                System.out.println("Found account : "+account_id);
            }
            else {
                account_id = -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException detected, returning false");
            account_id = -1;
        }
        return account_id;
    }


    public void openUsernameWindow(boolean state) {
        this.username_window =  new ChooseUsername(this, this.cs_controller, state);
    }

    public void openHistoryMessage() {
        this.HstrMssgSrLst = new HistoryMessageUserList(this, this.cs_controller);
    }

    public void openUserList(String name_array) {
        this.userlist_window = new UserList(this, this.cs_controller);
    }


    //**************         **************

    // When we start a session
    public void startSession(String name) {
        this.cs_controller.startSessionFromLocal(name);
    }

    // When remote user starts a session
    // Must update user list, so we can't start the same session twice
    public void remoteSessionStarted(String username) {
        //
    }

    private void endSession(String name) {
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
}
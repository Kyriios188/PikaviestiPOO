package chatSystem;

import windows.InputLogin;
import windows.ChooseUsername;
import windows.HistoryMessage;
import windows.UserList;
import java.time.*;

public class ChatSystemGUI {

    ChatSystemController cs_controller;

    private ChooseUsername username_window;
    private UserList userlist_window;

    public ChatSystemGUI() {

        // Launches the controller
        this.cs_controller = new ChatSystemController(this);

    }


    //************** Windows **************

    public void openLoginWindow() {
        InputLogin NptLgn = new InputLogin(this, this.cs_controller);
    }

    public void openUsernameWindow(boolean state) {
        this.username_window=  new ChooseUsername(this, this.cs_controller, state);
    }

    public void openHistoryMessage() {
        HistoryMessage HstrMssg = new HistoryMessage(this, this.cs_controller);
    }

    public void openUserList(String name_array) {
        this.userlist_window = new UserList(this.cs_controller);
    }


    //**************         **************


    private String getUserMessage() {
        return "";
    }

    private void startSession(String name) {
        this.cs_controller.startSession(name);
    }


    private void endSession(String name) {
        this.cs_controller.endSession(name);
    }

    public void addUserToUserlist(String name) {
        this.userlist_window.addUser(name);
    }
    
    public void changeDistantUsername(String old_name, String new_name) {
    }


    private void updateGUImessageReceived(String message_content, String sender_name, LocalTime time) {
    }


    private void showFailureToast() {
    }


    private void showSuccessToast() {
    }


}
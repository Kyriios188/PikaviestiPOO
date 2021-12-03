package chatSystem;

import chatSystem.ChatSystemController;
import windows.InputLogin;
import windows.ChooseUsername;
import windows.HistoryMessage;
import windows.UserList;
import java.time.*;

public class ChatSystemGUI {

    ChatSystemController cs_controller;

    //private GUI user_gui_window;
    private String local_message;

    public ChatSystemGUI() {

        // Launches the controller
        this.cs_controller = new ChatSystemController(this);

    }


    //************** Windows **************

    public void openLoginWindow() {
        InputLogin NptLgn = new InputLogin(this);
    }

    public void openUsernameWindow() {
        ChooseUsername ChUsrnm = new ChooseUsername(this);
    }

    public void openHistoryMessage() {
        HistoryMessage HstrMssg = new HistoryMessage(this);
    }

    public void openUserList(String name_array) {
        UserList SrLst = new UserList();
    }


    private String getUserMessage() {
        return "";
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


}
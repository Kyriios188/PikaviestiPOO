package chatSystem;

import chatSystem.ChatSystemController;
import windows.InputLogin;
import java.time.*;

public class ChatSystemGUI {

    ChatSystemController cs_controller;

    //private GUI user_gui_window;
    private String local_message;

    public ChatSystemGUI() {

        // Launches the controller
        this.cs_controller = new ChatSystemController(this);

    }

    private void showChatHistory(String target_name) {
    }


    private String getUserMessage() {
    return "";
    }

    public void openLoginWindow() {
        InputLogin ChUsrNm = new InputLogin();
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
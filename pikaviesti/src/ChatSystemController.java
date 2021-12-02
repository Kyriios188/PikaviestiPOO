




public class ChatSystemController {

    private User local_user;


    private ChatSystemGUI gui;


    private CommunicationSystem com_sys;


    private ChatSystemModel cs_model;



    //public chat_history getChatHistory(user target_user) {
    //}


    //public boolean checkNameUnique(String name) {
    //}


    //public void notifyNameChange(user new_user) {
    //}


    //public void stampMessage(message message) {
    //}


    //public void updateGUI(message new_message) {
    //}


    //public void updateChatHistory(message new_message) {
    //}


    //public user findUser(String name) {
    //}


    //public void receiveNotification(user user) {
    //}
    
    public void setLocalUser(String local_name, int local_id) {
    	this.local_user = new User(local_name, local_id);
    }


    public void showUserList() {
    }


    public void createMessage(String input, String username) {
    }

}
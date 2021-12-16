package windows;

import chatSystem.ChatSystemController;
import chatSystem.ChatSystemGUI;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import objects.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;

public class HistoryMessageUserList extends JDialog {
    private JFrame frameLogin = new JFrame();
    private JPanel contentPane;
    private JButton buttonSend;
    private JTextField textField1;
    DefaultListModel listMessageModel = new DefaultListModel();
    DefaultListModel listUserModel = new DefaultListModel();
    private JList<String> listMessage;
    private JList<String> userList;
    private String selected;
    private JButton changeUsernameButton;
    private String messageText;
    private ChatSystemGUI GUI;
    private ChatSystemController controller;

    public HistoryMessageUserList(ChatSystemGUI GUI, ChatSystemController cs_controller) {
        this.GUI = GUI;
        this.controller = cs_controller;

        this.frameLogin.setContentPane(contentPane);
        setModal(true);
        this.frameLogin.getRootPane().setDefaultButton(buttonSend);
        this.setTitle("Chat History");
        this.listMessage.setModel(listMessageModel);
        this.userList.setModel(listUserModel);


        buttonSend.addActionListener(e -> onSEND());
        changeUsernameButton.addActionListener(e -> onChangeUsername());

        // call onCancel() when cross is clicked
        this.frameLogin.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.frameLogin.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // call onSEND() on ENTER
        contentPane.registerKeyboardAction(e -> onSEND(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        userList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selected = userList.getSelectedValue().toString();
                System.out.println("You have selected : " + selected);
                GUI.startSession(selected);
                System.out.println("Start Session with " + selected);
                //addMessage("Start Session with " + selected);
                refreshMessageHistory(selected);
            }
        });

        this.refreshUserList();

        this.frameLogin.pack();
        this.frameLogin.setLocationRelativeTo(null);
        this.frameLogin.setVisible(true);
    }

    private void refreshUserList() {
        for (String name : controller.getStrUserList()) {
            this.addUser(name);
        }
    }

    // Adds name to users in the user list
    public void addUser(String name) {
        listUserModel.addElement(name);
    }

    public void delUser(String name) {
        int index = listUserModel.indexOf(name);
        listUserModel.remove(index);
    }

    // Modify name of user in the user list
    public void modifyUser(String old_name, String new_name) {
        int index = listUserModel.indexOf(old_name);
        listUserModel.setElementAt(new_name, index);
    }

    public void setUsername(String new_name) {
        this.frameLogin.setTitle(new_name);
        this.changeUsernameButton.setText(new_name);
    }

    private void addMessage(String message) {
        listMessageModel.addElement(message);
    }

    public void addFormattedMessage(String message_content, String sender_name, String time) {
        this.addMessage("[" + time + "] <" + sender_name + "> " + message_content);
    }

    public void refreshMessageHistory(String target_user) {
        listMessageModel.removeAllElements();
        for (Message message : controller.getChatHistory(target_user)) {
            try {
                this.addFormattedMessage(message.getContent(), this.controller.getControllerNameFromId(message.getSrcId()), Message.getFormattedTime(message.getTimeStamp()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void onSEND() {
        this.messageText = textField1.getText();
        System.out.println("Message : " + this.messageText);
        this.controller.sendChatMessage(this.selected, this.messageText);
        addFormattedMessage(this.messageText, this.controller.getLocalUser().getName(), Message.getFormattedTime(LocalTime.now()));
        textField1.setText("");
    }

    private void onChangeUsername() {
        System.out.println("Change Username");
        this.GUI.openUsernameWindow(false);
    }

    private void onCancel() {
        System.out.println("Dead");
        this.frameLogin.dispose();
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getSelected() {
        return this.selected;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(3, 2, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        textField1 = new JTextField();
        textField1.setText("");
        panel1.add(textField1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        buttonSend = new JButton();
        buttonSend.setText("Send");
        panel1.add(buttonSend, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        changeUsernameButton = new JButton();
        changeUsernameButton.setText("Change Username");
        panel1.add(changeUsernameButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        contentPane.add(scrollPane1, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        listMessage = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        listMessage.setModel(defaultListModel1);
        scrollPane1.setViewportView(listMessage);
        final JScrollPane scrollPane2 = new JScrollPane();
        contentPane.add(scrollPane2, new GridConstraints(1, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        userList = new JList();
        final DefaultListModel defaultListModel2 = new DefaultListModel();
        userList.setModel(defaultListModel2);
        scrollPane2.setViewportView(userList);
        final JLabel label1 = new JLabel();
        label1.setText("User List");
        contentPane.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}

package windows;

import chatSystem.ChatSystemController;
import chatSystem.ChatSystemGUI;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HistoryMessageUserList extends JDialog {
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

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonSend);
        this.setTitle("Chat History");


        buttonSend.addActionListener(e -> onSEND());
        changeUsernameButton.addActionListener(e -> onChangeUsername());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        userList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selected = userList.getSelectedValue().toString();
                System.out.println("You have selected : " + selected);
                GUI.startSession(selected);
                System.out.println("Start Session with " + selected);
            }
        });

        this.pack();
        this.setVisible(true);
    }

    //private void actualizeUserList() {
    //    for (String name controller.getStrUserList()) {
    //        this.addUser(name);
    //    }
    //}

    // Adds name to users in the user list
    public void addUser(String name) {
        userList.setModel(listUserModel);
        listUserModel.addElement(name);
    }

    private void addMessage(String message) {
        listMessage.setModel(listMessageModel);
        listMessageModel.addElement(message);
    }

    private void onSEND() {
        this.messageText = textField1.getText();
        System.out.println("Message : " + this.messageText);
        this.controller.sendChatMessage(this.selected, this.messageText);
        addMessage(this.messageText);
        textField1.setText("");
    }

    private void onChangeUsername() {
        System.out.println("Change Username");
        this.GUI.openUsernameWindow(false);
    }

    private void onCancel() {
        System.out.println("Dead");
        dispose();
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
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonSend = new JButton();
        buttonSend.setText("Send");
        panel2.add(buttonSend, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField1 = new JTextField();
        textField1.setText("");
        panel1.add(textField1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        panel1.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        changeUsernameButton = new JButton();
        changeUsernameButton.setText("Change Username");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(changeUsernameButton, gbc);
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

package windows;

import javax.swing.*;
import chatSystem.ChatSystemGUI;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class HistoryMessage extends JDialog {
    private JPanel contentPane;
    private JButton buttonSEND;
    private JTextField textField1;
    //private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> listMessage;
    private JButton userListButton;
    private String messageText;
    private ChatSystemGUI GUI;

    public HistoryMessage(ChatSystemGUI GUI) {
        this.GUI = GUI;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonSEND);
        //listMessage = new JList<>(listModel);


        buttonSEND.addActionListener(e -> onSEND());
        userListButton.addActionListener(e -> onUserList());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        this.pack();
        this.setVisible(true);
    }

    private void onSEND() {
        this.messageText = textField1.getText();
        System.out.println("Message : " + this.messageText);
        //listModel.addElement(this.messageText);
        textField1.setText("");
    }

    private void onUserList() {
        System.out.println("Show User List");

        this.GUI.openUserList(null);
    }

    private void onCancel() {
        System.out.println("Dead");
        dispose();
    }

}

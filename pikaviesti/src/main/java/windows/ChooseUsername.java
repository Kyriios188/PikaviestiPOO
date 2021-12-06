package windows;

import javax.swing.*;
import java.awt.event.*;
import chatSystem.ChatSystemGUI;
import communication.CommunicationSystem;
import chatSystem.ChatSystemController;

public class ChooseUsername extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private String username = null;
    private final ChatSystemGUI GUI;

    public ChooseUsername(ChatSystemGUI GUI) {
        this.GUI = GUI;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

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

    private void onOK() {
        this.username = textField1.getText();
        if (!this.username.contains(CommunicationSystem.delimiter)) {
            System.out.println("Username : " + this.username);
            dispose();
            this.GUI.openHistoryMessage();
            ChatSystemController cs_controller = new ChatSystemController(GUI);
            cs_controller.setLocalUser(this.username);
        } else {
            dispose();
            this.GUI.openUsernameWindow();
        }
    }

    private void onCancel() {
        System.out.println("Dead");
        dispose();
    }

    //************** GETTERS **************

    public String getUsername () {
        return this.username;
    }


    //************** SETTERS **************

    public void setUsername (String username) {
        this.username = username;
    }
}

package windows;

import javax.swing.*;
import java.awt.event.*;
import chatSystem.ChatSystemGUI;
import communication.CommunicationSystem;

public class ChooseUsername extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private String username = null;

    public ChooseUsername() {
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
        boolean bien = false;
        this.username = textField1.getText();
        while(!(bien)) {
            if (this.username.indexOf(CommunicationSystem.delimiter) == -1) {
                bien = true;
            }
        }
        System.out.println("Username : " + this.username);
        dispose();
        ChatSystemGUI GUI = new ChatSystemGUI();
        GUI.openHistoryMessage();
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

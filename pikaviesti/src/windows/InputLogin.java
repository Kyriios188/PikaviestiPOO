package windows;

import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;
import chatSystem.ChatSystemGUI;

public class InputLogin extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private String login = null;
    private String psswrd = null;
    private ChatSystemGUI GUI;

    public InputLogin(ChatSystemGUI GUI) {
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
        this.login = textField1.getText();
        this.psswrd = Arrays.toString(passwordField1.getPassword());
        System.out.println("Login : " + this.login);
        System.out.println("Password : " + Arrays.toString(passwordField1.getPassword()));
        dispose();

        this.GUI.openUsernameWindow();
    }

    private void onCancel() {
        System.out.println("Dead");
        dispose();
    }

    //************** GETTERS **************

    public String getLogin () {
        return this.login;
    }

    public String getPassword () {
        return this.psswrd;
    }


    //************** SETTERS **************

    public void setLogin (String login) {
        this.login = login;
    }

}

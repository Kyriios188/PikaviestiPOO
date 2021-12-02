package Windows;

import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;

public class ChooseUsername extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JPasswordField passwordField1;

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
    }

    private void onOK() {
        System.out.println("Username : " + textField1.getText());
        System.out.println("Password : " + Arrays.toString(passwordField1.getPassword()));
        dispose();
    }

    private void onCancel() {
        System.out.println("Dead");
        dispose();
    }

    public static void main(String[] args) {
        ChooseUsername dialog = new ChooseUsername();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}

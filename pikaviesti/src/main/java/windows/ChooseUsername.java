package windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import chatSystem.ChatSystemGUI;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import communication.CommunicationSystem;
import chatSystem.ChatSystemController;
import static javax.swing.JOptionPane.*;

public class ChooseUsername extends JDialog {
    private JFrame frameLogin = new JFrame();
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private String username = null;
    private final ChatSystemGUI GUI;
    private ChatSystemController controller;
    private boolean state;

    public ChooseUsername(ChatSystemGUI GUI, ChatSystemController controller, boolean state) {
        this.GUI = GUI;
        this.controller = controller;
        this.state = state;


        this.frameLogin.setContentPane(contentPane);
        setModal(true);
        this.frameLogin.getRootPane().setDefaultButton(buttonOK);
        this.frameLogin.setTitle("Choose Username");

        if (state) {
            buttonOK.addActionListener(e -> onOK());
            // call onOK() on ENTER
            contentPane.registerKeyboardAction(e -> onOK(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
            buttonCancel.addActionListener(e -> onCancel());

            // call onCancel() when cross is clicked
            this.frameLogin.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            this.frameLogin.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    onCancel();
                }
            });

            // call onCancel() on ESCAPE
            contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        } else {
            buttonOK.addActionListener(e -> onOKNew());
            // call onOKNew() on ENTER
            contentPane.registerKeyboardAction(e -> onOKNew(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
            buttonCancel.addActionListener(e -> onCancelNew());

            // call onCancel() when cross is clicked
            this.frameLogin.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            this.frameLogin.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    onCancelNew();
                }
            });

            // call onCancel() on ESCAPE
            contentPane.registerKeyboardAction(e -> onCancelNew(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        }




        this.frameLogin.setSize(300, 150);
        this.frameLogin.setLocationRelativeTo(null);
        this.frameLogin.setVisible(true);
    }

    private void onOK() {
        this.username = textField1.getText();
        this.frameLogin.dispose();
        if (!this.username.contains(CommunicationSystem.delimiter)) {
            System.out.println("Username : " + this.username);

            // if the name is valid, we are open to communications
            if (this.controller.checkNameUnique(this.username)) {
                this.controller.enableTCPMessaging();
                this.GUI.openHistoryMessage();
                this.GUI.changeOwnUsername(this.username);
            } else {
                showMessageDialog(null, "Your username must be unique", "Error", JOptionPane.ERROR_MESSAGE);
                this.GUI.openUsernameWindow(true);
            }

        } else {
            showMessageDialog(null, "Your username must not contains any character from this list: [\\, " + CommunicationSystem.delimiter + "]", "Error", JOptionPane.ERROR_MESSAGE);
            this.GUI.openUsernameWindow(true);
        }
    }

    private void onOKNew() {
        this.username = textField1.getText();
        if (!this.username.contains(CommunicationSystem.delimiter) & this.controller.checkNameUnique(this.username)) {
            System.out.println("New Username : " + this.username);
            this.GUI.changeOwnUsername(this.username);
            this.frameLogin.dispose();

            //if (!) {
            //    showMessageDialog(null, "Your username must be unique", "Error", JOptionPane.ERROR_MESSAGE);
            //    this.GUI.openUsernameWindow(false);
            //}

        } else {
            this.frameLogin.dispose();
            showMessageDialog(null, "Your username must not contains any character from this list: [\\, " + CommunicationSystem.delimiter + "]", "Error", JOptionPane.ERROR_MESSAGE);
            this.GUI.openUsernameWindow(false);
        }
    }

    private void onCancel() {
        System.out.println("Dead");
        this.controller.postLoginClose();
        this.frameLogin.dispose();
    }

    private void onCancelNew() {
        System.out.println("Dead");
        this.frameLogin.dispose();
    }

    //************** GETTERS **************

    public String getUsername() {
        return this.username;
    }


    //************** SETTERS **************

    public void setUsername(String username) {
        this.username = username;
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
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("OK");
        panel1.add(buttonOK, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel1.add(buttonCancel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        textField1 = new JTextField();
        panel2.add(textField1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Username");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}

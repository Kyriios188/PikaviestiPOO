package windows;

import javax.swing.*;

public class HistoryMessage extends JDialog {
    private JPanel contentPane;
    private JButton buttonSEND;
    private JTextField textField1;
    private JList list1;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> list2;
    private String messageText;

    public HistoryMessage() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonSEND);

        buttonSEND.addActionListener(e -> onSEND());
        list2 = new JList<>(listModel);
    }

    private void onSEND() {
        messageText = textField1.getText();
        System.out.println("Message : " + messageText);
        listModel.addElement(messageText);
        textField1.setText("");
    }

    public static void main(String[] args) {
        HistoryMessage dialog = new HistoryMessage();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}

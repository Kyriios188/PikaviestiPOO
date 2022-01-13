package windows;

import javax.swing.*;
import java.awt.*;

public class UserListCellRenderer extends JPanel implements ListCellRenderer<String> {

    JLabel label;

    public UserListCellRenderer() {
        label = new JLabel();
        add(label);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
        super.setForeground(Color.RED);

        return this;
    }

    public void setNameRed() {
    }

}

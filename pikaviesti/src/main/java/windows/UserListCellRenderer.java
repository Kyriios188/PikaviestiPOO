package windows;

import javax.swing.*;
import java.awt.*;

public class UserListCellRenderer extends JLabel implements ListCellRenderer<String> {

    public UserListCellRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
        super.setForeground(Color.RED);

        return this;
    }

    public void setNameRed() {
    }

}

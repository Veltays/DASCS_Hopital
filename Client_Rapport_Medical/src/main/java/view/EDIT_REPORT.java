package view;

import javax.swing.*;

public class EDIT_REPORT extends JDialog{
    private JPanel panel;
    private JTextField description;
    private JButton saveButton;

    public EDIT_REPORT()
    {
        setTitle("Modifier un rapport");
        setContentPane(panel);
        setModal(true);
        pack();
        setLocationRelativeTo(null);

        saveButton.addActionListener(e -> dispose());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

    }

    public String getDescription() { return description.getText().trim();}
    public JButton getSaveButton() {
        return saveButton;
    }

}



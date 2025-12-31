package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;

public class MainView extends JFrame {
    private JPanel panel;
    private JButton ADDREPORTButton;
    private JButton EDITREPORTButton;
    private JButton LOGOUTButton;
    private JTable table;

    public MainView()
    {
        super("Rapport Médical");
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Patient ID", "Date", "Description"},
                0
        );
        table.setModel(model);
    }
    public JTable getTable() { return table; }
    public JButton getADDREPORTButton() { return ADDREPORTButton; }
    public JButton getEDITREPORTButton() {
        return EDITREPORTButton;
    }
    public JButton getLOGOUTButton() {
        return LOGOUTButton;
    }

    public void addListener (ActionListener listener)
    {
        ADDREPORTButton.addActionListener(listener);
        EDITREPORTButton.addActionListener(listener);
        LOGOUTButton.addActionListener(listener);
    }


}



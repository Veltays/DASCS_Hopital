package view;

import javax.swing.*;
import java.awt.event.ActionListener;

public class MainView extends JFrame {
    private JPanel panel1;
    private JButton ADDREPORTButton;
    private JButton EDITREPORTButton;
    private JButton LISTREPORTButton;
    private JButton LOGOUTButton;
    private JTable table1;

    public MainView()
    {
        super("Rapport Médical");
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

    }

    public JButton getADDREPORTButton() {
        return ADDREPORTButton;
    }

    public JButton getEDITREPORTButton() {
        return EDITREPORTButton;
    }

    public JButton getLISTREPORTButton() {
        return LISTREPORTButton;
    }

    public JButton getLOGOUTButton() {
        return LOGOUTButton;
    }

    public void addListener (ActionListener listener)
    {
        ADDREPORTButton.addActionListener(listener);
        EDITREPORTButton.addActionListener(listener);
        LISTREPORTButton.addActionListener(listener);
        LOGOUTButton.addActionListener(listener);
    }
}



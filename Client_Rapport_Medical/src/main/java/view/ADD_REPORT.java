package view;

import javax.swing.*;

public class ADD_REPORT  extends JDialog{
    private JPanel panel1;
    private JTextField idReport;
    private JTextField idPatient;
    private JTextField dateReport;
    private JTextField description;
    private JButton enregisterButton;

    public ADD_REPORT()
    {
        setTitle("Ajouter un rapport");
        setContentPane(panel1);
        setModal(true);

//        // ✅ Taille et centrage
//        setPreferredSize(new Dimension(400, 200));
//        pack();
//        setLocationRelativeTo(null); // centre la fenêtre sur l’écran

        enregisterButton.addActionListener(e -> dispose());

        //fermer la page avec croix
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

    }



    //////////////////////////
    ////get///////////////
    //////////////////////////

    public String getIdReport() { return idReport.getText().trim();}

    public String getIdPatient() { return idReport.getText().trim(); }

    public String getDateReport() { return dateReport.getText().trim(); }

    public String getDescription() { return description.getText().trim(); }





}

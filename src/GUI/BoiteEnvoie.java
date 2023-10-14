package GUI;

import Email.EnvoieMail;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoiteEnvoie {
    private JTextField textFieldDestinataire;
    private JTextField textFieldObjet;
    private JTextArea textAreaMessage;
    private JList listPJ;
    private JButton buttonEnvoyer;
    private JButton buttonAjouterPieceJointe;
    private JButton buttonSupprimerPièceJointe;
    private JLabel JlabelDestinataire;
    private JLabel JlabelObjet;
    private JLabel JlabelPieceJointe;
    private JPanel panelBoiteEnvoie;
    private String destinataire;
    private String objet;
    private String message;
    EnvoieMail envoie = new EnvoieMail();
    private DefaultListModel<String> listModel;


    public BoiteEnvoie() {
        // Initialisez le modèle de liste
        listModel = new DefaultListModel<>();
        listPJ.setModel(listModel); // Associez le modèle à la JList

        buttonEnvoyer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                destinataire = textFieldDestinataire.getText();
                objet = textFieldObjet.getText();
                message = textAreaMessage.getText();
                envoie.send(destinataire, objet, message);
            }
        });
        buttonAjouterPieceJointe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomPieceJointe = envoie.AjoutPiecesJointes();

                if (nomPieceJointe != null) {
                    // Ajoutez le nom de la pièce jointe au modèle de liste
                    listModel.addElement(nomPieceJointe);
                }
            }
        });
        buttonSupprimerPièceJointe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = listPJ.getSelectedIndex();

                if (selectedIndex >= 0) {
                    // Supprimez la pièce jointe sélectionnée du modèle de liste
                    listModel.remove(selectedIndex);
                    envoie.RetirePiecesJointes();
                }
            }
        });
    }

    public JPanel getPanel() {
        return panelBoiteEnvoie;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Boite d'Envoi");
            frame.setContentPane(new BoiteEnvoie().panelBoiteEnvoie);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}

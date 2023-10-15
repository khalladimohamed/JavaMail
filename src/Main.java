import GUI.BoiteEnvoie;
import GUI.BoiteReception;
import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatDarculaLaf.setup();
            JFrame frame = new JFrame("Application de Messagerie");
            JTabbedPane tabbedPane = new JTabbedPane();

            // Ajouter les onglets
            BoiteEnvoie boiteEnvoie = new BoiteEnvoie();
            BoiteReception boiteReception = new BoiteReception();

            tabbedPane.addTab("Boite d'Envoi", boiteEnvoie.getPanel());
            tabbedPane.addTab("Boite de Réception", boiteReception.getPanel());

            frame.add(tabbedPane);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setVisible(true);
        });
    }
}
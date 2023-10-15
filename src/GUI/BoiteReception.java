package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Email.ReceptionMail;
import com.formdev.flatlaf.FlatDarculaLaf;

public class BoiteReception {
    private JTable tableEmailReception;
    private JPanel panelBoiteReception;
    private JScrollPane JScrollPaneBoiteReception;
    private JTree JtreeMTA;
    private JScrollPane JScrollPaneMTA;

    public BoiteReception() {
        String[] columnNames = {"Sender", "Subject", "Date"};
        DefaultTableModel emailTableModel = new DefaultTableModel(columnNames, 0);
        ReceptionMail threadMail = new ReceptionMail(tableEmailReception, JScrollPaneBoiteReception, emailTableModel, JtreeMTA);
        threadMail.start();
    }

    public JPanel getPanel() {
        return panelBoiteReception;
    }

    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        JFrame frame = new JFrame("Email Client");
        BoiteReception boiteReception = new BoiteReception();
        frame.setContentPane(boiteReception.panelBoiteReception);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

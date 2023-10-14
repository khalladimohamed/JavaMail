package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Email.ReceptionMail;

public class BoiteReception {
    private JTable tableEmailReception;
    private JPanel panel1;
    private JScrollPane JScrollPaneBoiteReception;
    private JTree JtreeMTA;
    private JScrollPane JScrollPaneMTA;

    public BoiteReception() {
        String[] columnNames = {"Sender", "Subject", "Date"};
        DefaultTableModel emailTableModel = new DefaultTableModel(columnNames, 0);

        ReceptionMail threadMail = new ReceptionMail(tableEmailReception, JScrollPaneBoiteReception, emailTableModel, JtreeMTA);
        threadMail.start();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Email Client");
        BoiteReception boiteReception = new BoiteReception();
        frame.setContentPane(boiteReception.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

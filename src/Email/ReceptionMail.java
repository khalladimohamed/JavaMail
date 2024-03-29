package Email;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class ReceptionMail extends Thread{
    static String host = "pop.gmail.com";
    private JTable tableEmailReception;
    private JScrollPane JScrollPaneBoiteReception;
    private DefaultTableModel emailTableModel;
    private JTree JtreeMTA;

    public ReceptionMail(JTable tableEmailReception, JScrollPane JScrollPaneBoiteReception, DefaultTableModel emailTableModel, JTree JtreeMTA)
    {
        this.tableEmailReception = tableEmailReception;
        this.JScrollPaneBoiteReception = JScrollPaneBoiteReception;
        this.emailTableModel = emailTableModel;
        this.JtreeMTA = JtreeMTA;
    }

    public void run()
    {
        int i = 0;
        while(!isInterrupted()) {
            try
            {
                Properties prop = System.getProperties();
                System.out.println("Création d'une session mail");
                prop.put("mail.pop3.host", host);
                prop.put("mail.pop3.port", "995");  // Port POP3 sécurisé
                prop.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                prop.put("mail.pop3.socketFactory.fallback", "false");
                prop.put("mail.pop3.socketFactory.port", "995");  // Utilisation du même port que le serveur

                Session sess = Session.getDefaultInstance(prop, null);

                System.out.println("Obtention d'un objet store");
                Store st = sess.getStore("pop3");

                st.connect(host, "mohamedkhalladihepl@gmail.com", "qemzvbgejalrirwr");
                System.out.println("Obtention d'un objet folder");
                Folder f = st.getFolder("INBOX");
                f.open(Folder.READ_ONLY);

                System.out.println("Obtention des messages");
                Message msg[] = f.getMessages();
                System.out.println("Nombre de messages : " + f.getMessageCount());
                System.out.println("Nombre de nouveaux messages : " + f.getNewMessageCount());
                System.out.println("Liste des messages : ");

                DefaultTreeModel treeModel = (DefaultTreeModel) JtreeMTA.getModel();
                DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel.getRoot();

                if(i == 0)
                {
                    // Réinitialiser le JTree
                    rootNode.removeAllChildren();
                    treeModel.reload();
                }

                for (; i < msg.length; i++)
                {
                    System.out.println("Message n° " + i);
                    System.out.println("Expéditeur : " + msg[i].getFrom() [0]);
                    System.out.println("Sujet = " + msg[i].getSubject());
                    System.out.println("Date : " + msg[i].getSentDate());

                    // Obtenir les en-têtes du message
                    Enumeration<Header> headers = msg[i].getAllHeaders();

                    // Créer un noeud pour les en-têtes du message
                    DefaultMutableTreeNode messageNode = new DefaultMutableTreeNode("Message " + (i + 1));

                    // Parcourir les en-têtes et ajouter-les au noeud
                    while (headers.hasMoreElements()) {
                        Header header = headers.nextElement();
                        String headerName = header.getName();
                        String headerValue = header.getValue();
                        messageNode.add(new DefaultMutableTreeNode(headerName + " --> " + headerValue));
                    }

                    // Ajouter le noeud des en-têtes au modèle du JTree
                    rootNode.add(messageNode);

                    // Mettre à jour le modèle du JTree
                    treeModel.reload();

                    Object[] rowData = {
                            msg[i].getFrom() [0],
                            msg[i].getSubject(),
                            msg[i].getSentDate()
                    };
                    emailTableModel.addRow(rowData);

                    // Récupération du conteneur Multipart
                    // Attention: il faudrait d’abord tester si on a affaire effectivement à un multipart => un if avec un isMimeTYPE()
                    if (msg[i].isMimeType("multipart/*"))
                    {
                        Multipart msgMP = (Multipart)msg[i].getContent();
                        int np = msgMP.getCount();
                        System.out.println("-- Nombre de composantes = " + np);

                        for (int j=0; j<np; j++)
                        {
                            System.out.println("--Composante n° " + j);
                            Part p = msgMP.getBodyPart(j);
                            String d = p.getDisposition();
                            if (p.isMimeType("text/plain"))
                                System.out.println("Texte : " + (String)p.getContent());

                            if (d!=null && d.equalsIgnoreCase(Part.ATTACHMENT))
                            {
                                InputStream is = p.getInputStream();
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                int c;
                                while ((c = is.read()) != -1)
                                    baos.write(c);

                                baos.flush();
                                String nf = p.getFileName();
                                // Nettoyer le nom du fichier en supprimant les caractères non valides
                                // Remplacer les caractères non valides par des underscores
                                nf = nf.replaceAll("[^a-zA-Z0-9.-]", "_");
                                FileOutputStream fos =new FileOutputStream(nf);
                                baos.writeTo(fos);
                                fos.close();
                                System.out.println("Pièce attachée " + nf + " récupérée");
                            }
                        }
                    }
                }

                tableEmailReception = new JTable(emailTableModel);
                JScrollPaneBoiteReception.setViewportView(tableEmailReception);

                System.out.println("Fin des messages");

                System.out.println("sleep 5 min");
                Thread.sleep(10000); //300000 milisec = 5 min - 10000 milisec = 10 sec
            }
            catch (InterruptedException ex) {
                Logger.getLogger(ReceptionMail.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (MessagingException ex) {
                Logger.getLogger(ReceptionMail.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IOException ex) {
                Logger.getLogger(ReceptionMail.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

package Email;

import java.awt.FileDialog;
import java.io.File;
import java.util.Properties;
import java.util.Vector;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JFrame;

public class EnvoieMail {
    static String host = "smtp.gmail.com";
    Vector VPJ = new Vector();

    public String AjoutPiecesJointes()
    {
        String nomfichier = null;

        FileDialog fd = new FileDialog(new JFrame());
        fd.setVisible(true);

        File[] f = fd.getFiles();
        if(f!=null)
        {
            System.out.println(fd.getFiles()[0].getAbsolutePath());
            VPJ.add(fd.getFiles()[0].getAbsolutePath());
            nomfichier = fd.getFiles()[0].getName();
        }

        return nomfichier;
    }

    public void RetirePiecesJointes()
    {
        VPJ.remove(VPJ.size()-1);
    }

    public int send(String dest, String obj, String message)
    {
        Properties prop = System.getProperties();

        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        System.out.println("Création d'une session mail");

        Session sess = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("mohamedkhalladihepl@gmail.com", "qemzvbgejalrirwr");
            }
        });

        try
        {
            System.out.println("Création du message");

            String exp = "mohamedkhalladihepl@gmail.com";

            MimeMessage msg = new MimeMessage(sess);
            msg.setFrom (new InternetAddress(exp));
            msg.setRecipient (Message.RecipientType.TO, new InternetAddress(dest));
            msg.setSubject(obj);

            System.out.println("Début construction du multipart");
            Multipart msgMP = new MimeMultipart();

            //le texte
            MimeBodyPart msgBP = new MimeBodyPart();
            msgBP.setText(message);
            msgMP.addBodyPart(msgBP);

            //la piece jointe
            if(VPJ.size()!=0)
            {
                for(int i=0 ; i<VPJ.size() ; i++)
                {
                    String pieceJointe = VPJ.get(i).toString();

                    System.out.println((i+1) +"ème composante");
                    String acces = pieceJointe;

                    msgBP = new MimeBodyPart();
                    DataSource source = new FileDataSource(acces);
                    msgBP.setDataHandler (new DataHandler (source));

                    msgBP.setFileName(acces);
                    msgMP.addBodyPart(msgBP);
                }
            }

            msg.setContent(msgMP);
            System.out.println("Envoi du message");
            Transport.send(msg);
            System.out.println("Message envoyé");

            return 0;
        }
        catch (MessagingException e)
        {
            System.out.println("Errreur sur message : " + e.getMessage());
            return 1;
        }
        catch (Exception e)
        {
            System.out.println("Errreur sur message : " + e.getMessage());
            return 1;
        }
    }

}


package ec.gob.dinardap.notarialregistral.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.mail.AuthenticationFailedException;
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
import javax.mail.internet.MimeUtility;

/**
 *
 * @author christian.gaona
 */
public class Email {

    private final Properties prop;
    private static final String FROM = "leonardo.munoz@dinardap.gob.ec";
    private URI uri;

    public Email() {
        ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            uri = new URI(ext.getRequestScheme(),
                    null, ext.getRequestServerName(), ext.getRequestServerPort(),
                    ext.getRequestContextPath(), null, null);
        } catch (URISyntaxException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        }
        prop = new Properties();
        prop.put("mail.smtp.host", "mail.dinardap.gob.ec");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "false");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.transport.protocol", "smtp");
    }

    public void sendMail(String para,
            String asunto,
            String cuerpo) throws AuthenticationFailedException,
            MessagingException {

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("leonardo.munoz@dinardap.gob.ec", "G3n3r1ca.321");
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(para));
        try {
            if (para != null && !para.isEmpty()) {
                message.setSubject(MimeUtility.encodeText(asunto, "UTF-8", "Q"));
                Multipart multipartes = new MimeMultipart();
                MimeBodyPart htmlPart = new MimeBodyPart();

                String cabecera = "<html><body><center><h1>Plataforma de Remanentes</h1></center><br/>";
                String contenido = "<center><p>" + cuerpo + "</p></center><br/>";
                //String boton = "<center><a href='" + uri.toASCIIString() + "'>Plataforma Remanentes</a></center>";
                String formulario = String.format("%s%s", cabecera, contenido);

                htmlPart.setContent(formulario, "text/html; charset=utf-8");
                multipartes.addBodyPart(htmlPart);
                message.setContent(multipartes);
                //Transport.send(message);
                
                
                MimeMessage mimeMessage = new MimeMessage(session);
                mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(para));
                mimeMessage.setContent(multipartes);
                Transport transport = session.getTransport("smtp");

                transport.connect("10.0.113.40", 465, "leonardo.munoz@dinardap.gob.ec", "G3n3r1ca.321");
                transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
                transport.close();
            }
        } catch (AuthenticationFailedException e) {
            throw e;
        } catch (MessagingException ex) {
            throw ex;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
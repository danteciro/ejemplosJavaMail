package com.pe.Email;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class MandaMail {
	 public void enviarMail(String mailOrigen, String mailDestinatario, String asunto, String textoMensaje)
             throws AddressException, MessagingException {

     //se puede poner en true para ver la interaccion del protocolo smtp por stdout.
     boolean debug = false;

     //setea el host y puerto donde esta el servidor de mail.
     Properties props = new Properties();
     props.put("mail.smtp.host", "localhost");
     props.put("mail.smtp.port", 25);

     Session session = Session.getDefaultInstance(props, null);
     session.setDebug(debug);

     // create a message
     Message msg = new MimeMessage(session);

     // set the from and to address
     InternetAddress addressFrom = new InternetAddress(mailOrigen);
     msg.setFrom(addressFrom);

     InternetAddress addressTo = new InternetAddress(mailDestinatario);
     msg.setRecipient(Message.RecipientType.TO, addressTo);

     // Setting the Subject and Content Type
     msg.setSubject(asunto);
     msg.setContent(textoMensaje, "text/plain");
     Transport.send(msg);
 }
}

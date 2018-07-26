package com.pe.Email;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class SendEmail
{
 public static void main(String [] args){
      String to = "dartica@gmail.com";//change accordingly
      String from = "malkosac@gmail.com";//change accordingly
      String host = "localhost";//or IP address

     //Get the session object
      Properties properties = System.getProperties();
      properties.setProperty("mail.smtp.host", host);
      Session session = Session.getDefaultInstance(properties);

     //compose the message
      try{
         MimeMessage message = new MimeMessage(session);
         message.setFrom(new InternetAddress(from));
         message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
         message.setSubject("Ping");
         message.setText("Hello, this is example of sending email  ");
         
         // Send message
         Transport.send(message);
         System.out.println("message sent successfully....");
         //muestra el contenido
         System.out.println("--------------------------------------");
                System.out.println("Asunto: " + message.getSubject());
   

      }catch (MessagingException mex) {mex.printStackTrace();}
   }
}

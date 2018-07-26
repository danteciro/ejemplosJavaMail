package com.pe.Email;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

public class OutlookReader_imap {

    Folder inbox;

    // Constructor of the calss.

    public OutlookReader_imap() {
        System.out.println("Inside MailReader()...");
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        /* Set the mail properties */
        /*
        props.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(props);
        MimeMessage msg = new MimeMessage(session);
        // set the message content here
        Transport.send(msg, username, password);
        */


        Properties props = System.getProperties();
        // Set manual Properties
        props.setProperty("mail.imaps.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.imaps.socketFactory.fallback", "false");
        props.setProperty("mail.imaps.port", "993");
        props.setProperty("mail.imaps.socketFactory.port", "993");
        props.put("mail.imaps.host", "imap-mail.outlook.com");


        try {
            /* Create the session and get the store for read the mail. */

            Session session = Session.getDefaultInstance(System.getProperties(), null);
            Store store = session.getStore("imaps");

            store.connect("imap-mail.outlook.com", 993, "dante.artica@mdp.com.pe", "password");
            seObtieneFoldersCorreo(store);
            /* Mention the folder name which you want to read. */

            // inbox = store.getDefaultFolder();
            // inbox = inbox.getFolder("INBOX");
            inbox = store.getFolder("INBOX");

            /* Open the inbox using store. */

            inbox.open(Folder.READ_ONLY);

            Message messages[] = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            
            System.out.println("No. of Unread Messages : " + inbox.getUnreadMessageCount());

            /* Use a suitable FetchProfile */
            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);

            inbox.fetch(messages, fp);

            try {

                printAllMessages(messages);

                inbox.close(true);
                store.close();

            } catch (Exception ex) {
                System.out.println("Exception arise at the time of read mail");
                ex.printStackTrace();
            }

        } catch (MessagingException e) {
            System.out.println("Exception while connecting to server: " + e.getLocalizedMessage());
            e.printStackTrace();
            System.exit(2);
        }

    }

    public void printAllMessages(Message[] msgs) throws Exception {
        for (int i = 0; i < msgs.length; i++) {
            System.out.println("MESSAGE #" + (i + 1) + ":");
            printEnvelope(msgs[i]);
        }
    }

    public void printEnvelope(Message message) throws Exception {

        Address[] a;

        // FROM
        if ((a = message.getFrom()) != null) {
            for (int j = 0; j < a.length; j++) {
                System.out.println("De : " + a[j].toString());
            }
        }

        String subject = message.getSubject();

        Date receivedDate = message.getReceivedDate();
        Date sentDate = message.getSentDate(); // receivedDate is returning
        // null. So used getSentDate()

        //Dar Formato a la fecha
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        System.out.println("Asunto : " + subject);

        if (receivedDate != null) {
            System.out.println("Recibido: " + df.format(receivedDate));
        }

        System.out.println("Enviado : " + df.format(sentDate));
    }


    public static void main(String args[]) {
        new OutlookReader_imap();
    }
    
    private static void seObtieneFoldersCorreo(Store store) throws MessagingException {
    	javax.mail.Folder[] folders = store.getDefaultFolder().list("*");
        for (javax.mail.Folder folder : folders) {
            if ((folder.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0) {
                System.out.println("foldername->"+folder.getFullName() + " folder msg count->" + folder.getMessageCount());
                Folder folderName = store.getFolder(folder.getFullName() );
                folderName.open(Folder.READ_ONLY);
                seObtieneMensajes(folderName);
                folderName.close(true);
            					}
        					}
    			}
    
    private static void seObtieneMensajes(javax.mail.Folder folder) throws MessagingException {
    	
    	
    	Message[] msgs = folder.getMessages();

        FetchProfile fp = new FetchProfile();
        fp.add(FetchProfile.Item.ENVELOPE);
        fp.add("X-mailer");
        folder.fetch(msgs, fp);
        for (int i = 0; i < folder.getMessageCount(); i++) {
      	  System.out.println(msgs[i].getFrom());
      	  System.out.println(msgs[i].getSubject());
      	  System.out.println(msgs[i].getHeader("X-mailer"));
      	
        }
    }
    
}
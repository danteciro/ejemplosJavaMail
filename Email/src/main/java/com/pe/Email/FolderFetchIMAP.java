package com.pe.Email;

import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.internet.*;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.IMAPStore;


public class FolderFetchIMAP {


    public static void main(String[] args) throws MessagingException, IOException {
        IMAPFolder folder = null;
        Store store = null;
        String subject = null;
        Flag flag = null;
        try 
        {
          Properties props = System.getProperties();
          props.setProperty("mail.store.protocol", "imaps");

          Session session = Session.getDefaultInstance(props, null);

          store = session.getStore("imaps");
          store.connect("imap.gmail.com","dartica@gmail.com", "password");
          
          seObtieneFoldersCorreo(store);
          
          
          
         /**
          folder = (IMAPFolder) store.getFolder("[Gmail]/Spam"); // This doesn't work for other email account
          //folder = (IMAPFolder) store.getFolder("inbox"); This works for both email account


          if(!folder.isOpen())
          folder.open(Folder.READ_WRITE);
          Message[] messages = folder.getMessages();
          System.out.println("No of Messages : " + folder.getMessageCount());
          System.out.println("No of Unread Messages : " + folder.getUnreadMessageCount());
          System.out.println(messages.length);
          for (int i=0; i < messages.length;i++) 
          {

            System.out.println("*****************************************************************************");
            System.out.println("MESSAGE " + (i + 1) + ":");
            Message msg =  messages[i];
            //System.out.println(msg.getMessageNumber());
            //Object String;
            //System.out.println(folder.getUID(msg)

            subject = msg.getSubject();

            System.out.println("Subject: " + subject);
            System.out.println("From: " + msg.getFrom()[0]);
           System.out.println("To: "+msg.getAllRecipients()[0]);
            System.out.println("Date: "+msg.getReceivedDate());
            System.out.println("Size: "+msg.getSize());
            System.out.println(msg.getFlags());
            System.out.println("Body: \n"+ msg.getContent());
            System.out.println(msg.getContentType());

          }
          */
        }
        finally 
        {
          if (folder != null && folder.isOpen()) { folder.close(true); }
          if (store != null) { store.close(); }
        }

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
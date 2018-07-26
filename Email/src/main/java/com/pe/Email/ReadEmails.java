package com.pe.Email;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.FolderNotFoundException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.ReadOnlyFolderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.StoreClosedException;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;

/**
* @author Eduardo Bregaida
* 
*/
public class ReadEmails {
private Store store = null;
private Folder folder = null;
private Message message = null;
private Message[] messages = null;
private Object msgObj = null;
private String sender = null;
@SuppressWarnings("unused")
private String subject = null;
private Multipart multipart = null;
private Part part = null;
private String contentType = null;

public ReadEmails() throws MessagingException {
processMail();
}

/**
* Processa o e-mail
* 
*/
public void processMail() throws MessagingException {
try {
store = conexaoServidorEMail();
folder = getPastaCaixaEntrada(store);
messages = folder.getMessages();

for (int messageNumber = 0; messageNumber < messages.length; messageNumber++) {
message = messages[messageNumber];
msgObj = message.getContent();

// Determine o tipo de email
if (msgObj instanceof Multipart) {

subject = message.getSubject();
multipart = (Multipart) message.getContent();

for (int i = 0; i < multipart.getCount(); i++) {

part = multipart.getBodyPart(i);
// pegando um tipo do conteúdo
contentType = part.getContentType();

String fileName2 = part.getFileName();
if(fileName2 != null) {
System.out.println(messageNumber + " " + fileName2 + " | " + message.getSubject());
}
fileName2 = null;

// Tela do conteúdo
if (contentType.startsWith("text/plain")) {
} else {
String fileName = part.getFileName();
@SuppressWarnings("unused")
Message[] mensagensXML = separaMensagensXML(i, fileName);

}
}
} else {
sender = ((InternetAddress) message.getFrom()[0]).getPersonal();
if (sender == null) {
sender = ((InternetAddress) message.getFrom()[0]).getAddress();
}
// Get the subject information
subject = message.getSubject();
}
}
// Fecha a pasta
folder.close(true);
// Histório de mensagens
store.close();
System.out.println("Terminado");
} catch (AuthenticationFailedException e) {
store.close();
e.printStackTrace();
} catch (FolderClosedException e) {
store.close();
e.printStackTrace();
} catch (FolderNotFoundException e) {
store.close();
e.printStackTrace();
} catch (NoSuchProviderException e) {
store.close();
e.printStackTrace();
} catch (ReadOnlyFolderException e) {
store.close();
e.printStackTrace();
} catch (StoreClosedException e) {
store.close();
e.printStackTrace();
} catch (Exception e) {
store.close();
e.printStackTrace();
}
}

/**
* @param i
* @param fileName
* @return
* @throws MessagingException
* @throws IOException
*/
private Message[] separaMensagensXML(int i, String fileName) throws MessagingException, IOException {
Message[] mensagensXML = folder.getMessages();;
if (fileName != null) {
int tamanhoString = fileName.length() - 3;
for (int a = 0; a < messages.length; a++) {
if (fileName.substring(tamanhoString).equals("xml")) {
mensagensXML[a] = message;
}
}
}
// Recebendo o nome do arquivo
@SuppressWarnings("unused")
String fileName2 = validarXML(part, store, folder, mensagensXML, i);
return mensagensXML;
}

/**
* @param messages
* @param i
* @throws MessagingException
*/
private void excluirMensagemInbox(Message[] messages, int i) throws MessagingException {
@SuppressWarnings("unused")
BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
messages[i].setFlag(Flags.Flag.DELETED, true);
}

/**
* Envia os arquivos da pasta princiál para a pasta reserva
* 
* @param store
* @param folder
* @param messages
* @throws MessagingException
*/
private boolean enviaArquivoPastaAuxiliar(Store store, Folder folder, Message[] messages, int i) throws MessagingException {

Folder folderAux;
folderAux = getPastaAuxiliar(store);
folder.copyMessages(messages, folderAux);
folderAux.close(true);
excluirMensagemInbox(messages, i);

return false;
}

/**
* Recebe o anexo e valida se é um XML, se sim ele salva o arquivo em uma
* pasta
* 
* @param part
* @return
* @throws MessagingException
* @throws IOException
*/
private String validarXML(Part part, Store store, Folder folder, Message[] messages, int i) throws MessagingException, IOException {
String fileName = part.getFileName();
if (fileName != null) {
int tamanhoString = fileName.length() - 3;
if (!fileName.substring(tamanhoString).equals("xml")) {
return fileName;
} else {
String disposition = part.getDisposition();
if ((disposition != null) && ((disposition.equals(Part.ATTACHMENT) || (disposition.equals(Part.INLINE))))) {
salvarArquivo(part);
enviaArquivoPastaAuxiliar(store, folder, messages, i);
}
}
}
return fileName;
}

/**
* Salva o arquivo em uma pasta
* 
* @param part
* @throws MessagingException
* @throws IOException
*/
private void salvarArquivo(Part part) throws IOException, MessagingException {
FileOutputStream fileOutputStream = new FileOutputStream(Constantes.PASTA_XML + part.getFileName());
Object obj = part.getContent();
if (obj instanceof InputStream) {
InputStream is = (InputStream) obj;
int ch = -1;
while ((ch = is.read()) != -1) {
fileOutputStream.write(ch);
}
}
}

/**
* Acessa a Caixa de Entrada (Inbox)
* 
* @param store
* @return
* @throws MessagingException
*/
private Folder getPastaCaixaEntrada(Store store) throws MessagingException {
Folder folder;
folder = store.getFolder(Constantes.PASTA_PRINCIPAL);
folder.open(Folder.READ_WRITE);
return folder;
}

/**
* Acessa a Pasta Auxiliar
* 
* @param store
* @return
* @throws MessagingException
*/
private Folder getPastaAuxiliar(Store store) throws MessagingException {
Folder folder;
folder = store.getFolder(Constantes.PASTA_BACKUP);
folder.open(Folder.READ_WRITE);
return folder;
}

/**
* Autenticação e conexão com o Servidor de e-mail
* 
* @return
* @throws NoSuchProviderException
* @throws MessagingException
*/
private Store conexaoServidorEMail() throws NoSuchProviderException, MessagingException {
Session session;
Store store;
Properties prop = new Properties();
prop.setProperty("mail.store.protocol", "imaps");
prop.setProperty("mail.imaps.host", "imap.gmail.com");
prop.setProperty("mail.imaps.port", "993");
prop.setProperty("mail.imaps.partialfetch", "false");


session = Session.getInstance(prop);
URLName url = new URLName(Constantes.IMAP, Constantes.HOST, Constantes.PORTA, Constantes.ARQUIVO_MSG, Constantes.LOGIN, Constantes.SENHA);
store = session.getStore("imaps");
store.connect();

return store;
}
}
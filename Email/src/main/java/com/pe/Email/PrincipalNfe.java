package com.pe.Email;

import javax.mail.MessagingException; 

/** 
* @author Eduardo Bregaida 
* 
*/ 
public class PrincipalNfe { 

public static void main(String[] args) { 
@SuppressWarnings("unused") 
ReadEmails readMail =null; 
try { 
readMail = new ReadEmails(); 
} catch (MessagingException e) { 
e.printStackTrace(); 
} 

} 

} 


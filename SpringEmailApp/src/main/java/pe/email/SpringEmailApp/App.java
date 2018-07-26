package pe.email.SpringEmailApp;


import java.util.HashMap;
import java.util.Map;
 
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
 

 
public class App {
 
    public static void main(String args[]) {
 
        Mail mail = new Mail();
        mail.setMailFrom("dartica@gmail.com");
        mail.setMailTo("dartica@gmail.com");
        mail.setMailSubject("Spring 4 - Email with velocity template");
 
        Map < String, Object > model = new HashMap < String, Object > ();
        model.put("firstName", "Yashwant");
        model.put("lastName", "Chavan");
        model.put("location", "Pune");
        model.put("signature", "www.technicalkeeda.com");
        mail.setModel(model);
 
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        MailService mailService = (MailService) context.getBean("mailService");
        mailService.sendEmail(mail);
        context.close();
    }
 
}
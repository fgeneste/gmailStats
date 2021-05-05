package fr.geneste.web.rest;

import fr.geneste.service.GmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.Address;
import javax.mail.Message;

/**
 * GmailResource controller
 */
@RestController
@RequestMapping("/api/gmail")
public class GmailResource {

    private final Logger log = LoggerFactory.getLogger(GmailResource.class);

    private final GmailService gmailService;

    public GmailResource(GmailService gmailService){
        this.gmailService = gmailService;
    }

    @GetMapping("/reset-mails")
    public void resetMails() {
        gmailService.resetMessageInDatabase();
    }

    /**
     * GET getMails
     */
    @GetMapping("/get-mails")
    public String getMails() throws Exception {
        gmailService.login("imap.gmail.com", "fgeneste",
            "mot_de_passe");
        int messageCount = gmailService.getMessageCount();

        //just for tutorial purpose
        if (messageCount > 5)
            messageCount = 5;
        Message[] messages = gmailService.getMessages();
        for (int i = 0; i < messageCount; i++) {
            fr.geneste.domain.Message m = new fr.geneste.domain.Message();
            String subject = "";
            if (messages[i].getSubject() != null)
                subject = messages[i].getSubject();
            Address[] fromAddress = messages[i].getFrom();

            m.setDate(messages[i].getReceivedDate().toInstant());
            m.setCorps(messages[i].getContent().toString());
            m.setFrom(gmailService.getFromToString(fromAddress));
            m.setObject(messages[i].getSubject());
            gmailService.saveMessageInBase(m);
        }
        gmailService.logout();
        return "getMails";
    }

    public static void main(String args[]) throws Exception  //static method
    {
        GmailService mailService = new GmailService();
        mailService.login("imap.gmail.com", "fgeneste",
            "mot_de_passe");
        int messageCount = mailService.getMessageCount();
        System.out.println(messageCount);
        //just for tutorial purpose
        if (messageCount > 5)
            messageCount = 5;
        Message[] messages = mailService.getMessages();
        for (int i = 0; i < messageCount; i++) {
            System.out.println("-------------------------------------------------------");
            String subject = "";
            if (messages[i].getSubject() != null)
                subject = messages[i].getSubject();
            System.out.println(subject);
            Address[] fromAddress = messages[i].getFrom();
            System.out.println(messages[i].getReceivedDate());
            System.out.println(mailService.getFromToString(fromAddress));
        }
        mailService.logout();
    }
}

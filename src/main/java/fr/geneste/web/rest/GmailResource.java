package fr.geneste.web.rest;

import com.sun.mail.util.BASE64DecoderStream;
import fr.geneste.domain.Attachement;
import fr.geneste.service.GmailService;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;


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

    public int current = 0;
    public int totalmessages = 0;
    public boolean stop = false;

    @GetMapping("/reset-mails")
    public void resetMails() {
        gmailService.resetMessageInDatabase();
    }

    @PostMapping("/stop-get-mails")
    public void stopGetMails() {
        stop=true;
    }

    /**
     * GET getMails
     */
    @GetMapping("/get-mails")
    public String getMails() throws Exception {
        stop=false;
        gmailService.login("imap.gmail.com", gmailService.getAccount(),
            gmailService.getPassword());
        int messageCount = gmailService.getMessageCount();

        //just for tutorial purpose
        //messageCount = 50;
            totalmessages = messageCount;
        Message[] messages = gmailService.getMessages();
        for (int i = 0; i < messageCount && !stop; i++) {
            System.out.println(i+1 + "/" + messageCount);
            current = i+1;
            fr.geneste.domain.Message m = new fr.geneste.domain.Message();
            m.setAccount(gmailService.getAccount());
            String subject = "";
            if (messages[i].getSubject() != null)
                subject = messages[i].getSubject();
            Address[] fromAddress = messages[i].getFrom();

            m.setDate(messages[i].getReceivedDate().toInstant());
            m.setCorps(getTextFromMessage(messages[i]));
            m.setFrom(gmailService.getFromToString(fromAddress));
            m.setObject(messages[i].getSubject());
            /*HashSet<Attachement> attachements = getFileFromMessage(messages[i], m);
            if(attachements!=null && !attachements.isEmpty())
                m.setAttachements(attachements);*/
            gmailService.saveMessageInBase(m);
        }
        gmailService.logout();
        return String.valueOf(messageCount);
    }

    @GetMapping("/get-fetched-mails")
    public String getunfetchedMails() throws Exception {
        /*gmailService.login("imap.gmail.com", gmailService.getAccount(),
            gmailService.getPassword());
        int messageCount = gmailService.getMessageCount();

        //just for tutorial purpose
        messageCount = 50;
        gmailService.logout();*/
        return String.valueOf(totalmessages);
    }

    @GetMapping("/get-unfetched-mails")
    public String getfetchedMails() throws Exception {
        return String.valueOf(current);
    }

    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(
        MimeMultipart mimeMultipart)  throws MessagingException, IOException{
        String result = "";
        int count = mimeMultipart.getCount();
        try {
            for (int i = 0; i < count; i++) {
                BodyPart bodyPart = mimeMultipart.getBodyPart(i);
                if (bodyPart.isMimeType("text/plain")) {
                    result = result + "\n" + bodyPart.getContent();
                    break; // without break same text appears twice in my tests
                } else if (bodyPart.isMimeType("text/html")) {
                    String html = (String) bodyPart.getContent();
                    result = result + "\n" + Jsoup.parse(html).text();
                } else if (bodyPart.getContent() instanceof MimeMultipart) {
                    result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
                }
            }
        }
        catch (Exception e){
            return e.getMessage();
        }

        return result;
    }

    private HashSet<Attachement> getFileFromMessage(Message message, fr.geneste.domain.Message m) throws MessagingException, IOException {
        HashSet<Attachement> result = new HashSet<>();
        if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            //Multipart multiPart = (Multipart) message.getContent();
            int numberOfParts = mimeMultipart.getCount();
            for (int partCount = 0; partCount < numberOfParts; partCount++) {
                MimeBodyPart part = (MimeBodyPart) mimeMultipart.getBodyPart(partCount);
                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                    Attachement a = new Attachement();
                    // this part is attachment
                    String fileName = part.getFileName();
                    System.out.print(fileName);
                    a.setFileContentType(part.getContentType());
                    BASE64DecoderStream stream = (BASE64DecoderStream)part.getContent();
                    a.setFile(stream.readAllBytes());
                    a.setMessage(m);
                    result.add(a);
                }
            }
        }
        else
            return null;

        return result;
    }

    public static void main(String args[]) throws Exception  //static method
    {
        /*GmailService mailService = new GmailService();
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
        mailService.logout();*/
    }
}

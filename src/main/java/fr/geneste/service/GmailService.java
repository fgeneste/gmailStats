package fr.geneste.service;

import fr.geneste.repository.MessageRepository;
import fr.geneste.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Properties;

import javax.mail.*;

@Service
@Transactional
public class GmailService {

    private final Logger log = LoggerFactory.getLogger(GmailService.class);

    private MessageRepository messageRepository;

    private Session session;
    private Store store;
    private Folder folder;

    // hardcoding protocol and the folder
    // it can be parameterized and enhanced as required
    private String protocol = "imaps";
    private String file = "INBOX";

    public GmailService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public GmailService() {
    }

    public void resetMessageInDatabase(){
        messageRepository.deleteAll();
    }

    public void saveMessageInBase(fr.geneste.domain.Message message){
        messageRepository.save(message);
    }

    public boolean isLoggedIn() {
        return store.isConnected();
    }

    /**
     * to login to the mail host server
     */
    public void login(String host, String username, String password)
        throws Exception {
        URLName url = new URLName(protocol, host, 993, file, username, password);

        if (session == null) {
            Properties props = null;
            try {
                props = System.getProperties();
            } catch (SecurityException sex) {
                props = new Properties();
            }
            session = Session.getInstance(props, null);
        }
        store = session.getStore(url);
        store.connect();
        folder = store.getFolder(url);

        folder.open(Folder.READ_WRITE);
    }

    /**
     * to logout from the mail host server
     */
    public void logout() throws MessagingException {
        folder.close(false);
        store.close();
        store = null;
        session = null;
    }

    public int getMessageCount() {
        int messageCount = 0;
        try {
            messageCount = folder.getMessageCount();
        } catch (MessagingException me) {
            me.printStackTrace();
        }
        return messageCount;
    }

    public Message[] getMessages() throws MessagingException {
        return folder.getMessages();
    }

    public String getFromToString(Address[] addresses){
        String add = new String();
        for( Address addresse : addresses ) {
            if(addresse.toString().contains("<")){
                int indexFrom = addresse.toString().indexOf("<")+1;
                int indexTo = addresse.toString().indexOf(">");
                add += addresse.toString().substring(indexFrom, indexTo) + ";";
            }
            else
                add += addresse + ";";
        }
        return add.substring(0, add.lastIndexOf(";"));
    }
}

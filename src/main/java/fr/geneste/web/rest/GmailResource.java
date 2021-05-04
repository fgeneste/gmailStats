package fr.geneste.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * GmailResource controller
 */
@RestController
@RequestMapping("/api/gmail")
public class GmailResource {

    private final Logger log = LoggerFactory.getLogger(GmailResource.class);

    /**
     * GET getMails
     */
    @GetMapping("/get-mails")
    public String getMails() {
        return "getMails";
    }
}

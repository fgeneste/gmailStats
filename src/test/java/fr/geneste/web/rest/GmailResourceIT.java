package fr.geneste.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.geneste.IntegrationTest;
import fr.geneste.repository.MessageRepository;
import fr.geneste.repository.UserRepository;
import fr.geneste.service.GmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Test class for the GmailResource REST controller.
 *
 * @see GmailResource
 */
@IntegrationTest
class GmailResourceIT {

    private MockMvc restMockMvc;

    @Autowired
    private GmailService gmailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        GmailResource gmailResource = new GmailResource(gmailService);
        restMockMvc = MockMvcBuilders.standaloneSetup(gmailResource).build();
    }

    /**
     * Test getMails
     */
    @Test
    void testGetMails() throws Exception {
        restMockMvc.perform(get("/api/gmail/get-mails")).andExpect(status().isOk());
    }
}

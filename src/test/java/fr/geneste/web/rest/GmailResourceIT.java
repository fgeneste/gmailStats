package fr.geneste.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.geneste.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        GmailResource gmailResource = new GmailResource();
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

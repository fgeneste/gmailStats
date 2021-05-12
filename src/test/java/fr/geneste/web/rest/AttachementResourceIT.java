package fr.geneste.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.geneste.IntegrationTest;
import fr.geneste.domain.Attachement;
import fr.geneste.repository.AttachementRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link AttachementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AttachementResourceIT {

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/attachements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AttachementRepository attachementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAttachementMockMvc;

    private Attachement attachement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attachement createEntity(EntityManager em) {
        Attachement attachement = new Attachement().file(DEFAULT_FILE).fileContentType(DEFAULT_FILE_CONTENT_TYPE);
        return attachement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attachement createUpdatedEntity(EntityManager em) {
        Attachement attachement = new Attachement().file(UPDATED_FILE).fileContentType(UPDATED_FILE_CONTENT_TYPE);
        return attachement;
    }

    @BeforeEach
    public void initTest() {
        attachement = createEntity(em);
    }

    @Test
    @Transactional
    void createAttachement() throws Exception {
        int databaseSizeBeforeCreate = attachementRepository.findAll().size();
        // Create the Attachement
        restAttachementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attachement)))
            .andExpect(status().isCreated());

        // Validate the Attachement in the database
        List<Attachement> attachementList = attachementRepository.findAll();
        assertThat(attachementList).hasSize(databaseSizeBeforeCreate + 1);
        Attachement testAttachement = attachementList.get(attachementList.size() - 1);
        assertThat(testAttachement.getFile()).isEqualTo(DEFAULT_FILE);
        assertThat(testAttachement.getFileContentType()).isEqualTo(DEFAULT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createAttachementWithExistingId() throws Exception {
        // Create the Attachement with an existing ID
        attachement.setId(1L);

        int databaseSizeBeforeCreate = attachementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttachementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attachement)))
            .andExpect(status().isBadRequest());

        // Validate the Attachement in the database
        List<Attachement> attachementList = attachementRepository.findAll();
        assertThat(attachementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAttachements() throws Exception {
        // Initialize the database
        attachementRepository.saveAndFlush(attachement);

        // Get all the attachementList
        restAttachementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attachement.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))));
    }

    @Test
    @Transactional
    void getAttachement() throws Exception {
        // Initialize the database
        attachementRepository.saveAndFlush(attachement);

        // Get the attachement
        restAttachementMockMvc
            .perform(get(ENTITY_API_URL_ID, attachement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attachement.getId().intValue()))
            .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.file").value(Base64Utils.encodeToString(DEFAULT_FILE)));
    }

    @Test
    @Transactional
    void getNonExistingAttachement() throws Exception {
        // Get the attachement
        restAttachementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAttachement() throws Exception {
        // Initialize the database
        attachementRepository.saveAndFlush(attachement);

        int databaseSizeBeforeUpdate = attachementRepository.findAll().size();

        // Update the attachement
        Attachement updatedAttachement = attachementRepository.findById(attachement.getId()).get();
        // Disconnect from session so that the updates on updatedAttachement are not directly saved in db
        em.detach(updatedAttachement);
        updatedAttachement.file(UPDATED_FILE).fileContentType(UPDATED_FILE_CONTENT_TYPE);

        restAttachementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAttachement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAttachement))
            )
            .andExpect(status().isOk());

        // Validate the Attachement in the database
        List<Attachement> attachementList = attachementRepository.findAll();
        assertThat(attachementList).hasSize(databaseSizeBeforeUpdate);
        Attachement testAttachement = attachementList.get(attachementList.size() - 1);
        assertThat(testAttachement.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testAttachement.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingAttachement() throws Exception {
        int databaseSizeBeforeUpdate = attachementRepository.findAll().size();
        attachement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttachementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attachement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attachement in the database
        List<Attachement> attachementList = attachementRepository.findAll();
        assertThat(attachementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAttachement() throws Exception {
        int databaseSizeBeforeUpdate = attachementRepository.findAll().size();
        attachement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attachement in the database
        List<Attachement> attachementList = attachementRepository.findAll();
        assertThat(attachementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAttachement() throws Exception {
        int databaseSizeBeforeUpdate = attachementRepository.findAll().size();
        attachement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attachement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Attachement in the database
        List<Attachement> attachementList = attachementRepository.findAll();
        assertThat(attachementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAttachementWithPatch() throws Exception {
        // Initialize the database
        attachementRepository.saveAndFlush(attachement);

        int databaseSizeBeforeUpdate = attachementRepository.findAll().size();

        // Update the attachement using partial update
        Attachement partialUpdatedAttachement = new Attachement();
        partialUpdatedAttachement.setId(attachement.getId());

        partialUpdatedAttachement.file(UPDATED_FILE).fileContentType(UPDATED_FILE_CONTENT_TYPE);

        restAttachementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttachement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttachement))
            )
            .andExpect(status().isOk());

        // Validate the Attachement in the database
        List<Attachement> attachementList = attachementRepository.findAll();
        assertThat(attachementList).hasSize(databaseSizeBeforeUpdate);
        Attachement testAttachement = attachementList.get(attachementList.size() - 1);
        assertThat(testAttachement.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testAttachement.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateAttachementWithPatch() throws Exception {
        // Initialize the database
        attachementRepository.saveAndFlush(attachement);

        int databaseSizeBeforeUpdate = attachementRepository.findAll().size();

        // Update the attachement using partial update
        Attachement partialUpdatedAttachement = new Attachement();
        partialUpdatedAttachement.setId(attachement.getId());

        partialUpdatedAttachement.file(UPDATED_FILE).fileContentType(UPDATED_FILE_CONTENT_TYPE);

        restAttachementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttachement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttachement))
            )
            .andExpect(status().isOk());

        // Validate the Attachement in the database
        List<Attachement> attachementList = attachementRepository.findAll();
        assertThat(attachementList).hasSize(databaseSizeBeforeUpdate);
        Attachement testAttachement = attachementList.get(attachementList.size() - 1);
        assertThat(testAttachement.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testAttachement.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingAttachement() throws Exception {
        int databaseSizeBeforeUpdate = attachementRepository.findAll().size();
        attachement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttachementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attachement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attachement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attachement in the database
        List<Attachement> attachementList = attachementRepository.findAll();
        assertThat(attachementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAttachement() throws Exception {
        int databaseSizeBeforeUpdate = attachementRepository.findAll().size();
        attachement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attachement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attachement in the database
        List<Attachement> attachementList = attachementRepository.findAll();
        assertThat(attachementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAttachement() throws Exception {
        int databaseSizeBeforeUpdate = attachementRepository.findAll().size();
        attachement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(attachement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Attachement in the database
        List<Attachement> attachementList = attachementRepository.findAll();
        assertThat(attachementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAttachement() throws Exception {
        // Initialize the database
        attachementRepository.saveAndFlush(attachement);

        int databaseSizeBeforeDelete = attachementRepository.findAll().size();

        // Delete the attachement
        restAttachementMockMvc
            .perform(delete(ENTITY_API_URL_ID, attachement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Attachement> attachementList = attachementRepository.findAll();
        assertThat(attachementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package fr.geneste.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.geneste.IntegrationTest;
import fr.geneste.domain.Conf;
import fr.geneste.repository.ConfRepository;
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

/**
 * Integration tests for the {@link ConfResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConfResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/confs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConfRepository confRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConfMockMvc;

    private Conf conf;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conf createEntity(EntityManager em) {
        Conf conf = new Conf().key(DEFAULT_KEY).value(DEFAULT_VALUE);
        return conf;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conf createUpdatedEntity(EntityManager em) {
        Conf conf = new Conf().key(UPDATED_KEY).value(UPDATED_VALUE);
        return conf;
    }

    @BeforeEach
    public void initTest() {
        conf = createEntity(em);
    }

    @Test
    @Transactional
    void createConf() throws Exception {
        int databaseSizeBeforeCreate = confRepository.findAll().size();
        // Create the Conf
        restConfMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conf)))
            .andExpect(status().isCreated());

        // Validate the Conf in the database
        List<Conf> confList = confRepository.findAll();
        assertThat(confList).hasSize(databaseSizeBeforeCreate + 1);
        Conf testConf = confList.get(confList.size() - 1);
        assertThat(testConf.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testConf.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createConfWithExistingId() throws Exception {
        // Create the Conf with an existing ID
        conf.setId(1L);

        int databaseSizeBeforeCreate = confRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conf)))
            .andExpect(status().isBadRequest());

        // Validate the Conf in the database
        List<Conf> confList = confRepository.findAll();
        assertThat(confList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConfs() throws Exception {
        // Initialize the database
        confRepository.saveAndFlush(conf);

        // Get all the confList
        restConfMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conf.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getConf() throws Exception {
        // Initialize the database
        confRepository.saveAndFlush(conf);

        // Get the conf
        restConfMockMvc
            .perform(get(ENTITY_API_URL_ID, conf.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(conf.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingConf() throws Exception {
        // Get the conf
        restConfMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewConf() throws Exception {
        // Initialize the database
        confRepository.saveAndFlush(conf);

        int databaseSizeBeforeUpdate = confRepository.findAll().size();

        // Update the conf
        Conf updatedConf = confRepository.findById(conf.getId()).get();
        // Disconnect from session so that the updates on updatedConf are not directly saved in db
        em.detach(updatedConf);
        updatedConf.key(UPDATED_KEY).value(UPDATED_VALUE);

        restConfMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConf.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConf))
            )
            .andExpect(status().isOk());

        // Validate the Conf in the database
        List<Conf> confList = confRepository.findAll();
        assertThat(confList).hasSize(databaseSizeBeforeUpdate);
        Conf testConf = confList.get(confList.size() - 1);
        assertThat(testConf.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testConf.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingConf() throws Exception {
        int databaseSizeBeforeUpdate = confRepository.findAll().size();
        conf.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfMockMvc
            .perform(
                put(ENTITY_API_URL_ID, conf.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conf))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conf in the database
        List<Conf> confList = confRepository.findAll();
        assertThat(confList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConf() throws Exception {
        int databaseSizeBeforeUpdate = confRepository.findAll().size();
        conf.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conf))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conf in the database
        List<Conf> confList = confRepository.findAll();
        assertThat(confList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConf() throws Exception {
        int databaseSizeBeforeUpdate = confRepository.findAll().size();
        conf.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conf)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Conf in the database
        List<Conf> confList = confRepository.findAll();
        assertThat(confList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConfWithPatch() throws Exception {
        // Initialize the database
        confRepository.saveAndFlush(conf);

        int databaseSizeBeforeUpdate = confRepository.findAll().size();

        // Update the conf using partial update
        Conf partialUpdatedConf = new Conf();
        partialUpdatedConf.setId(conf.getId());

        partialUpdatedConf.key(UPDATED_KEY).value(UPDATED_VALUE);

        restConfMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConf.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConf))
            )
            .andExpect(status().isOk());

        // Validate the Conf in the database
        List<Conf> confList = confRepository.findAll();
        assertThat(confList).hasSize(databaseSizeBeforeUpdate);
        Conf testConf = confList.get(confList.size() - 1);
        assertThat(testConf.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testConf.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateConfWithPatch() throws Exception {
        // Initialize the database
        confRepository.saveAndFlush(conf);

        int databaseSizeBeforeUpdate = confRepository.findAll().size();

        // Update the conf using partial update
        Conf partialUpdatedConf = new Conf();
        partialUpdatedConf.setId(conf.getId());

        partialUpdatedConf.key(UPDATED_KEY).value(UPDATED_VALUE);

        restConfMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConf.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConf))
            )
            .andExpect(status().isOk());

        // Validate the Conf in the database
        List<Conf> confList = confRepository.findAll();
        assertThat(confList).hasSize(databaseSizeBeforeUpdate);
        Conf testConf = confList.get(confList.size() - 1);
        assertThat(testConf.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testConf.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingConf() throws Exception {
        int databaseSizeBeforeUpdate = confRepository.findAll().size();
        conf.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, conf.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conf))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conf in the database
        List<Conf> confList = confRepository.findAll();
        assertThat(confList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConf() throws Exception {
        int databaseSizeBeforeUpdate = confRepository.findAll().size();
        conf.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conf))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conf in the database
        List<Conf> confList = confRepository.findAll();
        assertThat(confList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConf() throws Exception {
        int databaseSizeBeforeUpdate = confRepository.findAll().size();
        conf.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(conf)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Conf in the database
        List<Conf> confList = confRepository.findAll();
        assertThat(confList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConf() throws Exception {
        // Initialize the database
        confRepository.saveAndFlush(conf);

        int databaseSizeBeforeDelete = confRepository.findAll().size();

        // Delete the conf
        restConfMockMvc
            .perform(delete(ENTITY_API_URL_ID, conf.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Conf> confList = confRepository.findAll();
        assertThat(confList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

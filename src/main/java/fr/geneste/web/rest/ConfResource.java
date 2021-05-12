package fr.geneste.web.rest;

import fr.geneste.domain.Conf;
import fr.geneste.repository.ConfRepository;
import fr.geneste.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.geneste.domain.Conf}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ConfResource {

    private final Logger log = LoggerFactory.getLogger(ConfResource.class);

    private static final String ENTITY_NAME = "conf";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfRepository confRepository;

    public ConfResource(ConfRepository confRepository) {
        this.confRepository = confRepository;
    }

    /**
     * {@code POST  /confs} : Create a new conf.
     *
     * @param conf the conf to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new conf, or with status {@code 400 (Bad Request)} if the conf has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/confs")
    public ResponseEntity<Conf> createConf(@RequestBody Conf conf) throws URISyntaxException {
        log.debug("REST request to save Conf : {}", conf);
        if (conf.getId() != null) {
            throw new BadRequestAlertException("A new conf cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Conf result = confRepository.save(conf);
        return ResponseEntity
            .created(new URI("/api/confs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /confs/:id} : Updates an existing conf.
     *
     * @param id the id of the conf to save.
     * @param conf the conf to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conf,
     * or with status {@code 400 (Bad Request)} if the conf is not valid,
     * or with status {@code 500 (Internal Server Error)} if the conf couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/confs/{id}")
    public ResponseEntity<Conf> updateConf(@PathVariable(value = "id", required = false) final Long id, @RequestBody Conf conf)
        throws URISyntaxException {
        log.debug("REST request to update Conf : {}, {}", id, conf);
        if (conf.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conf.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!confRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Conf result = confRepository.save(conf);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, conf.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /confs/:id} : Partial updates given fields of an existing conf, field will ignore if it is null
     *
     * @param id the id of the conf to save.
     * @param conf the conf to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conf,
     * or with status {@code 400 (Bad Request)} if the conf is not valid,
     * or with status {@code 404 (Not Found)} if the conf is not found,
     * or with status {@code 500 (Internal Server Error)} if the conf couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/confs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Conf> partialUpdateConf(@PathVariable(value = "id", required = false) final Long id, @RequestBody Conf conf)
        throws URISyntaxException {
        log.debug("REST request to partial update Conf partially : {}, {}", id, conf);
        if (conf.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conf.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!confRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Conf> result = confRepository
            .findById(conf.getId())
            .map(
                existingConf -> {
                    if (conf.getKey() != null) {
                        existingConf.setKey(conf.getKey());
                    }
                    if (conf.getValue() != null) {
                        existingConf.setValue(conf.getValue());
                    }

                    return existingConf;
                }
            )
            .map(confRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, conf.getId().toString())
        );
    }

    /**
     * {@code GET  /confs} : get all the confs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of confs in body.
     */
    @GetMapping("/confs")
    public List<Conf> getAllConfs() {
        log.debug("REST request to get all Confs");
        return confRepository.findAll();
    }

    /**
     * {@code GET  /confs/:id} : get the "id" conf.
     *
     * @param id the id of the conf to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the conf, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/confs/{id}")
    public ResponseEntity<Conf> getConf(@PathVariable Long id) {
        log.debug("REST request to get Conf : {}", id);
        Optional<Conf> conf = confRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(conf);
    }

    /**
     * {@code DELETE  /confs/:id} : delete the "id" conf.
     *
     * @param id the id of the conf to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/confs/{id}")
    public ResponseEntity<Void> deleteConf(@PathVariable Long id) {
        log.debug("REST request to delete Conf : {}", id);
        confRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

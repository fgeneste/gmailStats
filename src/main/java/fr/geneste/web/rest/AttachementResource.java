package fr.geneste.web.rest;

import fr.geneste.domain.Attachement;
import fr.geneste.repository.AttachementRepository;
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
 * REST controller for managing {@link fr.geneste.domain.Attachement}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AttachementResource {

    private final Logger log = LoggerFactory.getLogger(AttachementResource.class);

    private static final String ENTITY_NAME = "attachement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttachementRepository attachementRepository;

    public AttachementResource(AttachementRepository attachementRepository) {
        this.attachementRepository = attachementRepository;
    }

    /**
     * {@code POST  /attachements} : Create a new attachement.
     *
     * @param attachement the attachement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attachement, or with status {@code 400 (Bad Request)} if the attachement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attachements")
    public ResponseEntity<Attachement> createAttachement(@RequestBody Attachement attachement) throws URISyntaxException {
        log.debug("REST request to save Attachement : {}", attachement);
        if (attachement.getId() != null) {
            throw new BadRequestAlertException("A new attachement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Attachement result = attachementRepository.save(attachement);
        return ResponseEntity
            .created(new URI("/api/attachements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /attachements/:id} : Updates an existing attachement.
     *
     * @param id the id of the attachement to save.
     * @param attachement the attachement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attachement,
     * or with status {@code 400 (Bad Request)} if the attachement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attachement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attachements/{id}")
    public ResponseEntity<Attachement> updateAttachement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Attachement attachement
    ) throws URISyntaxException {
        log.debug("REST request to update Attachement : {}, {}", id, attachement);
        if (attachement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attachement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attachementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Attachement result = attachementRepository.save(attachement);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attachement.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /attachements/:id} : Partial updates given fields of an existing attachement, field will ignore if it is null
     *
     * @param id the id of the attachement to save.
     * @param attachement the attachement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attachement,
     * or with status {@code 400 (Bad Request)} if the attachement is not valid,
     * or with status {@code 404 (Not Found)} if the attachement is not found,
     * or with status {@code 500 (Internal Server Error)} if the attachement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/attachements/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Attachement> partialUpdateAttachement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Attachement attachement
    ) throws URISyntaxException {
        log.debug("REST request to partial update Attachement partially : {}, {}", id, attachement);
        if (attachement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attachement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attachementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Attachement> result = attachementRepository
            .findById(attachement.getId())
            .map(
                existingAttachement -> {
                    if (attachement.getFile() != null) {
                        existingAttachement.setFile(attachement.getFile());
                    }
                    if (attachement.getFileContentType() != null) {
                        existingAttachement.setFileContentType(attachement.getFileContentType());
                    }

                    return existingAttachement;
                }
            )
            .map(attachementRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attachement.getId().toString())
        );
    }

    /**
     * {@code GET  /attachements} : get all the attachements.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attachements in body.
     */
    @GetMapping("/attachements")
    public List<Attachement> getAllAttachements() {
        log.debug("REST request to get all Attachements");
        return attachementRepository.findAll();
    }

    /**
     * {@code GET  /attachements/:id} : get the "id" attachement.
     *
     * @param id the id of the attachement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attachement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/attachements/{id}")
    public ResponseEntity<Attachement> getAttachement(@PathVariable Long id) {
        log.debug("REST request to get Attachement : {}", id);
        Optional<Attachement> attachement = attachementRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(attachement);
    }

    /**
     * {@code DELETE  /attachements/:id} : delete the "id" attachement.
     *
     * @param id the id of the attachement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/attachements/{id}")
    public ResponseEntity<Void> deleteAttachement(@PathVariable Long id) {
        log.debug("REST request to delete Attachement : {}", id);
        attachementRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

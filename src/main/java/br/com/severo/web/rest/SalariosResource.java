package br.com.severo.web.rest;

import br.com.severo.repository.SalariosRepository;
import br.com.severo.service.SalariosService;
import br.com.severo.service.dto.SalariosDTO;
import br.com.severo.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.severo.domain.Salarios}.
 */
@RestController
@RequestMapping("/api")
public class SalariosResource {

    private final Logger log = LoggerFactory.getLogger(SalariosResource.class);

    private static final String ENTITY_NAME = "salarios";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SalariosService salariosService;

    private final SalariosRepository salariosRepository;

    public SalariosResource(SalariosService salariosService, SalariosRepository salariosRepository) {
        this.salariosService = salariosService;
        this.salariosRepository = salariosRepository;
    }

    /**
     * {@code POST  /salarios} : Create a new salarios.
     *
     * @param salariosDTO the salariosDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new salariosDTO, or with status {@code 400 (Bad Request)} if the salarios has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/salarios")
    public ResponseEntity<SalariosDTO> createSalarios(@RequestBody SalariosDTO salariosDTO) throws URISyntaxException {
        log.debug("REST request to save Salarios : {}", salariosDTO);
        if (salariosDTO.getId() != null) {
            throw new BadRequestAlertException("A new salarios cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SalariosDTO result = salariosService.save(salariosDTO);
        return ResponseEntity
            .created(new URI("/api/salarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /salarios/:id} : Updates an existing salarios.
     *
     * @param id the id of the salariosDTO to save.
     * @param salariosDTO the salariosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salariosDTO,
     * or with status {@code 400 (Bad Request)} if the salariosDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the salariosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/salarios/{id}")
    public ResponseEntity<SalariosDTO> updateSalarios(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SalariosDTO salariosDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Salarios : {}, {}", id, salariosDTO);
        if (salariosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salariosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!salariosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SalariosDTO result = salariosService.update(salariosDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, salariosDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /salarios/:id} : Partial updates given fields of an existing salarios, field will ignore if it is null
     *
     * @param id the id of the salariosDTO to save.
     * @param salariosDTO the salariosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salariosDTO,
     * or with status {@code 400 (Bad Request)} if the salariosDTO is not valid,
     * or with status {@code 404 (Not Found)} if the salariosDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the salariosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/salarios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SalariosDTO> partialUpdateSalarios(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SalariosDTO salariosDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Salarios partially : {}, {}", id, salariosDTO);
        if (salariosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salariosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!salariosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SalariosDTO> result = salariosService.partialUpdate(salariosDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, salariosDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /salarios} : get all the salarios.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of salarios in body.
     */
    @GetMapping("/salarios")
    public ResponseEntity<List<SalariosDTO>> getAllSalarios(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Salarios");
        Page<SalariosDTO> page = salariosService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /salarios/:id} : get the "id" salarios.
     *
     * @param id the id of the salariosDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the salariosDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/salarios/{id}")
    public ResponseEntity<SalariosDTO> getSalarios(@PathVariable Long id) {
        log.debug("REST request to get Salarios : {}", id);
        Optional<SalariosDTO> salariosDTO = salariosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(salariosDTO);
    }

    /**
     * {@code DELETE  /salarios/:id} : delete the "id" salarios.
     *
     * @param id the id of the salariosDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/salarios/{id}")
    public ResponseEntity<Void> deleteSalarios(@PathVariable Long id) {
        log.debug("REST request to delete Salarios : {}", id);
        salariosService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

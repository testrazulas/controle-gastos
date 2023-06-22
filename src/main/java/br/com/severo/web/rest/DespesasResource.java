package br.com.severo.web.rest;

import br.com.severo.repository.DespesasRepository;
import br.com.severo.service.DespesasService;
import br.com.severo.service.dto.DespesasDTO;
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
 * REST controller for managing {@link br.com.severo.domain.Despesas}.
 */
@RestController
@RequestMapping("/api")
public class DespesasResource {

    private final Logger log = LoggerFactory.getLogger(DespesasResource.class);

    private static final String ENTITY_NAME = "despesas";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DespesasService despesasService;

    private final DespesasRepository despesasRepository;

    public DespesasResource(DespesasService despesasService, DespesasRepository despesasRepository) {
        this.despesasService = despesasService;
        this.despesasRepository = despesasRepository;
    }

    /**
     * {@code POST  /despesas} : Create a new despesas.
     *
     * @param despesasDTO the despesasDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new despesasDTO, or with status {@code 400 (Bad Request)} if the despesas has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/despesas")
    public ResponseEntity<DespesasDTO> createDespesas(@RequestBody DespesasDTO despesasDTO) throws URISyntaxException {
        log.debug("REST request to save Despesas : {}", despesasDTO);
        if (despesasDTO.getId() != null) {
            throw new BadRequestAlertException("A new despesas cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DespesasDTO result = despesasService.save(despesasDTO);
        return ResponseEntity
            .created(new URI("/api/despesas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /despesas/:id} : Updates an existing despesas.
     *
     * @param id the id of the despesasDTO to save.
     * @param despesasDTO the despesasDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated despesasDTO,
     * or with status {@code 400 (Bad Request)} if the despesasDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the despesasDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/despesas/{id}")
    public ResponseEntity<DespesasDTO> updateDespesas(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DespesasDTO despesasDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Despesas : {}, {}", id, despesasDTO);
        if (despesasDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, despesasDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!despesasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DespesasDTO result = despesasService.update(despesasDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, despesasDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /despesas/:id} : Partial updates given fields of an existing despesas, field will ignore if it is null
     *
     * @param id the id of the despesasDTO to save.
     * @param despesasDTO the despesasDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated despesasDTO,
     * or with status {@code 400 (Bad Request)} if the despesasDTO is not valid,
     * or with status {@code 404 (Not Found)} if the despesasDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the despesasDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/despesas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DespesasDTO> partialUpdateDespesas(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DespesasDTO despesasDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Despesas partially : {}, {}", id, despesasDTO);
        if (despesasDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, despesasDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!despesasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DespesasDTO> result = despesasService.partialUpdate(despesasDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, despesasDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /despesas} : get all the despesas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of despesas in body.
     */
    @GetMapping("/despesas")
    public ResponseEntity<List<DespesasDTO>> getAllDespesas(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Despesas");
        Page<DespesasDTO> page = despesasService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /despesas/:id} : get the "id" despesas.
     *
     * @param id the id of the despesasDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the despesasDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/despesas/{id}")
    public ResponseEntity<DespesasDTO> getDespesas(@PathVariable Long id) {
        log.debug("REST request to get Despesas : {}", id);
        Optional<DespesasDTO> despesasDTO = despesasService.findOne(id);
        return ResponseUtil.wrapOrNotFound(despesasDTO);
    }

    /**
     * {@code DELETE  /despesas/:id} : delete the "id" despesas.
     *
     * @param id the id of the despesasDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/despesas/{id}")
    public ResponseEntity<Void> deleteDespesas(@PathVariable Long id) {
        log.debug("REST request to delete Despesas : {}", id);
        despesasService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

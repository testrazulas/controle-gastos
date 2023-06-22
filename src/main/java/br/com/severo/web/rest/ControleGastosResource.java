package br.com.severo.web.rest;

import br.com.severo.repository.ControleGastosRepository;
import br.com.severo.service.ControleGastosService;
import br.com.severo.service.dto.ControleGastosDTO;
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
 * REST controller for managing {@link br.com.severo.domain.ControleGastos}.
 */
@RestController
@RequestMapping("/api")
public class ControleGastosResource {

    private final Logger log = LoggerFactory.getLogger(ControleGastosResource.class);

    private static final String ENTITY_NAME = "controleGastos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ControleGastosService controleGastosService;

    private final ControleGastosRepository controleGastosRepository;

    public ControleGastosResource(ControleGastosService controleGastosService, ControleGastosRepository controleGastosRepository) {
        this.controleGastosService = controleGastosService;
        this.controleGastosRepository = controleGastosRepository;
    }

    /**
     * {@code POST  /controle-gastos} : Create a new controleGastos.
     *
     * @param controleGastosDTO the controleGastosDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new controleGastosDTO, or with status {@code 400 (Bad Request)} if the controleGastos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/controle-gastos")
    public ResponseEntity<ControleGastosDTO> createControleGastos(@RequestBody ControleGastosDTO controleGastosDTO)
        throws URISyntaxException {
        log.debug("REST request to save ControleGastos : {}", controleGastosDTO);
        if (controleGastosDTO.getId() != null) {
            throw new BadRequestAlertException("A new controleGastos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ControleGastosDTO result = controleGastosService.save(controleGastosDTO);
        return ResponseEntity
            .created(new URI("/api/controle-gastos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /controle-gastos/:id} : Updates an existing controleGastos.
     *
     * @param id the id of the controleGastosDTO to save.
     * @param controleGastosDTO the controleGastosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated controleGastosDTO,
     * or with status {@code 400 (Bad Request)} if the controleGastosDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the controleGastosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/controle-gastos/{id}")
    public ResponseEntity<ControleGastosDTO> updateControleGastos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ControleGastosDTO controleGastosDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ControleGastos : {}, {}", id, controleGastosDTO);
        if (controleGastosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, controleGastosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!controleGastosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ControleGastosDTO result = controleGastosService.update(controleGastosDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, controleGastosDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /controle-gastos/:id} : Partial updates given fields of an existing controleGastos, field will ignore if it is null
     *
     * @param id the id of the controleGastosDTO to save.
     * @param controleGastosDTO the controleGastosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated controleGastosDTO,
     * or with status {@code 400 (Bad Request)} if the controleGastosDTO is not valid,
     * or with status {@code 404 (Not Found)} if the controleGastosDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the controleGastosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/controle-gastos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ControleGastosDTO> partialUpdateControleGastos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ControleGastosDTO controleGastosDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ControleGastos partially : {}, {}", id, controleGastosDTO);
        if (controleGastosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, controleGastosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!controleGastosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ControleGastosDTO> result = controleGastosService.partialUpdate(controleGastosDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, controleGastosDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /controle-gastos} : get all the controleGastos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of controleGastos in body.
     */
    @GetMapping("/controle-gastos")
    public ResponseEntity<List<ControleGastosDTO>> getAllControleGastos(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ControleGastos");
        Page<ControleGastosDTO> page = controleGastosService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /controle-gastos/:id} : get the "id" controleGastos.
     *
     * @param id the id of the controleGastosDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the controleGastosDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/controle-gastos/{id}")
    public ResponseEntity<ControleGastosDTO> getControleGastos(@PathVariable Long id) {
        log.debug("REST request to get ControleGastos : {}", id);
        Optional<ControleGastosDTO> controleGastosDTO = controleGastosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(controleGastosDTO);
    }

    /**
     * {@code DELETE  /controle-gastos/:id} : delete the "id" controleGastos.
     *
     * @param id the id of the controleGastosDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/controle-gastos/{id}")
    public ResponseEntity<Void> deleteControleGastos(@PathVariable Long id) {
        log.debug("REST request to delete ControleGastos : {}", id);
        controleGastosService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

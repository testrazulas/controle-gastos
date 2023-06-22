package br.com.severo.web.rest;

import br.com.severo.repository.CartoesRepository;
import br.com.severo.service.CartoesService;
import br.com.severo.service.dto.CartoesDTO;
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
 * REST controller for managing {@link br.com.severo.domain.Cartoes}.
 */
@RestController
@RequestMapping("/api")
public class CartoesResource {

    private final Logger log = LoggerFactory.getLogger(CartoesResource.class);

    private static final String ENTITY_NAME = "cartoes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CartoesService cartoesService;

    private final CartoesRepository cartoesRepository;

    public CartoesResource(CartoesService cartoesService, CartoesRepository cartoesRepository) {
        this.cartoesService = cartoesService;
        this.cartoesRepository = cartoesRepository;
    }

    /**
     * {@code POST  /cartoes} : Create a new cartoes.
     *
     * @param cartoesDTO the cartoesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cartoesDTO, or with status {@code 400 (Bad Request)} if the cartoes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cartoes")
    public ResponseEntity<CartoesDTO> createCartoes(@RequestBody CartoesDTO cartoesDTO) throws URISyntaxException {
        log.debug("REST request to save Cartoes : {}", cartoesDTO);
        if (cartoesDTO.getId() != null) {
            throw new BadRequestAlertException("A new cartoes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CartoesDTO result = cartoesService.save(cartoesDTO);
        return ResponseEntity
            .created(new URI("/api/cartoes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cartoes/:id} : Updates an existing cartoes.
     *
     * @param id the id of the cartoesDTO to save.
     * @param cartoesDTO the cartoesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cartoesDTO,
     * or with status {@code 400 (Bad Request)} if the cartoesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cartoesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cartoes/{id}")
    public ResponseEntity<CartoesDTO> updateCartoes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CartoesDTO cartoesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Cartoes : {}, {}", id, cartoesDTO);
        if (cartoesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cartoesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cartoesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CartoesDTO result = cartoesService.update(cartoesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cartoesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cartoes/:id} : Partial updates given fields of an existing cartoes, field will ignore if it is null
     *
     * @param id the id of the cartoesDTO to save.
     * @param cartoesDTO the cartoesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cartoesDTO,
     * or with status {@code 400 (Bad Request)} if the cartoesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cartoesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cartoesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cartoes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CartoesDTO> partialUpdateCartoes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CartoesDTO cartoesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cartoes partially : {}, {}", id, cartoesDTO);
        if (cartoesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cartoesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cartoesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CartoesDTO> result = cartoesService.partialUpdate(cartoesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cartoesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cartoes} : get all the cartoes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cartoes in body.
     */
    @GetMapping("/cartoes")
    public ResponseEntity<List<CartoesDTO>> getAllCartoes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Cartoes");
        Page<CartoesDTO> page = cartoesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cartoes/:id} : get the "id" cartoes.
     *
     * @param id the id of the cartoesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cartoesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cartoes/{id}")
    public ResponseEntity<CartoesDTO> getCartoes(@PathVariable Long id) {
        log.debug("REST request to get Cartoes : {}", id);
        Optional<CartoesDTO> cartoesDTO = cartoesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cartoesDTO);
    }

    /**
     * {@code DELETE  /cartoes/:id} : delete the "id" cartoes.
     *
     * @param id the id of the cartoesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cartoes/{id}")
    public ResponseEntity<Void> deleteCartoes(@PathVariable Long id) {
        log.debug("REST request to delete Cartoes : {}", id);
        cartoesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

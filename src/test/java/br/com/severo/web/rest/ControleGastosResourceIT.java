package br.com.severo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.severo.IntegrationTest;
import br.com.severo.domain.ControleGastos;
import br.com.severo.repository.ControleGastosRepository;
import br.com.severo.service.dto.ControleGastosDTO;
import br.com.severo.service.mapper.ControleGastosMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ControleGastosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ControleGastosResourceIT {

    private static final LocalDate DEFAULT_MES = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MES = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/controle-gastos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ControleGastosRepository controleGastosRepository;

    @Autowired
    private ControleGastosMapper controleGastosMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restControleGastosMockMvc;

    private ControleGastos controleGastos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ControleGastos createEntity(EntityManager em) {
        ControleGastos controleGastos = new ControleGastos().mes(DEFAULT_MES);
        return controleGastos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ControleGastos createUpdatedEntity(EntityManager em) {
        ControleGastos controleGastos = new ControleGastos().mes(UPDATED_MES);
        return controleGastos;
    }

    @BeforeEach
    public void initTest() {
        controleGastos = createEntity(em);
    }

    @Test
    @Transactional
    void createControleGastos() throws Exception {
        int databaseSizeBeforeCreate = controleGastosRepository.findAll().size();
        // Create the ControleGastos
        ControleGastosDTO controleGastosDTO = controleGastosMapper.toDto(controleGastos);
        restControleGastosMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(controleGastosDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ControleGastos in the database
        List<ControleGastos> controleGastosList = controleGastosRepository.findAll();
        assertThat(controleGastosList).hasSize(databaseSizeBeforeCreate + 1);
        ControleGastos testControleGastos = controleGastosList.get(controleGastosList.size() - 1);
        assertThat(testControleGastos.getMes()).isEqualTo(DEFAULT_MES);
    }

    @Test
    @Transactional
    void createControleGastosWithExistingId() throws Exception {
        // Create the ControleGastos with an existing ID
        controleGastos.setId(1L);
        ControleGastosDTO controleGastosDTO = controleGastosMapper.toDto(controleGastos);

        int databaseSizeBeforeCreate = controleGastosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restControleGastosMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(controleGastosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ControleGastos in the database
        List<ControleGastos> controleGastosList = controleGastosRepository.findAll();
        assertThat(controleGastosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllControleGastos() throws Exception {
        // Initialize the database
        controleGastosRepository.saveAndFlush(controleGastos);

        // Get all the controleGastosList
        restControleGastosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(controleGastos.getId().intValue())))
            .andExpect(jsonPath("$.[*].mes").value(hasItem(DEFAULT_MES.toString())));
    }

    @Test
    @Transactional
    void getControleGastos() throws Exception {
        // Initialize the database
        controleGastosRepository.saveAndFlush(controleGastos);

        // Get the controleGastos
        restControleGastosMockMvc
            .perform(get(ENTITY_API_URL_ID, controleGastos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(controleGastos.getId().intValue()))
            .andExpect(jsonPath("$.mes").value(DEFAULT_MES.toString()));
    }

    @Test
    @Transactional
    void getNonExistingControleGastos() throws Exception {
        // Get the controleGastos
        restControleGastosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingControleGastos() throws Exception {
        // Initialize the database
        controleGastosRepository.saveAndFlush(controleGastos);

        int databaseSizeBeforeUpdate = controleGastosRepository.findAll().size();

        // Update the controleGastos
        ControleGastos updatedControleGastos = controleGastosRepository.findById(controleGastos.getId()).get();
        // Disconnect from session so that the updates on updatedControleGastos are not directly saved in db
        em.detach(updatedControleGastos);
        updatedControleGastos.mes(UPDATED_MES);
        ControleGastosDTO controleGastosDTO = controleGastosMapper.toDto(updatedControleGastos);

        restControleGastosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, controleGastosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(controleGastosDTO))
            )
            .andExpect(status().isOk());

        // Validate the ControleGastos in the database
        List<ControleGastos> controleGastosList = controleGastosRepository.findAll();
        assertThat(controleGastosList).hasSize(databaseSizeBeforeUpdate);
        ControleGastos testControleGastos = controleGastosList.get(controleGastosList.size() - 1);
        assertThat(testControleGastos.getMes()).isEqualTo(UPDATED_MES);
    }

    @Test
    @Transactional
    void putNonExistingControleGastos() throws Exception {
        int databaseSizeBeforeUpdate = controleGastosRepository.findAll().size();
        controleGastos.setId(count.incrementAndGet());

        // Create the ControleGastos
        ControleGastosDTO controleGastosDTO = controleGastosMapper.toDto(controleGastos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restControleGastosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, controleGastosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(controleGastosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ControleGastos in the database
        List<ControleGastos> controleGastosList = controleGastosRepository.findAll();
        assertThat(controleGastosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchControleGastos() throws Exception {
        int databaseSizeBeforeUpdate = controleGastosRepository.findAll().size();
        controleGastos.setId(count.incrementAndGet());

        // Create the ControleGastos
        ControleGastosDTO controleGastosDTO = controleGastosMapper.toDto(controleGastos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restControleGastosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(controleGastosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ControleGastos in the database
        List<ControleGastos> controleGastosList = controleGastosRepository.findAll();
        assertThat(controleGastosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamControleGastos() throws Exception {
        int databaseSizeBeforeUpdate = controleGastosRepository.findAll().size();
        controleGastos.setId(count.incrementAndGet());

        // Create the ControleGastos
        ControleGastosDTO controleGastosDTO = controleGastosMapper.toDto(controleGastos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restControleGastosMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(controleGastosDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ControleGastos in the database
        List<ControleGastos> controleGastosList = controleGastosRepository.findAll();
        assertThat(controleGastosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateControleGastosWithPatch() throws Exception {
        // Initialize the database
        controleGastosRepository.saveAndFlush(controleGastos);

        int databaseSizeBeforeUpdate = controleGastosRepository.findAll().size();

        // Update the controleGastos using partial update
        ControleGastos partialUpdatedControleGastos = new ControleGastos();
        partialUpdatedControleGastos.setId(controleGastos.getId());

        partialUpdatedControleGastos.mes(UPDATED_MES);

        restControleGastosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedControleGastos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedControleGastos))
            )
            .andExpect(status().isOk());

        // Validate the ControleGastos in the database
        List<ControleGastos> controleGastosList = controleGastosRepository.findAll();
        assertThat(controleGastosList).hasSize(databaseSizeBeforeUpdate);
        ControleGastos testControleGastos = controleGastosList.get(controleGastosList.size() - 1);
        assertThat(testControleGastos.getMes()).isEqualTo(UPDATED_MES);
    }

    @Test
    @Transactional
    void fullUpdateControleGastosWithPatch() throws Exception {
        // Initialize the database
        controleGastosRepository.saveAndFlush(controleGastos);

        int databaseSizeBeforeUpdate = controleGastosRepository.findAll().size();

        // Update the controleGastos using partial update
        ControleGastos partialUpdatedControleGastos = new ControleGastos();
        partialUpdatedControleGastos.setId(controleGastos.getId());

        partialUpdatedControleGastos.mes(UPDATED_MES);

        restControleGastosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedControleGastos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedControleGastos))
            )
            .andExpect(status().isOk());

        // Validate the ControleGastos in the database
        List<ControleGastos> controleGastosList = controleGastosRepository.findAll();
        assertThat(controleGastosList).hasSize(databaseSizeBeforeUpdate);
        ControleGastos testControleGastos = controleGastosList.get(controleGastosList.size() - 1);
        assertThat(testControleGastos.getMes()).isEqualTo(UPDATED_MES);
    }

    @Test
    @Transactional
    void patchNonExistingControleGastos() throws Exception {
        int databaseSizeBeforeUpdate = controleGastosRepository.findAll().size();
        controleGastos.setId(count.incrementAndGet());

        // Create the ControleGastos
        ControleGastosDTO controleGastosDTO = controleGastosMapper.toDto(controleGastos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restControleGastosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, controleGastosDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(controleGastosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ControleGastos in the database
        List<ControleGastos> controleGastosList = controleGastosRepository.findAll();
        assertThat(controleGastosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchControleGastos() throws Exception {
        int databaseSizeBeforeUpdate = controleGastosRepository.findAll().size();
        controleGastos.setId(count.incrementAndGet());

        // Create the ControleGastos
        ControleGastosDTO controleGastosDTO = controleGastosMapper.toDto(controleGastos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restControleGastosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(controleGastosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ControleGastos in the database
        List<ControleGastos> controleGastosList = controleGastosRepository.findAll();
        assertThat(controleGastosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamControleGastos() throws Exception {
        int databaseSizeBeforeUpdate = controleGastosRepository.findAll().size();
        controleGastos.setId(count.incrementAndGet());

        // Create the ControleGastos
        ControleGastosDTO controleGastosDTO = controleGastosMapper.toDto(controleGastos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restControleGastosMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(controleGastosDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ControleGastos in the database
        List<ControleGastos> controleGastosList = controleGastosRepository.findAll();
        assertThat(controleGastosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteControleGastos() throws Exception {
        // Initialize the database
        controleGastosRepository.saveAndFlush(controleGastos);

        int databaseSizeBeforeDelete = controleGastosRepository.findAll().size();

        // Delete the controleGastos
        restControleGastosMockMvc
            .perform(delete(ENTITY_API_URL_ID, controleGastos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ControleGastos> controleGastosList = controleGastosRepository.findAll();
        assertThat(controleGastosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package br.com.severo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.severo.IntegrationTest;
import br.com.severo.domain.Salarios;
import br.com.severo.repository.SalariosRepository;
import br.com.severo.service.dto.SalariosDTO;
import br.com.severo.service.mapper.SalariosMapper;
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
 * Integration tests for the {@link SalariosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SalariosResourceIT {

    private static final Double DEFAULT_VALOR = 1D;
    private static final Double UPDATED_VALOR = 2D;

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_RECEBIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_RECEBIMENTO = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/salarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SalariosRepository salariosRepository;

    @Autowired
    private SalariosMapper salariosMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSalariosMockMvc;

    private Salarios salarios;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Salarios createEntity(EntityManager em) {
        Salarios salarios = new Salarios().valor(DEFAULT_VALOR).nome(DEFAULT_NOME).dataRecebimento(DEFAULT_DATA_RECEBIMENTO);
        return salarios;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Salarios createUpdatedEntity(EntityManager em) {
        Salarios salarios = new Salarios().valor(UPDATED_VALOR).nome(UPDATED_NOME).dataRecebimento(UPDATED_DATA_RECEBIMENTO);
        return salarios;
    }

    @BeforeEach
    public void initTest() {
        salarios = createEntity(em);
    }

    @Test
    @Transactional
    void createSalarios() throws Exception {
        int databaseSizeBeforeCreate = salariosRepository.findAll().size();
        // Create the Salarios
        SalariosDTO salariosDTO = salariosMapper.toDto(salarios);
        restSalariosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salariosDTO)))
            .andExpect(status().isCreated());

        // Validate the Salarios in the database
        List<Salarios> salariosList = salariosRepository.findAll();
        assertThat(salariosList).hasSize(databaseSizeBeforeCreate + 1);
        Salarios testSalarios = salariosList.get(salariosList.size() - 1);
        assertThat(testSalarios.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testSalarios.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testSalarios.getDataRecebimento()).isEqualTo(DEFAULT_DATA_RECEBIMENTO);
    }

    @Test
    @Transactional
    void createSalariosWithExistingId() throws Exception {
        // Create the Salarios with an existing ID
        salarios.setId(1L);
        SalariosDTO salariosDTO = salariosMapper.toDto(salarios);

        int databaseSizeBeforeCreate = salariosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalariosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salariosDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Salarios in the database
        List<Salarios> salariosList = salariosRepository.findAll();
        assertThat(salariosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSalarios() throws Exception {
        // Initialize the database
        salariosRepository.saveAndFlush(salarios);

        // Get all the salariosList
        restSalariosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salarios.getId().intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].dataRecebimento").value(hasItem(DEFAULT_DATA_RECEBIMENTO.toString())));
    }

    @Test
    @Transactional
    void getSalarios() throws Exception {
        // Initialize the database
        salariosRepository.saveAndFlush(salarios);

        // Get the salarios
        restSalariosMockMvc
            .perform(get(ENTITY_API_URL_ID, salarios.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(salarios.getId().intValue()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.doubleValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.dataRecebimento").value(DEFAULT_DATA_RECEBIMENTO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSalarios() throws Exception {
        // Get the salarios
        restSalariosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSalarios() throws Exception {
        // Initialize the database
        salariosRepository.saveAndFlush(salarios);

        int databaseSizeBeforeUpdate = salariosRepository.findAll().size();

        // Update the salarios
        Salarios updatedSalarios = salariosRepository.findById(salarios.getId()).get();
        // Disconnect from session so that the updates on updatedSalarios are not directly saved in db
        em.detach(updatedSalarios);
        updatedSalarios.valor(UPDATED_VALOR).nome(UPDATED_NOME).dataRecebimento(UPDATED_DATA_RECEBIMENTO);
        SalariosDTO salariosDTO = salariosMapper.toDto(updatedSalarios);

        restSalariosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salariosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salariosDTO))
            )
            .andExpect(status().isOk());

        // Validate the Salarios in the database
        List<Salarios> salariosList = salariosRepository.findAll();
        assertThat(salariosList).hasSize(databaseSizeBeforeUpdate);
        Salarios testSalarios = salariosList.get(salariosList.size() - 1);
        assertThat(testSalarios.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testSalarios.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testSalarios.getDataRecebimento()).isEqualTo(UPDATED_DATA_RECEBIMENTO);
    }

    @Test
    @Transactional
    void putNonExistingSalarios() throws Exception {
        int databaseSizeBeforeUpdate = salariosRepository.findAll().size();
        salarios.setId(count.incrementAndGet());

        // Create the Salarios
        SalariosDTO salariosDTO = salariosMapper.toDto(salarios);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalariosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salariosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salariosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salarios in the database
        List<Salarios> salariosList = salariosRepository.findAll();
        assertThat(salariosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSalarios() throws Exception {
        int databaseSizeBeforeUpdate = salariosRepository.findAll().size();
        salarios.setId(count.incrementAndGet());

        // Create the Salarios
        SalariosDTO salariosDTO = salariosMapper.toDto(salarios);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalariosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salariosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salarios in the database
        List<Salarios> salariosList = salariosRepository.findAll();
        assertThat(salariosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSalarios() throws Exception {
        int databaseSizeBeforeUpdate = salariosRepository.findAll().size();
        salarios.setId(count.incrementAndGet());

        // Create the Salarios
        SalariosDTO salariosDTO = salariosMapper.toDto(salarios);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalariosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salariosDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Salarios in the database
        List<Salarios> salariosList = salariosRepository.findAll();
        assertThat(salariosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSalariosWithPatch() throws Exception {
        // Initialize the database
        salariosRepository.saveAndFlush(salarios);

        int databaseSizeBeforeUpdate = salariosRepository.findAll().size();

        // Update the salarios using partial update
        Salarios partialUpdatedSalarios = new Salarios();
        partialUpdatedSalarios.setId(salarios.getId());

        restSalariosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalarios.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalarios))
            )
            .andExpect(status().isOk());

        // Validate the Salarios in the database
        List<Salarios> salariosList = salariosRepository.findAll();
        assertThat(salariosList).hasSize(databaseSizeBeforeUpdate);
        Salarios testSalarios = salariosList.get(salariosList.size() - 1);
        assertThat(testSalarios.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testSalarios.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testSalarios.getDataRecebimento()).isEqualTo(DEFAULT_DATA_RECEBIMENTO);
    }

    @Test
    @Transactional
    void fullUpdateSalariosWithPatch() throws Exception {
        // Initialize the database
        salariosRepository.saveAndFlush(salarios);

        int databaseSizeBeforeUpdate = salariosRepository.findAll().size();

        // Update the salarios using partial update
        Salarios partialUpdatedSalarios = new Salarios();
        partialUpdatedSalarios.setId(salarios.getId());

        partialUpdatedSalarios.valor(UPDATED_VALOR).nome(UPDATED_NOME).dataRecebimento(UPDATED_DATA_RECEBIMENTO);

        restSalariosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalarios.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalarios))
            )
            .andExpect(status().isOk());

        // Validate the Salarios in the database
        List<Salarios> salariosList = salariosRepository.findAll();
        assertThat(salariosList).hasSize(databaseSizeBeforeUpdate);
        Salarios testSalarios = salariosList.get(salariosList.size() - 1);
        assertThat(testSalarios.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testSalarios.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testSalarios.getDataRecebimento()).isEqualTo(UPDATED_DATA_RECEBIMENTO);
    }

    @Test
    @Transactional
    void patchNonExistingSalarios() throws Exception {
        int databaseSizeBeforeUpdate = salariosRepository.findAll().size();
        salarios.setId(count.incrementAndGet());

        // Create the Salarios
        SalariosDTO salariosDTO = salariosMapper.toDto(salarios);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalariosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, salariosDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salariosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salarios in the database
        List<Salarios> salariosList = salariosRepository.findAll();
        assertThat(salariosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSalarios() throws Exception {
        int databaseSizeBeforeUpdate = salariosRepository.findAll().size();
        salarios.setId(count.incrementAndGet());

        // Create the Salarios
        SalariosDTO salariosDTO = salariosMapper.toDto(salarios);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalariosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salariosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salarios in the database
        List<Salarios> salariosList = salariosRepository.findAll();
        assertThat(salariosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSalarios() throws Exception {
        int databaseSizeBeforeUpdate = salariosRepository.findAll().size();
        salarios.setId(count.incrementAndGet());

        // Create the Salarios
        SalariosDTO salariosDTO = salariosMapper.toDto(salarios);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalariosMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(salariosDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Salarios in the database
        List<Salarios> salariosList = salariosRepository.findAll();
        assertThat(salariosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSalarios() throws Exception {
        // Initialize the database
        salariosRepository.saveAndFlush(salarios);

        int databaseSizeBeforeDelete = salariosRepository.findAll().size();

        // Delete the salarios
        restSalariosMockMvc
            .perform(delete(ENTITY_API_URL_ID, salarios.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Salarios> salariosList = salariosRepository.findAll();
        assertThat(salariosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

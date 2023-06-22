package br.com.severo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.severo.IntegrationTest;
import br.com.severo.domain.Despesas;
import br.com.severo.repository.DespesasRepository;
import br.com.severo.service.dto.DespesasDTO;
import br.com.severo.service.mapper.DespesasMapper;
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
 * Integration tests for the {@link DespesasResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DespesasResourceIT {

    private static final Double DEFAULT_VALOR = 1D;
    private static final Double UPDATED_VALOR = 2D;

    private static final String DEFAULT_CATEGORIA = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORIA = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/despesas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DespesasRepository despesasRepository;

    @Autowired
    private DespesasMapper despesasMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDespesasMockMvc;

    private Despesas despesas;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Despesas createEntity(EntityManager em) {
        Despesas despesas = new Despesas().valor(DEFAULT_VALOR).categoria(DEFAULT_CATEGORIA).data(DEFAULT_DATA);
        return despesas;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Despesas createUpdatedEntity(EntityManager em) {
        Despesas despesas = new Despesas().valor(UPDATED_VALOR).categoria(UPDATED_CATEGORIA).data(UPDATED_DATA);
        return despesas;
    }

    @BeforeEach
    public void initTest() {
        despesas = createEntity(em);
    }

    @Test
    @Transactional
    void createDespesas() throws Exception {
        int databaseSizeBeforeCreate = despesasRepository.findAll().size();
        // Create the Despesas
        DespesasDTO despesasDTO = despesasMapper.toDto(despesas);
        restDespesasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(despesasDTO)))
            .andExpect(status().isCreated());

        // Validate the Despesas in the database
        List<Despesas> despesasList = despesasRepository.findAll();
        assertThat(despesasList).hasSize(databaseSizeBeforeCreate + 1);
        Despesas testDespesas = despesasList.get(despesasList.size() - 1);
        assertThat(testDespesas.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testDespesas.getCategoria()).isEqualTo(DEFAULT_CATEGORIA);
        assertThat(testDespesas.getData()).isEqualTo(DEFAULT_DATA);
    }

    @Test
    @Transactional
    void createDespesasWithExistingId() throws Exception {
        // Create the Despesas with an existing ID
        despesas.setId(1L);
        DespesasDTO despesasDTO = despesasMapper.toDto(despesas);

        int databaseSizeBeforeCreate = despesasRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDespesasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(despesasDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Despesas in the database
        List<Despesas> despesasList = despesasRepository.findAll();
        assertThat(despesasList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDespesas() throws Exception {
        // Initialize the database
        despesasRepository.saveAndFlush(despesas);

        // Get all the despesasList
        restDespesasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(despesas.getId().intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())))
            .andExpect(jsonPath("$.[*].categoria").value(hasItem(DEFAULT_CATEGORIA)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())));
    }

    @Test
    @Transactional
    void getDespesas() throws Exception {
        // Initialize the database
        despesasRepository.saveAndFlush(despesas);

        // Get the despesas
        restDespesasMockMvc
            .perform(get(ENTITY_API_URL_ID, despesas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(despesas.getId().intValue()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.doubleValue()))
            .andExpect(jsonPath("$.categoria").value(DEFAULT_CATEGORIA))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDespesas() throws Exception {
        // Get the despesas
        restDespesasMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDespesas() throws Exception {
        // Initialize the database
        despesasRepository.saveAndFlush(despesas);

        int databaseSizeBeforeUpdate = despesasRepository.findAll().size();

        // Update the despesas
        Despesas updatedDespesas = despesasRepository.findById(despesas.getId()).get();
        // Disconnect from session so that the updates on updatedDespesas are not directly saved in db
        em.detach(updatedDespesas);
        updatedDespesas.valor(UPDATED_VALOR).categoria(UPDATED_CATEGORIA).data(UPDATED_DATA);
        DespesasDTO despesasDTO = despesasMapper.toDto(updatedDespesas);

        restDespesasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, despesasDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(despesasDTO))
            )
            .andExpect(status().isOk());

        // Validate the Despesas in the database
        List<Despesas> despesasList = despesasRepository.findAll();
        assertThat(despesasList).hasSize(databaseSizeBeforeUpdate);
        Despesas testDespesas = despesasList.get(despesasList.size() - 1);
        assertThat(testDespesas.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testDespesas.getCategoria()).isEqualTo(UPDATED_CATEGORIA);
        assertThat(testDespesas.getData()).isEqualTo(UPDATED_DATA);
    }

    @Test
    @Transactional
    void putNonExistingDespesas() throws Exception {
        int databaseSizeBeforeUpdate = despesasRepository.findAll().size();
        despesas.setId(count.incrementAndGet());

        // Create the Despesas
        DespesasDTO despesasDTO = despesasMapper.toDto(despesas);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDespesasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, despesasDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(despesasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Despesas in the database
        List<Despesas> despesasList = despesasRepository.findAll();
        assertThat(despesasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDespesas() throws Exception {
        int databaseSizeBeforeUpdate = despesasRepository.findAll().size();
        despesas.setId(count.incrementAndGet());

        // Create the Despesas
        DespesasDTO despesasDTO = despesasMapper.toDto(despesas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDespesasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(despesasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Despesas in the database
        List<Despesas> despesasList = despesasRepository.findAll();
        assertThat(despesasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDespesas() throws Exception {
        int databaseSizeBeforeUpdate = despesasRepository.findAll().size();
        despesas.setId(count.incrementAndGet());

        // Create the Despesas
        DespesasDTO despesasDTO = despesasMapper.toDto(despesas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDespesasMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(despesasDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Despesas in the database
        List<Despesas> despesasList = despesasRepository.findAll();
        assertThat(despesasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDespesasWithPatch() throws Exception {
        // Initialize the database
        despesasRepository.saveAndFlush(despesas);

        int databaseSizeBeforeUpdate = despesasRepository.findAll().size();

        // Update the despesas using partial update
        Despesas partialUpdatedDespesas = new Despesas();
        partialUpdatedDespesas.setId(despesas.getId());

        partialUpdatedDespesas.valor(UPDATED_VALOR).categoria(UPDATED_CATEGORIA).data(UPDATED_DATA);

        restDespesasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDespesas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDespesas))
            )
            .andExpect(status().isOk());

        // Validate the Despesas in the database
        List<Despesas> despesasList = despesasRepository.findAll();
        assertThat(despesasList).hasSize(databaseSizeBeforeUpdate);
        Despesas testDespesas = despesasList.get(despesasList.size() - 1);
        assertThat(testDespesas.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testDespesas.getCategoria()).isEqualTo(UPDATED_CATEGORIA);
        assertThat(testDespesas.getData()).isEqualTo(UPDATED_DATA);
    }

    @Test
    @Transactional
    void fullUpdateDespesasWithPatch() throws Exception {
        // Initialize the database
        despesasRepository.saveAndFlush(despesas);

        int databaseSizeBeforeUpdate = despesasRepository.findAll().size();

        // Update the despesas using partial update
        Despesas partialUpdatedDespesas = new Despesas();
        partialUpdatedDespesas.setId(despesas.getId());

        partialUpdatedDespesas.valor(UPDATED_VALOR).categoria(UPDATED_CATEGORIA).data(UPDATED_DATA);

        restDespesasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDespesas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDespesas))
            )
            .andExpect(status().isOk());

        // Validate the Despesas in the database
        List<Despesas> despesasList = despesasRepository.findAll();
        assertThat(despesasList).hasSize(databaseSizeBeforeUpdate);
        Despesas testDespesas = despesasList.get(despesasList.size() - 1);
        assertThat(testDespesas.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testDespesas.getCategoria()).isEqualTo(UPDATED_CATEGORIA);
        assertThat(testDespesas.getData()).isEqualTo(UPDATED_DATA);
    }

    @Test
    @Transactional
    void patchNonExistingDespesas() throws Exception {
        int databaseSizeBeforeUpdate = despesasRepository.findAll().size();
        despesas.setId(count.incrementAndGet());

        // Create the Despesas
        DespesasDTO despesasDTO = despesasMapper.toDto(despesas);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDespesasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, despesasDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(despesasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Despesas in the database
        List<Despesas> despesasList = despesasRepository.findAll();
        assertThat(despesasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDespesas() throws Exception {
        int databaseSizeBeforeUpdate = despesasRepository.findAll().size();
        despesas.setId(count.incrementAndGet());

        // Create the Despesas
        DespesasDTO despesasDTO = despesasMapper.toDto(despesas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDespesasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(despesasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Despesas in the database
        List<Despesas> despesasList = despesasRepository.findAll();
        assertThat(despesasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDespesas() throws Exception {
        int databaseSizeBeforeUpdate = despesasRepository.findAll().size();
        despesas.setId(count.incrementAndGet());

        // Create the Despesas
        DespesasDTO despesasDTO = despesasMapper.toDto(despesas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDespesasMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(despesasDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Despesas in the database
        List<Despesas> despesasList = despesasRepository.findAll();
        assertThat(despesasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDespesas() throws Exception {
        // Initialize the database
        despesasRepository.saveAndFlush(despesas);

        int databaseSizeBeforeDelete = despesasRepository.findAll().size();

        // Delete the despesas
        restDespesasMockMvc
            .perform(delete(ENTITY_API_URL_ID, despesas.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Despesas> despesasList = despesasRepository.findAll();
        assertThat(despesasList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package br.com.severo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.severo.IntegrationTest;
import br.com.severo.domain.Cartoes;
import br.com.severo.repository.CartoesRepository;
import br.com.severo.service.dto.CartoesDTO;
import br.com.severo.service.mapper.CartoesMapper;
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
 * Integration tests for the {@link CartoesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CartoesResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final Double DEFAULT_VALOR = 1D;
    private static final Double UPDATED_VALOR = 2D;

    private static final String ENTITY_API_URL = "/api/cartoes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CartoesRepository cartoesRepository;

    @Autowired
    private CartoesMapper cartoesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCartoesMockMvc;

    private Cartoes cartoes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cartoes createEntity(EntityManager em) {
        Cartoes cartoes = new Cartoes().nome(DEFAULT_NOME).valor(DEFAULT_VALOR);
        return cartoes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cartoes createUpdatedEntity(EntityManager em) {
        Cartoes cartoes = new Cartoes().nome(UPDATED_NOME).valor(UPDATED_VALOR);
        return cartoes;
    }

    @BeforeEach
    public void initTest() {
        cartoes = createEntity(em);
    }

    @Test
    @Transactional
    void createCartoes() throws Exception {
        int databaseSizeBeforeCreate = cartoesRepository.findAll().size();
        // Create the Cartoes
        CartoesDTO cartoesDTO = cartoesMapper.toDto(cartoes);
        restCartoesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartoesDTO)))
            .andExpect(status().isCreated());

        // Validate the Cartoes in the database
        List<Cartoes> cartoesList = cartoesRepository.findAll();
        assertThat(cartoesList).hasSize(databaseSizeBeforeCreate + 1);
        Cartoes testCartoes = cartoesList.get(cartoesList.size() - 1);
        assertThat(testCartoes.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testCartoes.getValor()).isEqualTo(DEFAULT_VALOR);
    }

    @Test
    @Transactional
    void createCartoesWithExistingId() throws Exception {
        // Create the Cartoes with an existing ID
        cartoes.setId(1L);
        CartoesDTO cartoesDTO = cartoesMapper.toDto(cartoes);

        int databaseSizeBeforeCreate = cartoesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCartoesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartoesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cartoes in the database
        List<Cartoes> cartoesList = cartoesRepository.findAll();
        assertThat(cartoesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCartoes() throws Exception {
        // Initialize the database
        cartoesRepository.saveAndFlush(cartoes);

        // Get all the cartoesList
        restCartoesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cartoes.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())));
    }

    @Test
    @Transactional
    void getCartoes() throws Exception {
        // Initialize the database
        cartoesRepository.saveAndFlush(cartoes);

        // Get the cartoes
        restCartoesMockMvc
            .perform(get(ENTITY_API_URL_ID, cartoes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cartoes.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingCartoes() throws Exception {
        // Get the cartoes
        restCartoesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCartoes() throws Exception {
        // Initialize the database
        cartoesRepository.saveAndFlush(cartoes);

        int databaseSizeBeforeUpdate = cartoesRepository.findAll().size();

        // Update the cartoes
        Cartoes updatedCartoes = cartoesRepository.findById(cartoes.getId()).get();
        // Disconnect from session so that the updates on updatedCartoes are not directly saved in db
        em.detach(updatedCartoes);
        updatedCartoes.nome(UPDATED_NOME).valor(UPDATED_VALOR);
        CartoesDTO cartoesDTO = cartoesMapper.toDto(updatedCartoes);

        restCartoesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cartoesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cartoesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cartoes in the database
        List<Cartoes> cartoesList = cartoesRepository.findAll();
        assertThat(cartoesList).hasSize(databaseSizeBeforeUpdate);
        Cartoes testCartoes = cartoesList.get(cartoesList.size() - 1);
        assertThat(testCartoes.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCartoes.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    @Transactional
    void putNonExistingCartoes() throws Exception {
        int databaseSizeBeforeUpdate = cartoesRepository.findAll().size();
        cartoes.setId(count.incrementAndGet());

        // Create the Cartoes
        CartoesDTO cartoesDTO = cartoesMapper.toDto(cartoes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartoesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cartoesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cartoesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cartoes in the database
        List<Cartoes> cartoesList = cartoesRepository.findAll();
        assertThat(cartoesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCartoes() throws Exception {
        int databaseSizeBeforeUpdate = cartoesRepository.findAll().size();
        cartoes.setId(count.incrementAndGet());

        // Create the Cartoes
        CartoesDTO cartoesDTO = cartoesMapper.toDto(cartoes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartoesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cartoesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cartoes in the database
        List<Cartoes> cartoesList = cartoesRepository.findAll();
        assertThat(cartoesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCartoes() throws Exception {
        int databaseSizeBeforeUpdate = cartoesRepository.findAll().size();
        cartoes.setId(count.incrementAndGet());

        // Create the Cartoes
        CartoesDTO cartoesDTO = cartoesMapper.toDto(cartoes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartoesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartoesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cartoes in the database
        List<Cartoes> cartoesList = cartoesRepository.findAll();
        assertThat(cartoesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCartoesWithPatch() throws Exception {
        // Initialize the database
        cartoesRepository.saveAndFlush(cartoes);

        int databaseSizeBeforeUpdate = cartoesRepository.findAll().size();

        // Update the cartoes using partial update
        Cartoes partialUpdatedCartoes = new Cartoes();
        partialUpdatedCartoes.setId(cartoes.getId());

        partialUpdatedCartoes.valor(UPDATED_VALOR);

        restCartoesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCartoes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCartoes))
            )
            .andExpect(status().isOk());

        // Validate the Cartoes in the database
        List<Cartoes> cartoesList = cartoesRepository.findAll();
        assertThat(cartoesList).hasSize(databaseSizeBeforeUpdate);
        Cartoes testCartoes = cartoesList.get(cartoesList.size() - 1);
        assertThat(testCartoes.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testCartoes.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    @Transactional
    void fullUpdateCartoesWithPatch() throws Exception {
        // Initialize the database
        cartoesRepository.saveAndFlush(cartoes);

        int databaseSizeBeforeUpdate = cartoesRepository.findAll().size();

        // Update the cartoes using partial update
        Cartoes partialUpdatedCartoes = new Cartoes();
        partialUpdatedCartoes.setId(cartoes.getId());

        partialUpdatedCartoes.nome(UPDATED_NOME).valor(UPDATED_VALOR);

        restCartoesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCartoes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCartoes))
            )
            .andExpect(status().isOk());

        // Validate the Cartoes in the database
        List<Cartoes> cartoesList = cartoesRepository.findAll();
        assertThat(cartoesList).hasSize(databaseSizeBeforeUpdate);
        Cartoes testCartoes = cartoesList.get(cartoesList.size() - 1);
        assertThat(testCartoes.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCartoes.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    @Transactional
    void patchNonExistingCartoes() throws Exception {
        int databaseSizeBeforeUpdate = cartoesRepository.findAll().size();
        cartoes.setId(count.incrementAndGet());

        // Create the Cartoes
        CartoesDTO cartoesDTO = cartoesMapper.toDto(cartoes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartoesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cartoesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cartoesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cartoes in the database
        List<Cartoes> cartoesList = cartoesRepository.findAll();
        assertThat(cartoesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCartoes() throws Exception {
        int databaseSizeBeforeUpdate = cartoesRepository.findAll().size();
        cartoes.setId(count.incrementAndGet());

        // Create the Cartoes
        CartoesDTO cartoesDTO = cartoesMapper.toDto(cartoes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartoesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cartoesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cartoes in the database
        List<Cartoes> cartoesList = cartoesRepository.findAll();
        assertThat(cartoesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCartoes() throws Exception {
        int databaseSizeBeforeUpdate = cartoesRepository.findAll().size();
        cartoes.setId(count.incrementAndGet());

        // Create the Cartoes
        CartoesDTO cartoesDTO = cartoesMapper.toDto(cartoes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartoesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cartoesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cartoes in the database
        List<Cartoes> cartoesList = cartoesRepository.findAll();
        assertThat(cartoesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCartoes() throws Exception {
        // Initialize the database
        cartoesRepository.saveAndFlush(cartoes);

        int databaseSizeBeforeDelete = cartoesRepository.findAll().size();

        // Delete the cartoes
        restCartoesMockMvc
            .perform(delete(ENTITY_API_URL_ID, cartoes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cartoes> cartoesList = cartoesRepository.findAll();
        assertThat(cartoesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

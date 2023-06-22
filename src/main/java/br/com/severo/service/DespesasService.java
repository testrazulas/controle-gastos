package br.com.severo.service;

import br.com.severo.domain.Despesas;
import br.com.severo.repository.DespesasRepository;
import br.com.severo.service.dto.DespesasDTO;
import br.com.severo.service.mapper.DespesasMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Despesas}.
 */
@Service
@Transactional
public class DespesasService {

    private final Logger log = LoggerFactory.getLogger(DespesasService.class);

    private final DespesasRepository despesasRepository;

    private final DespesasMapper despesasMapper;

    public DespesasService(DespesasRepository despesasRepository, DespesasMapper despesasMapper) {
        this.despesasRepository = despesasRepository;
        this.despesasMapper = despesasMapper;
    }

    /**
     * Save a despesas.
     *
     * @param despesasDTO the entity to save.
     * @return the persisted entity.
     */
    public DespesasDTO save(DespesasDTO despesasDTO) {
        log.debug("Request to save Despesas : {}", despesasDTO);
        Despesas despesas = despesasMapper.toEntity(despesasDTO);
        despesas = despesasRepository.save(despesas);
        return despesasMapper.toDto(despesas);
    }

    /**
     * Update a despesas.
     *
     * @param despesasDTO the entity to save.
     * @return the persisted entity.
     */
    public DespesasDTO update(DespesasDTO despesasDTO) {
        log.debug("Request to update Despesas : {}", despesasDTO);
        Despesas despesas = despesasMapper.toEntity(despesasDTO);
        despesas = despesasRepository.save(despesas);
        return despesasMapper.toDto(despesas);
    }

    /**
     * Partially update a despesas.
     *
     * @param despesasDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DespesasDTO> partialUpdate(DespesasDTO despesasDTO) {
        log.debug("Request to partially update Despesas : {}", despesasDTO);

        return despesasRepository
            .findById(despesasDTO.getId())
            .map(existingDespesas -> {
                despesasMapper.partialUpdate(existingDespesas, despesasDTO);

                return existingDespesas;
            })
            .map(despesasRepository::save)
            .map(despesasMapper::toDto);
    }

    /**
     * Get all the despesas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DespesasDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Despesas");
        return despesasRepository.findAll(pageable).map(despesasMapper::toDto);
    }

    /**
     * Get one despesas by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DespesasDTO> findOne(Long id) {
        log.debug("Request to get Despesas : {}", id);
        return despesasRepository.findById(id).map(despesasMapper::toDto);
    }

    /**
     * Delete the despesas by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Despesas : {}", id);
        despesasRepository.deleteById(id);
    }
}

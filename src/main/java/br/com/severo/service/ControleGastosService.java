package br.com.severo.service;

import br.com.severo.domain.ControleGastos;
import br.com.severo.repository.ControleGastosRepository;
import br.com.severo.service.dto.ControleGastosDTO;
import br.com.severo.service.mapper.ControleGastosMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ControleGastos}.
 */
@Service
@Transactional
public class ControleGastosService {

    private final Logger log = LoggerFactory.getLogger(ControleGastosService.class);

    private final ControleGastosRepository controleGastosRepository;

    private final ControleGastosMapper controleGastosMapper;

    public ControleGastosService(ControleGastosRepository controleGastosRepository, ControleGastosMapper controleGastosMapper) {
        this.controleGastosRepository = controleGastosRepository;
        this.controleGastosMapper = controleGastosMapper;
    }

    /**
     * Save a controleGastos.
     *
     * @param controleGastosDTO the entity to save.
     * @return the persisted entity.
     */
    public ControleGastosDTO save(ControleGastosDTO controleGastosDTO) {
        log.debug("Request to save ControleGastos : {}", controleGastosDTO);
        ControleGastos controleGastos = controleGastosMapper.toEntity(controleGastosDTO);
        controleGastos = controleGastosRepository.save(controleGastos);
        return controleGastosMapper.toDto(controleGastos);
    }

    /**
     * Update a controleGastos.
     *
     * @param controleGastosDTO the entity to save.
     * @return the persisted entity.
     */
    public ControleGastosDTO update(ControleGastosDTO controleGastosDTO) {
        log.debug("Request to update ControleGastos : {}", controleGastosDTO);
        ControleGastos controleGastos = controleGastosMapper.toEntity(controleGastosDTO);
        controleGastos = controleGastosRepository.save(controleGastos);
        return controleGastosMapper.toDto(controleGastos);
    }

    /**
     * Partially update a controleGastos.
     *
     * @param controleGastosDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ControleGastosDTO> partialUpdate(ControleGastosDTO controleGastosDTO) {
        log.debug("Request to partially update ControleGastos : {}", controleGastosDTO);

        return controleGastosRepository
            .findById(controleGastosDTO.getId())
            .map(existingControleGastos -> {
                controleGastosMapper.partialUpdate(existingControleGastos, controleGastosDTO);

                return existingControleGastos;
            })
            .map(controleGastosRepository::save)
            .map(controleGastosMapper::toDto);
    }

    /**
     * Get all the controleGastos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ControleGastosDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ControleGastos");
        return controleGastosRepository.findAll(pageable).map(controleGastosMapper::toDto);
    }

    /**
     * Get one controleGastos by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ControleGastosDTO> findOne(Long id) {
        log.debug("Request to get ControleGastos : {}", id);
        return controleGastosRepository.findById(id).map(controleGastosMapper::toDto);
    }

    /**
     * Delete the controleGastos by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ControleGastos : {}", id);
        controleGastosRepository.deleteById(id);
    }
}

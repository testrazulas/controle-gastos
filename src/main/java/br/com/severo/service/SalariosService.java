package br.com.severo.service;

import br.com.severo.domain.Salarios;
import br.com.severo.repository.SalariosRepository;
import br.com.severo.service.dto.SalariosDTO;
import br.com.severo.service.mapper.SalariosMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Salarios}.
 */
@Service
@Transactional
public class SalariosService {

    private final Logger log = LoggerFactory.getLogger(SalariosService.class);

    private final SalariosRepository salariosRepository;

    private final SalariosMapper salariosMapper;

    public SalariosService(SalariosRepository salariosRepository, SalariosMapper salariosMapper) {
        this.salariosRepository = salariosRepository;
        this.salariosMapper = salariosMapper;
    }

    /**
     * Save a salarios.
     *
     * @param salariosDTO the entity to save.
     * @return the persisted entity.
     */
    public SalariosDTO save(SalariosDTO salariosDTO) {
        log.debug("Request to save Salarios : {}", salariosDTO);
        Salarios salarios = salariosMapper.toEntity(salariosDTO);
        salarios = salariosRepository.save(salarios);
        return salariosMapper.toDto(salarios);
    }

    /**
     * Update a salarios.
     *
     * @param salariosDTO the entity to save.
     * @return the persisted entity.
     */
    public SalariosDTO update(SalariosDTO salariosDTO) {
        log.debug("Request to update Salarios : {}", salariosDTO);
        Salarios salarios = salariosMapper.toEntity(salariosDTO);
        salarios = salariosRepository.save(salarios);
        return salariosMapper.toDto(salarios);
    }

    /**
     * Partially update a salarios.
     *
     * @param salariosDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SalariosDTO> partialUpdate(SalariosDTO salariosDTO) {
        log.debug("Request to partially update Salarios : {}", salariosDTO);

        return salariosRepository
            .findById(salariosDTO.getId())
            .map(existingSalarios -> {
                salariosMapper.partialUpdate(existingSalarios, salariosDTO);

                return existingSalarios;
            })
            .map(salariosRepository::save)
            .map(salariosMapper::toDto);
    }

    /**
     * Get all the salarios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SalariosDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Salarios");
        return salariosRepository.findAll(pageable).map(salariosMapper::toDto);
    }

    /**
     * Get one salarios by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SalariosDTO> findOne(Long id) {
        log.debug("Request to get Salarios : {}", id);
        return salariosRepository.findById(id).map(salariosMapper::toDto);
    }

    /**
     * Delete the salarios by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Salarios : {}", id);
        salariosRepository.deleteById(id);
    }
}

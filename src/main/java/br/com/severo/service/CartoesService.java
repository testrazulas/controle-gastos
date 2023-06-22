package br.com.severo.service;

import br.com.severo.domain.Cartoes;
import br.com.severo.repository.CartoesRepository;
import br.com.severo.service.dto.CartoesDTO;
import br.com.severo.service.mapper.CartoesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cartoes}.
 */
@Service
@Transactional
public class CartoesService {

    private final Logger log = LoggerFactory.getLogger(CartoesService.class);

    private final CartoesRepository cartoesRepository;

    private final CartoesMapper cartoesMapper;

    public CartoesService(CartoesRepository cartoesRepository, CartoesMapper cartoesMapper) {
        this.cartoesRepository = cartoesRepository;
        this.cartoesMapper = cartoesMapper;
    }

    /**
     * Save a cartoes.
     *
     * @param cartoesDTO the entity to save.
     * @return the persisted entity.
     */
    public CartoesDTO save(CartoesDTO cartoesDTO) {
        log.debug("Request to save Cartoes : {}", cartoesDTO);
        Cartoes cartoes = cartoesMapper.toEntity(cartoesDTO);
        cartoes = cartoesRepository.save(cartoes);
        return cartoesMapper.toDto(cartoes);
    }

    /**
     * Update a cartoes.
     *
     * @param cartoesDTO the entity to save.
     * @return the persisted entity.
     */
    public CartoesDTO update(CartoesDTO cartoesDTO) {
        log.debug("Request to update Cartoes : {}", cartoesDTO);
        Cartoes cartoes = cartoesMapper.toEntity(cartoesDTO);
        cartoes = cartoesRepository.save(cartoes);
        return cartoesMapper.toDto(cartoes);
    }

    /**
     * Partially update a cartoes.
     *
     * @param cartoesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CartoesDTO> partialUpdate(CartoesDTO cartoesDTO) {
        log.debug("Request to partially update Cartoes : {}", cartoesDTO);

        return cartoesRepository
            .findById(cartoesDTO.getId())
            .map(existingCartoes -> {
                cartoesMapper.partialUpdate(existingCartoes, cartoesDTO);

                return existingCartoes;
            })
            .map(cartoesRepository::save)
            .map(cartoesMapper::toDto);
    }

    /**
     * Get all the cartoes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CartoesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cartoes");
        return cartoesRepository.findAll(pageable).map(cartoesMapper::toDto);
    }

    /**
     * Get one cartoes by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CartoesDTO> findOne(Long id) {
        log.debug("Request to get Cartoes : {}", id);
        return cartoesRepository.findById(id).map(cartoesMapper::toDto);
    }

    /**
     * Delete the cartoes by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Cartoes : {}", id);
        cartoesRepository.deleteById(id);
    }
}

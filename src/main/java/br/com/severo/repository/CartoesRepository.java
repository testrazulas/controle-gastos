package br.com.severo.repository;

import br.com.severo.domain.Cartoes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Cartoes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CartoesRepository extends JpaRepository<Cartoes, Long> {}

package br.com.severo.repository;

import br.com.severo.domain.Salarios;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Salarios entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalariosRepository extends JpaRepository<Salarios, Long> {}

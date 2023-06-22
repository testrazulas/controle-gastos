package br.com.severo.repository;

import br.com.severo.domain.ControleGastos;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ControleGastos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ControleGastosRepository extends JpaRepository<ControleGastos, Long> {}

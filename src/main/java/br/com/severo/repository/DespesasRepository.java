package br.com.severo.repository;

import br.com.severo.domain.Despesas;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Despesas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DespesasRepository extends JpaRepository<Despesas, Long> {}

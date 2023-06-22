package br.com.severo.service.mapper;

import br.com.severo.domain.Despesas;
import br.com.severo.service.dto.DespesasDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Despesas} and its DTO {@link DespesasDTO}.
 */
@Mapper(componentModel = "spring")
public interface DespesasMapper extends EntityMapper<DespesasDTO, Despesas> {}

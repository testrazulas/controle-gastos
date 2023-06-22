package br.com.severo.service.mapper;

import br.com.severo.domain.ControleGastos;
import br.com.severo.service.dto.ControleGastosDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ControleGastos} and its DTO {@link ControleGastosDTO}.
 */
@Mapper(componentModel = "spring")
public interface ControleGastosMapper extends EntityMapper<ControleGastosDTO, ControleGastos> {}

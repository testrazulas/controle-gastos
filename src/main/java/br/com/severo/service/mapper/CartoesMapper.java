package br.com.severo.service.mapper;

import br.com.severo.domain.Cartoes;
import br.com.severo.service.dto.CartoesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cartoes} and its DTO {@link CartoesDTO}.
 */
@Mapper(componentModel = "spring")
public interface CartoesMapper extends EntityMapper<CartoesDTO, Cartoes> {}

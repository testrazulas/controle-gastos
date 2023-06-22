package br.com.severo.service.mapper;

import br.com.severo.domain.Salarios;
import br.com.severo.service.dto.SalariosDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Salarios} and its DTO {@link SalariosDTO}.
 */
@Mapper(componentModel = "spring")
public interface SalariosMapper extends EntityMapper<SalariosDTO, Salarios> {}

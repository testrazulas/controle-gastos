package br.com.severo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DespesasMapperTest {

    private DespesasMapper despesasMapper;

    @BeforeEach
    public void setUp() {
        despesasMapper = new DespesasMapperImpl();
    }
}

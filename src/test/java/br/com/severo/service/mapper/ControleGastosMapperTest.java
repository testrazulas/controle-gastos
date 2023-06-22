package br.com.severo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ControleGastosMapperTest {

    private ControleGastosMapper controleGastosMapper;

    @BeforeEach
    public void setUp() {
        controleGastosMapper = new ControleGastosMapperImpl();
    }
}

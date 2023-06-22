package br.com.severo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CartoesMapperTest {

    private CartoesMapper cartoesMapper;

    @BeforeEach
    public void setUp() {
        cartoesMapper = new CartoesMapperImpl();
    }
}

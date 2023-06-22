package br.com.severo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SalariosMapperTest {

    private SalariosMapper salariosMapper;

    @BeforeEach
    public void setUp() {
        salariosMapper = new SalariosMapperImpl();
    }
}

package br.com.severo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.severo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CartoesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CartoesDTO.class);
        CartoesDTO cartoesDTO1 = new CartoesDTO();
        cartoesDTO1.setId(1L);
        CartoesDTO cartoesDTO2 = new CartoesDTO();
        assertThat(cartoesDTO1).isNotEqualTo(cartoesDTO2);
        cartoesDTO2.setId(cartoesDTO1.getId());
        assertThat(cartoesDTO1).isEqualTo(cartoesDTO2);
        cartoesDTO2.setId(2L);
        assertThat(cartoesDTO1).isNotEqualTo(cartoesDTO2);
        cartoesDTO1.setId(null);
        assertThat(cartoesDTO1).isNotEqualTo(cartoesDTO2);
    }
}

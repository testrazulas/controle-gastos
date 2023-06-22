package br.com.severo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.severo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ControleGastosDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ControleGastosDTO.class);
        ControleGastosDTO controleGastosDTO1 = new ControleGastosDTO();
        controleGastosDTO1.setId(1L);
        ControleGastosDTO controleGastosDTO2 = new ControleGastosDTO();
        assertThat(controleGastosDTO1).isNotEqualTo(controleGastosDTO2);
        controleGastosDTO2.setId(controleGastosDTO1.getId());
        assertThat(controleGastosDTO1).isEqualTo(controleGastosDTO2);
        controleGastosDTO2.setId(2L);
        assertThat(controleGastosDTO1).isNotEqualTo(controleGastosDTO2);
        controleGastosDTO1.setId(null);
        assertThat(controleGastosDTO1).isNotEqualTo(controleGastosDTO2);
    }
}

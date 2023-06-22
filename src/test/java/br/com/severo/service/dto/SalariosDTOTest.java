package br.com.severo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.severo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SalariosDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalariosDTO.class);
        SalariosDTO salariosDTO1 = new SalariosDTO();
        salariosDTO1.setId(1L);
        SalariosDTO salariosDTO2 = new SalariosDTO();
        assertThat(salariosDTO1).isNotEqualTo(salariosDTO2);
        salariosDTO2.setId(salariosDTO1.getId());
        assertThat(salariosDTO1).isEqualTo(salariosDTO2);
        salariosDTO2.setId(2L);
        assertThat(salariosDTO1).isNotEqualTo(salariosDTO2);
        salariosDTO1.setId(null);
        assertThat(salariosDTO1).isNotEqualTo(salariosDTO2);
    }
}

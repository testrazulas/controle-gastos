package br.com.severo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.severo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SalariosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Salarios.class);
        Salarios salarios1 = new Salarios();
        salarios1.setId(1L);
        Salarios salarios2 = new Salarios();
        salarios2.setId(salarios1.getId());
        assertThat(salarios1).isEqualTo(salarios2);
        salarios2.setId(2L);
        assertThat(salarios1).isNotEqualTo(salarios2);
        salarios1.setId(null);
        assertThat(salarios1).isNotEqualTo(salarios2);
    }
}

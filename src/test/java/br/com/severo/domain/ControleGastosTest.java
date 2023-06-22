package br.com.severo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.severo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ControleGastosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ControleGastos.class);
        ControleGastos controleGastos1 = new ControleGastos();
        controleGastos1.setId(1L);
        ControleGastos controleGastos2 = new ControleGastos();
        controleGastos2.setId(controleGastos1.getId());
        assertThat(controleGastos1).isEqualTo(controleGastos2);
        controleGastos2.setId(2L);
        assertThat(controleGastos1).isNotEqualTo(controleGastos2);
        controleGastos1.setId(null);
        assertThat(controleGastos1).isNotEqualTo(controleGastos2);
    }
}

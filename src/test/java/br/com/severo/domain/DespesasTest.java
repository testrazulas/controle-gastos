package br.com.severo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.severo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DespesasTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Despesas.class);
        Despesas despesas1 = new Despesas();
        despesas1.setId(1L);
        Despesas despesas2 = new Despesas();
        despesas2.setId(despesas1.getId());
        assertThat(despesas1).isEqualTo(despesas2);
        despesas2.setId(2L);
        assertThat(despesas1).isNotEqualTo(despesas2);
        despesas1.setId(null);
        assertThat(despesas1).isNotEqualTo(despesas2);
    }
}

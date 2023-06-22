package br.com.severo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.severo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CartoesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cartoes.class);
        Cartoes cartoes1 = new Cartoes();
        cartoes1.setId(1L);
        Cartoes cartoes2 = new Cartoes();
        cartoes2.setId(cartoes1.getId());
        assertThat(cartoes1).isEqualTo(cartoes2);
        cartoes2.setId(2L);
        assertThat(cartoes1).isNotEqualTo(cartoes2);
        cartoes1.setId(null);
        assertThat(cartoes1).isNotEqualTo(cartoes2);
    }
}

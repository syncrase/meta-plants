package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VarieteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Variete.class);
        Variete variete1 = new Variete();
        variete1.setId(1L);
        Variete variete2 = new Variete();
        variete2.setId(variete1.getId());
        assertThat(variete1).isEqualTo(variete2);
        variete2.setId(2L);
        assertThat(variete1).isNotEqualTo(variete2);
        variete1.setId(null);
        assertThat(variete1).isNotEqualTo(variete2);
    }
}

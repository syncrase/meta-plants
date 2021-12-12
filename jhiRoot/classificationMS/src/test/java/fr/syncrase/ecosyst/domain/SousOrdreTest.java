package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SousOrdreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SousOrdre.class);
        SousOrdre sousOrdre1 = new SousOrdre();
        sousOrdre1.setId(1L);
        SousOrdre sousOrdre2 = new SousOrdre();
        sousOrdre2.setId(sousOrdre1.getId());
        assertThat(sousOrdre1).isEqualTo(sousOrdre2);
        sousOrdre2.setId(2L);
        assertThat(sousOrdre1).isNotEqualTo(sousOrdre2);
        sousOrdre1.setId(null);
        assertThat(sousOrdre1).isNotEqualTo(sousOrdre2);
    }
}

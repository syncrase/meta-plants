package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SousRegneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SousRegne.class);
        SousRegne sousRegne1 = new SousRegne();
        sousRegne1.setId(1L);
        SousRegne sousRegne2 = new SousRegne();
        sousRegne2.setId(sousRegne1.getId());
        assertThat(sousRegne1).isEqualTo(sousRegne2);
        sousRegne2.setId(2L);
        assertThat(sousRegne1).isNotEqualTo(sousRegne2);
        sousRegne1.setId(null);
        assertThat(sousRegne1).isNotEqualTo(sousRegne2);
    }
}

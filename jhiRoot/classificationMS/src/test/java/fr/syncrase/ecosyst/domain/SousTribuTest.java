package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SousTribuTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SousTribu.class);
        SousTribu sousTribu1 = new SousTribu();
        sousTribu1.setId(1L);
        SousTribu sousTribu2 = new SousTribu();
        sousTribu2.setId(sousTribu1.getId());
        assertThat(sousTribu1).isEqualTo(sousTribu2);
        sousTribu2.setId(2L);
        assertThat(sousTribu1).isNotEqualTo(sousTribu2);
        sousTribu1.setId(null);
        assertThat(sousTribu1).isNotEqualTo(sousTribu2);
    }
}

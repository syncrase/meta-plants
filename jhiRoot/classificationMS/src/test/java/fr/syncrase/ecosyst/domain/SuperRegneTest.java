package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SuperRegneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SuperRegne.class);
        SuperRegne superRegne1 = new SuperRegne();
        superRegne1.setId(1L);
        SuperRegne superRegne2 = new SuperRegne();
        superRegne2.setId(superRegne1.getId());
        assertThat(superRegne1).isEqualTo(superRegne2);
        superRegne2.setId(2L);
        assertThat(superRegne1).isNotEqualTo(superRegne2);
        superRegne1.setId(null);
        assertThat(superRegne1).isNotEqualTo(superRegne2);
    }
}

package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SuperOrdreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SuperOrdre.class);
        SuperOrdre superOrdre1 = new SuperOrdre();
        superOrdre1.setId(1L);
        SuperOrdre superOrdre2 = new SuperOrdre();
        superOrdre2.setId(superOrdre1.getId());
        assertThat(superOrdre1).isEqualTo(superOrdre2);
        superOrdre2.setId(2L);
        assertThat(superOrdre1).isNotEqualTo(superOrdre2);
        superOrdre1.setId(null);
        assertThat(superOrdre1).isNotEqualTo(superOrdre2);
    }
}

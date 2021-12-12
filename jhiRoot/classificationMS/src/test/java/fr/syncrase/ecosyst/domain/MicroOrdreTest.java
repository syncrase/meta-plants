package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MicroOrdreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MicroOrdre.class);
        MicroOrdre microOrdre1 = new MicroOrdre();
        microOrdre1.setId(1L);
        MicroOrdre microOrdre2 = new MicroOrdre();
        microOrdre2.setId(microOrdre1.getId());
        assertThat(microOrdre1).isEqualTo(microOrdre2);
        microOrdre2.setId(2L);
        assertThat(microOrdre1).isNotEqualTo(microOrdre2);
        microOrdre1.setId(null);
        assertThat(microOrdre1).isNotEqualTo(microOrdre2);
    }
}

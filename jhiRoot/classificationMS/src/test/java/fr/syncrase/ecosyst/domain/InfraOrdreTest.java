package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InfraOrdreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InfraOrdre.class);
        InfraOrdre infraOrdre1 = new InfraOrdre();
        infraOrdre1.setId(1L);
        InfraOrdre infraOrdre2 = new InfraOrdre();
        infraOrdre2.setId(infraOrdre1.getId());
        assertThat(infraOrdre1).isEqualTo(infraOrdre2);
        infraOrdre2.setId(2L);
        assertThat(infraOrdre1).isNotEqualTo(infraOrdre2);
        infraOrdre1.setId(null);
        assertThat(infraOrdre1).isNotEqualTo(infraOrdre2);
    }
}

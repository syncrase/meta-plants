package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SousSectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SousSection.class);
        SousSection sousSection1 = new SousSection();
        sousSection1.setId(1L);
        SousSection sousSection2 = new SousSection();
        sousSection2.setId(sousSection1.getId());
        assertThat(sousSection1).isEqualTo(sousSection2);
        sousSection2.setId(2L);
        assertThat(sousSection1).isNotEqualTo(sousSection2);
        sousSection1.setId(null);
        assertThat(sousSection1).isNotEqualTo(sousSection2);
    }
}

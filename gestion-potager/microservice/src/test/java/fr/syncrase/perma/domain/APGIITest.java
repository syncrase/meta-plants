package fr.syncrase.perma.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.perma.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class APGIITest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(APGII.class);
        APGII aPGII1 = new APGII();
        aPGII1.setId(1L);
        APGII aPGII2 = new APGII();
        aPGII2.setId(aPGII1.getId());
        assertThat(aPGII1).isEqualTo(aPGII2);
        aPGII2.setId(2L);
        assertThat(aPGII1).isNotEqualTo(aPGII2);
        aPGII1.setId(null);
        assertThat(aPGII1).isNotEqualTo(aPGII2);
    }
}

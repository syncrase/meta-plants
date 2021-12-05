package fr.syncrase.perma.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.perma.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class APGIIITest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(APGIII.class);
        APGIII aPGIII1 = new APGIII();
        aPGIII1.setId(1L);
        APGIII aPGIII2 = new APGIII();
        aPGIII2.setId(aPGIII1.getId());
        assertThat(aPGIII1).isEqualTo(aPGIII2);
        aPGIII2.setId(2L);
        assertThat(aPGIII1).isNotEqualTo(aPGIII2);
        aPGIII1.setId(null);
        assertThat(aPGIII1).isNotEqualTo(aPGIII2);
    }
}

package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SousVarieteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SousVariete.class);
        SousVariete sousVariete1 = new SousVariete();
        sousVariete1.setId(1L);
        SousVariete sousVariete2 = new SousVariete();
        sousVariete2.setId(sousVariete1.getId());
        assertThat(sousVariete1).isEqualTo(sousVariete2);
        sousVariete2.setId(2L);
        assertThat(sousVariete1).isNotEqualTo(sousVariete2);
        sousVariete1.setId(null);
        assertThat(sousVariete1).isNotEqualTo(sousVariete2);
    }
}

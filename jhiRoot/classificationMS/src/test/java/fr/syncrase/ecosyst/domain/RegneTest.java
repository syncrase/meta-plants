package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RegneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Regne.class);
        Regne regne1 = new Regne();
        regne1.setId(1L);
        Regne regne2 = new Regne();
        regne2.setId(regne1.getId());
        assertThat(regne1).isEqualTo(regne2);
        regne2.setId(2L);
        assertThat(regne1).isNotEqualTo(regne2);
        regne1.setId(null);
        assertThat(regne1).isNotEqualTo(regne2);
    }
}

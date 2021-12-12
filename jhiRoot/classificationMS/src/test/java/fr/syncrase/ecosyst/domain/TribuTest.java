package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TribuTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tribu.class);
        Tribu tribu1 = new Tribu();
        tribu1.setId(1L);
        Tribu tribu2 = new Tribu();
        tribu2.setId(tribu1.getId());
        assertThat(tribu1).isEqualTo(tribu2);
        tribu2.setId(2L);
        assertThat(tribu1).isNotEqualTo(tribu2);
        tribu1.setId(null);
        assertThat(tribu1).isNotEqualTo(tribu2);
    }
}

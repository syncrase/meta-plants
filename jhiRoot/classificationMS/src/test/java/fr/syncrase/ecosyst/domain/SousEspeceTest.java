package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SousEspeceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SousEspece.class);
        SousEspece sousEspece1 = new SousEspece();
        sousEspece1.setId(1L);
        SousEspece sousEspece2 = new SousEspece();
        sousEspece2.setId(sousEspece1.getId());
        assertThat(sousEspece1).isEqualTo(sousEspece2);
        sousEspece2.setId(2L);
        assertThat(sousEspece1).isNotEqualTo(sousEspece2);
        sousEspece1.setId(null);
        assertThat(sousEspece1).isNotEqualTo(sousEspece2);
    }
}

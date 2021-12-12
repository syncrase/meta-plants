package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SuperFamilleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SuperFamille.class);
        SuperFamille superFamille1 = new SuperFamille();
        superFamille1.setId(1L);
        SuperFamille superFamille2 = new SuperFamille();
        superFamille2.setId(superFamille1.getId());
        assertThat(superFamille1).isEqualTo(superFamille2);
        superFamille2.setId(2L);
        assertThat(superFamille1).isNotEqualTo(superFamille2);
        superFamille1.setId(null);
        assertThat(superFamille1).isNotEqualTo(superFamille2);
    }
}

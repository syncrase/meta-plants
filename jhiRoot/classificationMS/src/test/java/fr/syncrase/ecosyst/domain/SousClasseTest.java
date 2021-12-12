package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SousClasseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SousClasse.class);
        SousClasse sousClasse1 = new SousClasse();
        sousClasse1.setId(1L);
        SousClasse sousClasse2 = new SousClasse();
        sousClasse2.setId(sousClasse1.getId());
        assertThat(sousClasse1).isEqualTo(sousClasse2);
        sousClasse2.setId(2L);
        assertThat(sousClasse1).isNotEqualTo(sousClasse2);
        sousClasse1.setId(null);
        assertThat(sousClasse1).isNotEqualTo(sousClasse2);
    }
}

package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SuperClasseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SuperClasse.class);
        SuperClasse superClasse1 = new SuperClasse();
        superClasse1.setId(1L);
        SuperClasse superClasse2 = new SuperClasse();
        superClasse2.setId(superClasse1.getId());
        assertThat(superClasse1).isEqualTo(superClasse2);
        superClasse2.setId(2L);
        assertThat(superClasse1).isNotEqualTo(superClasse2);
        superClasse1.setId(null);
        assertThat(superClasse1).isNotEqualTo(superClasse2);
    }
}

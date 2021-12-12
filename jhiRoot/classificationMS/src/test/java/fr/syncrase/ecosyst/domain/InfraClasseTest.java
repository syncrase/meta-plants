package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InfraClasseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InfraClasse.class);
        InfraClasse infraClasse1 = new InfraClasse();
        infraClasse1.setId(1L);
        InfraClasse infraClasse2 = new InfraClasse();
        infraClasse2.setId(infraClasse1.getId());
        assertThat(infraClasse1).isEqualTo(infraClasse2);
        infraClasse2.setId(2L);
        assertThat(infraClasse1).isNotEqualTo(infraClasse2);
        infraClasse1.setId(null);
        assertThat(infraClasse1).isNotEqualTo(infraClasse2);
    }
}

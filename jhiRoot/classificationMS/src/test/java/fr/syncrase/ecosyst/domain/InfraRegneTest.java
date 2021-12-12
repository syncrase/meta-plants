package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InfraRegneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InfraRegne.class);
        InfraRegne infraRegne1 = new InfraRegne();
        infraRegne1.setId(1L);
        InfraRegne infraRegne2 = new InfraRegne();
        infraRegne2.setId(infraRegne1.getId());
        assertThat(infraRegne1).isEqualTo(infraRegne2);
        infraRegne2.setId(2L);
        assertThat(infraRegne1).isNotEqualTo(infraRegne2);
        infraRegne1.setId(null);
        assertThat(infraRegne1).isNotEqualTo(infraRegne2);
    }
}

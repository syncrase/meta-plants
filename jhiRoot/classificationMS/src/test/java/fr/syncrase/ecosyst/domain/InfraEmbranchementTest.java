package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InfraEmbranchementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InfraEmbranchement.class);
        InfraEmbranchement infraEmbranchement1 = new InfraEmbranchement();
        infraEmbranchement1.setId(1L);
        InfraEmbranchement infraEmbranchement2 = new InfraEmbranchement();
        infraEmbranchement2.setId(infraEmbranchement1.getId());
        assertThat(infraEmbranchement1).isEqualTo(infraEmbranchement2);
        infraEmbranchement2.setId(2L);
        assertThat(infraEmbranchement1).isNotEqualTo(infraEmbranchement2);
        infraEmbranchement1.setId(null);
        assertThat(infraEmbranchement1).isNotEqualTo(infraEmbranchement2);
    }
}

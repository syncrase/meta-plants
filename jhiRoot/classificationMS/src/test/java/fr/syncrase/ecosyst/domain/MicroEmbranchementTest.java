package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MicroEmbranchementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MicroEmbranchement.class);
        MicroEmbranchement microEmbranchement1 = new MicroEmbranchement();
        microEmbranchement1.setId(1L);
        MicroEmbranchement microEmbranchement2 = new MicroEmbranchement();
        microEmbranchement2.setId(microEmbranchement1.getId());
        assertThat(microEmbranchement1).isEqualTo(microEmbranchement2);
        microEmbranchement2.setId(2L);
        assertThat(microEmbranchement1).isNotEqualTo(microEmbranchement2);
        microEmbranchement1.setId(null);
        assertThat(microEmbranchement1).isNotEqualTo(microEmbranchement2);
    }
}

package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RaunkierPlanteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RaunkierPlante.class);
        RaunkierPlante raunkierPlante1 = new RaunkierPlante();
        raunkierPlante1.setId(1L);
        RaunkierPlante raunkierPlante2 = new RaunkierPlante();
        raunkierPlante2.setId(raunkierPlante1.getId());
        assertThat(raunkierPlante1).isEqualTo(raunkierPlante2);
        raunkierPlante2.setId(2L);
        assertThat(raunkierPlante1).isNotEqualTo(raunkierPlante2);
        raunkierPlante1.setId(null);
        assertThat(raunkierPlante1).isNotEqualTo(raunkierPlante2);
    }
}

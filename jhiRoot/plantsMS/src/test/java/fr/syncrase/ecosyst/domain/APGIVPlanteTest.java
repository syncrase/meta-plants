package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class APGIVPlanteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(APGIVPlante.class);
        APGIVPlante aPGIVPlante1 = new APGIVPlante();
        aPGIVPlante1.setId(1L);
        APGIVPlante aPGIVPlante2 = new APGIVPlante();
        aPGIVPlante2.setId(aPGIVPlante1.getId());
        assertThat(aPGIVPlante1).isEqualTo(aPGIVPlante2);
        aPGIVPlante2.setId(2L);
        assertThat(aPGIVPlante1).isNotEqualTo(aPGIVPlante2);
        aPGIVPlante1.setId(null);
        assertThat(aPGIVPlante1).isNotEqualTo(aPGIVPlante2);
    }
}

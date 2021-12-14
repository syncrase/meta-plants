package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class APGIIIPlanteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(APGIIIPlante.class);
        APGIIIPlante aPGIIIPlante1 = new APGIIIPlante();
        aPGIIIPlante1.setId(1L);
        APGIIIPlante aPGIIIPlante2 = new APGIIIPlante();
        aPGIIIPlante2.setId(aPGIIIPlante1.getId());
        assertThat(aPGIIIPlante1).isEqualTo(aPGIIIPlante2);
        aPGIIIPlante2.setId(2L);
        assertThat(aPGIIIPlante1).isNotEqualTo(aPGIIIPlante2);
        aPGIIIPlante1.setId(null);
        assertThat(aPGIIIPlante1).isNotEqualTo(aPGIIIPlante2);
    }
}

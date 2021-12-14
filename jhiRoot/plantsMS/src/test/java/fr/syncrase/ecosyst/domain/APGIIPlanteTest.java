package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class APGIIPlanteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(APGIIPlante.class);
        APGIIPlante aPGIIPlante1 = new APGIIPlante();
        aPGIIPlante1.setId(1L);
        APGIIPlante aPGIIPlante2 = new APGIIPlante();
        aPGIIPlante2.setId(aPGIIPlante1.getId());
        assertThat(aPGIIPlante1).isEqualTo(aPGIIPlante2);
        aPGIIPlante2.setId(2L);
        assertThat(aPGIIPlante1).isNotEqualTo(aPGIIPlante2);
        aPGIIPlante1.setId(null);
        assertThat(aPGIIPlante1).isNotEqualTo(aPGIIPlante2);
    }
}

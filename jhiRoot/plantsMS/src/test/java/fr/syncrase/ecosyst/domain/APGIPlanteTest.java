package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class APGIPlanteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(APGIPlante.class);
        APGIPlante aPGIPlante1 = new APGIPlante();
        aPGIPlante1.setId(1L);
        APGIPlante aPGIPlante2 = new APGIPlante();
        aPGIPlante2.setId(aPGIPlante1.getId());
        assertThat(aPGIPlante1).isEqualTo(aPGIPlante2);
        aPGIPlante2.setId(2L);
        assertThat(aPGIPlante1).isNotEqualTo(aPGIPlante2);
        aPGIPlante1.setId(null);
        assertThat(aPGIPlante1).isNotEqualTo(aPGIPlante2);
    }
}

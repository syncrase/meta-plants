package fr.syncrase.perma.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.perma.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class APGIIIDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(APGIIIDTO.class);
        APGIIIDTO aPGIIIDTO1 = new APGIIIDTO();
        aPGIIIDTO1.setId(1L);
        APGIIIDTO aPGIIIDTO2 = new APGIIIDTO();
        assertThat(aPGIIIDTO1).isNotEqualTo(aPGIIIDTO2);
        aPGIIIDTO2.setId(aPGIIIDTO1.getId());
        assertThat(aPGIIIDTO1).isEqualTo(aPGIIIDTO2);
        aPGIIIDTO2.setId(2L);
        assertThat(aPGIIIDTO1).isNotEqualTo(aPGIIIDTO2);
        aPGIIIDTO1.setId(null);
        assertThat(aPGIIIDTO1).isNotEqualTo(aPGIIIDTO2);
    }
}

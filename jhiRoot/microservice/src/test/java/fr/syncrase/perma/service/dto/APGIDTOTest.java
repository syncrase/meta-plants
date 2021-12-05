package fr.syncrase.perma.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.perma.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class APGIDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(APGIDTO.class);
        APGIDTO aPGIDTO1 = new APGIDTO();
        aPGIDTO1.setId(1L);
        APGIDTO aPGIDTO2 = new APGIDTO();
        assertThat(aPGIDTO1).isNotEqualTo(aPGIDTO2);
        aPGIDTO2.setId(aPGIDTO1.getId());
        assertThat(aPGIDTO1).isEqualTo(aPGIDTO2);
        aPGIDTO2.setId(2L);
        assertThat(aPGIDTO1).isNotEqualTo(aPGIDTO2);
        aPGIDTO1.setId(null);
        assertThat(aPGIDTO1).isNotEqualTo(aPGIDTO2);
    }
}

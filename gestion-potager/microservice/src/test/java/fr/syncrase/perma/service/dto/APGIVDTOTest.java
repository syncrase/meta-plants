package fr.syncrase.perma.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.perma.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class APGIVDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(APGIVDTO.class);
        APGIVDTO aPGIVDTO1 = new APGIVDTO();
        aPGIVDTO1.setId(1L);
        APGIVDTO aPGIVDTO2 = new APGIVDTO();
        assertThat(aPGIVDTO1).isNotEqualTo(aPGIVDTO2);
        aPGIVDTO2.setId(aPGIVDTO1.getId());
        assertThat(aPGIVDTO1).isEqualTo(aPGIVDTO2);
        aPGIVDTO2.setId(2L);
        assertThat(aPGIVDTO1).isNotEqualTo(aPGIVDTO2);
        aPGIVDTO1.setId(null);
        assertThat(aPGIVDTO1).isNotEqualTo(aPGIVDTO2);
    }
}

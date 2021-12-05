package fr.syncrase.perma.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.perma.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StrateDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StrateDTO.class);
        StrateDTO strateDTO1 = new StrateDTO();
        strateDTO1.setId(1L);
        StrateDTO strateDTO2 = new StrateDTO();
        assertThat(strateDTO1).isNotEqualTo(strateDTO2);
        strateDTO2.setId(strateDTO1.getId());
        assertThat(strateDTO1).isEqualTo(strateDTO2);
        strateDTO2.setId(2L);
        assertThat(strateDTO1).isNotEqualTo(strateDTO2);
        strateDTO1.setId(null);
        assertThat(strateDTO1).isNotEqualTo(strateDTO2);
    }
}

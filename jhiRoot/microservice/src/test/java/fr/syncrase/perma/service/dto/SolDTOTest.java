package fr.syncrase.perma.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.perma.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SolDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SolDTO.class);
        SolDTO solDTO1 = new SolDTO();
        solDTO1.setId(1L);
        SolDTO solDTO2 = new SolDTO();
        assertThat(solDTO1).isNotEqualTo(solDTO2);
        solDTO2.setId(solDTO1.getId());
        assertThat(solDTO1).isEqualTo(solDTO2);
        solDTO2.setId(2L);
        assertThat(solDTO1).isNotEqualTo(solDTO2);
        solDTO1.setId(null);
        assertThat(solDTO1).isNotEqualTo(solDTO2);
    }
}

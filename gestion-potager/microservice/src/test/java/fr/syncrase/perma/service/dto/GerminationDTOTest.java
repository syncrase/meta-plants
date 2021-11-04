package fr.syncrase.perma.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.perma.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GerminationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GerminationDTO.class);
        GerminationDTO germinationDTO1 = new GerminationDTO();
        germinationDTO1.setId(1L);
        GerminationDTO germinationDTO2 = new GerminationDTO();
        assertThat(germinationDTO1).isNotEqualTo(germinationDTO2);
        germinationDTO2.setId(germinationDTO1.getId());
        assertThat(germinationDTO1).isEqualTo(germinationDTO2);
        germinationDTO2.setId(2L);
        assertThat(germinationDTO1).isNotEqualTo(germinationDTO2);
        germinationDTO1.setId(null);
        assertThat(germinationDTO1).isNotEqualTo(germinationDTO2);
    }
}

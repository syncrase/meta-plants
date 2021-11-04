package fr.syncrase.perma.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.perma.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RacineDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RacineDTO.class);
        RacineDTO racineDTO1 = new RacineDTO();
        racineDTO1.setId(1L);
        RacineDTO racineDTO2 = new RacineDTO();
        assertThat(racineDTO1).isNotEqualTo(racineDTO2);
        racineDTO2.setId(racineDTO1.getId());
        assertThat(racineDTO1).isEqualTo(racineDTO2);
        racineDTO2.setId(2L);
        assertThat(racineDTO1).isNotEqualTo(racineDTO2);
        racineDTO1.setId(null);
        assertThat(racineDTO1).isNotEqualTo(racineDTO2);
    }
}

package fr.syncrase.perma.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.perma.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EnsoleillementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EnsoleillementDTO.class);
        EnsoleillementDTO ensoleillementDTO1 = new EnsoleillementDTO();
        ensoleillementDTO1.setId(1L);
        EnsoleillementDTO ensoleillementDTO2 = new EnsoleillementDTO();
        assertThat(ensoleillementDTO1).isNotEqualTo(ensoleillementDTO2);
        ensoleillementDTO2.setId(ensoleillementDTO1.getId());
        assertThat(ensoleillementDTO1).isEqualTo(ensoleillementDTO2);
        ensoleillementDTO2.setId(2L);
        assertThat(ensoleillementDTO1).isNotEqualTo(ensoleillementDTO2);
        ensoleillementDTO1.setId(null);
        assertThat(ensoleillementDTO1).isNotEqualTo(ensoleillementDTO2);
    }
}

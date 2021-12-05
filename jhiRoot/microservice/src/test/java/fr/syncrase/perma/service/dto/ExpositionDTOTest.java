package fr.syncrase.perma.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.perma.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExpositionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpositionDTO.class);
        ExpositionDTO expositionDTO1 = new ExpositionDTO();
        expositionDTO1.setId(1L);
        ExpositionDTO expositionDTO2 = new ExpositionDTO();
        assertThat(expositionDTO1).isNotEqualTo(expositionDTO2);
        expositionDTO2.setId(expositionDTO1.getId());
        assertThat(expositionDTO1).isEqualTo(expositionDTO2);
        expositionDTO2.setId(2L);
        assertThat(expositionDTO1).isNotEqualTo(expositionDTO2);
        expositionDTO1.setId(null);
        assertThat(expositionDTO1).isNotEqualTo(expositionDTO2);
    }
}

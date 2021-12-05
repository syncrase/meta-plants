package fr.syncrase.perma.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.perma.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AllelopathieDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AllelopathieDTO.class);
        AllelopathieDTO allelopathieDTO1 = new AllelopathieDTO();
        allelopathieDTO1.setId(1L);
        AllelopathieDTO allelopathieDTO2 = new AllelopathieDTO();
        assertThat(allelopathieDTO1).isNotEqualTo(allelopathieDTO2);
        allelopathieDTO2.setId(allelopathieDTO1.getId());
        assertThat(allelopathieDTO1).isEqualTo(allelopathieDTO2);
        allelopathieDTO2.setId(2L);
        assertThat(allelopathieDTO1).isNotEqualTo(allelopathieDTO2);
        allelopathieDTO1.setId(null);
        assertThat(allelopathieDTO1).isNotEqualTo(allelopathieDTO2);
    }
}

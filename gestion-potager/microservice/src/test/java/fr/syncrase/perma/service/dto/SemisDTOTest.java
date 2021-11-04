package fr.syncrase.perma.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.perma.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SemisDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SemisDTO.class);
        SemisDTO semisDTO1 = new SemisDTO();
        semisDTO1.setId(1L);
        SemisDTO semisDTO2 = new SemisDTO();
        assertThat(semisDTO1).isNotEqualTo(semisDTO2);
        semisDTO2.setId(semisDTO1.getId());
        assertThat(semisDTO1).isEqualTo(semisDTO2);
        semisDTO2.setId(2L);
        assertThat(semisDTO1).isNotEqualTo(semisDTO2);
        semisDTO1.setId(null);
        assertThat(semisDTO1).isNotEqualTo(semisDTO2);
    }
}

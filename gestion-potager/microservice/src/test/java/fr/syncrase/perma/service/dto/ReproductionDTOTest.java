package fr.syncrase.perma.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.perma.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReproductionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReproductionDTO.class);
        ReproductionDTO reproductionDTO1 = new ReproductionDTO();
        reproductionDTO1.setId(1L);
        ReproductionDTO reproductionDTO2 = new ReproductionDTO();
        assertThat(reproductionDTO1).isNotEqualTo(reproductionDTO2);
        reproductionDTO2.setId(reproductionDTO1.getId());
        assertThat(reproductionDTO1).isEqualTo(reproductionDTO2);
        reproductionDTO2.setId(2L);
        assertThat(reproductionDTO1).isNotEqualTo(reproductionDTO2);
        reproductionDTO1.setId(null);
        assertThat(reproductionDTO1).isNotEqualTo(reproductionDTO2);
    }
}

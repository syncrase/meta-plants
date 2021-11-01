package fr.syncrase.perma.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.syncrase.perma.web.rest.TestUtil;

public class PlanteDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanteDTO.class);
        PlanteDTO planteDTO1 = new PlanteDTO();
        planteDTO1.setId(1L);
        PlanteDTO planteDTO2 = new PlanteDTO();
        assertThat(planteDTO1).isNotEqualTo(planteDTO2);
        planteDTO2.setId(planteDTO1.getId());
        assertThat(planteDTO1).isEqualTo(planteDTO2);
        planteDTO2.setId(2L);
        assertThat(planteDTO1).isNotEqualTo(planteDTO2);
        planteDTO1.setId(null);
        assertThat(planteDTO1).isNotEqualTo(planteDTO2);
    }
}

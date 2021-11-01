package fr.syncrase.perma.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.syncrase.perma.web.rest.TestUtil;

public class CycleDeVieDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CycleDeVieDTO.class);
        CycleDeVieDTO cycleDeVieDTO1 = new CycleDeVieDTO();
        cycleDeVieDTO1.setId(1L);
        CycleDeVieDTO cycleDeVieDTO2 = new CycleDeVieDTO();
        assertThat(cycleDeVieDTO1).isNotEqualTo(cycleDeVieDTO2);
        cycleDeVieDTO2.setId(cycleDeVieDTO1.getId());
        assertThat(cycleDeVieDTO1).isEqualTo(cycleDeVieDTO2);
        cycleDeVieDTO2.setId(2L);
        assertThat(cycleDeVieDTO1).isNotEqualTo(cycleDeVieDTO2);
        cycleDeVieDTO1.setId(null);
        assertThat(cycleDeVieDTO1).isNotEqualTo(cycleDeVieDTO2);
    }
}

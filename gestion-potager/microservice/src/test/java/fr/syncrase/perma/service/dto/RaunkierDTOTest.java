package fr.syncrase.perma.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.syncrase.perma.web.rest.TestUtil;

public class RaunkierDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RaunkierDTO.class);
        RaunkierDTO raunkierDTO1 = new RaunkierDTO();
        raunkierDTO1.setId(1L);
        RaunkierDTO raunkierDTO2 = new RaunkierDTO();
        assertThat(raunkierDTO1).isNotEqualTo(raunkierDTO2);
        raunkierDTO2.setId(raunkierDTO1.getId());
        assertThat(raunkierDTO1).isEqualTo(raunkierDTO2);
        raunkierDTO2.setId(2L);
        assertThat(raunkierDTO1).isNotEqualTo(raunkierDTO2);
        raunkierDTO1.setId(null);
        assertThat(raunkierDTO1).isNotEqualTo(raunkierDTO2);
    }
}

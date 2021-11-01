package fr.syncrase.perma.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.syncrase.perma.web.rest.TestUtil;

public class TypeSemisDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeSemisDTO.class);
        TypeSemisDTO typeSemisDTO1 = new TypeSemisDTO();
        typeSemisDTO1.setId(1L);
        TypeSemisDTO typeSemisDTO2 = new TypeSemisDTO();
        assertThat(typeSemisDTO1).isNotEqualTo(typeSemisDTO2);
        typeSemisDTO2.setId(typeSemisDTO1.getId());
        assertThat(typeSemisDTO1).isEqualTo(typeSemisDTO2);
        typeSemisDTO2.setId(2L);
        assertThat(typeSemisDTO1).isNotEqualTo(typeSemisDTO2);
        typeSemisDTO1.setId(null);
        assertThat(typeSemisDTO1).isNotEqualTo(typeSemisDTO2);
    }
}

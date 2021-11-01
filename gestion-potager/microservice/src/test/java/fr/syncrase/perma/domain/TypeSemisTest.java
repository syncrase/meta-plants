package fr.syncrase.perma.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.syncrase.perma.web.rest.TestUtil;

public class TypeSemisTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeSemis.class);
        TypeSemis typeSemis1 = new TypeSemis();
        typeSemis1.setId(1L);
        TypeSemis typeSemis2 = new TypeSemis();
        typeSemis2.setId(typeSemis1.getId());
        assertThat(typeSemis1).isEqualTo(typeSemis2);
        typeSemis2.setId(2L);
        assertThat(typeSemis1).isNotEqualTo(typeSemis2);
        typeSemis1.setId(null);
        assertThat(typeSemis1).isNotEqualTo(typeSemis2);
    }
}

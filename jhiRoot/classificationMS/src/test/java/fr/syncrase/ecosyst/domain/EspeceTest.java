package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EspeceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Espece.class);
        Espece espece1 = new Espece();
        espece1.setId(1L);
        Espece espece2 = new Espece();
        espece2.setId(espece1.getId());
        assertThat(espece1).isEqualTo(espece2);
        espece2.setId(2L);
        assertThat(espece1).isNotEqualTo(espece2);
        espece1.setId(null);
        assertThat(espece1).isNotEqualTo(espece2);
    }
}

package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SousFormeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SousForme.class);
        SousForme sousForme1 = new SousForme();
        sousForme1.setId(1L);
        SousForme sousForme2 = new SousForme();
        sousForme2.setId(sousForme1.getId());
        assertThat(sousForme1).isEqualTo(sousForme2);
        sousForme2.setId(2L);
        assertThat(sousForme1).isNotEqualTo(sousForme2);
        sousForme1.setId(null);
        assertThat(sousForme1).isNotEqualTo(sousForme2);
    }
}

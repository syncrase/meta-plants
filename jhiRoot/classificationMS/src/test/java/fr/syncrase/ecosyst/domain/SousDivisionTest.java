package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SousDivisionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SousDivision.class);
        SousDivision sousDivision1 = new SousDivision();
        sousDivision1.setId(1L);
        SousDivision sousDivision2 = new SousDivision();
        sousDivision2.setId(sousDivision1.getId());
        assertThat(sousDivision1).isEqualTo(sousDivision2);
        sousDivision2.setId(2L);
        assertThat(sousDivision1).isNotEqualTo(sousDivision2);
        sousDivision1.setId(null);
        assertThat(sousDivision1).isNotEqualTo(sousDivision2);
    }
}

package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SuperDivisionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SuperDivision.class);
        SuperDivision superDivision1 = new SuperDivision();
        superDivision1.setId(1L);
        SuperDivision superDivision2 = new SuperDivision();
        superDivision2.setId(superDivision1.getId());
        assertThat(superDivision1).isEqualTo(superDivision2);
        superDivision2.setId(2L);
        assertThat(superDivision1).isNotEqualTo(superDivision2);
        superDivision1.setId(null);
        assertThat(superDivision1).isNotEqualTo(superDivision2);
    }
}

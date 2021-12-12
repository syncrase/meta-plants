package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RameauTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rameau.class);
        Rameau rameau1 = new Rameau();
        rameau1.setId(1L);
        Rameau rameau2 = new Rameau();
        rameau2.setId(rameau1.getId());
        assertThat(rameau1).isEqualTo(rameau2);
        rameau2.setId(2L);
        assertThat(rameau1).isNotEqualTo(rameau2);
        rameau1.setId(null);
        assertThat(rameau1).isNotEqualTo(rameau2);
    }
}

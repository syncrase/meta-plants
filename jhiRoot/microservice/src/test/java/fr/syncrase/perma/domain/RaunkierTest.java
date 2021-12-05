package fr.syncrase.perma.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.perma.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RaunkierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Raunkier.class);
        Raunkier raunkier1 = new Raunkier();
        raunkier1.setId(1L);
        Raunkier raunkier2 = new Raunkier();
        raunkier2.setId(raunkier1.getId());
        assertThat(raunkier1).isEqualTo(raunkier2);
        raunkier2.setId(2L);
        assertThat(raunkier1).isNotEqualTo(raunkier2);
        raunkier1.setId(null);
        assertThat(raunkier1).isNotEqualTo(raunkier2);
    }
}

package fr.syncrase.perma.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.perma.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExpositionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Exposition.class);
        Exposition exposition1 = new Exposition();
        exposition1.setId(1L);
        Exposition exposition2 = new Exposition();
        exposition2.setId(exposition1.getId());
        assertThat(exposition1).isEqualTo(exposition2);
        exposition2.setId(2L);
        assertThat(exposition1).isNotEqualTo(exposition2);
        exposition1.setId(null);
        assertThat(exposition1).isNotEqualTo(exposition2);
    }
}

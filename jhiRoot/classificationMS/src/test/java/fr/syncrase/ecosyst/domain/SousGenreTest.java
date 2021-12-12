package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SousGenreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SousGenre.class);
        SousGenre sousGenre1 = new SousGenre();
        sousGenre1.setId(1L);
        SousGenre sousGenre2 = new SousGenre();
        sousGenre2.setId(sousGenre1.getId());
        assertThat(sousGenre1).isEqualTo(sousGenre2);
        sousGenre2.setId(2L);
        assertThat(sousGenre1).isNotEqualTo(sousGenre2);
        sousGenre1.setId(null);
        assertThat(sousGenre1).isNotEqualTo(sousGenre2);
    }
}

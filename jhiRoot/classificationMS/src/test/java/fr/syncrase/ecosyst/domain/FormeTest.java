package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Forme.class);
        Forme forme1 = new Forme();
        forme1.setId(1L);
        Forme forme2 = new Forme();
        forme2.setId(forme1.getId());
        assertThat(forme1).isEqualTo(forme2);
        forme2.setId(2L);
        assertThat(forme1).isNotEqualTo(forme2);
        forme1.setId(null);
        assertThat(forme1).isNotEqualTo(forme2);
    }
}

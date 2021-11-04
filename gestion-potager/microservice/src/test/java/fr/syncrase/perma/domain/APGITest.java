package fr.syncrase.perma.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.syncrase.perma.web.rest.TestUtil;

public class APGITest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(APGI.class);
        APGI aPGI1 = new APGI();
        aPGI1.setId(1L);
        APGI aPGI2 = new APGI();
        aPGI2.setId(aPGI1.getId());
        assertThat(aPGI1).isEqualTo(aPGI2);
        aPGI2.setId(2L);
        assertThat(aPGI1).isNotEqualTo(aPGI2);
        aPGI1.setId(null);
        assertThat(aPGI1).isNotEqualTo(aPGI2);
    }
}
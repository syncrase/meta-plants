package fr.syncrase.perma.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.syncrase.perma.web.rest.TestUtil;

public class APGIVTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(APGIV.class);
        APGIV aPGIV1 = new APGIV();
        aPGIV1.setId(1L);
        APGIV aPGIV2 = new APGIV();
        aPGIV2.setId(aPGIV1.getId());
        assertThat(aPGIV1).isEqualTo(aPGIV2);
        aPGIV2.setId(2L);
        assertThat(aPGIV1).isNotEqualTo(aPGIV2);
        aPGIV1.setId(null);
        assertThat(aPGIV1).isNotEqualTo(aPGIV2);
    }
}
package fr.syncrase.perma.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.syncrase.perma.web.rest.TestUtil;

public class APGIIDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(APGIIDTO.class);
        APGIIDTO aPGIIDTO1 = new APGIIDTO();
        aPGIIDTO1.setId(1L);
        APGIIDTO aPGIIDTO2 = new APGIIDTO();
        assertThat(aPGIIDTO1).isNotEqualTo(aPGIIDTO2);
        aPGIIDTO2.setId(aPGIIDTO1.getId());
        assertThat(aPGIIDTO1).isEqualTo(aPGIIDTO2);
        aPGIIDTO2.setId(2L);
        assertThat(aPGIIDTO1).isNotEqualTo(aPGIIDTO2);
        aPGIIDTO1.setId(null);
        assertThat(aPGIIDTO1).isNotEqualTo(aPGIIDTO2);
    }
}

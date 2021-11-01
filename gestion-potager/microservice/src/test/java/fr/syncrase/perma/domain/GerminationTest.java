package fr.syncrase.perma.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.syncrase.perma.web.rest.TestUtil;

public class GerminationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Germination.class);
        Germination germination1 = new Germination();
        germination1.setId(1L);
        Germination germination2 = new Germination();
        germination2.setId(germination1.getId());
        assertThat(germination1).isEqualTo(germination2);
        germination2.setId(2L);
        assertThat(germination1).isNotEqualTo(germination2);
        germination1.setId(null);
        assertThat(germination1).isNotEqualTo(germination2);
    }
}

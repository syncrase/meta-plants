package fr.syncrase.perma.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.syncrase.perma.web.rest.TestUtil;

public class PlanteTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Plante.class);
        Plante plante1 = new Plante();
        plante1.setId(1L);
        Plante plante2 = new Plante();
        plante2.setId(plante1.getId());
        assertThat(plante1).isEqualTo(plante2);
        plante2.setId(2L);
        assertThat(plante1).isNotEqualTo(plante2);
        plante1.setId(null);
        assertThat(plante1).isNotEqualTo(plante2);
    }
}

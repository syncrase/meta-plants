package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CronquistPlanteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CronquistPlante.class);
        CronquistPlante cronquistPlante1 = new CronquistPlante();
        cronquistPlante1.setId(1L);
        CronquistPlante cronquistPlante2 = new CronquistPlante();
        cronquistPlante2.setId(cronquistPlante1.getId());
        assertThat(cronquistPlante1).isEqualTo(cronquistPlante2);
        cronquistPlante2.setId(2L);
        assertThat(cronquistPlante1).isNotEqualTo(cronquistPlante2);
        cronquistPlante1.setId(null);
        assertThat(cronquistPlante1).isNotEqualTo(cronquistPlante2);
    }
}

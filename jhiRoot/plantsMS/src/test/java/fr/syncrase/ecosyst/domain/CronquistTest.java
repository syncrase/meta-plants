package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CronquistTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cronquist.class);
        Cronquist cronquist1 = new Cronquist();
        cronquist1.setId(1L);
        Cronquist cronquist2 = new Cronquist();
        cronquist2.setId(cronquist1.getId());
        assertThat(cronquist1).isEqualTo(cronquist2);
        cronquist2.setId(2L);
        assertThat(cronquist1).isNotEqualTo(cronquist2);
        cronquist1.setId(null);
        assertThat(cronquist1).isNotEqualTo(cronquist2);
    }
}

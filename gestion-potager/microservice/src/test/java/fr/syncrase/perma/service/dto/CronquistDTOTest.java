package fr.syncrase.perma.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.perma.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CronquistDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CronquistDTO.class);
        CronquistDTO cronquistDTO1 = new CronquistDTO();
        cronquistDTO1.setId(1L);
        CronquistDTO cronquistDTO2 = new CronquistDTO();
        assertThat(cronquistDTO1).isNotEqualTo(cronquistDTO2);
        cronquistDTO2.setId(cronquistDTO1.getId());
        assertThat(cronquistDTO1).isEqualTo(cronquistDTO2);
        cronquistDTO2.setId(2L);
        assertThat(cronquistDTO1).isNotEqualTo(cronquistDTO2);
        cronquistDTO1.setId(null);
        assertThat(cronquistDTO1).isNotEqualTo(cronquistDTO2);
    }
}

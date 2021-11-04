package fr.syncrase.perma.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.perma.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MoisDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoisDTO.class);
        MoisDTO moisDTO1 = new MoisDTO();
        moisDTO1.setId(1L);
        MoisDTO moisDTO2 = new MoisDTO();
        assertThat(moisDTO1).isNotEqualTo(moisDTO2);
        moisDTO2.setId(moisDTO1.getId());
        assertThat(moisDTO1).isEqualTo(moisDTO2);
        moisDTO2.setId(2L);
        assertThat(moisDTO1).isNotEqualTo(moisDTO2);
        moisDTO1.setId(null);
        assertThat(moisDTO1).isNotEqualTo(moisDTO2);
    }
}

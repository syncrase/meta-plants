package fr.syncrase.perma.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.perma.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RessemblanceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RessemblanceDTO.class);
        RessemblanceDTO ressemblanceDTO1 = new RessemblanceDTO();
        ressemblanceDTO1.setId(1L);
        RessemblanceDTO ressemblanceDTO2 = new RessemblanceDTO();
        assertThat(ressemblanceDTO1).isNotEqualTo(ressemblanceDTO2);
        ressemblanceDTO2.setId(ressemblanceDTO1.getId());
        assertThat(ressemblanceDTO1).isEqualTo(ressemblanceDTO2);
        ressemblanceDTO2.setId(2L);
        assertThat(ressemblanceDTO1).isNotEqualTo(ressemblanceDTO2);
        ressemblanceDTO1.setId(null);
        assertThat(ressemblanceDTO1).isNotEqualTo(ressemblanceDTO2);
    }
}

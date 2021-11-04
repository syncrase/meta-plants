package fr.syncrase.perma.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.perma.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PeriodeAnneeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PeriodeAnneeDTO.class);
        PeriodeAnneeDTO periodeAnneeDTO1 = new PeriodeAnneeDTO();
        periodeAnneeDTO1.setId(1L);
        PeriodeAnneeDTO periodeAnneeDTO2 = new PeriodeAnneeDTO();
        assertThat(periodeAnneeDTO1).isNotEqualTo(periodeAnneeDTO2);
        periodeAnneeDTO2.setId(periodeAnneeDTO1.getId());
        assertThat(periodeAnneeDTO1).isEqualTo(periodeAnneeDTO2);
        periodeAnneeDTO2.setId(2L);
        assertThat(periodeAnneeDTO1).isNotEqualTo(periodeAnneeDTO2);
        periodeAnneeDTO1.setId(null);
        assertThat(periodeAnneeDTO1).isNotEqualTo(periodeAnneeDTO2);
    }
}

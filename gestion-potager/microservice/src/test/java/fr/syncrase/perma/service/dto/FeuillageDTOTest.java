package fr.syncrase.perma.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.perma.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeuillageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeuillageDTO.class);
        FeuillageDTO feuillageDTO1 = new FeuillageDTO();
        feuillageDTO1.setId(1L);
        FeuillageDTO feuillageDTO2 = new FeuillageDTO();
        assertThat(feuillageDTO1).isNotEqualTo(feuillageDTO2);
        feuillageDTO2.setId(feuillageDTO1.getId());
        assertThat(feuillageDTO1).isEqualTo(feuillageDTO2);
        feuillageDTO2.setId(2L);
        assertThat(feuillageDTO1).isNotEqualTo(feuillageDTO2);
        feuillageDTO1.setId(null);
        assertThat(feuillageDTO1).isNotEqualTo(feuillageDTO2);
    }
}

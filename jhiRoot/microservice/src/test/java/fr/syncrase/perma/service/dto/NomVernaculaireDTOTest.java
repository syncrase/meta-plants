package fr.syncrase.perma.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.perma.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NomVernaculaireDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NomVernaculaireDTO.class);
        NomVernaculaireDTO nomVernaculaireDTO1 = new NomVernaculaireDTO();
        nomVernaculaireDTO1.setId(1L);
        NomVernaculaireDTO nomVernaculaireDTO2 = new NomVernaculaireDTO();
        assertThat(nomVernaculaireDTO1).isNotEqualTo(nomVernaculaireDTO2);
        nomVernaculaireDTO2.setId(nomVernaculaireDTO1.getId());
        assertThat(nomVernaculaireDTO1).isEqualTo(nomVernaculaireDTO2);
        nomVernaculaireDTO2.setId(2L);
        assertThat(nomVernaculaireDTO1).isNotEqualTo(nomVernaculaireDTO2);
        nomVernaculaireDTO1.setId(null);
        assertThat(nomVernaculaireDTO1).isNotEqualTo(nomVernaculaireDTO2);
    }
}

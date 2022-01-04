package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassificationNomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassificationNom.class);
        ClassificationNom classificationNom1 = new ClassificationNom();
        classificationNom1.setId(1L);
        ClassificationNom classificationNom2 = new ClassificationNom();
        classificationNom2.setId(classificationNom1.getId());
        assertThat(classificationNom1).isEqualTo(classificationNom2);
        classificationNom2.setId(2L);
        assertThat(classificationNom1).isNotEqualTo(classificationNom2);
        classificationNom1.setId(null);
        assertThat(classificationNom1).isNotEqualTo(classificationNom2);
    }
}

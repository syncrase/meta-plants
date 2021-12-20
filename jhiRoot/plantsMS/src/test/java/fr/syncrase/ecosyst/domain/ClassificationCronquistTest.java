package fr.syncrase.ecosyst.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.syncrase.ecosyst.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassificationCronquistTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassificationCronquist.class);
        ClassificationCronquist classificationCronquist1 = new ClassificationCronquist();
        classificationCronquist1.setId(1L);
        ClassificationCronquist classificationCronquist2 = new ClassificationCronquist();
        classificationCronquist2.setId(classificationCronquist1.getId());
        assertThat(classificationCronquist1).isEqualTo(classificationCronquist2);
        classificationCronquist2.setId(2L);
        assertThat(classificationCronquist1).isNotEqualTo(classificationCronquist2);
        classificationCronquist1.setId(null);
        assertThat(classificationCronquist1).isNotEqualTo(classificationCronquist2);
    }
}

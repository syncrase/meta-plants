package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClassificationMapperTest {

	private ClassificationMapper classificationMapper;

	@BeforeEach
	public void setUp() {
        classificationMapper = new ClassificationMapperImpl();
	}

	@Test
	public void testEntityFromId() {
		Long id = 1L;
		assertThat(classificationMapper.fromId(id).getId()).isEqualTo(id);
		assertThat(classificationMapper.fromId(null)).isNull();
	}
}

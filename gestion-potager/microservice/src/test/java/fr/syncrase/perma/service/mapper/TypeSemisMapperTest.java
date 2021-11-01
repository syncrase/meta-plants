package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TypeSemisMapperTest {

	private TypeSemisMapper typeSemisMapper;

	@BeforeEach
	public void setUp() {
        typeSemisMapper = new TypeSemisMapperImpl();
	}

	@Test
	public void testEntityFromId() {
		Long id = 1L;
		assertThat(typeSemisMapper.fromId(id).getId()).isEqualTo(id);
		assertThat(typeSemisMapper.fromId(null)).isNull();
	}
}

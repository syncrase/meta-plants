package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SemisMapperTest {

	private SemisMapper semisMapper;

	@BeforeEach
	public void setUp() {
        semisMapper = new SemisMapperImpl();
	}

	@Test
	public void testEntityFromId() {
		Long id = 1L;
		assertThat(semisMapper.fromId(id).getId()).isEqualTo(id);
		assertThat(semisMapper.fromId(null)).isNull();
	}
}

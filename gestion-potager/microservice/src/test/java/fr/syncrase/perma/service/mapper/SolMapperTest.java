package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SolMapperTest {

	private SolMapper solMapper;

	@BeforeEach
	public void setUp() {
        solMapper = new SolMapperImpl();
	}

	@Test
	public void testEntityFromId() {
		Long id = 1L;
		assertThat(solMapper.fromId(id).getId()).isEqualTo(id);
		assertThat(solMapper.fromId(null)).isNull();
	}
}

package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GerminationMapperTest {

	private GerminationMapper germinationMapper;

	@BeforeEach
	public void setUp() {
        germinationMapper = new GerminationMapperImpl();
	}

	@Test
	public void testEntityFromId() {
		Long id = 1L;
		assertThat(germinationMapper.fromId(id).getId()).isEqualTo(id);
		assertThat(germinationMapper.fromId(null)).isNull();
	}
}

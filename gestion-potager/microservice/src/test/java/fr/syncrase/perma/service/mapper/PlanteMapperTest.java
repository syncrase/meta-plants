package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlanteMapperTest {

	private PlanteMapper planteMapper;

	@BeforeEach
	public void setUp() {
        planteMapper = new PlanteMapperImpl();
	}

	@Test
	public void testEntityFromId() {
		Long id = 1L;
		assertThat(planteMapper.fromId(id).getId()).isEqualTo(id);
		assertThat(planteMapper.fromId(null)).isNull();
	}
}

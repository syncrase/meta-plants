package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CycleDeVieMapperTest {

	private CycleDeVieMapper cycleDeVieMapper;

	@BeforeEach
	public void setUp() {
        cycleDeVieMapper = new CycleDeVieMapperImpl();
	}

	@Test
	public void testEntityFromId() {
		Long id = 1L;
		assertThat(cycleDeVieMapper.fromId(id).getId()).isEqualTo(id);
		assertThat(cycleDeVieMapper.fromId(null)).isNull();
	}
}

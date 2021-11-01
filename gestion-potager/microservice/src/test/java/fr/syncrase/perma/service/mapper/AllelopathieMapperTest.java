package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AllelopathieMapperTest {

	private AllelopathieMapper allelopathieMapper;

	@BeforeEach
	public void setUp() {
        allelopathieMapper = new AllelopathieMapperImpl();
	}

	@Test
	public void testEntityFromId() {
		Long id = 1L;
		assertThat(allelopathieMapper.fromId(id).getId()).isEqualTo(id);
		assertThat(allelopathieMapper.fromId(null)).isNull();
	}
}

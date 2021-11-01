package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RaunkierMapperTest {

	private RaunkierMapper raunkierMapper;

	@BeforeEach
	public void setUp() {
        raunkierMapper = new RaunkierMapperImpl();
	}

	@Test
	public void testEntityFromId() {
		Long id = 1L;
		assertThat(raunkierMapper.fromId(id).getId()).isEqualTo(id);
		assertThat(raunkierMapper.fromId(null)).isNull();
	}
}

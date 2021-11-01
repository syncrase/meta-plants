package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RessemblanceMapperTest {

	private RessemblanceMapper ressemblanceMapper;

	@BeforeEach
	public void setUp() {
        ressemblanceMapper = new RessemblanceMapperImpl();
	}

	@Test
	public void testEntityFromId() {
		Long id = 1L;
		assertThat(ressemblanceMapper.fromId(id).getId()).isEqualTo(id);
		assertThat(ressemblanceMapper.fromId(null)).isNull();
	}
}

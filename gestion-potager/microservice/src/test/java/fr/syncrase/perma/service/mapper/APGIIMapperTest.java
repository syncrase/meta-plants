package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class APGIIMapperTest {

	private APGIIMapper aPGIIMapper;

	@BeforeEach
	public void setUp() {
		aPGIIMapper = new APGIIMapperImpl();
	}

	@Test
	public void testEntityFromId() {
		Long id = 1L;
		assertThat(aPGIIMapper.fromId(id).getId()).isEqualTo(id);
		assertThat(aPGIIMapper.fromId(null)).isNull();
	}
}

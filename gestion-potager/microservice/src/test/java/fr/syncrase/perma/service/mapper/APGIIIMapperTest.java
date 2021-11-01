package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class APGIIIMapperTest {

	private APGIIIMapper aPGIIIMapper;

	@BeforeEach
	public void setUp() {
        aPGIIIMapper = new APGIIIMapperImpl();
	}

	@Test
	public void testEntityFromId() {
		Long id = 1L;
		assertThat(aPGIIIMapper.fromId(id).getId()).isEqualTo(id);
		assertThat(aPGIIIMapper.fromId(null)).isNull();
	}
}

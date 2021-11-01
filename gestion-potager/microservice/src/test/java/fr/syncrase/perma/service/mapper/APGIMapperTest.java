package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class APGIMapperTest {

	private APGIMapper aPGIMapper;

	@BeforeEach
	public void setUp() {
        aPGIMapper = new APGIMapperImpl();
	}

	@Test
	public void testEntityFromId() {
		Long id = 1L;
		assertThat(aPGIMapper.fromId(id).getId()).isEqualTo(id);
		assertThat(aPGIMapper.fromId(null)).isNull();
	}
}

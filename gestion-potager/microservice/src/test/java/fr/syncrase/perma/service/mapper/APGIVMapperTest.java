package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class APGIVMapperTest {

	private APGIVMapper aPGIVMapper;

	@BeforeEach
	public void setUp() {
        aPGIVMapper = new APGIVMapperImpl();
	}

	@Test
	public void testEntityFromId() {
		Long id = 1L;
		assertThat(aPGIVMapper.fromId(id).getId()).isEqualTo(id);
		assertThat(aPGIVMapper.fromId(null)).isNull();
	}
}

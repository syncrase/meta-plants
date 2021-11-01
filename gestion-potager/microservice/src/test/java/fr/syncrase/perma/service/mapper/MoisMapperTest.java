package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MoisMapperTest {

	private MoisMapper moisMapper;

	@BeforeEach
	public void setUp() {
        moisMapper = new MoisMapperImpl();
	}

	@Test
	public void testEntityFromId() {
		Long id = 1L;
		assertThat(moisMapper.fromId(id).getId()).isEqualTo(id);
		assertThat(moisMapper.fromId(null)).isNull();
	}
}

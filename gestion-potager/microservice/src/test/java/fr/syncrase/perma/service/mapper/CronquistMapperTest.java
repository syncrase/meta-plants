package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class CronquistMapperTest {

	private CronquistMapper cronquistMapper;

	@BeforeEach
	public void setUp() {
		cronquistMapper = Mappers.getMapper(CronquistMapper.class);
	}

	@Test
	public void testEntityFromId() {
		Long id = 1L;
		assertThat(cronquistMapper.fromId(id).getId()).isEqualTo(id);
		assertThat(cronquistMapper.fromId(null)).isNull();
	}
}

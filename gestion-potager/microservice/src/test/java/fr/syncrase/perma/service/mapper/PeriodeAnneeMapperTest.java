package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PeriodeAnneeMapperTest {

	private PeriodeAnneeMapper periodeAnneeMapper;

	@BeforeEach
	public void setUp() {
        periodeAnneeMapper = new PeriodeAnneeMapperImpl();
	}

	@Test
	public void testEntityFromId() {
		Long id = 1L;
		assertThat(periodeAnneeMapper.fromId(id).getId()).isEqualTo(id);
		assertThat(periodeAnneeMapper.fromId(null)).isNull();
	}
}

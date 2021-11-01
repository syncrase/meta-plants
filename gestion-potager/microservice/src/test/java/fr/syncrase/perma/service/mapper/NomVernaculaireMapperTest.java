package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NomVernaculaireMapperTest {

	private NomVernaculaireMapper nomVernaculaireMapper;

	@BeforeEach
	public void setUp() {
        nomVernaculaireMapper = new NomVernaculaireMapperImpl();
	}

	@Test
	public void testEntityFromId() {
		Long id = 1L;
		assertThat(nomVernaculaireMapper.fromId(id).getId()).isEqualTo(id);
		assertThat(nomVernaculaireMapper.fromId(null)).isNull();
	}
}

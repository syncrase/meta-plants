package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NomVernaculaireMapperTest {

    private NomVernaculaireMapper nomVernaculaireMapper;

    @BeforeEach
    public void setUp() {
        nomVernaculaireMapper = new NomVernaculaireMapperImpl();
    }
}

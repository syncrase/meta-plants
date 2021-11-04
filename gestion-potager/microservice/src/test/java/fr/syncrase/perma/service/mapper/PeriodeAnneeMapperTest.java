package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PeriodeAnneeMapperTest {

    private PeriodeAnneeMapper periodeAnneeMapper;

    @BeforeEach
    public void setUp() {
        periodeAnneeMapper = new PeriodeAnneeMapperImpl();
    }
}

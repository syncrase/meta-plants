package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FeuillageMapperTest {

    private FeuillageMapper feuillageMapper;

    @BeforeEach
    public void setUp() {
        feuillageMapper = new FeuillageMapperImpl();
    }
}

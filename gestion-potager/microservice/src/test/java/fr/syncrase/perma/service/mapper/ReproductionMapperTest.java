package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReproductionMapperTest {

    private ReproductionMapper reproductionMapper;

    @BeforeEach
    public void setUp() {
        reproductionMapper = new ReproductionMapperImpl();
    }
}

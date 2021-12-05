package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CycleDeVieMapperTest {

    private CycleDeVieMapper cycleDeVieMapper;

    @BeforeEach
    public void setUp() {
        cycleDeVieMapper = new CycleDeVieMapperImpl();
    }
}

package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlanteMapperTest {

    private PlanteMapper planteMapper;

    @BeforeEach
    public void setUp() {
        planteMapper = new PlanteMapperImpl();
    }
}

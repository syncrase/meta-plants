package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RacineMapperTest {

    private RacineMapper racineMapper;

    @BeforeEach
    public void setUp() {
        racineMapper = new RacineMapperImpl();
    }
}

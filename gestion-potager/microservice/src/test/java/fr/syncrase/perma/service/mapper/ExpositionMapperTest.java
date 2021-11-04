package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExpositionMapperTest {

    private ExpositionMapper expositionMapper;

    @BeforeEach
    public void setUp() {
        expositionMapper = new ExpositionMapperImpl();
    }
}

package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AllelopathieMapperTest {

    private AllelopathieMapper allelopathieMapper;

    @BeforeEach
    public void setUp() {
        allelopathieMapper = new AllelopathieMapperImpl();
    }
}

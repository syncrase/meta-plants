package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SemisMapperTest {

    private SemisMapper semisMapper;

    @BeforeEach
    public void setUp() {
        semisMapper = new SemisMapperImpl();
    }
}

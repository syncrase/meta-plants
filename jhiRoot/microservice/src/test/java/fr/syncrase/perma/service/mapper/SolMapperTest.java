package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SolMapperTest {

    private SolMapper solMapper;

    @BeforeEach
    public void setUp() {
        solMapper = new SolMapperImpl();
    }
}

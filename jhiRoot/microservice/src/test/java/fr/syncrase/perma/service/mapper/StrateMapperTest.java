package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StrateMapperTest {

    private StrateMapper strateMapper;

    @BeforeEach
    public void setUp() {
        strateMapper = new StrateMapperImpl();
    }
}

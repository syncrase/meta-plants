package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MoisMapperTest {

    private MoisMapper moisMapper;

    @BeforeEach
    public void setUp() {
        moisMapper = new MoisMapperImpl();
    }
}

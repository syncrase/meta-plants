package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class APGIVMapperTest {

    private APGIVMapper aPGIVMapper;

    @BeforeEach
    public void setUp() {
        aPGIVMapper = new APGIVMapperImpl();
    }
}

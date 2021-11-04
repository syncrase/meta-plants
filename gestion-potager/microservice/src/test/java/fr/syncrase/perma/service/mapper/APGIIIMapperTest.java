package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class APGIIIMapperTest {

    private APGIIIMapper aPGIIIMapper;

    @BeforeEach
    public void setUp() {
        aPGIIIMapper = new APGIIIMapperImpl();
    }
}

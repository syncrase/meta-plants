package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class APGIIMapperTest {

    private APGIIMapper aPGIIMapper;

    @BeforeEach
    public void setUp() {
        aPGIIMapper = new APGIIMapperImpl();
    }
}

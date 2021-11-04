package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class APGIMapperTest {

    private APGIMapper aPGIMapper;

    @BeforeEach
    public void setUp() {
        aPGIMapper = new APGIMapperImpl();
    }
}

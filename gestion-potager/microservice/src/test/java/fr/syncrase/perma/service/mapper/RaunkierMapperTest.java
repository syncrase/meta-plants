package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RaunkierMapperTest {

    private RaunkierMapper raunkierMapper;

    @BeforeEach
    public void setUp() {
        raunkierMapper = new RaunkierMapperImpl();
    }
}

package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RessemblanceMapperTest {

    private RessemblanceMapper ressemblanceMapper;

    @BeforeEach
    public void setUp() {
        ressemblanceMapper = new RessemblanceMapperImpl();
    }
}

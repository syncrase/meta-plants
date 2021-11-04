package fr.syncrase.perma.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CronquistMapperTest {

    private CronquistMapper cronquistMapper;

    @BeforeEach
    public void setUp() {
        cronquistMapper = new CronquistMapperImpl();
    }
}

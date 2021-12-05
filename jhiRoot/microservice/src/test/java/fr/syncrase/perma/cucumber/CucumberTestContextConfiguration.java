package fr.syncrase.perma.cucumber;

import fr.syncrase.perma.MicroserviceApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = MicroserviceApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}

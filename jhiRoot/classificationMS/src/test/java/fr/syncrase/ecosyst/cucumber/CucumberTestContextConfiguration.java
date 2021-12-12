package fr.syncrase.ecosyst.cucumber;

import fr.syncrase.ecosyst.ClassificationMsApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = ClassificationMsApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}

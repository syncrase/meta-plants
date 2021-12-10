package fr.syncrase.ecosyst.cucumber;

import fr.syncrase.ecosyst.CrawlerApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = CrawlerApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}

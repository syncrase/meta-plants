package fr.syncrase.perma.plantes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import fr.syncrase.perma.plantes.modeles.PeriodeAnnee;
import fr.syncrase.perma.plantes.modeles.Plante;
import fr.syncrase.perma.plantes.repositories.PlantesRepository;

@SpringBootApplication
public class PlantesApplication extends WebSecurityConfigurerAdapter implements CommandLineRunner {

	@Autowired
	private PlantesRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(PlantesApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// test l'insertion
		Plante pt = new Plante("papaver somniferum",
				new PeriodeAnnee(PeriodeAnnee.Mois.JANVIER, PeriodeAnnee.Mois.MIAVRIL),
				new PeriodeAnnee(PeriodeAnnee.Mois.JANVIER, PeriodeAnnee.Mois.MIAVRIL));

		repository.save(pt);

		// test la récupération
		System.out.println("Plantes findAll():");
		System.out.println("-------------------------------");
		for (Plante plante : repository.findAll()) {
			System.out.println(plante.toString());
		}

//		System.out.println(repository.findByNom("papaver somniferum"));
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http.authorizeRequests(
				a -> a.antMatchers("/", "/error", "/webjars/**").permitAll().anyRequest().authenticated())
				.exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
				.logout(l -> l.logoutSuccessUrl("/").permitAll())//
				.csrf(c -> c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))//
				.oauth2Login();
		// @formatter:on

	}
}

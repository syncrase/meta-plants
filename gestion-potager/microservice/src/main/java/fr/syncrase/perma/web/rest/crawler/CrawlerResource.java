/**
 * 
 */
package fr.syncrase.perma.web.rest.crawler;

import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fr.syncrase.perma.domain.Plante;
import fr.syncrase.perma.service.crawler.CrawlerService;
import io.github.jhipster.web.util.PaginationUtil;

/**
 * @author syncrase
 *
 */
/**
 * REST controller for managing the crawling
 */
@RestController
@RequestMapping("/api")
public class CrawlerResource {

	private final Logger log = LoggerFactory.getLogger(CrawlerResource.class);

//	private static final String ENTITY_NAME = "microserviceCrawler";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final CrawlerService crawlerService;

//    , CrawlerQueryService crawlerQueryService
	public CrawlerResource(CrawlerService crawlerService) {
		this.crawlerService = crawlerService;
//        this.crawlerQueryService = crawlerQueryService;
	}

	/**
	 * {@code GET  /crawler} : Crawler websites and populate database
	 *
	 * @param allelopathieDTO the allelopathieDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new allelopathieDTO, or with status
	 *         {@code 400 (Bad Request)} if the allelopathie has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@GetMapping("/crawler")
	public ResponseEntity<List<Plante>> brutalCrawl() throws URISyntaxException {
		log.debug("REST request to crawl all websites");
		List<Plante> result = crawlerService.crawl();

//        Page<AllelopathieDTO> page = allelopathieQueryService.findByCriteria(criteria, pageable);
		Page<Plante> page = new PageImpl<>(result);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(result);
//		return null;
	}

}

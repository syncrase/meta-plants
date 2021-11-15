package fr.syncrase.perma.aop.dev.init;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import fr.syncrase.perma.domain.Allelopathie;
import fr.syncrase.perma.domain.Classification;
import fr.syncrase.perma.domain.Cronquist;
import fr.syncrase.perma.domain.Feuillage;
import fr.syncrase.perma.domain.Mois;
import fr.syncrase.perma.domain.NomVernaculaire;
import fr.syncrase.perma.domain.Plante;
import fr.syncrase.perma.domain.Racine;
import fr.syncrase.perma.domain.Sol;
import fr.syncrase.perma.domain.Strate;
import fr.syncrase.perma.repository.AllelopathieRepository;
import fr.syncrase.perma.repository.ClassificationRepository;
import fr.syncrase.perma.repository.FeuillageRepository;
import fr.syncrase.perma.repository.MoisRepository;
import fr.syncrase.perma.repository.PlanteRepository;
import fr.syncrase.perma.repository.RacineRepository;
import fr.syncrase.perma.repository.SolRepository;
import fr.syncrase.perma.repository.StrateRepository;

@Component
public class InsertData implements ApplicationListener<ContextRefreshedEvent> {
// implements ApplicationListener<ContextRefreshedEvent>
	private Logger log = LoggerFactory.getLogger(InsertData.class);

	@Autowired
	private MoisRepository moisRepository;
	@Autowired
	private RacineRepository racineRepository;
	@Autowired
	private StrateRepository strateRepository;
	@Autowired
	private SolRepository solRepository;
	@Autowired
	private FeuillageRepository feuillageRepository;
	@Autowired
	private PlanteRepository planteRepository;
	@Autowired
	private AllelopathieRepository allelopathieRepository;
	@Autowired
	ClassificationRepository classificationRepository;
//	@Autowired
//	public void setAllelopathieRepository(
//			AllelopathieRepository allelopathieRepository) {
//		this.allelopathieRepository = allelopathieRepository;
//	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
//		@ Override
		insertMois();
		insertTypeRacine();
		insertStrate();
		insertTypeFeuillage();
//		insertTypeTerre();
		insertRichesseSol();
//		insertEnsoleillement();
//		insertVitesseCroissance();

		Map<String, Plante> insertedPlants = new HashMap<>();
		Map<String, String> data = new HashMap<>();

		data.put("Ordre", "liliales");
		data.put("Famille", "liliaceae");
		data.put("Genre", "allium");
		data.put("Espece", "allium sativum");
		data.put("Plante", "ail");
		insertedPlants.put("ail", insertPlant(data));

		data.put("Ordre", "apiales");
		data.put("Famille", "apiaceae");
		data.put("Genre", "daucus");
		data.put("Espece", "daucus carota");
		data.put("Plante", "carotte");
		insertedPlants.put("carotte", insertPlant(data));

		data.put("Ordre", "apiales");
		data.put("Famille", "apiaceae");
		data.put("Genre", "anethum");
		data.put("Espece", "anethum graveolens");
		data.put("Plante", "aneth");
		insertedPlants.put("aneth", insertPlant(data));

		data.put("Ordre", "liliales");
		data.put("Famille", "liliaceae");
		data.put("Genre", "allium");
		data.put("Espece", "allium cepa");
		data.put("Plante", "échalotte");
		insertedPlants.put("échalotte", insertPlant(data));
		data.put("Plante", "oignon");
		insertedPlants.put("oignon", insertPlant(data));

		data.put("Ordre", "liliales");
		data.put("Famille", "alliaceae");
		data.put("Genre", "allium");
		data.put("Espece", "allium porrum");
		data.put("Plante", "poireau");
		insertedPlants.put("poireau", insertPlant(data));

		data.put("Ordre", "brassicales");
		data.put("Famille", "brassicaceae");
		data.put("Genre", "brassica");
		data.put("Espece", "brassica oleracea");
		data.put("Plante", "chou commun");
		insertedPlants.put("chou commun", insertPlant(data));
		data.put("Plante", "chou-rave");
		insertedPlants.put("chou-rave", insertPlant(data));
		data.put("Plante", "brocoli");
		insertedPlants.put("brocoli", insertPlant(data));
		data.put("Plante", "chou fleur");
		insertedPlants.put("chou fleur", insertPlant(data));

		data.put("Ordre", "lamiales");
		data.put("Famille", "lamiaceae");
		data.put("Genre", "satureja");
		data.put("Espece", "satureja hortensis");
		data.put("Plante", "sarriette");
		insertedPlants.put("sarriette", insertPlant(data));

		data.put("Ordre", "caryophyllales");
		data.put("Famille", "chenopodiaceae");
		data.put("Genre", "beta");
		data.put("Espece", "beta vulgaris");
		data.put("Plante", "betterave");
		insertedPlants.put("betterave", insertPlant(data));
		data.put("Plante", "bette");
		insertedPlants.put("bette", insertPlant(data));

		data.put("Ordre", "solanales");
		data.put("Famille", "solanaceae");
		data.put("Genre", "solanum");
		data.put("Espece", "solanum lycopersicum");
		data.put("Plante", "tomate");
		insertedPlants.put("tomate", insertPlant(data));

		data.put("Ordre", "asterales");
		data.put("Famille", "asteraceae");
		data.put("Genre", "lactuca");
		data.put("Espece", "lactuca sativa");
		data.put("Plante", "laitue");
		insertedPlants.put("laitue", insertPlant(data));

		data.put("Ordre", "violales");
		data.put("Famille", "cucurbitaceae");
		data.put("Genre", "cucumis");
		data.put("Espece", "cucumis sativus");
		data.put("Plante", "concombre");
		insertedPlants.put("concombre", insertPlant(data));

		data.put("Ordre", "lamiales");
		data.put("Famille", "lamiaceae");
		data.put("Genre", "ocimum");
		data.put("Espece", "ocimum basilicum");
		data.put("Plante", "basilic");
		insertedPlants.put("basilic", insertPlant(data));

		data.put("Ordre", "fabales");
		data.put("Famille", "fabaceae");
		data.put("Genre", "phaseolus");
		data.put("Espece", "phaseolus vulgaris");
		data.put("Plante", "haricot");
		insertedPlants.put("haricot", insertPlant(data));

		data.put("Genre", "pisum");
		data.put("Espece", "pisum sativum");
		data.put("Plante", "pois");
		insertedPlants.put("pois", insertPlant(data));

		data.put("Genre", "vicia");
		data.put("Espece", "vicia faba");
		data.put("Plante", "fève");
		insertedPlants.put("fève", insertPlant(data));

		data.put("Ordre", "solanales");
		data.put("Famille", "solanaceae");
		data.put("Genre", "solanum");
		data.put("Espece", "solanum tuberosum");
		data.put("Plante", "pomme de terre");
		insertedPlants.put("pomme de terre", insertPlant(data));

		data.put("Ordre", "apiales");
		data.put("Famille", "apiaceae");
		data.put("Genre", "petroselinum");
		data.put("Espece", "petroselinum crispum");
		data.put("Plante", "persil");
		insertedPlants.put("persil", insertPlant(data));

		data.put("Ordre", "apiales");
		data.put("Famille", "apiaceae");
		data.put("Genre", "coriandrum");
		data.put("Espece", "coriandrum sativum");
		data.put("Plante", "coriandre");
		insertedPlants.put("coriandre", insertPlant(data));

		data.put("Ordre", "cyperales");
		data.put("Famille", "poaceae");
		data.put("Genre", "zea");
		data.put("Espece", "zea mays");
		data.put("Plante", "maïs");
		insertedPlants.put("maïs", insertPlant(data));

		data.put("Ordre", "violales");
		data.put("Famille", "cucurbitaceae");
		data.put("Genre", "cucurbita");
		data.put("Espece", "cucurbita maxima");
		data.put("Plante", "courge");
		insertedPlants.put("courge", insertPlant(data));
		data.put("Plante", "potiron");
		insertedPlants.put("potiron", insertPlant(data));
		data.put("Plante", "courgette");
		insertedPlants.put("courgette", insertPlant(data));

		data.put("Ordre", "rosales");
		data.put("Famille", "rosaceae");
		data.put("Genre", "rubus");
		data.put("Espece", "rubus idaeus");
		data.put("Plante", "framboisier");
		insertedPlants.put("framboisier", insertPlant(data));

		data.put("Ordre", "lamiales");
		data.put("Famille", "boraginaceae");
		data.put("Genre", "myosotis");
		data.put("Espece", "myosotis arvensis");
		data.put("Plante", "myosotis des champs");
		insertedPlants.put("myosotis des champs", insertPlant(data));

		data.put("Ordre", "geraniales");
		data.put("Famille", "tropaeolaceae");
		data.put("Genre", "tropaeolum");
		data.put("Espece", "tropaeolum majus");
		data.put("Plante", "grande capucine");
		insertedPlants.put("grande capucine", insertPlant(data));

		data.put("Ordre", "asterales");
		data.put("Famille", "asteraceae");
		data.put("Genre", "tagetes");
		data.put("Espece", "tagetes patula");
		data.put("Plante", "oeillet d'inde");
		insertedPlants.put("oeillet d'inde", insertPlant(data));

		data.put("Ordre", "liliales");
		data.put("Famille", "liliaceae");
		data.put("Genre", "allium");
		data.put("Espece", "allium schoenoprasum");
		data.put("Plante", "ciboulette");
		insertedPlants.put("ciboulette", insertPlant(data));

		data.put("Ordre", "rosales");
		data.put("Famille", "rosaceae");
		data.put("Genre", "rosa");
		data.put("Espece", "rosa gallica");
		data.put("Plante", "rosier de france");
		insertedPlants.put("rosier de france", insertPlant(data));

		data.put("Ordre", "lamiales");
		data.put("Famille", "lamiaceae");
		data.put("Genre", "rosmarinus");
		data.put("Espece", "rosmarinus officinalis");
		data.put("Plante", "romarin");
		insertedPlants.put("romarin", insertPlant(data));

		data.put("Ordre", "lamiales");
		data.put("Famille", "lamiaceae");
		data.put("Genre", "thymus");
		data.put("Espece", "thymus vulgaris");
		data.put("Plante", "thym commun");
		insertedPlants.put("thym commun", insertPlant(data));

		data.put("Ordre", "liliales");
		data.put("Famille", "liliaceae");
		data.put("Genre", "asparagus");
		data.put("Espece", "asparagus officinalis");
		data.put("Plante", "asperge");
		insertedPlants.put("asperge", insertPlant(data));

		data.put("Ordre", "solanales");
		data.put("Famille", "solanaceae");
		data.put("Genre", "solanum");
		data.put("Espece", "solanum melongena");
		data.put("Plante", "aubergine");
		insertedPlants.put("aubergine", insertPlant(data));

		data.put("Ordre", "capparales");
		data.put("Famille", "brassicaceae");
		data.put("Genre", "raphanus");
		data.put("Espece", "raphanus sativus");
		data.put("Plante", "radis");
		insertedPlants.put("radis", insertPlant(data));

		data.put("Ordre", "lamiales");
		data.put("Famille", "lamiaceae");
		data.put("Genre", "salvia");
		data.put("Espece", "alvia officinalis");
		data.put("Plante", "sauge");
		insertedPlants.put("sauge", insertPlant(data));

		data.put("Ordre", "lamiales");
		data.put("Famille", "lamiaceae");
		data.put("Genre", "mentha");
		data.put("Espece", "mentha xpiperita");
		data.put("Plante", "menthe poivrée");
		insertedPlants.put("menthe poivrée", insertPlant(data));

		data.put("Ordre", "lamiales");
		data.put("Famille", "lamiaceae");
		data.put("Genre", "origanum");
		data.put("Espece", "origanum vulgare");
		data.put("Plante", "origan");
		insertedPlants.put("origan", insertPlant(data));

		data.put("Ordre", "asterales");
		data.put("Famille", "asteraceae");
		data.put("Genre", "helianthus");
		data.put("Espece", "helianthus annuu");
		data.put("Plante", "tournesol");
		insertedPlants.put("tournesol", insertPlant(data));

		data.put("Ordre", "rosales");
		data.put("Famille", "rosaceae");
		data.put("Genre", "fragaria");
		data.put("Espece", "fragaria vesca");
		data.put("Plante", "fraisier des bois");
		insertedPlants.put("fraisier des bois", insertPlant(data));

		data.put("Ordre", "caryophyllales");
		data.put("Famille", "chenopodiaceae");
		data.put("Genre", "spinacia");
		data.put("Espece", "spinacia oleracea");
		data.put("Plante", "épinard");
		insertedPlants.put("épinard", insertPlant(data));

		data.put("Ordre", "lamiales");
		data.put("Famille", "boraginaceae");
		data.put("Genre", "borago");
		data.put("Espece", "borago officinalis");
		data.put("Plante", "bourrache");
		insertedPlants.put("bourrache", insertPlant(data));

		data.put("Ordre", "asterales");
		data.put("Famille", "asteraceae");
		data.put("Genre", "calendula");
		data.put("Espece", "calendula officinalis");
		data.put("Plante", "souci officinal");
		insertedPlants.put("souci officinal", insertPlant(data));

		data.put("Ordre", "violales");
		data.put("Famille", "cucurbitaceae");
		data.put("Genre", "cucumis");
		data.put("Espece", "cucumis melo");
		data.put("Plante", "melon");
		insertedPlants.put("melon", insertPlant(data));

		data.put("Ordre", "asterales");
		data.put("Famille", "asteraceae");
		data.put("Genre", "tanacetum");
		data.put("Espece", "tanacetum vulgare");
		data.put("Plante", "tanaisie");
		insertedPlants.put("tanaisie", insertPlant(data));

		data.put("Ordre", "rosales");
		data.put("Famille", "rosaceae");
		data.put("Genre", "prunus");
		data.put("Espece", "prunus persica");
		data.put("Plante", "pêcher");
		insertedPlants.put("pêcher", insertPlant(data));

		data.put("Ordre", "rosales");
		data.put("Famille", "rosaceae");
		data.put("Genre", "pyrus");
		data.put("Espece", "pyrus communis");
		data.put("Plante", "poirier");
		insertedPlants.put("poirier", insertPlant(data));

		data.put("Ordre", "rosales");
		data.put("Famille", "rosaceae");
		data.put("Genre", "malus");
		data.put("Espece", "malus domestica");
		data.put("Plante", "pommier");
		insertedPlants.put("pommier", insertPlant(data));

		data.put("Ordre", "fagales");
		data.put("Famille", "betulaceae");
		data.put("Genre", "betula");
		data.put("Espece", "betula pendula");
		data.put("Plante", "bouleau");
		insertedPlants.put("bouleau", insertPlant(data));

		data.put("Ordre", "geraniales");
		data.put("Famille", "geraniaceae");
		data.put("Genre", "geranium");
		data.put("Espece", "geranium pratense");
		data.put("Plante", "géranium des prés");
		insertedPlants.put("géranium des prés", insertPlant(data));

		data.put("Ordre", "lamiales");
		data.put("Famille", "lamiaceae");
		data.put("Genre", "hyssopus");
		data.put("Espece", "hyssopus officinalis");
		data.put("Plante", "hysope");
		insertedPlants.put("hysope", insertPlant(data));

		data.put("Ordre", "apiales");
		data.put("Famille", "apiaceae");
		data.put("Genre", "apium");
		data.put("Espece", "apium graveolens");
		data.put("Plante", "céleri");
		insertedPlants.put("céleri", insertPlant(data));

		data.put("Ordre", "lamiales");
		data.put("Famille", "lamiaceae");
		data.put("Genre", "nepeta");
		data.put("Espece", "nepeta cataria");
		data.put("Plante", "cataire");
		insertedPlants.put("cataire", insertPlant(data));

		data.put("Ordre", "solanales");
		data.put("Famille", "solanaceae");
		data.put("Genre", "capsicum");
		data.put("Espece", "capsicum annuum");
		data.put("Plante", "poivron");
		insertedPlants.put("poivron", insertPlant(data));
		data.put("Plante", "piment");
		insertedPlants.put("piment", insertPlant(data));

		data.put("Ordre", "asterales");
		data.put("Famille", "asteraceae");
		data.put("Genre", "cynara");
		data.put("Espece", "cynara cardunculus");
		data.put("Plante", "artichaut");
		insertedPlants.put("artichaut", insertPlant(data));

		data.put("Ordre", "asterales");
		data.put("Famille", "asteraceae");
		data.put("Genre", "scorzonera");
		data.put("Espece", "scorzonera humilis");
		data.put("Plante", "scorsonère des prés");
		insertedPlants.put("scorsonère des prés", insertPlant(data));

		data.put("Ordre", "fabales");
		data.put("Famille", "fabaceae");
		data.put("Genre", "lens");
		data.put("Espece", "lens culinaris");
		data.put("Plante", "lentille");
		insertedPlants.put("lentille", insertPlant(data));

		data.put("Ordre", "apiales");
		data.put("Famille", "apiaceae");
		data.put("Genre", "carum");
		data.put("Espece", "carum carvi");
		data.put("Plante", "carvi");
		insertedPlants.put("carvi", insertPlant(data));

		data.put("Ordre", "apiales");
		data.put("Famille", "apiaceae");
		data.put("Genre", "pimpinella");
		data.put("Espece", "pimpinella anisum");
		data.put("Plante", "anis vert");
		insertedPlants.put("anis vert", insertPlant(data));

		data.put("Ordre", "apiales");
		data.put("Famille", "apiaceae");
		data.put("Genre", "foeniculum");
		data.put("Espece", "foeniculum vulgare");
		data.put("Plante", "fenouil");
		insertedPlants.put("fenouil", insertPlant(data));

		data.put("Ordre", "asterales");
		data.put("Famille", "asteraceae");
		data.put("Genre", "artemisia");
		data.put("Espece", "artemisia absinthium");
		data.put("Plante", "absinthe");
		insertedPlants.put("absinthe", insertPlant(data));

		data.put("Ordre", "sapindales");
		data.put("Famille", "rutaceae");
		data.put("Genre", "ruta");
		data.put("Espece", "ruta graveolens");
		data.put("Plante", "rue officinale");
		insertedPlants.put("rue officinale", insertPlant(data));

		data.put("Ordre", "juglandales");
		data.put("Famille", "juglandaceae");
		data.put("Genre", "juglans");
		data.put("Espece", "juglans regia");
		data.put("Plante", "noyer");
		insertedPlants.put("noyer", insertPlant(data));

		data.put("Ordre", "capparales");
		data.put("Famille", "brassicaceae");
		data.put("Genre", "lepidium");
		data.put("Espece", "lepidium sativum");
		data.put("Plante", "cresson");
		insertedPlants.put("cresson", insertPlant(data));

		data.put("Ordre", "apiales");
		data.put("Famille", "apiaceae");
		data.put("Genre", "anthriscus");
		data.put("Espece", "anthriscus cerefolium");
		data.put("Plante", "cerfeuil");
		insertedPlants.put("cerfeuil", insertPlant(data));

		data.put("Ordre", "caryophyllales");
		data.put("Famille", "chenopodiaceae");
		data.put("Genre", "atriplex");
		data.put("Espece", "atriplex hortensis");
		data.put("Plante", "arroche des jardins");
		insertedPlants.put("arroche des jardins", insertPlant(data));

		data.put("Ordre", "capparales");
		data.put("Famille", "brassicaceae");
		data.put("Genre", "armoracia");
		data.put("Espece", "armoracia rusticana");
		data.put("Plante", "raifort");
		insertedPlants.put("raifort", insertPlant(data));

		log.info("Insert plant interaction!!!");
		log.info("Plant quantity: " + insertedPlants.size());

		interactionPourCarotte(insertedPlants);

		interactionPourChouCommun(insertedPlants);

		interactionPourHaricot(insertedPlants);

		interactionPourConcombre(insertedPlants);

		interactionPourPommeDeTerre(insertedPlants);

		interactionPourTomate(insertedPlants);

		interactionPourAsperge(insertedPlants);

		interactionPourBetterave(insertedPlants);

		// Pour framboisier
		interactionPourFramboisier(insertedPlants);

		interactionPourAubergine(insertedPlants);

		interactionPourCourge(insertedPlants);

		interactionPourPotiron(insertedPlants);

		interactionPourFraisier(insertedPlants);

		interactionPourLaitue(insertedPlants);

		interactionPourMais(insertedPlants);

		interactionPourMelon(insertedPlants);

		interactionPourOignon(insertedPlants);

		interactionPourPoireau(insertedPlants);

		interactionPourPecher(insertedPlants);

		interactionPourPoirier(insertedPlants);

		interactionPourPommier(insertedPlants);

		interactionPourPois(insertedPlants);

		interactionPourRadis(insertedPlants);

		interactionPourRosier(insertedPlants);

		interactionPourPiment(insertedPlants);

		interactionPourPoivron(insertedPlants);

		interactionPourArtichaut(insertedPlants);

		interactionPourBette(insertedPlants);

		interactionPourCeleri(insertedPlants);

		interactionContreChouFleur(insertedPlants);

		interactionContreAil(insertedPlants);

		interactionContreCarotte(insertedPlants);

		interactionContreAbsinthe(insertedPlants);

		interactionContreArtichaut(insertedPlants);

		interactionContreBasilic(insertedPlants);

		interactionContreBetterave(insertedPlants);

		interactionContreRadis(insertedPlants);

		interactionContreChou(insertedPlants);

		interactionContreConcombre(insertedPlants);

		interactionContreCourge(insertedPlants);

		interactionContreEchalotte(insertedPlants);

		interactionContreEpinard(insertedPlants);

		interactionContreFenouil(insertedPlants);

		interactionContreHaricot(insertedPlants);

		interactionContreLaitue(insertedPlants);

		interactionContreMelon(insertedPlants);

		interactionContreOignon(insertedPlants);

		interactionContrePoireau(insertedPlants);

		interactionContrePois(insertedPlants);

		interactionContrePommeDeTerre(insertedPlants);

		interactionContreTomate(insertedPlants);

		interactionContreSauge(insertedPlants);

		interactionContrePersil(insertedPlants);

		interactionContreFraisier(insertedPlants);

		interactionContreAsperge(insertedPlants);

		interactionContreAubergine(insertedPlants);

	}

	private void interactionContreAubergine(Map<String, Plante> insertedPlants) {
		log.info("interactionContreAubergine"); // Contre aubergine
		Allelopathie interactionContreAubergine1 = new Allelopathie(-5, "", null, insertedPlants.get("haricot"),
				insertedPlants.get("aubergine"));
		allelopathieRepository.save(interactionContreAubergine1);
	}

	private void interactionContreAsperge(Map<String, Plante> insertedPlants) {
		log.info("interactionContreAsperge"); // Contre asperge
		Allelopathie interactionContreAsperge1 = new Allelopathie(-5, "", null, insertedPlants.get("tomate"),
				insertedPlants.get("asperge"));
		allelopathieRepository.save(interactionContreAsperge1);
		Allelopathie interactionContreAsperge2 = new Allelopathie(-5, "", null, insertedPlants.get("haricot"),
				insertedPlants.get("asperge"));
		allelopathieRepository.save(interactionContreAsperge2);
		Allelopathie interactionContreAsperge3 = new Allelopathie(-5, "", null, insertedPlants.get("persil"),
				insertedPlants.get("asperge"));
		allelopathieRepository.save(interactionContreAsperge3);
	}

	private void interactionContreFraisier(Map<String, Plante> insertedPlants) {
		log.info("interactionContreFraisier"); // Contre fraisier
		Allelopathie interactionContreFraisier1 = new Allelopathie(-5, "", null, insertedPlants.get("chou commun"),
				insertedPlants.get("fraisier des bois"));
		allelopathieRepository.save(interactionContreFraisier1);
	}

	private void interactionContrePersil(Map<String, Plante> insertedPlants) {
		log.info("interactionContrePersil"); // Contre persil
		Allelopathie interactionContrePersil1 = new Allelopathie(-5, "", null, insertedPlants.get("laitue"),
				insertedPlants.get("persil"));
		allelopathieRepository.save(interactionContrePersil1);
	}

	private void interactionContreSauge(Map<String, Plante> insertedPlants) {
		log.info("interactionContreSauge"); // Contre sauge
		Allelopathie interactionContreSauge1 = new Allelopathie(-5, "", null, insertedPlants.get("oignon"),
				insertedPlants.get("sauge"));
		allelopathieRepository.save(interactionContreSauge1);
	}

	private void interactionContreTomate(Map<String, Plante> insertedPlants) {
		log.info("interactionContreTomate"); // Contre tomate
		Allelopathie interactionContreTomate1 = new Allelopathie(-5, "", null, insertedPlants.get("haricot"),
				insertedPlants.get("tomate"));
		allelopathieRepository.save(interactionContreTomate1);
		Allelopathie interactionContreTomate2 = new Allelopathie(-5, "", null, insertedPlants.get("concombre"),
				insertedPlants.get("tomate"));
		allelopathieRepository.save(interactionContreTomate2);
		Allelopathie interactionContreTomate3 = new Allelopathie(-5, "", null, insertedPlants.get("chou-rave"),
				insertedPlants.get("tomate"));
		allelopathieRepository.save(interactionContreTomate3);
		Allelopathie interactionContreTomate4 = new Allelopathie(-5, "", null, insertedPlants.get("chou commun"),
				insertedPlants.get("tomate"));
		allelopathieRepository.save(interactionContreTomate4);
		Allelopathie interactionContreTomate5 = new Allelopathie(-5, "", null, insertedPlants.get("pomme de terre"),
				insertedPlants.get("tomate"));
		allelopathieRepository.save(interactionContreTomate5);
		Allelopathie interactionContreTomate6 = new Allelopathie(-5, "", null, insertedPlants.get("betterave"),
				insertedPlants.get("tomate"));
		allelopathieRepository.save(interactionContreTomate6);
		Allelopathie interactionContreTomate7 = new Allelopathie(-5, "", null, insertedPlants.get("pois"),
				insertedPlants.get("tomate"));
		allelopathieRepository.save(interactionContreTomate7);
	}

	private void interactionContrePommeDeTerre(Map<String, Plante> insertedPlants) {
		log.info("interactionContrePommeDeTerre"); // Contre pommeDeTerre
		Allelopathie interactionContrePommeDeTerre1 = new Allelopathie(-5, "", null, insertedPlants.get("tomate"),
				insertedPlants.get("pomme de terre"));
		allelopathieRepository.save(interactionContrePommeDeTerre1);
		Allelopathie interactionContrePommeDeTerre2 = new Allelopathie(-5, "", null, insertedPlants.get("courge"),
				insertedPlants.get("pomme de terre"));
		allelopathieRepository.save(interactionContrePommeDeTerre2);
		Allelopathie interactionContrePommeDeTerre3 = new Allelopathie(-5, "", null, insertedPlants.get("carotte"),
				insertedPlants.get("pomme de terre"));
		allelopathieRepository.save(interactionContrePommeDeTerre3);
		Allelopathie interactionContrePommeDeTerre4 = new Allelopathie(-5, "", null, insertedPlants.get("oignon"),
				insertedPlants.get("pomme de terre"));
		allelopathieRepository.save(interactionContrePommeDeTerre4);
		Allelopathie interactionContrePommeDeTerre5 = new Allelopathie(-5, "", null, insertedPlants.get("framboisier"),
				insertedPlants.get("pomme de terre"));
		allelopathieRepository.save(interactionContrePommeDeTerre5);
		Allelopathie interactionContrePommeDeTerre6 = new Allelopathie(-5, "", null,
				insertedPlants.get("arroche des jardins"), insertedPlants.get("pomme de terre"));
		allelopathieRepository.save(interactionContrePommeDeTerre6);
		Allelopathie interactionContrePommeDeTerre7 = new Allelopathie(-5, "", null, insertedPlants.get("tournesol"),
				insertedPlants.get("pomme de terre"));
		allelopathieRepository.save(interactionContrePommeDeTerre7);
		Allelopathie interactionContrePommeDeTerre8 = new Allelopathie(-5, "", null, insertedPlants.get("melon"),
				insertedPlants.get("pomme de terre"));
		allelopathieRepository.save(interactionContrePommeDeTerre8);
	}

	private void interactionContrePois(Map<String, Plante> insertedPlants) {
		log.info("interactionContrePois"); // Contre pois
		Allelopathie interactionContrePois1 = new Allelopathie(-5, "", null, insertedPlants.get("ail"),
				insertedPlants.get("pois"));
		allelopathieRepository.save(interactionContrePois1);
		Allelopathie interactionContrePois2 = new Allelopathie(-5, "", null, insertedPlants.get("échalotte"),
				insertedPlants.get("pois"));
		allelopathieRepository.save(interactionContrePois2);
		Allelopathie interactionContrePois3 = new Allelopathie(-5, "", null, insertedPlants.get("oignon"),
				insertedPlants.get("pois"));
		allelopathieRepository.save(interactionContrePois3);
		Allelopathie interactionContrePois4 = new Allelopathie(-5, "", null, insertedPlants.get("poireau"),
				insertedPlants.get("pois"));
		allelopathieRepository.save(interactionContrePois4);
		Allelopathie interactionContrePois5 = new Allelopathie(-5, "", null, insertedPlants.get("tomate"),
				insertedPlants.get("pois"));
		allelopathieRepository.save(interactionContrePois5);
	}

	private void interactionContrePoireau(Map<String, Plante> insertedPlants) {
		log.info("interactionContrePoireau"); // Contre poireau
		Allelopathie interactionContrePoireau1 = new Allelopathie(-5, "", null, insertedPlants.get("brocoli"),
				insertedPlants.get("poireau"));
		allelopathieRepository.save(interactionContrePoireau1);
		Allelopathie interactionContrePoireau2 = new Allelopathie(-5, "", null, insertedPlants.get("haricot"),
				insertedPlants.get("poireau"));
		allelopathieRepository.save(interactionContrePoireau2);
		Allelopathie interactionContrePoireau3 = new Allelopathie(-5, "", null, insertedPlants.get("fève"),
				insertedPlants.get("poireau"));
		allelopathieRepository.save(interactionContrePoireau3);
	}

	private void interactionContreOignon(Map<String, Plante> insertedPlants) {
		log.info("interactionContreOignon"); // Contre oignon
		Allelopathie interactionContreOignon1 = new Allelopathie(-5, "", null, insertedPlants.get("pois"),
				insertedPlants.get("oignon"));
		allelopathieRepository.save(interactionContreOignon1);
		Allelopathie interactionContreOignon2 = new Allelopathie(-5, "", null, insertedPlants.get("haricot"),
				insertedPlants.get("oignon"));
		allelopathieRepository.save(interactionContreOignon2);
		Allelopathie interactionContreOignon3 = new Allelopathie(-5, "", null, insertedPlants.get("fève"),
				insertedPlants.get("oignon"));
		allelopathieRepository.save(interactionContreOignon3);
		Allelopathie interactionContreOignon4 = new Allelopathie(-5, "", null, insertedPlants.get("lentille"),
				insertedPlants.get("oignon"));
		allelopathieRepository.save(interactionContreOignon4);
	}

	private void interactionContreMelon(Map<String, Plante> insertedPlants) {
		log.info("interactionContreMelon"); // Contre melon
		Allelopathie interactionContreMelon1 = new Allelopathie(-5, "", null, insertedPlants.get("concombre"),
				insertedPlants.get("melon"));
		allelopathieRepository.save(interactionContreMelon1);
		Allelopathie interactionContreMelon2 = new Allelopathie(-5, "", null, insertedPlants.get("courge"),
				insertedPlants.get("melon"));
		allelopathieRepository.save(interactionContreMelon2);
	}

	private void interactionContreLaitue(Map<String, Plante> insertedPlants) {
		log.info("interactionContreLaitue"); // Contre laitue
		Allelopathie interactionContreLaitue1 = new Allelopathie(-5, "", null, insertedPlants.get("tournesol"),
				insertedPlants.get("laitue"));
		allelopathieRepository.save(interactionContreLaitue1);
		Allelopathie interactionContreLaitue2 = new Allelopathie(-5, "", null, insertedPlants.get("persil"),
				insertedPlants.get("laitue"));
		allelopathieRepository.save(interactionContreLaitue2);
	}

	private void interactionContreHaricot(Map<String, Plante> insertedPlants) {
		log.info("interactionContreHaricot"); // Contre haricot
		Allelopathie interactionContreHaricot1 = new Allelopathie(-5, "", null, insertedPlants.get("oignon"),
				insertedPlants.get("haricot"));
		allelopathieRepository.save(interactionContreHaricot1);
		Allelopathie interactionContreHaricot2 = new Allelopathie(-5, "", null, insertedPlants.get("ail"),
				insertedPlants.get("haricot"));
		allelopathieRepository.save(interactionContreHaricot2);
		Allelopathie interactionContreHaricot3 = new Allelopathie(-5, "", null, insertedPlants.get("échalotte"),
				insertedPlants.get("haricot"));
		allelopathieRepository.save(interactionContreHaricot3);
		Allelopathie interactionContreHaricot4 = new Allelopathie(-5, "", null, insertedPlants.get("tomate"),
				insertedPlants.get("haricot"));
		allelopathieRepository.save(interactionContreHaricot4);
		Allelopathie interactionContreHaricot5 = new Allelopathie(-5, "", null, insertedPlants.get("fenouil"),
				insertedPlants.get("haricot"));
		allelopathieRepository.save(interactionContreHaricot5);
		Allelopathie interactionContreHaricot6 = new Allelopathie(-5, "", null, insertedPlants.get("pois"),
				insertedPlants.get("haricot"));
		allelopathieRepository.save(interactionContreHaricot6);
	}

	private void interactionContreFenouil(Map<String, Plante> insertedPlants) {
		log.info("interactionContreFenouil"); // Contre fenouil
		Allelopathie interactionContreFenouil1 = new Allelopathie(-5, "", null, insertedPlants.get("tomate"),
				insertedPlants.get("fenouil"));
		allelopathieRepository.save(interactionContreFenouil1);
		Allelopathie interactionContreFenouil2 = new Allelopathie(-5, "", null, insertedPlants.get("chou-rave"),
				insertedPlants.get("fenouil"));
		allelopathieRepository.save(interactionContreFenouil2);

		Allelopathie interactionContreFenouil4 = new Allelopathie(-5, "", null, insertedPlants.get("carvi"),
				insertedPlants.get("fenouil"));
		allelopathieRepository.save(interactionContreFenouil4);
		Allelopathie interactionContreFenouil5 = new Allelopathie(-5, "", null, insertedPlants.get("haricot"),
				insertedPlants.get("fenouil"));
		allelopathieRepository.save(interactionContreFenouil5);
		Allelopathie interactionContreFenouil6 = new Allelopathie(-5, "", null, insertedPlants.get("pois"),
				insertedPlants.get("fenouil"));
		allelopathieRepository.save(interactionContreFenouil6);
		Allelopathie interactionContreFenouil7 = new Allelopathie(-5, "", null, insertedPlants.get("échalotte"),
				insertedPlants.get("fenouil"));
		allelopathieRepository.save(interactionContreFenouil7);
		Allelopathie interactionContreFenouil8 = new Allelopathie(-5,
				"L'absinthe semble gêner la formation des graines", null, insertedPlants.get("absinthe"),
				insertedPlants.get("fenouil"));
		allelopathieRepository.save(interactionContreFenouil8);
		Allelopathie interactionContreFenouil9 = new Allelopathie(-5,
				"La coriandre semble gêner la formation des graines", null, insertedPlants.get("coriandre"),
				insertedPlants.get("fenouil"));
		allelopathieRepository.save(interactionContreFenouil9);
	}

	private void interactionContreEpinard(Map<String, Plante> insertedPlants) {
		log.info("interactionContreEpinard"); // Contre epinard
		Allelopathie interactionContreEpinard1 = new Allelopathie(-5, "", null, insertedPlants.get("pomme de terre"),
				insertedPlants.get("épinard"));
		allelopathieRepository.save(interactionContreEpinard1);
		Allelopathie interactionContreEpinard2 = new Allelopathie(-5, "", null, insertedPlants.get("betterave"),
				insertedPlants.get("épinard"));
		allelopathieRepository.save(interactionContreEpinard2);
	}

	private void interactionContreEchalotte(Map<String, Plante> insertedPlants) {
		log.info("interactionContreEchalotte"); // Contre echalotte
		Allelopathie interactionContreEchalotte1 = new Allelopathie(-5, "", null, insertedPlants.get("pois"),
				insertedPlants.get("échalotte"));
		allelopathieRepository.save(interactionContreEchalotte1);
		Allelopathie interactionContreEchalotte2 = new Allelopathie(-5, "", null, insertedPlants.get("haricot"),
				insertedPlants.get("échalotte"));
		allelopathieRepository.save(interactionContreEchalotte2);
		Allelopathie interactionContreEchalotte3 = new Allelopathie(-5, "", null, insertedPlants.get("fève"),
				insertedPlants.get("échalotte"));
		allelopathieRepository.save(interactionContreEchalotte3);
		Allelopathie interactionContreEchalotte4 = new Allelopathie(-5, "", null, insertedPlants.get("lentille"),
				insertedPlants.get("échalotte"));
		allelopathieRepository.save(interactionContreEchalotte4);
	}

	private void interactionContreCourge(Map<String, Plante> insertedPlants) {
		log.info("interactionContreCourge"); // Contre courge
		Allelopathie interactionContreCourge1 = new Allelopathie(-5, "", null, insertedPlants.get("pomme de terre"),
				insertedPlants.get("courge"));
		allelopathieRepository.save(interactionContreCourge1);
	}

	private void interactionContreConcombre(Map<String, Plante> insertedPlants) {
		log.info("interactionContreConcombre"); // Contre concombre
		Allelopathie interactionContreConcombre1 = new Allelopathie(-5, "", null, insertedPlants.get("pomme de terre"),
				insertedPlants.get("concombre"));
		allelopathieRepository.save(interactionContreConcombre1);
		Allelopathie interactionContreConcombre2 = new Allelopathie(-5, "", null, insertedPlants.get("tomate"),
				insertedPlants.get("concombre"));
		allelopathieRepository.save(interactionContreConcombre2);
		Allelopathie interactionContreConcombre3 = new Allelopathie(-5, "", null, insertedPlants.get("courgette"),
				insertedPlants.get("concombre"));
		allelopathieRepository.save(interactionContreConcombre3);
	}

	private void interactionContreChou(Map<String, Plante> insertedPlants) {
		log.info("interactionContreChou"); // Contre chou
		Allelopathie interactionContreChou1 = new Allelopathie(-5, "", null, insertedPlants.get("fraisier des bois"),
				insertedPlants.get("chou commun"));
		allelopathieRepository.save(interactionContreChou1);
		Allelopathie interactionContreChou2 = new Allelopathie(-5, "", null, insertedPlants.get("oignon"),
				insertedPlants.get("chou commun"));
		allelopathieRepository.save(interactionContreChou2);
		Allelopathie interactionContreChou3 = new Allelopathie(-5, "", null, insertedPlants.get("tomate"),
				insertedPlants.get("chou commun"));
		allelopathieRepository.save(interactionContreChou3);
	}

	private void interactionContreRadis(Map<String, Plante> insertedPlants) {
		log.info("interactionContreRadis"); // Contre radis
		Allelopathie interactionContreRadis1 = new Allelopathie(-5, "", null, insertedPlants.get("cerfeuil"),
				insertedPlants.get("radis"));
		allelopathieRepository.save(interactionContreRadis1);
	}

	private void interactionContreBetterave(Map<String, Plante> insertedPlants) {
		log.info("interactionContreBetterave"); // Contre betterave
		Allelopathie interactionContreBetterave1 = new Allelopathie(-5, "", null, insertedPlants.get("haricot"),
				insertedPlants.get("betterave"));
		allelopathieRepository.save(interactionContreBetterave1);
		Allelopathie interactionContreBetterave2 = new Allelopathie(-5, "", null, insertedPlants.get("épinard"),
				insertedPlants.get("betterave"));
		allelopathieRepository.save(interactionContreBetterave2);
	}

	private void interactionContreBasilic(Map<String, Plante> insertedPlants) {
		log.info("interactionContreBasilic"); // Contre basilic
		Allelopathie interactionContreBasilic1 = new Allelopathie(-5, "", null, insertedPlants.get("rue officinale"),
				insertedPlants.get("basilic"));
		allelopathieRepository.save(interactionContreBasilic1);
	}

	private void interactionContreArtichaut(Map<String, Plante> insertedPlants) {
		log.info("interactionContreArtichaut"); // Contre artichaut
		Allelopathie interactionContreArtichaut1 = new Allelopathie(-5, "", null, insertedPlants.get("fève"),
				insertedPlants.get("artichaut"));
		allelopathieRepository.save(interactionContreArtichaut1);
	}

	private void interactionContreAbsinthe(Map<String, Plante> insertedPlants) {
		log.info("interactionContreAbsinthe"); // Contre absinthe
		Allelopathie interactionContreAbsinthe1 = new Allelopathie(-5, "", null, insertedPlants.get("carvi"),
				insertedPlants.get("absinthe"));
		allelopathieRepository.save(interactionContreAbsinthe1);
		Allelopathie interactionContreAbsinthe2 = new Allelopathie(-5, "", null, insertedPlants.get("sauge"),
				insertedPlants.get("absinthe"));
		allelopathieRepository.save(interactionContreAbsinthe2);
		Allelopathie interactionContreAbsinthe3 = new Allelopathie(-5, "", null, insertedPlants.get("anis vert"),
				insertedPlants.get("absinthe"));
		allelopathieRepository.save(interactionContreAbsinthe3);
		Allelopathie interactionContreAbsinthe4 = new Allelopathie(-5, "", null, insertedPlants.get("fenouil"),
				insertedPlants.get("absinthe"));
		allelopathieRepository.save(interactionContreAbsinthe4);
	}

	private void interactionContreCarotte(Map<String, Plante> insertedPlants) {
		log.info("interactionContreCarotte"); // Contre carotte
		Allelopathie interactionContreCarotte1 = new Allelopathie(-5,
				"En compagnie d'aneth la carotte est sensible aux maladies et aux ravageurs et au mieux végètera. "
						+ "Elle ne poussent pas là où auparavant il y en avait déjà",
				null, insertedPlants.get("aneth"), insertedPlants.get("carotte"));
		allelopathieRepository.save(interactionContreCarotte1);
	}

	private void interactionContreAil(Map<String, Plante> insertedPlants) {
		log.info("interactionContreAil"); // Contre insertedPlants.get("ail")
		Allelopathie interactionContreAil1 = new Allelopathie(-5, "", null, insertedPlants.get("fève"),
				insertedPlants.get("ail"));
		allelopathieRepository.save(interactionContreAil1);
		Allelopathie interactionContreAil2 = new Allelopathie(-5, "", null, insertedPlants.get("lentille"),
				insertedPlants.get("ail"));
		allelopathieRepository.save(interactionContreAil2);
		Allelopathie interactionContreAil3 = new Allelopathie(-5, "", null, insertedPlants.get("haricot"),
				insertedPlants.get("ail"));
		allelopathieRepository.save(interactionContreAil3);
		Allelopathie interactionContreAil4 = new Allelopathie(-5, "", null, insertedPlants.get("pois"),
				insertedPlants.get("ail"));
		allelopathieRepository.save(interactionContreAil4);
	}

	private void interactionContreChouFleur(Map<String, Plante> insertedPlants) {
		log.info("interactionContreChouFleur"); // Contre chouFleur
		Allelopathie interactionContreChouFleur1 = new Allelopathie(-5,
				"Le chou fleur utilise mieux les nutriments présents dans le sols lorsqu'il accompagné du céleri, de même pour ce dernier.",
				null, insertedPlants.get("céleri"), insertedPlants.get("chou fleur"));
		allelopathieRepository.save(interactionContreChouFleur1);
	}

	private void interactionPourCeleri(Map<String, Plante> insertedPlants) {
		log.info("interactionPourCeleri"); // Pour celeri
		Allelopathie interactionPourCeleri1 = new Allelopathie(5, "", null, insertedPlants.get("poireau"),
				insertedPlants.get("céleri"));
		allelopathieRepository.save(interactionPourCeleri1);
		Allelopathie interactionPourCeleri2 = new Allelopathie(5, "", null, insertedPlants.get("tomate"),
				insertedPlants.get("céleri"));
		allelopathieRepository.save(interactionPourCeleri2);
		Allelopathie interactionPourCeleri3 = new Allelopathie(5,
				"Le céleri cultivé seul n'utilise qu'une partie des substances nutritives trouvées dans le sol. Quand on le plante avec du chou-fleur, "
						+ "il les utilise mieux, de même que ce dernier. La récolte est alors meilleure pour les deux plantes.\r\n",
				null, insertedPlants.get("chou fleur"), insertedPlants.get("céleri"));
		allelopathieRepository.save(interactionPourCeleri3);
		Allelopathie interactionPourCeleri4 = new Allelopathie(5, "", null, insertedPlants.get("épinard"),
				insertedPlants.get("céleri"));
		allelopathieRepository.save(interactionPourCeleri4);
		Allelopathie interactionPourCeleri5 = new Allelopathie(5, "", null, insertedPlants.get("concombre"),
				insertedPlants.get("céleri"));
		allelopathieRepository.save(interactionPourCeleri5);
		Allelopathie interactionPourCeleri6 = new Allelopathie(5, "", null, insertedPlants.get("haricot"),
				insertedPlants.get("céleri"));
		allelopathieRepository.save(interactionPourCeleri6);
	}

	private void interactionPourBette(Map<String, Plante> insertedPlants) {
		log.info("interactionPourBette"); // Pour bette
		Allelopathie interactionPourBette1 = new Allelopathie(5, "", null, insertedPlants.get("radis"),
				insertedPlants.get("bette"));
		allelopathieRepository.save(interactionPourBette1);
		Allelopathie interactionPourBette2 = new Allelopathie(5, "", null, insertedPlants.get("carotte"),
				insertedPlants.get("bette"));
		allelopathieRepository.save(interactionPourBette2);
		Allelopathie interactionPourBette3 = new Allelopathie(5, "", null, insertedPlants.get("haricot"),
				insertedPlants.get("bette"));
		allelopathieRepository.save(interactionPourBette3);
		Allelopathie interactionPourBette4 = new Allelopathie(5, "", null, insertedPlants.get("raifort"),
				insertedPlants.get("bette"));
		allelopathieRepository.save(interactionPourBette4);
	}

	private void interactionPourArtichaut(Map<String, Plante> insertedPlants) {
		log.info("interactionPourArtichaut"); // Pour artichaut
		Allelopathie interactionPourArtichaut1 = new Allelopathie(5, "", null, insertedPlants.get("fève"),
				insertedPlants.get("artichaut"));
		allelopathieRepository.save(interactionPourArtichaut1);
	}

	private void interactionPourPoivron(Map<String, Plante> insertedPlants) {
		log.info("interactionPourPoivron"); // Pour poivron
		Allelopathie interactionPourPoivron1 = new Allelopathie(5,
				"Le basilic s'associe parfaitement avec le genre capsicum", null, insertedPlants.get("basilic"),
				insertedPlants.get("poivron"));
		allelopathieRepository.save(interactionPourPoivron1);
	}

	private void interactionPourPiment(Map<String, Plante> insertedPlants) {
		log.info("interactionPourPiment"); // Pour piment
		Allelopathie interactionPourPiment1 = new Allelopathie(5,
				"Le basilic s'associe parfaitement avec le genre capsicum", null, insertedPlants.get("basilic"),
				insertedPlants.get("piment"));
		allelopathieRepository.save(interactionPourPiment1);
	}

	private void interactionPourRosier(Map<String, Plante> insertedPlants) {
		log.info("interactionPourRosier"); // Pour rosiers
		Allelopathie interactionPourRosier1 = new Allelopathie(5, "Les géraniums protègent les rosiers", null,
				insertedPlants.get("géranium des prés"), insertedPlants.get("rosier de france"));
		allelopathieRepository.save(interactionPourRosier1);
		Allelopathie interactionPourRosier2 = new Allelopathie(5,
				"L'ail planté au pied des rosiersles rends plus beaux et résistants", null, insertedPlants.get("ail"),
				insertedPlants.get("rosier de france"));
		allelopathieRepository.save(interactionPourRosier2);
		Allelopathie interactionPourRosier3 = new Allelopathie(5,
				"Les pucerons noirs des rosiers sont repoussés par la menthe verte ou poivrée", null,
				insertedPlants.get("menthe poivrée"), insertedPlants.get("rosier de france"));
		allelopathieRepository.save(interactionPourRosier3);
	}

	private void interactionPourRadis(Map<String, Plante> insertedPlants) {
		log.info("interactionPourRadis"); // Pour radis
		Allelopathie interactionPourRadis1 = new Allelopathie(5, "", null, insertedPlants.get("épinard"),
				insertedPlants.get("radis"));
		allelopathieRepository.save(interactionPourRadis1);
		Allelopathie interactionPourRadis2 = new Allelopathie(5, "", null, insertedPlants.get("menthe poivrée"),
				insertedPlants.get("radis"));
		allelopathieRepository.save(interactionPourRadis2);
		Allelopathie interactionPourRadis3 = new Allelopathie(5, "", null, insertedPlants.get("concombre"),
				insertedPlants.get("radis"));
		allelopathieRepository.save(interactionPourRadis3);
		Allelopathie interactionPourRadis4 = new Allelopathie(5, "", null, insertedPlants.get("grande capucine"),
				insertedPlants.get("radis"));
		allelopathieRepository.save(interactionPourRadis4);
		Allelopathie interactionPourRadis5 = new Allelopathie(5, "", null, insertedPlants.get("carotte"),
				insertedPlants.get("radis"));
		allelopathieRepository.save(interactionPourRadis5);
		Allelopathie interactionPourRadis6 = new Allelopathie(5, "", null, insertedPlants.get("laitue"),
				insertedPlants.get("radis"));
		allelopathieRepository.save(interactionPourRadis6);
		Allelopathie interactionPourRadis7 = new Allelopathie(5, "", null, insertedPlants.get("pois"),
				insertedPlants.get("radis"));
		allelopathieRepository.save(interactionPourRadis7);
	}

	private void interactionPourPois(Map<String, Plante> insertedPlants) {
		log.info("interactionPourPois"); // Pour pois
		Allelopathie interactionPourPois1 = new Allelopathie(5, "", null, insertedPlants.get("radis"),
				insertedPlants.get("pois"));
		allelopathieRepository.save(interactionPourPois1);
		Allelopathie interactionPourPois2 = new Allelopathie(5, "", null, insertedPlants.get("carotte"),
				insertedPlants.get("pois"));
		allelopathieRepository.save(interactionPourPois2);
		Allelopathie interactionPourPois3 = new Allelopathie(5, "", null, insertedPlants.get("concombre"),
				insertedPlants.get("pois"));
		allelopathieRepository.save(interactionPourPois3);
		Allelopathie interactionPourPois4 = new Allelopathie(5, "", null, insertedPlants.get("maïs"),
				insertedPlants.get("pois"));
		allelopathieRepository.save(interactionPourPois4);
		Allelopathie interactionPourPois5 = new Allelopathie(5, "", null, insertedPlants.get("pomme de terre"),
				insertedPlants.get("pois"));
		allelopathieRepository.save(interactionPourPois5);
	}

	private void interactionPourPommier(Map<String, Plante> insertedPlants) {
		log.info("interactionPourPommier"); // Pour pommier
		Allelopathie interactionPourPommier1 = new Allelopathie(5,
				"La ciboulette se plante près des pommiers pour prévenir de la tavelure, de la gale et des chancres",
				null, insertedPlants.get("ciboulette"), insertedPlants.get("pommier"));
		allelopathieRepository.save(interactionPourPommier1);
		Allelopathie interactionPourPommier2 = new Allelopathie(5,
				"Plantée au pied des pommiers, elle prévient contre le puceron lanigère", null,
				insertedPlants.get("grande capucine"), insertedPlants.get("pommier"));
		allelopathieRepository.save(interactionPourPommier2);
	}

	private void interactionPourPoirier(Map<String, Plante> insertedPlants) {
		log.info("interactionPourPoirier"); // Pour poirier
		Allelopathie interactionPourPoirier1 = new Allelopathie(5, "", null, insertedPlants.get("sauge"),
				insertedPlants.get("poirier"));
		allelopathieRepository.save(interactionPourPoirier1);
	}

	private void interactionPourPecher(Map<String, Plante> insertedPlants) {
		log.info("interactionPourPecher"); // Pour pecher
		Allelopathie interactionPourPecher1 = new Allelopathie(5, "", null, insertedPlants.get("tanaisie"),
				insertedPlants.get("pêcher"));
		allelopathieRepository.save(interactionPourPecher1);
		Allelopathie interactionPourPecher2 = new Allelopathie(5,
				"L'ail planté aux pieds des pêchers pour protéger de la cloque", null, insertedPlants.get("ail"),
				insertedPlants.get("pêcher"));
		allelopathieRepository.save(interactionPourPecher2);
		Allelopathie interactionPourPecher3 = new Allelopathie(5,
				"L'oignon planté aux pieds des pêchers pour protéger de la cloque", null, insertedPlants.get("oignon"),
				insertedPlants.get("pêcher"));
		allelopathieRepository.save(interactionPourPecher3);
	}

	private void interactionPourPoireau(Map<String, Plante> insertedPlants) {
		log.info("interactionPourPoireau"); // Pour poireau
		Allelopathie interactionPourPoireau1 = new Allelopathie(5,
				"Les carottes contribues à la lutte contre la teigne du poireau", null, insertedPlants.get("carotte"),
				insertedPlants.get("poireau"));
		allelopathieRepository.save(interactionPourPoireau1);
	}

	private void interactionPourOignon(Map<String, Plante> insertedPlants) {
		log.info("interactionPourOignon"); // Pour oignon
		Allelopathie interactionPourOignon1 = new Allelopathie(5,
				"La mouche de l’oignon est repoussée par les carottes. Leur odeur repousse les mouches.", null,
				insertedPlants.get("carotte"), insertedPlants.get("oignon"));
		allelopathieRepository.save(interactionPourOignon1);
		Allelopathie interactionPourOignon2 = new Allelopathie(5, "", null, insertedPlants.get("fraisier des bois"),
				insertedPlants.get("oignon"));
		allelopathieRepository.save(interactionPourOignon2);
	}

	private void interactionPourMelon(Map<String, Plante> insertedPlants) {
		log.info("interactionPourMelon"); // Pour melon
		Allelopathie interactionPourMelon1 = new Allelopathie(5, "", null, insertedPlants.get("origan"),
				insertedPlants.get("melon"));
		allelopathieRepository.save(interactionPourMelon1);
	}

	private void interactionPourMais(Map<String, Plante> insertedPlants) {
		log.info("interactionPourMais"); // Pour mais
		Allelopathie interactionPourMais1 = new Allelopathie(5, "", null, insertedPlants.get("haricot"),
				insertedPlants.get("maïs"));
		allelopathieRepository.save(interactionPourMais1);
		Allelopathie interactionPourMais2 = new Allelopathie(5, "", null, insertedPlants.get("pois"),
				insertedPlants.get("maïs"));
		allelopathieRepository.save(interactionPourMais2);
		Allelopathie interactionPourMais3 = new Allelopathie(5, "", null, insertedPlants.get("concombre"),
				insertedPlants.get("maïs"));
		allelopathieRepository.save(interactionPourMais3);
		Allelopathie interactionPourMais4 = new Allelopathie(5, "", null, insertedPlants.get("courge"),
				insertedPlants.get("maïs"));
		allelopathieRepository.save(interactionPourMais4);
		Allelopathie interactionPourMais5 = new Allelopathie(5, "", null, insertedPlants.get("potiron"),
				insertedPlants.get("maïs"));
		allelopathieRepository.save(interactionPourMais5);
	}

	private void interactionPourLaitue(Map<String, Plante> insertedPlants) {
		log.info("interactionPourLaitue"); // Pour laitue
		Allelopathie interactionPourLaitue1 = new Allelopathie(5, "", null, insertedPlants.get("fraisier des bois"),
				insertedPlants.get("laitue"));
		allelopathieRepository.save(interactionPourLaitue1);
		Allelopathie interactionPourLaitue2 = new Allelopathie(5, "", null, insertedPlants.get("carotte"),
				insertedPlants.get("laitue"));
		allelopathieRepository.save(interactionPourLaitue2);
		Allelopathie interactionPourLaitue3 = new Allelopathie(5, "", null, insertedPlants.get("radis"),
				insertedPlants.get("laitue"));
		allelopathieRepository.save(interactionPourLaitue3);
	}

	private void interactionPourFraisier(Map<String, Plante> insertedPlants) {
		log.info("interactionPourFraisier"); // Pour fraisier
		Allelopathie interactionPourFraisier1 = new Allelopathie(5, "", null, insertedPlants.get("oignon"),
				insertedPlants.get("fraisier des bois"));
		allelopathieRepository.save(interactionPourFraisier1);
		Allelopathie interactionPourFraisier2 = new Allelopathie(5, "", null, insertedPlants.get("bourrache"),
				insertedPlants.get("fraisier des bois"));
		allelopathieRepository.save(interactionPourFraisier2);
		Allelopathie interactionPourFraisier3 = new Allelopathie(5, "", null, insertedPlants.get("épinard"),
				insertedPlants.get("fraisier des bois"));
		allelopathieRepository.save(interactionPourFraisier3);
		Allelopathie interactionPourFraisier4 = new Allelopathie(5, "", null, insertedPlants.get("laitue"),
				insertedPlants.get("fraisier des bois"));
		allelopathieRepository.save(interactionPourFraisier4);
	}

	private void interactionPourPotiron(Map<String, Plante> insertedPlants) {
		log.info("interactionPourPotiron"); // Pour potiron
		Allelopathie interactionPourPotiron1 = new Allelopathie(5, "", null, insertedPlants.get("grande capucine"),
				insertedPlants.get("potiron"));
		allelopathieRepository.save(interactionPourPotiron1);
		Allelopathie interactionPourPotiron2 = new Allelopathie(5, "", null, insertedPlants.get("pomme de terre"),
				insertedPlants.get("potiron"));
		allelopathieRepository.save(interactionPourPotiron2);
		Allelopathie interactionPourPotiron3 = new Allelopathie(5, "", null, insertedPlants.get("épinard"),
				insertedPlants.get("potiron"));
		allelopathieRepository.save(interactionPourPotiron3);
		Allelopathie interactionPourPotiron4 = new Allelopathie(5, "", null, insertedPlants.get("fraisier des bois"),
				insertedPlants.get("potiron"));
		allelopathieRepository.save(interactionPourPotiron4);
		Allelopathie interactionPourPotiron5 = new Allelopathie(5, "", null, insertedPlants.get("maïs"),
				insertedPlants.get("potiron"));
		allelopathieRepository.save(interactionPourPotiron5);
		Allelopathie interactionPourPotiron6 = new Allelopathie(5, "", null, insertedPlants.get("radis"),
				insertedPlants.get("potiron"));
		allelopathieRepository.save(interactionPourPotiron6);
	}

	private void interactionPourCourge(Map<String, Plante> insertedPlants) {
		log.info("interactionPourCourge"); // Pour courge
		Allelopathie interactionPourCourge1 = new Allelopathie(5, "", null, insertedPlants.get("grande capucine"),
				insertedPlants.get("courge"));
		allelopathieRepository.save(interactionPourCourge1);
		Allelopathie interactionPourCourge2 = new Allelopathie(5, "", null, insertedPlants.get("pomme de terre"),
				insertedPlants.get("courge"));
		allelopathieRepository.save(interactionPourCourge2);
		Allelopathie interactionPourCourge3 = new Allelopathie(5, "", null, insertedPlants.get("épinard"),
				insertedPlants.get("courge"));
		allelopathieRepository.save(interactionPourCourge3);
		Allelopathie interactionPourCourge4 = new Allelopathie(5, "", null, insertedPlants.get("fraisier des bois"),
				insertedPlants.get("courge"));
		allelopathieRepository.save(interactionPourCourge4);
		Allelopathie interactionPourCourge5 = new Allelopathie(5, "", null, insertedPlants.get("maïs"),
				insertedPlants.get("courge"));
		allelopathieRepository.save(interactionPourCourge5);
		Allelopathie interactionPourCourge6 = new Allelopathie(5, "", null, insertedPlants.get("radis"),
				insertedPlants.get("courge"));
		allelopathieRepository.save(interactionPourCourge6);
		Allelopathie interactionPourCourge7 = new Allelopathie(5, "", null, insertedPlants.get("bourrache"),
				insertedPlants.get("courge"));
		allelopathieRepository.save(interactionPourCourge7);
		Allelopathie interactionPourCourge8 = new Allelopathie(5,
				"La capucine éloigne les punaises des courgettes et citrouilles", null,
				insertedPlants.get("grande capucine"), insertedPlants.get("courge"));
		allelopathieRepository.save(interactionPourCourge8);
	}

	private void interactionPourAubergine(Map<String, Plante> insertedPlants) {
		log.info("interactionPourAubergine"); // Pour aubergine
		Allelopathie interactionPourAubergine1 = new Allelopathie(5, "", null, insertedPlants.get("haricot"),
				insertedPlants.get("aubergine"));
		allelopathieRepository.save(interactionPourAubergine1);
		Allelopathie interactionPourAubergine2 = new Allelopathie(5, "", null, insertedPlants.get("basilic"),
				insertedPlants.get("aubergine"));
		allelopathieRepository.save(interactionPourAubergine2);
	}

	private void interactionPourFramboisier(Map<String, Plante> insertedPlants) {
		log.info("interactionPourFramboisier");
		Allelopathie interactionPourFramboisier1 = new Allelopathie(5,
				"Le vers de framboisier est repoussé par la myosotis. Il empêche le vers de proliférer", null,
				insertedPlants.get("myosotis des champs"), insertedPlants.get("framboisier"));
		allelopathieRepository.save(interactionPourFramboisier1);
	}

	private void interactionPourBetterave(Map<String, Plante> insertedPlants) {
		log.info("interactionPourBetterave"); // Pour betterave
		Allelopathie interactionPourBetterave1 = new Allelopathie(5, "", null, insertedPlants.get("coriandre"),
				insertedPlants.get("betterave"));
		allelopathieRepository.save(interactionPourBetterave1);
		Allelopathie interactionPourBetterave2 = new Allelopathie(5, "", null, insertedPlants.get("chou commun"),
				insertedPlants.get("betterave"));
		allelopathieRepository.save(interactionPourBetterave2);
		Allelopathie interactionPourBetterave3 = new Allelopathie(5, "", null, insertedPlants.get("haricot"),
				insertedPlants.get("betterave"));
		allelopathieRepository.save(interactionPourBetterave3);
		Allelopathie interactionPourBetterave4 = new Allelopathie(5, "", null, insertedPlants.get("oignon"),
				insertedPlants.get("betterave"));
		allelopathieRepository.save(interactionPourBetterave4);
		Allelopathie interactionPourBetterave5 = new Allelopathie(5, "", null, insertedPlants.get("laitue"),
				insertedPlants.get("betterave"));
		allelopathieRepository.save(interactionPourBetterave5);
		Allelopathie interactionPourBetterave6 = new Allelopathie(5, "", null, insertedPlants.get("fraisier des bois"),
				insertedPlants.get("betterave"));
		allelopathieRepository.save(interactionPourBetterave6);
		Allelopathie interactionPourBetterave7 = new Allelopathie(5, "", null, insertedPlants.get("chou-rave"),
				insertedPlants.get("betterave"));
		allelopathieRepository.save(interactionPourBetterave7);
		Allelopathie interactionPourBetterave8 = new Allelopathie(5, "", null, insertedPlants.get("concombre"),
				insertedPlants.get("betterave"));
		allelopathieRepository.save(interactionPourBetterave8);
	}

	private void interactionPourAsperge(Map<String, Plante> insertedPlants) {
		log.info("interactionPourAsperge"); // Pour asperge
		Allelopathie interactionPourAsperge1 = new Allelopathie(5, "", null, insertedPlants.get("tomate"),
				insertedPlants.get("asperge"));
		allelopathieRepository.save(interactionPourAsperge1);
		Allelopathie interactionPourAsperge2 = new Allelopathie(5, "", null, insertedPlants.get("persil"),
				insertedPlants.get("asperge"));
		allelopathieRepository.save(interactionPourAsperge2);
		Allelopathie interactionPourAsperge3 = new Allelopathie(5, "", null, insertedPlants.get("basilic"),
				insertedPlants.get("asperge"));
		allelopathieRepository.save(interactionPourAsperge3);
		Allelopathie interactionPourAsperge4 = new Allelopathie(5, "", null, insertedPlants.get("haricot"),
				insertedPlants.get("asperge"));
		allelopathieRepository.save(interactionPourAsperge4);
	}

	private void interactionPourTomate(Map<String, Plante> insertedPlants) {
		log.info("interactionPourTomate"); // Pour tomate
		Allelopathie interactionPourTomate1 = new Allelopathie(5,
				"Le persil rend la tomate plus résistante aux maladies", null, insertedPlants.get("persil"),
				insertedPlants.get("tomate"));
		allelopathieRepository.save(interactionPourTomate1);
		Allelopathie interactionPourTomate2 = new Allelopathie(5, "", null, insertedPlants.get("oignon"),
				insertedPlants.get("tomate"));
		allelopathieRepository.save(interactionPourTomate2);
		Allelopathie interactionPourTomate3 = new Allelopathie(5, "", null, insertedPlants.get("poireau"),
				insertedPlants.get("tomate"));
		allelopathieRepository.save(interactionPourTomate3);
		Allelopathie interactionPourTomate4 = new Allelopathie(5, "", null, insertedPlants.get("oeillet d'inde"),
				insertedPlants.get("tomate"));
		allelopathieRepository.save(interactionPourTomate4);
		Allelopathie interactionPourTomate5 = new Allelopathie(5, "", null, insertedPlants.get("carotte"),
				insertedPlants.get("tomate"));
		allelopathieRepository.save(interactionPourTomate5);
		Allelopathie interactionPourTomate6 = new Allelopathie(5,
				"Le basilic rend la tomate plus résistante aux maladies", null, insertedPlants.get("basilic"),
				insertedPlants.get("tomate"));
		allelopathieRepository.save(interactionPourTomate6);
		Allelopathie interactionPourTomate7 = new Allelopathie(5, "", null, insertedPlants.get("asperge"),
				insertedPlants.get("tomate"));
		allelopathieRepository.save(interactionPourTomate7);
		Allelopathie interactionPourTomate8 = new Allelopathie(5, "", null, insertedPlants.get("souci officinal"),
				insertedPlants.get("tomate"));
		allelopathieRepository.save(interactionPourTomate8);
		Allelopathie interactionPourTomate9 = new Allelopathie(5, "", null, insertedPlants.get("bourrache"),
				insertedPlants.get("tomate"));
		allelopathieRepository.save(interactionPourTomate9);
		Allelopathie interactionPourTomate10 = new Allelopathie(5, "", null, insertedPlants.get("grande capucine"),
				insertedPlants.get("tomate"));
		allelopathieRepository.save(interactionPourTomate10);
	}

	private void interactionPourPommeDeTerre(Map<String, Plante> insertedPlants) {
		log.info("interactionPourPommeDeTerre"); // Pour pommeDeTerre
		Allelopathie interactionPourPommeDeTerre1 = new Allelopathie(5, "", null, insertedPlants.get("chou commun"),
				insertedPlants.get("pomme de terre"));
		allelopathieRepository.save(interactionPourPommeDeTerre1);
		Allelopathie interactionPourPommeDeTerre2 = new Allelopathie(5, "", null, insertedPlants.get("fève"),
				insertedPlants.get("pomme de terre"));
		allelopathieRepository.save(interactionPourPommeDeTerre2);
		Allelopathie interactionPourPommeDeTerre3 = new Allelopathie(5,
				"Le doryphore de la pomme de terre est repoussé par le pois", null, insertedPlants.get("pois"),
				insertedPlants.get("pomme de terre"));
		allelopathieRepository.save(interactionPourPommeDeTerre3);
		Allelopathie interactionPourPommeDeTerre4 = new Allelopathie(5, "", null, insertedPlants.get("courge"),
				insertedPlants.get("pomme de terre"));
		allelopathieRepository.save(interactionPourPommeDeTerre4);
		Allelopathie interactionPourPommeDeTerre5 = new Allelopathie(5, "", null, insertedPlants.get("potiron"),
				insertedPlants.get("pomme de terre"));
		allelopathieRepository.save(interactionPourPommeDeTerre5);
		Allelopathie interactionPourPommeDeTerre6 = new Allelopathie(5, "", null, insertedPlants.get("haricot"),
				insertedPlants.get("pomme de terre"));
		allelopathieRepository.save(interactionPourPommeDeTerre6);
		Allelopathie interactionPourPommeDeTerre7 = new Allelopathie(5, "", null, insertedPlants.get("oeillet d'inde"),
				insertedPlants.get("pomme de terre"));
		allelopathieRepository.save(interactionPourPommeDeTerre7);
		Allelopathie interactionPourPommeDeTerre8 = new Allelopathie(5, "", null, insertedPlants.get("souci officinal"),
				insertedPlants.get("pomme de terre"));
		allelopathieRepository.save(interactionPourPommeDeTerre8);
		Allelopathie interactionPourPommeDeTerre9 = new Allelopathie(5, "", null, insertedPlants.get("concombre"),
				insertedPlants.get("pomme de terre"));
		allelopathieRepository.save(interactionPourPommeDeTerre9);
		Allelopathie interactionPourPommeDeTerre10 = new Allelopathie(5, "", null, insertedPlants.get("bourrache"),
				insertedPlants.get("pomme de terre"));
		allelopathieRepository.save(interactionPourPommeDeTerre10);
	}

	private void interactionPourConcombre(Map<String, Plante> insertedPlants) {
		log.info("interactionPourConcombre"); // Pour concombre
		Allelopathie interactionPourConcombre1 = new Allelopathie(5, "", null, insertedPlants.get("basilic"),
				insertedPlants.get("concombre"));
		allelopathieRepository.save(interactionPourConcombre1);
		Allelopathie interactionPourConcombre2 = new Allelopathie(5, "", null, insertedPlants.get("chou commun"),
				insertedPlants.get("concombre"));
		allelopathieRepository.save(interactionPourConcombre2);
		Allelopathie interactionPourConcombre3 = new Allelopathie(5, "", null, insertedPlants.get("haricot"),
				insertedPlants.get("concombre"));
		allelopathieRepository.save(interactionPourConcombre3);
		Allelopathie interactionPourConcombre4 = new Allelopathie(5, "", null, insertedPlants.get("laitue"),
				insertedPlants.get("concombre"));
		allelopathieRepository.save(interactionPourConcombre4);
		Allelopathie interactionPourConcombre5 = new Allelopathie(5, "", null, insertedPlants.get("maïs"),
				insertedPlants.get("concombre"));
		allelopathieRepository.save(interactionPourConcombre5);
		Allelopathie interactionPourConcombre6 = new Allelopathie(5, "", null, insertedPlants.get("tournesol"),
				insertedPlants.get("concombre"));
		allelopathieRepository.save(interactionPourConcombre6);
		Allelopathie interactionPourConcombre7 = new Allelopathie(5, "", null, insertedPlants.get("pomme de terre"),
				insertedPlants.get("concombre"));
		allelopathieRepository.save(interactionPourConcombre7);
		Allelopathie interactionPourConcombre8 = new Allelopathie(5, "", null, insertedPlants.get("radis"),
				insertedPlants.get("concombre"));
		allelopathieRepository.save(interactionPourConcombre8);
		Allelopathie interactionPourConcombre9 = new Allelopathie(5, "", null, insertedPlants.get("origan"),
				insertedPlants.get("concombre"));
		allelopathieRepository.save(interactionPourConcombre9);
		Allelopathie interactionPourConcombre10 = new Allelopathie(5, "", null, insertedPlants.get("pois"),
				insertedPlants.get("concombre"));
		allelopathieRepository.save(interactionPourConcombre10);
		Allelopathie interactionPourConcombre11 = new Allelopathie(5, "L’aneth protège les concombres", null,
				insertedPlants.get("aneth"), insertedPlants.get("concombre"));
		allelopathieRepository.save(interactionPourConcombre11);
	}

	private void interactionPourHaricot(Map<String, Plante> insertedPlants) {
		log.info("interactionPourHaricot"); // Pour haricot
		Allelopathie interactionPourHaricot1 = new Allelopathie(5, "", null, insertedPlants.get("concombre"),
				insertedPlants.get("haricot"));
		allelopathieRepository.save(interactionPourHaricot1);
		Allelopathie interactionPourHaricot2 = new Allelopathie(5, "", null, insertedPlants.get("laitue"),
				insertedPlants.get("haricot"));
		allelopathieRepository.save(interactionPourHaricot2);
		Allelopathie interactionPourHaricot3 = new Allelopathie(5, "", null, insertedPlants.get("tomate"),
				insertedPlants.get("haricot"));
		allelopathieRepository.save(interactionPourHaricot3);
		Allelopathie interactionPourHaricot4 = new Allelopathie(5, "", null, insertedPlants.get("carotte"),
				insertedPlants.get("haricot"));
		allelopathieRepository.save(interactionPourHaricot4);
		Allelopathie interactionPourHaricot5 = new Allelopathie(5, "", null, insertedPlants.get("aubergine"),
				insertedPlants.get("haricot"));
		allelopathieRepository.save(interactionPourHaricot5);
		Allelopathie interactionPourHaricot6 = new Allelopathie(5, "", null, insertedPlants.get("oeillet d'inde"),
				insertedPlants.get("haricot"));
		allelopathieRepository.save(interactionPourHaricot6);
		Allelopathie interactionPourHaricot7 = new Allelopathie(5, "", null, insertedPlants.get("souci officinal"),
				insertedPlants.get("haricot"));
		allelopathieRepository.save(interactionPourHaricot7);
		Allelopathie interactionPourHaricot8 = new Allelopathie(5, "", null, insertedPlants.get("betterave"),
				insertedPlants.get("haricot"));
		allelopathieRepository.save(interactionPourHaricot8);
		Allelopathie interactionPourHaricot9 = new Allelopathie(5, "", null, insertedPlants.get("maïs"),
				insertedPlants.get("haricot"));
		allelopathieRepository.save(interactionPourHaricot9);
		Allelopathie interactionPourHaricot10 = new Allelopathie(5, "", null, insertedPlants.get("pomme de terre"),
				insertedPlants.get("haricot"));
		allelopathieRepository.save(interactionPourHaricot10);
	}

	private void interactionPourChouCommun(Map<String, Plante> insertedPlants) {
		log.info("interactionPourChouCommun"); // Pour chouCommun
		Allelopathie interactionPourChouCommun1 = new Allelopathie(5, "", null, insertedPlants.get("sarriette"),
				insertedPlants.get("chou commun"));
		allelopathieRepository.save(interactionPourChouCommun1);
		Allelopathie interactionPourChouCommun2 = new Allelopathie(5, "", null, insertedPlants.get("betterave"),
				insertedPlants.get("chou commun"));
		allelopathieRepository.save(interactionPourChouCommun2);
		Allelopathie interactionPourChouCommun3 = new Allelopathie(5, "La piéride du chou est repoussée par la tomate",
				null, insertedPlants.get("tomate"), insertedPlants.get("chou commun"));
		allelopathieRepository.save(interactionPourChouCommun3);
		Allelopathie interactionPourChouCommun4 = new Allelopathie(5, "", null, insertedPlants.get("romarin"),
				insertedPlants.get("chou commun"));
		allelopathieRepository.save(interactionPourChouCommun4);
		Allelopathie interactionPourChouCommun5 = new Allelopathie(5, "La menthe protège les choux des papillons", null,
				insertedPlants.get("menthe poivrée"), insertedPlants.get("chou commun"));
		allelopathieRepository.save(interactionPourChouCommun5);
		Allelopathie interactionPourChouCommun6 = new Allelopathie(5, "La sauge protège les choux des papillons", null,
				insertedPlants.get("sauge"), insertedPlants.get("chou commun"));
		allelopathieRepository.save(interactionPourChouCommun6);
		Allelopathie interactionPourChouCommun7 = new Allelopathie(5, "", null, insertedPlants.get("thym commun"),
				insertedPlants.get("chou commun"));
		allelopathieRepository.save(interactionPourChouCommun7);
		Allelopathie interactionPourChouCommun8 = new Allelopathie(5, "", null, insertedPlants.get("grande capucine"),
				insertedPlants.get("chou commun"));
		allelopathieRepository.save(interactionPourChouCommun8);
		Allelopathie interactionPourChouCommun9 = new Allelopathie(5, "", null, insertedPlants.get("pomme de terre"),
				insertedPlants.get("chou commun"));
		allelopathieRepository.save(interactionPourChouCommun9);
		Allelopathie interactionPourChouCommun10 = new Allelopathie(5,
				"L'hysope empêche les mouches blanche de pondre dans les choux", null, insertedPlants.get("hysope"),
				insertedPlants.get("chou commun"));
		allelopathieRepository.save(interactionPourChouCommun10);
		Allelopathie interactionPourChouCommun11 = new Allelopathie(5, "La piéride du chou est repoussée par le céleri",
				null, insertedPlants.get("céleri"), insertedPlants.get("chou commun"));
		allelopathieRepository.save(interactionPourChouCommun11);
		Allelopathie interactionPourChouCommun12 = new Allelopathie(5, "", null, insertedPlants.get("bourrache"),
				insertedPlants.get("chou commun"));
		allelopathieRepository.save(interactionPourChouCommun12);
		Allelopathie interactionPourChouCommun13 = new Allelopathie(5, "", null, insertedPlants.get("oeillet d'inde"),
				insertedPlants.get("chou commun"));
		allelopathieRepository.save(interactionPourChouCommun13);
		Allelopathie interactionPourChouCommun14 = new Allelopathie(5,
				"La piéride du chou n'aime pas l'odeur de la tomate. "
						+ "L'effet protecteur est renforcé lorsqu'on met entre les plantes menacées, les gourmands des tomates.",
				null, insertedPlants.get("tomate"), insertedPlants.get("chou commun"));
		allelopathieRepository.save(interactionPourChouCommun14);
		Allelopathie interactionPourChouCommun15 = new Allelopathie(5,
				"La piéride du chou n'aime pas l'odeur du céleri", null, insertedPlants.get("céleri"),
				insertedPlants.get("chou commun"));
		allelopathieRepository.save(interactionPourChouCommun15);
	}

	private void interactionPourCarotte(Map<String, Plante> insertedPlants) {
		log.info("interactionPourCarotte"); // Pour carotte
		Allelopathie interactionPourCarotte1 = new Allelopathie(5, "", null, insertedPlants.get("ail"),
				insertedPlants.get("carotte"));

		allelopathieRepository.save(interactionPourCarotte1);

		Allelopathie interactionPourCarotte2 = new Allelopathie(5, "L’aneth protège les carottes et aide à la levée",
				null, insertedPlants.get("aneth"), insertedPlants.get("carotte"));
		allelopathieRepository.save(interactionPourCarotte2);

		Allelopathie interactionPourCarotte3 = new Allelopathie(5, "", null, insertedPlants.get("échalotte"),
				insertedPlants.get("carotte"));
		allelopathieRepository.save(interactionPourCarotte3);
		Allelopathie interactionPourCarotte4 = new Allelopathie(5, "", null, insertedPlants.get("poireau"),
				insertedPlants.get("carotte"));
		allelopathieRepository.save(interactionPourCarotte4);
		Allelopathie interactionPourCarotte5 = new Allelopathie(5, "", null, insertedPlants.get("tomate"),
				insertedPlants.get("carotte"));
		allelopathieRepository.save(interactionPourCarotte5);
		Allelopathie interactionPourCarotte6 = new Allelopathie(5, "", null, insertedPlants.get("laitue"),
				insertedPlants.get("carotte"));
		allelopathieRepository.save(interactionPourCarotte6);
		Allelopathie interactionPourCarotte7 = new Allelopathie(5, "", null, insertedPlants.get("ciboulette"),
				insertedPlants.get("carotte"));
		allelopathieRepository.save(interactionPourCarotte7);
		Allelopathie interactionPourCarotte8 = new Allelopathie(5, "", null, insertedPlants.get("radis"),
				insertedPlants.get("carotte"));
		allelopathieRepository.save(interactionPourCarotte8);
		Allelopathie interactionPourCarotte9 = new Allelopathie(5, "L'oignon repousse la mouche de la carotte", null,
				insertedPlants.get("oignon"), insertedPlants.get("carotte"));
		allelopathieRepository.save(interactionPourCarotte9);
		Allelopathie interactionPourCarotte10 = new Allelopathie(5, "", null, insertedPlants.get("pois"),
				insertedPlants.get("carotte"));
		allelopathieRepository.save(interactionPourCarotte10);
		Allelopathie interactionPourCarotte11 = new Allelopathie(5, "Le poireau repousse la mouche de la carotte", null,
				insertedPlants.get("poireau"), insertedPlants.get("carotte"));
		allelopathieRepository.save(interactionPourCarotte11);
		Allelopathie interactionPourCarotte12 = new Allelopathie(5, "", null, insertedPlants.get("carotte"),
				insertedPlants.get("carotte"));
		allelopathieRepository.save(interactionPourCarotte12);
		Allelopathie interactionPourCarotte13 = new Allelopathie(5, "", null, insertedPlants.get("carotte"),
				insertedPlants.get("carotte"));
		allelopathieRepository.save(interactionPourCarotte13);
		Allelopathie interactionPourCarotte14 = new Allelopathie(5, "", null, insertedPlants.get("romarin"),
				insertedPlants.get("carotte"));
		allelopathieRepository.save(interactionPourCarotte14);
		Allelopathie interactionPourCarotte15 = new Allelopathie(5, "", null, insertedPlants.get("scorsonère des prés"),
				insertedPlants.get("carotte"));
		allelopathieRepository.save(interactionPourCarotte15);
	}

//	private void insertVitesseCroissance() {
//		try {
//			VitesseCroissance tresLente = new VitesseCroissance("tresLente");
//			VitesseCroissance lente = new VitesseCroissance("lente");
//			VitesseCroissance vcNormale = new VitesseCroissance("normale");
//			VitesseCroissance rapide = new VitesseCroissance("rapide");
//			VitesseCroissance tresRapide = new VitesseCroissance("tresRapide");
//			vitesseCroissanceRepository.save(tresLente);
//			vitesseCroissanceRepository.save(lente);
//			vitesseCroissanceRepository.save(vcNormale);
//			vitesseCroissanceRepository.save(rapide);
//			vitesseCroissanceRepository.save(tresRapide);
//		} catch (Exception e) {
//			log.error("Error when trying to insert in table VitesseCroissance. {" + e.getMessage() + "}");
//		}
//	}

//	private void insertEnsoleillement() {
//		try {
//			Ensoleillement soleil = new Ensoleillement("soleil");
//			Ensoleillement miOmbre = new Ensoleillement("miOmbre");
//			Ensoleillement ombre = new Ensoleillement("ombre");
//			ensoleillementRepository.save(soleil);
//			ensoleillementRepository.save(miOmbre);
//			ensoleillementRepository.save(ombre);
//		} catch (Exception e) {
//			log.error("Error when trying to insert in table Ensoleillement. {" + e.getMessage() + "}");
//		}
//	}

	private void insertRichesseSol() {
		try {
			Sol tresPauvre = new Sol(null, null, null, "tresPauvre");
			Sol pauvre = new Sol(null, null, null, "pauvre");
			Sol normale = new Sol(null, null, null, "normale");
			Sol riche = new Sol(null, null, null, "riche");
			Sol tresRiche = new Sol(null, null, null, "tresRiche");
			solRepository.save(tresPauvre);
			solRepository.save(pauvre);
			solRepository.save(normale);
			solRepository.save(riche);
			solRepository.save(tresRiche);
			Sol argileuse = new Sol(null, null, "argileuse", "");
			Sol calcaire = new Sol(null, null, "calcaire", "");
			Sol humifere = new Sol(null, null, "humifere", "");
			Sol sableuse = new Sol(null, null, "sableuse", "");
			solRepository.save(argileuse);
			solRepository.save(calcaire);
			solRepository.save(humifere);
			solRepository.save(sableuse);
		} catch (Exception e) {
			log.error("Error when trying to insert in table RichesseSol. {" + e.getMessage() + "}");
		}
	}

	private void insertTypeFeuillage() {
		try {
			Feuillage persistant = new Feuillage("persistant");
			Feuillage semiPersistant = new Feuillage("semiPersistant");
			Feuillage marcescent = new Feuillage("marcescent");
			Feuillage caduc = new Feuillage("caduc");
			feuillageRepository.save(persistant);
			feuillageRepository.save(semiPersistant);
			feuillageRepository.save(marcescent);
			feuillageRepository.save(caduc);
		} catch (Exception e) {
			log.error("Error when trying to insert in table TypeFeuillage. {" + e.getMessage() + "}");
		}
	}

	private void insertStrate() {
		try {
			Strate hypogee = new Strate("hypogee");
			Strate muscinale = new Strate("muscinale");
			Strate herbacee = new Strate("herbacee");
			Strate arbustive = new Strate("arbustive");
			Strate arboree = new Strate("arboree");
			strateRepository.save(hypogee);
			strateRepository.save(muscinale);
			strateRepository.save(herbacee);
			strateRepository.save(arbustive);
			strateRepository.save(arboree);
		} catch (Exception e) {
			log.error("Error when trying to insert in table Strate. {" + e.getMessage() + "}");
		}
	}

	private void insertTypeRacine() {
		try {
			Racine pivotante = new Racine();
			pivotante.setType("pivotante");
			Racine fasciculaire = new Racine();
			pivotante.setType("fasciculaire");
			Racine adventice = new Racine();
			pivotante.setType("adventice");
			Racine tracante = new Racine();
			pivotante.setType("tracante");
			Racine contrefort = new Racine();
			pivotante.setType("contrefort");
			Racine crampon = new Racine();
			pivotante.setType("crampon");
			Racine echasse = new Racine();
			pivotante.setType("echasse");
			Racine aerienne = new Racine();
			pivotante.setType("aerienne");
			Racine liane = new Racine();
			pivotante.setType("liane");
			Racine ventouse = new Racine();
			pivotante.setType("ventouse");
			Racine pneumatophore = new Racine();
			pivotante.setType("pneumatophore");
			racineRepository.save(pivotante);
			racineRepository.save(fasciculaire);
			racineRepository.save(adventice);
			racineRepository.save(tracante);
			racineRepository.save(contrefort);
			racineRepository.save(crampon);
			racineRepository.save(echasse);
			racineRepository.save(aerienne);
			racineRepository.save(liane);
			racineRepository.save(ventouse);
			racineRepository.save(pneumatophore);
		} catch (Exception e) {
			log.error("Error when trying to insert in table TypeRacine. {" + e.getMessage() + "}");
		}
	}

	private void insertMois() {
		try {
			Mois janvier = new Mois(1.0, "Janvier");
			Mois fevrier = new Mois(2.0, "fevrier");
			Mois mars = new Mois(3.0, "mars");
			Mois avril = new Mois(4.0, "avril");
			Mois mai = new Mois(5.0, "mai");
			Mois juin = new Mois(6.0, "juin");
			Mois juillet = new Mois(7.0, "juillet");
			Mois aout = new Mois(8.0, "aout");
			Mois septembre = new Mois(9.0, "septembre");
			Mois octobre = new Mois(10.0, "octobre");
			Mois novembre = new Mois(11.0, "novembre");
			Mois decembre = new Mois(12.0, "decembre");
			moisRepository.save(janvier);
			moisRepository.save(fevrier);
			moisRepository.save(mars);
			moisRepository.save(avril);
			moisRepository.save(mai);
			moisRepository.save(juin);
			moisRepository.save(juillet);
			moisRepository.save(aout);
			moisRepository.save(septembre);
			moisRepository.save(octobre);
			moisRepository.save(novembre);
			moisRepository.save(decembre);
		} catch (Exception e) {
			log.error("Error when trying to insert in table Mois. {" + e.getMessage() + "}");
		}
	}

	/**
	 * @param data null if failed to insert something
	 * @return
	 */
	private Plante insertPlant(Map<String, String> data) {

		Plante plante;
		NomVernaculaire nom = new NomVernaculaire(data.get("Plante"));
		try {

			Cronquist cronquist = new Cronquist(null, null, null, null, null, //
					data.get("Ordre"), //
					data.get("Famille"), //
					data.get("Genre"), //
					data.get("Espece")//
			);
			Classification classification = new Classification();
			classification.setCronquist(cronquist);
			if (!classificationRepository.exists(Example.of(classification))) {
				classificationRepository.save(classification);
			} else {
				Optional<Classification> returned = classificationRepository.findOne(Example.of(classification));
				if (returned.isPresent()) {
					classification = returned.get();
					log.info("Existing classification : " + classification.toString());
				} else {
					log.error("Unable to get instance of : " + classification.toString());
				}
			}
			plante = new Plante();
			plante.addNomsVernaculaires(nom);
			plante.setClassification(classification);
			if (!planteRepository.exists(Example.of(plante))) {
				planteRepository.save(plante);
			} else {
				Optional<Plante> returned = planteRepository.findOne(Example.of(plante));
				if (returned.isPresent()) {
					plante = returned.get();
				} else {
					log.error("Unable to get instance of : " + plante.toString());
				}
			}
			log.info("Plant saved : " + plante.toString());
			return plante;
		} catch (Exception e) {
			log.error("Unable to insert {" + nom.getNom() + "}");
		}
		return null;
	}
}
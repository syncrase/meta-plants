package fr.syncrase.perma.aop.init;

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

import fr.syncrase.perma.domain.Mois;
import fr.syncrase.perma.domain.Plante;
import fr.syncrase.perma.repository.AllelopathieRepository;
import fr.syncrase.perma.repository.FeuillageRepository;
import fr.syncrase.perma.repository.MoisRepository;
import fr.syncrase.perma.repository.PlanteRepository;
import fr.syncrase.perma.repository.RacineRepository;
import fr.syncrase.perma.repository.SolRepository;
import fr.syncrase.perma.repository.StrateRepository;

@Component
public class AppLoading implements ApplicationListener<ContextRefreshedEvent> {
// implements ApplicationListener<ContextRefreshedEvent>
	private Logger log = LoggerFactory.getLogger(AppLoading.class);

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

//	@Autowired
//	public void setInteractionPlantePlanteRepository(
//			InteractionPlantePlanteRepository interactionPlantePlanteRepository) {
//		this.interactionPlantePlanteRepository = interactionPlantePlanteRepository;
//	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
//		@ Override
		insertMois();
		insertTypeRacine();
		insertStrate();
		insertTypeFeuillage();
		insertTypeTerre();
		insertRichesseSol();
		insertEnsoleillement();
		insertVitesseCroissance();

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
		InteractionPlantePlante interactionContreAubergine1 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("haricot"), insertedPlants.get("aubergine"));
		interactionPlantePlanteRepository.save(interactionContreAubergine1);
	}

	private void interactionContreAsperge(Map<String, Plante> insertedPlants) {
		log.info("interactionContreAsperge"); // Contre asperge
		InteractionPlantePlante interactionContreAsperge1 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("tomate"), insertedPlants.get("asperge"));
		interactionPlantePlanteRepository.save(interactionContreAsperge1);
		InteractionPlantePlante interactionContreAsperge2 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("haricot"), insertedPlants.get("asperge"));
		interactionPlantePlanteRepository.save(interactionContreAsperge2);
		InteractionPlantePlante interactionContreAsperge3 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("persil"), insertedPlants.get("asperge"));
		interactionPlantePlanteRepository.save(interactionContreAsperge3);
	}

	private void interactionContreFraisier(Map<String, Plante> insertedPlants) {
		log.info("interactionContreFraisier"); // Contre fraisier
		InteractionPlantePlante interactionContreFraisier1 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("chou commun"), insertedPlants.get("fraisier des bois"));
		interactionPlantePlanteRepository.save(interactionContreFraisier1);
	}

	private void interactionContrePersil(Map<String, Plante> insertedPlants) {
		log.info("interactionContrePersil"); // Contre persil
		InteractionPlantePlante interactionContrePersil1 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("laitue"), insertedPlants.get("persil"));
		interactionPlantePlanteRepository.save(interactionContrePersil1);
	}

	private void interactionContreSauge(Map<String, Plante> insertedPlants) {
		log.info("interactionContreSauge"); // Contre sauge
		InteractionPlantePlante interactionContreSauge1 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("oignon"), insertedPlants.get("sauge"));
		interactionPlantePlanteRepository.save(interactionContreSauge1);
	}

	private void interactionContreTomate(Map<String, Plante> insertedPlants) {
		log.info("interactionContreTomate"); // Contre tomate
		InteractionPlantePlante interactionContreTomate1 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("haricot"), insertedPlants.get("tomate"));
		interactionPlantePlanteRepository.save(interactionContreTomate1);
		InteractionPlantePlante interactionContreTomate2 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("concombre"), insertedPlants.get("tomate"));
		interactionPlantePlanteRepository.save(interactionContreTomate2);
		InteractionPlantePlante interactionContreTomate3 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("chou-rave"), insertedPlants.get("tomate"));
		interactionPlantePlanteRepository.save(interactionContreTomate3);
		InteractionPlantePlante interactionContreTomate4 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("chou commun"), insertedPlants.get("tomate"));
		interactionPlantePlanteRepository.save(interactionContreTomate4);
		InteractionPlantePlante interactionContreTomate5 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("pomme de terre"), insertedPlants.get("tomate"));
		interactionPlantePlanteRepository.save(interactionContreTomate5);
		InteractionPlantePlante interactionContreTomate6 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("betterave"), insertedPlants.get("tomate"));
		interactionPlantePlanteRepository.save(interactionContreTomate6);
		InteractionPlantePlante interactionContreTomate7 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("pois"), insertedPlants.get("tomate"));
		interactionPlantePlanteRepository.save(interactionContreTomate7);
	}

	private void interactionContrePommeDeTerre(Map<String, Plante> insertedPlants) {
		log.info("interactionContrePommeDeTerre"); // Contre pommeDeTerre
		InteractionPlantePlante interactionContrePommeDeTerre1 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("tomate"), insertedPlants.get("pomme de terre"));
		interactionPlantePlanteRepository.save(interactionContrePommeDeTerre1);
		InteractionPlantePlante interactionContrePommeDeTerre2 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("courge"), insertedPlants.get("pomme de terre"));
		interactionPlantePlanteRepository.save(interactionContrePommeDeTerre2);
		InteractionPlantePlante interactionContrePommeDeTerre3 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("carotte"), insertedPlants.get("pomme de terre"));
		interactionPlantePlanteRepository.save(interactionContrePommeDeTerre3);
		InteractionPlantePlante interactionContrePommeDeTerre4 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("oignon"), insertedPlants.get("pomme de terre"));
		interactionPlantePlanteRepository.save(interactionContrePommeDeTerre4);
		InteractionPlantePlante interactionContrePommeDeTerre5 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("framboisier"), insertedPlants.get("pomme de terre"));
		interactionPlantePlanteRepository.save(interactionContrePommeDeTerre5);
		InteractionPlantePlante interactionContrePommeDeTerre6 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("arroche des jardins"), insertedPlants.get("pomme de terre"));
		interactionPlantePlanteRepository.save(interactionContrePommeDeTerre6);
		InteractionPlantePlante interactionContrePommeDeTerre7 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("tournesol"), insertedPlants.get("pomme de terre"));
		interactionPlantePlanteRepository.save(interactionContrePommeDeTerre7);
		InteractionPlantePlante interactionContrePommeDeTerre8 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("melon"), insertedPlants.get("pomme de terre"));
		interactionPlantePlanteRepository.save(interactionContrePommeDeTerre8);
	}

	private void interactionContrePois(Map<String, Plante> insertedPlants) {
		log.info("interactionContrePois"); // Contre pois
		InteractionPlantePlante interactionContrePois1 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("ail"), insertedPlants.get("pois"));
		interactionPlantePlanteRepository.save(interactionContrePois1);
		InteractionPlantePlante interactionContrePois2 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("échalotte"), insertedPlants.get("pois"));
		interactionPlantePlanteRepository.save(interactionContrePois2);
		InteractionPlantePlante interactionContrePois3 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("oignon"), insertedPlants.get("pois"));
		interactionPlantePlanteRepository.save(interactionContrePois3);
		InteractionPlantePlante interactionContrePois4 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("poireau"), insertedPlants.get("pois"));
		interactionPlantePlanteRepository.save(interactionContrePois4);
		InteractionPlantePlante interactionContrePois5 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("tomate"), insertedPlants.get("pois"));
		interactionPlantePlanteRepository.save(interactionContrePois5);
	}

	private void interactionContrePoireau(Map<String, Plante> insertedPlants) {
		log.info("interactionContrePoireau"); // Contre poireau
		InteractionPlantePlante interactionContrePoireau1 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("brocoli"), insertedPlants.get("poireau"));
		interactionPlantePlanteRepository.save(interactionContrePoireau1);
		InteractionPlantePlante interactionContrePoireau2 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("haricot"), insertedPlants.get("poireau"));
		interactionPlantePlanteRepository.save(interactionContrePoireau2);
		InteractionPlantePlante interactionContrePoireau3 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("fève"), insertedPlants.get("poireau"));
		interactionPlantePlanteRepository.save(interactionContrePoireau3);
	}

	private void interactionContreOignon(Map<String, Plante> insertedPlants) {
		log.info("interactionContreOignon"); // Contre oignon
		InteractionPlantePlante interactionContreOignon1 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("pois"), insertedPlants.get("oignon"));
		interactionPlantePlanteRepository.save(interactionContreOignon1);
		InteractionPlantePlante interactionContreOignon2 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("haricot"), insertedPlants.get("oignon"));
		interactionPlantePlanteRepository.save(interactionContreOignon2);
		InteractionPlantePlante interactionContreOignon3 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("fève"), insertedPlants.get("oignon"));
		interactionPlantePlanteRepository.save(interactionContreOignon3);
		InteractionPlantePlante interactionContreOignon4 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("lentille"), insertedPlants.get("oignon"));
		interactionPlantePlanteRepository.save(interactionContreOignon4);
	}

	private void interactionContreMelon(Map<String, Plante> insertedPlants) {
		log.info("interactionContreMelon"); // Contre melon
		InteractionPlantePlante interactionContreMelon1 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("concombre"), insertedPlants.get("melon"));
		interactionPlantePlanteRepository.save(interactionContreMelon1);
		InteractionPlantePlante interactionContreMelon2 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("courge"), insertedPlants.get("melon"));
		interactionPlantePlanteRepository.save(interactionContreMelon2);
	}

	private void interactionContreLaitue(Map<String, Plante> insertedPlants) {
		log.info("interactionContreLaitue"); // Contre laitue
		InteractionPlantePlante interactionContreLaitue1 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("tournesol"), insertedPlants.get("laitue"));
		interactionPlantePlanteRepository.save(interactionContreLaitue1);
		InteractionPlantePlante interactionContreLaitue2 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("persil"), insertedPlants.get("laitue"));
		interactionPlantePlanteRepository.save(interactionContreLaitue2);
	}

	private void interactionContreHaricot(Map<String, Plante> insertedPlants) {
		log.info("interactionContreHaricot"); // Contre haricot
		InteractionPlantePlante interactionContreHaricot1 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("oignon"), insertedPlants.get("haricot"));
		interactionPlantePlanteRepository.save(interactionContreHaricot1);
		InteractionPlantePlante interactionContreHaricot2 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("ail"), insertedPlants.get("haricot"));
		interactionPlantePlanteRepository.save(interactionContreHaricot2);
		InteractionPlantePlante interactionContreHaricot3 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("échalotte"), insertedPlants.get("haricot"));
		interactionPlantePlanteRepository.save(interactionContreHaricot3);
		InteractionPlantePlante interactionContreHaricot4 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("tomate"), insertedPlants.get("haricot"));
		interactionPlantePlanteRepository.save(interactionContreHaricot4);
		InteractionPlantePlante interactionContreHaricot5 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("fenouil"), insertedPlants.get("haricot"));
		interactionPlantePlanteRepository.save(interactionContreHaricot5);
		InteractionPlantePlante interactionContreHaricot6 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("pois"), insertedPlants.get("haricot"));
		interactionPlantePlanteRepository.save(interactionContreHaricot6);
	}

	private void interactionContreFenouil(Map<String, Plante> insertedPlants) {
		log.info("interactionContreFenouil"); // Contre fenouil
		InteractionPlantePlante interactionContreFenouil1 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("tomate"), insertedPlants.get("fenouil"));
		interactionPlantePlanteRepository.save(interactionContreFenouil1);
		InteractionPlantePlante interactionContreFenouil2 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("chou-rave"), insertedPlants.get("fenouil"));
		interactionPlantePlanteRepository.save(interactionContreFenouil2);

		InteractionPlantePlante interactionContreFenouil4 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("carvi"), insertedPlants.get("fenouil"));
		interactionPlantePlanteRepository.save(interactionContreFenouil4);
		InteractionPlantePlante interactionContreFenouil5 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("haricot"), insertedPlants.get("fenouil"));
		interactionPlantePlanteRepository.save(interactionContreFenouil5);
		InteractionPlantePlante interactionContreFenouil6 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("pois"), insertedPlants.get("fenouil"));
		interactionPlantePlanteRepository.save(interactionContreFenouil6);
		InteractionPlantePlante interactionContreFenouil7 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("échalotte"), insertedPlants.get("fenouil"));
		interactionPlantePlanteRepository.save(interactionContreFenouil7);
		InteractionPlantePlante interactionContreFenouil8 = new InteractionPlantePlante("-",
				"L'absinthe semble gêner la formation des graines", null, insertedPlants.get("absinthe"),
				insertedPlants.get("fenouil"));
		interactionPlantePlanteRepository.save(interactionContreFenouil8);
		InteractionPlantePlante interactionContreFenouil9 = new InteractionPlantePlante("-",
				"La coriandre semble gêner la formation des graines", null, insertedPlants.get("coriandre"),
				insertedPlants.get("fenouil"));
		interactionPlantePlanteRepository.save(interactionContreFenouil9);
	}

	private void interactionContreEpinard(Map<String, Plante> insertedPlants) {
		log.info("interactionContreEpinard"); // Contre epinard
		InteractionPlantePlante interactionContreEpinard1 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("pomme de terre"), insertedPlants.get("épinard"));
		interactionPlantePlanteRepository.save(interactionContreEpinard1);
		InteractionPlantePlante interactionContreEpinard2 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("betterave"), insertedPlants.get("épinard"));
		interactionPlantePlanteRepository.save(interactionContreEpinard2);
	}

	private void interactionContreEchalotte(Map<String, Plante> insertedPlants) {
		log.info("interactionContreEchalotte"); // Contre echalotte
		InteractionPlantePlante interactionContreEchalotte1 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("pois"), insertedPlants.get("échalotte"));
		interactionPlantePlanteRepository.save(interactionContreEchalotte1);
		InteractionPlantePlante interactionContreEchalotte2 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("haricot"), insertedPlants.get("échalotte"));
		interactionPlantePlanteRepository.save(interactionContreEchalotte2);
		InteractionPlantePlante interactionContreEchalotte3 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("fève"), insertedPlants.get("échalotte"));
		interactionPlantePlanteRepository.save(interactionContreEchalotte3);
		InteractionPlantePlante interactionContreEchalotte4 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("lentille"), insertedPlants.get("échalotte"));
		interactionPlantePlanteRepository.save(interactionContreEchalotte4);
	}

	private void interactionContreCourge(Map<String, Plante> insertedPlants) {
		log.info("interactionContreCourge"); // Contre courge
		InteractionPlantePlante interactionContreCourge1 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("pomme de terre"), insertedPlants.get("courge"));
		interactionPlantePlanteRepository.save(interactionContreCourge1);
	}

	private void interactionContreConcombre(Map<String, Plante> insertedPlants) {
		log.info("interactionContreConcombre"); // Contre concombre
		InteractionPlantePlante interactionContreConcombre1 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("pomme de terre"), insertedPlants.get("concombre"));
		interactionPlantePlanteRepository.save(interactionContreConcombre1);
		InteractionPlantePlante interactionContreConcombre2 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("tomate"), insertedPlants.get("concombre"));
		interactionPlantePlanteRepository.save(interactionContreConcombre2);
		InteractionPlantePlante interactionContreConcombre3 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("courgette"), insertedPlants.get("concombre"));
		interactionPlantePlanteRepository.save(interactionContreConcombre3);
	}

	private void interactionContreChou(Map<String, Plante> insertedPlants) {
		log.info("interactionContreChou"); // Contre chou
		InteractionPlantePlante interactionContreChou1 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("fraisier des bois"), insertedPlants.get("chou commun"));
		interactionPlantePlanteRepository.save(interactionContreChou1);
		InteractionPlantePlante interactionContreChou2 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("oignon"), insertedPlants.get("chou commun"));
		interactionPlantePlanteRepository.save(interactionContreChou2);
		InteractionPlantePlante interactionContreChou3 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("tomate"), insertedPlants.get("chou commun"));
		interactionPlantePlanteRepository.save(interactionContreChou3);
	}

	private void interactionContreRadis(Map<String, Plante> insertedPlants) {
		log.info("interactionContreRadis"); // Contre radis
		InteractionPlantePlante interactionContreRadis1 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("cerfeuil"), insertedPlants.get("radis"));
		interactionPlantePlanteRepository.save(interactionContreRadis1);
	}

	private void interactionContreBetterave(Map<String, Plante> insertedPlants) {
		log.info("interactionContreBetterave"); // Contre betterave
		InteractionPlantePlante interactionContreBetterave1 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("haricot"), insertedPlants.get("betterave"));
		interactionPlantePlanteRepository.save(interactionContreBetterave1);
		InteractionPlantePlante interactionContreBetterave2 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("épinard"), insertedPlants.get("betterave"));
		interactionPlantePlanteRepository.save(interactionContreBetterave2);
	}

	private void interactionContreBasilic(Map<String, Plante> insertedPlants) {
		log.info("interactionContreBasilic"); // Contre basilic
		InteractionPlantePlante interactionContreBasilic1 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("rue officinale"), insertedPlants.get("basilic"));
		interactionPlantePlanteRepository.save(interactionContreBasilic1);
	}

	private void interactionContreArtichaut(Map<String, Plante> insertedPlants) {
		log.info("interactionContreArtichaut"); // Contre artichaut
		InteractionPlantePlante interactionContreArtichaut1 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("fève"), insertedPlants.get("artichaut"));
		interactionPlantePlanteRepository.save(interactionContreArtichaut1);
	}

	private void interactionContreAbsinthe(Map<String, Plante> insertedPlants) {
		log.info("interactionContreAbsinthe"); // Contre absinthe
		InteractionPlantePlante interactionContreAbsinthe1 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("carvi"), insertedPlants.get("absinthe"));
		interactionPlantePlanteRepository.save(interactionContreAbsinthe1);
		InteractionPlantePlante interactionContreAbsinthe2 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("sauge"), insertedPlants.get("absinthe"));
		interactionPlantePlanteRepository.save(interactionContreAbsinthe2);
		InteractionPlantePlante interactionContreAbsinthe3 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("anis vert"), insertedPlants.get("absinthe"));
		interactionPlantePlanteRepository.save(interactionContreAbsinthe3);
		InteractionPlantePlante interactionContreAbsinthe4 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("fenouil"), insertedPlants.get("absinthe"));
		interactionPlantePlanteRepository.save(interactionContreAbsinthe4);
	}

	private void interactionContreCarotte(Map<String, Plante> insertedPlants) {
		log.info("interactionContreCarotte"); // Contre carotte
		InteractionPlantePlante interactionContreCarotte1 = new InteractionPlantePlante("-",
				"En compagnie d'aneth la carotte est sensible aux maladies et aux ravageurs et au mieux végètera. "
						+ "Elle ne poussent pas là où auparavant il y en avait déjà",
				null, insertedPlants.get("aneth"), insertedPlants.get("carotte"));
		interactionPlantePlanteRepository.save(interactionContreCarotte1);
	}

	private void interactionContreAil(Map<String, Plante> insertedPlants) {
		log.info("interactionContreAil"); // Contre insertedPlants.get("ail")
		InteractionPlantePlante interactionContreAil1 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("fève"), insertedPlants.get("ail"));
		interactionPlantePlanteRepository.save(interactionContreAil1);
		InteractionPlantePlante interactionContreAil2 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("lentille"), insertedPlants.get("ail"));
		interactionPlantePlanteRepository.save(interactionContreAil2);
		InteractionPlantePlante interactionContreAil3 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("haricot"), insertedPlants.get("ail"));
		interactionPlantePlanteRepository.save(interactionContreAil3);
		InteractionPlantePlante interactionContreAil4 = new InteractionPlantePlante("-", "", null,
				insertedPlants.get("pois"), insertedPlants.get("ail"));
		interactionPlantePlanteRepository.save(interactionContreAil4);
	}

	private void interactionContreChouFleur(Map<String, Plante> insertedPlants) {
		log.info("interactionContreChouFleur"); // Contre chouFleur
		InteractionPlantePlante interactionContreChouFleur1 = new InteractionPlantePlante("-",
				"Le chou fleur utilise mieux les nutriments présents dans le sols lorsqu'il accompagné du céleri, de même pour ce dernier.",
				null, insertedPlants.get("céleri"), insertedPlants.get("chou fleur"));
		interactionPlantePlanteRepository.save(interactionContreChouFleur1);
	}

	private void interactionPourCeleri(Map<String, Plante> insertedPlants) {
		log.info("interactionPourCeleri"); // Pour celeri
		InteractionPlantePlante interactionPourCeleri1 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("poireau"), insertedPlants.get("céleri"));
		interactionPlantePlanteRepository.save(interactionPourCeleri1);
		InteractionPlantePlante interactionPourCeleri2 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("tomate"), insertedPlants.get("céleri"));
		interactionPlantePlanteRepository.save(interactionPourCeleri2);
		InteractionPlantePlante interactionPourCeleri3 = new InteractionPlantePlante("+",
				"Le céleri cultivé seul n'utilise qu'une partie des substances nutritives trouvées dans le sol. Quand on le plante avec du chou-fleur, "
						+ "il les utilise mieux, de même que ce dernier. La récolte est alors meilleure pour les deux plantes.\r\n",
				null, insertedPlants.get("chou fleur"), insertedPlants.get("céleri"));
		interactionPlantePlanteRepository.save(interactionPourCeleri3);
		InteractionPlantePlante interactionPourCeleri4 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("épinard"), insertedPlants.get("céleri"));
		interactionPlantePlanteRepository.save(interactionPourCeleri4);
		InteractionPlantePlante interactionPourCeleri5 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("concombre"), insertedPlants.get("céleri"));
		interactionPlantePlanteRepository.save(interactionPourCeleri5);
		InteractionPlantePlante interactionPourCeleri6 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("haricot"), insertedPlants.get("céleri"));
		interactionPlantePlanteRepository.save(interactionPourCeleri6);
	}

	private void interactionPourBette(Map<String, Plante> insertedPlants) {
		log.info("interactionPourBette"); // Pour bette
		InteractionPlantePlante interactionPourBette1 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("radis"), insertedPlants.get("bette"));
		interactionPlantePlanteRepository.save(interactionPourBette1);
		InteractionPlantePlante interactionPourBette2 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("carotte"), insertedPlants.get("bette"));
		interactionPlantePlanteRepository.save(interactionPourBette2);
		InteractionPlantePlante interactionPourBette3 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("haricot"), insertedPlants.get("bette"));
		interactionPlantePlanteRepository.save(interactionPourBette3);
		InteractionPlantePlante interactionPourBette4 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("raifort"), insertedPlants.get("bette"));
		interactionPlantePlanteRepository.save(interactionPourBette4);
	}

	private void interactionPourArtichaut(Map<String, Plante> insertedPlants) {
		log.info("interactionPourArtichaut"); // Pour artichaut
		InteractionPlantePlante interactionPourArtichaut1 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("fève"), insertedPlants.get("artichaut"));
		interactionPlantePlanteRepository.save(interactionPourArtichaut1);
	}

	private void interactionPourPoivron(Map<String, Plante> insertedPlants) {
		log.info("interactionPourPoivron"); // Pour poivron
		InteractionPlantePlante interactionPourPoivron1 = new InteractionPlantePlante("+",
				"Le basilic s'associe parfaitement avec le genre capsicum", null, insertedPlants.get("basilic"),
				insertedPlants.get("poivron"));
		interactionPlantePlanteRepository.save(interactionPourPoivron1);
	}

	private void interactionPourPiment(Map<String, Plante> insertedPlants) {
		log.info("interactionPourPiment"); // Pour piment
		InteractionPlantePlante interactionPourPiment1 = new InteractionPlantePlante("+",
				"Le basilic s'associe parfaitement avec le genre capsicum", null, insertedPlants.get("basilic"),
				insertedPlants.get("piment"));
		interactionPlantePlanteRepository.save(interactionPourPiment1);
	}

	private void interactionPourRosier(Map<String, Plante> insertedPlants) {
		log.info("interactionPourRosier"); // Pour rosiers
		InteractionPlantePlante interactionPourRosier1 = new InteractionPlantePlante("+",
				"Les géraniums protègent les rosiers", null, insertedPlants.get("géranium des prés"),
				insertedPlants.get("rosier de france"));
		interactionPlantePlanteRepository.save(interactionPourRosier1);
		InteractionPlantePlante interactionPourRosier2 = new InteractionPlantePlante("+",
				"L'ail planté au pied des rosiersles rends plus beaux et résistants", null, insertedPlants.get("ail"),
				insertedPlants.get("rosier de france"));
		interactionPlantePlanteRepository.save(interactionPourRosier2);
		InteractionPlantePlante interactionPourRosier3 = new InteractionPlantePlante("+",
				"Les pucerons noirs des rosiers sont repoussés par la menthe verte ou poivrée", null,
				insertedPlants.get("menthe poivrée"), insertedPlants.get("rosier de france"));
		interactionPlantePlanteRepository.save(interactionPourRosier3);
	}

	private void interactionPourRadis(Map<String, Plante> insertedPlants) {
		log.info("interactionPourRadis"); // Pour radis
		InteractionPlantePlante interactionPourRadis1 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("épinard"), insertedPlants.get("radis"));
		interactionPlantePlanteRepository.save(interactionPourRadis1);
		InteractionPlantePlante interactionPourRadis2 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("menthe poivrée"), insertedPlants.get("radis"));
		interactionPlantePlanteRepository.save(interactionPourRadis2);
		InteractionPlantePlante interactionPourRadis3 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("concombre"), insertedPlants.get("radis"));
		interactionPlantePlanteRepository.save(interactionPourRadis3);
		InteractionPlantePlante interactionPourRadis4 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("grande capucine"), insertedPlants.get("radis"));
		interactionPlantePlanteRepository.save(interactionPourRadis4);
		InteractionPlantePlante interactionPourRadis5 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("carotte"), insertedPlants.get("radis"));
		interactionPlantePlanteRepository.save(interactionPourRadis5);
		InteractionPlantePlante interactionPourRadis6 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("laitue"), insertedPlants.get("radis"));
		interactionPlantePlanteRepository.save(interactionPourRadis6);
		InteractionPlantePlante interactionPourRadis7 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("pois"), insertedPlants.get("radis"));
		interactionPlantePlanteRepository.save(interactionPourRadis7);
	}

	private void interactionPourPois(Map<String, Plante> insertedPlants) {
		log.info("interactionPourPois"); // Pour pois
		InteractionPlantePlante interactionPourPois1 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("radis"), insertedPlants.get("pois"));
		interactionPlantePlanteRepository.save(interactionPourPois1);
		InteractionPlantePlante interactionPourPois2 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("carotte"), insertedPlants.get("pois"));
		interactionPlantePlanteRepository.save(interactionPourPois2);
		InteractionPlantePlante interactionPourPois3 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("concombre"), insertedPlants.get("pois"));
		interactionPlantePlanteRepository.save(interactionPourPois3);
		InteractionPlantePlante interactionPourPois4 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("maïs"), insertedPlants.get("pois"));
		interactionPlantePlanteRepository.save(interactionPourPois4);
		InteractionPlantePlante interactionPourPois5 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("pomme de terre"), insertedPlants.get("pois"));
		interactionPlantePlanteRepository.save(interactionPourPois5);
	}

	private void interactionPourPommier(Map<String, Plante> insertedPlants) {
		log.info("interactionPourPommier"); // Pour pommier
		InteractionPlantePlante interactionPourPommier1 = new InteractionPlantePlante("+",
				"La ciboulette se plante près des pommiers pour prévenir de la tavelure, de la gale et des chancres",
				null, insertedPlants.get("ciboulette"), insertedPlants.get("pommier"));
		interactionPlantePlanteRepository.save(interactionPourPommier1);
		InteractionPlantePlante interactionPourPommier2 = new InteractionPlantePlante("+",
				"Plantée au pied des pommiers, elle prévient contre le puceron lanigère", null,
				insertedPlants.get("grande capucine"), insertedPlants.get("pommier"));
		interactionPlantePlanteRepository.save(interactionPourPommier2);
	}

	private void interactionPourPoirier(Map<String, Plante> insertedPlants) {
		log.info("interactionPourPoirier"); // Pour poirier
		InteractionPlantePlante interactionPourPoirier1 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("sauge"), insertedPlants.get("poirier"));
		interactionPlantePlanteRepository.save(interactionPourPoirier1);
	}

	private void interactionPourPecher(Map<String, Plante> insertedPlants) {
		log.info("interactionPourPecher"); // Pour pecher
		InteractionPlantePlante interactionPourPecher1 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("tanaisie"), insertedPlants.get("pêcher"));
		interactionPlantePlanteRepository.save(interactionPourPecher1);
		InteractionPlantePlante interactionPourPecher2 = new InteractionPlantePlante("+",
				"L'ail planté aux pieds des pêchers pour protéger de la cloque", null, insertedPlants.get("ail"),
				insertedPlants.get("pêcher"));
		interactionPlantePlanteRepository.save(interactionPourPecher2);
		InteractionPlantePlante interactionPourPecher3 = new InteractionPlantePlante("+",
				"L'oignon planté aux pieds des pêchers pour protéger de la cloque", null, insertedPlants.get("oignon"),
				insertedPlants.get("pêcher"));
		interactionPlantePlanteRepository.save(interactionPourPecher3);
	}

	private void interactionPourPoireau(Map<String, Plante> insertedPlants) {
		log.info("interactionPourPoireau"); // Pour poireau
		InteractionPlantePlante interactionPourPoireau1 = new InteractionPlantePlante("+",
				"Les carottes contribues à la lutte contre la teigne du poireau", null, insertedPlants.get("carotte"),
				insertedPlants.get("poireau"));
		interactionPlantePlanteRepository.save(interactionPourPoireau1);
	}

	private void interactionPourOignon(Map<String, Plante> insertedPlants) {
		log.info("interactionPourOignon"); // Pour oignon
		InteractionPlantePlante interactionPourOignon1 = new InteractionPlantePlante("+",
				"La mouche de l’oignon est repoussée par les carottes. Leur odeur repousse les mouches.", null,
				insertedPlants.get("carotte"), insertedPlants.get("oignon"));
		interactionPlantePlanteRepository.save(interactionPourOignon1);
		InteractionPlantePlante interactionPourOignon2 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("fraisier des bois"), insertedPlants.get("oignon"));
		interactionPlantePlanteRepository.save(interactionPourOignon2);
	}

	private void interactionPourMelon(Map<String, Plante> insertedPlants) {
		log.info("interactionPourMelon"); // Pour melon
		InteractionPlantePlante interactionPourMelon1 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("origan"), insertedPlants.get("melon"));
		interactionPlantePlanteRepository.save(interactionPourMelon1);
	}

	private void interactionPourMais(Map<String, Plante> insertedPlants) {
		log.info("interactionPourMais"); // Pour mais
		InteractionPlantePlante interactionPourMais1 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("haricot"), insertedPlants.get("maïs"));
		interactionPlantePlanteRepository.save(interactionPourMais1);
		InteractionPlantePlante interactionPourMais2 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("pois"), insertedPlants.get("maïs"));
		interactionPlantePlanteRepository.save(interactionPourMais2);
		InteractionPlantePlante interactionPourMais3 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("concombre"), insertedPlants.get("maïs"));
		interactionPlantePlanteRepository.save(interactionPourMais3);
		InteractionPlantePlante interactionPourMais4 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("courge"), insertedPlants.get("maïs"));
		interactionPlantePlanteRepository.save(interactionPourMais4);
		InteractionPlantePlante interactionPourMais5 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("potiron"), insertedPlants.get("maïs"));
		interactionPlantePlanteRepository.save(interactionPourMais5);
	}

	private void interactionPourLaitue(Map<String, Plante> insertedPlants) {
		log.info("interactionPourLaitue"); // Pour laitue
		InteractionPlantePlante interactionPourLaitue1 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("fraisier des bois"), insertedPlants.get("laitue"));
		interactionPlantePlanteRepository.save(interactionPourLaitue1);
		InteractionPlantePlante interactionPourLaitue2 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("carotte"), insertedPlants.get("laitue"));
		interactionPlantePlanteRepository.save(interactionPourLaitue2);
		InteractionPlantePlante interactionPourLaitue3 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("radis"), insertedPlants.get("laitue"));
		interactionPlantePlanteRepository.save(interactionPourLaitue3);
	}

	private void interactionPourFraisier(Map<String, Plante> insertedPlants) {
		log.info("interactionPourFraisier"); // Pour fraisier
		InteractionPlantePlante interactionPourFraisier1 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("oignon"), insertedPlants.get("fraisier des bois"));
		interactionPlantePlanteRepository.save(interactionPourFraisier1);
		InteractionPlantePlante interactionPourFraisier2 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("bourrache"), insertedPlants.get("fraisier des bois"));
		interactionPlantePlanteRepository.save(interactionPourFraisier2);
		InteractionPlantePlante interactionPourFraisier3 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("épinard"), insertedPlants.get("fraisier des bois"));
		interactionPlantePlanteRepository.save(interactionPourFraisier3);
		InteractionPlantePlante interactionPourFraisier4 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("laitue"), insertedPlants.get("fraisier des bois"));
		interactionPlantePlanteRepository.save(interactionPourFraisier4);
	}

	private void interactionPourPotiron(Map<String, Plante> insertedPlants) {
		log.info("interactionPourPotiron"); // Pour potiron
		InteractionPlantePlante interactionPourPotiron1 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("grande capucine"), insertedPlants.get("potiron"));
		interactionPlantePlanteRepository.save(interactionPourPotiron1);
		InteractionPlantePlante interactionPourPotiron2 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("pomme de terre"), insertedPlants.get("potiron"));
		interactionPlantePlanteRepository.save(interactionPourPotiron2);
		InteractionPlantePlante interactionPourPotiron3 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("épinard"), insertedPlants.get("potiron"));
		interactionPlantePlanteRepository.save(interactionPourPotiron3);
		InteractionPlantePlante interactionPourPotiron4 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("fraisier des bois"), insertedPlants.get("potiron"));
		interactionPlantePlanteRepository.save(interactionPourPotiron4);
		InteractionPlantePlante interactionPourPotiron5 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("maïs"), insertedPlants.get("potiron"));
		interactionPlantePlanteRepository.save(interactionPourPotiron5);
		InteractionPlantePlante interactionPourPotiron6 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("radis"), insertedPlants.get("potiron"));
		interactionPlantePlanteRepository.save(interactionPourPotiron6);
	}

	private void interactionPourCourge(Map<String, Plante> insertedPlants) {
		log.info("interactionPourCourge"); // Pour courge
		InteractionPlantePlante interactionPourCourge1 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("grande capucine"), insertedPlants.get("courge"));
		interactionPlantePlanteRepository.save(interactionPourCourge1);
		InteractionPlantePlante interactionPourCourge2 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("pomme de terre"), insertedPlants.get("courge"));
		interactionPlantePlanteRepository.save(interactionPourCourge2);
		InteractionPlantePlante interactionPourCourge3 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("épinard"), insertedPlants.get("courge"));
		interactionPlantePlanteRepository.save(interactionPourCourge3);
		InteractionPlantePlante interactionPourCourge4 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("fraisier des bois"), insertedPlants.get("courge"));
		interactionPlantePlanteRepository.save(interactionPourCourge4);
		InteractionPlantePlante interactionPourCourge5 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("maïs"), insertedPlants.get("courge"));
		interactionPlantePlanteRepository.save(interactionPourCourge5);
		InteractionPlantePlante interactionPourCourge6 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("radis"), insertedPlants.get("courge"));
		interactionPlantePlanteRepository.save(interactionPourCourge6);
		InteractionPlantePlante interactionPourCourge7 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("bourrache"), insertedPlants.get("courge"));
		interactionPlantePlanteRepository.save(interactionPourCourge7);
		InteractionPlantePlante interactionPourCourge8 = new InteractionPlantePlante("+",
				"La capucine éloigne les punaises des courgettes et citrouilles", null,
				insertedPlants.get("grande capucine"), insertedPlants.get("courge"));
		interactionPlantePlanteRepository.save(interactionPourCourge8);
	}

	private void interactionPourAubergine(Map<String, Plante> insertedPlants) {
		log.info("interactionPourAubergine"); // Pour aubergine
		InteractionPlantePlante interactionPourAubergine1 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("haricot"), insertedPlants.get("aubergine"));
		interactionPlantePlanteRepository.save(interactionPourAubergine1);
		InteractionPlantePlante interactionPourAubergine2 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("basilic"), insertedPlants.get("aubergine"));
		interactionPlantePlanteRepository.save(interactionPourAubergine2);
	}

	private void interactionPourFramboisier(Map<String, Plante> insertedPlants) {
		log.info("interactionPourFramboisier");
		InteractionPlantePlante interactionPourFramboisier1 = new InteractionPlantePlante("+",
				"Le vers de framboisier est repoussé par la myosotis. Il empêche le vers de proliférer", null,
				insertedPlants.get("myosotis des champs"), insertedPlants.get("framboisier"));
		interactionPlantePlanteRepository.save(interactionPourFramboisier1);
	}

	private void interactionPourBetterave(Map<String, Plante> insertedPlants) {
		log.info("interactionPourBetterave"); // Pour betterave
		InteractionPlantePlante interactionPourBetterave1 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("coriandre"), insertedPlants.get("betterave"));
		interactionPlantePlanteRepository.save(interactionPourBetterave1);
		InteractionPlantePlante interactionPourBetterave2 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("chou commun"), insertedPlants.get("betterave"));
		interactionPlantePlanteRepository.save(interactionPourBetterave2);
		InteractionPlantePlante interactionPourBetterave3 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("haricot"), insertedPlants.get("betterave"));
		interactionPlantePlanteRepository.save(interactionPourBetterave3);
		InteractionPlantePlante interactionPourBetterave4 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("oignon"), insertedPlants.get("betterave"));
		interactionPlantePlanteRepository.save(interactionPourBetterave4);
		InteractionPlantePlante interactionPourBetterave5 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("laitue"), insertedPlants.get("betterave"));
		interactionPlantePlanteRepository.save(interactionPourBetterave5);
		InteractionPlantePlante interactionPourBetterave6 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("fraisier des bois"), insertedPlants.get("betterave"));
		interactionPlantePlanteRepository.save(interactionPourBetterave6);
		InteractionPlantePlante interactionPourBetterave7 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("chou-rave"), insertedPlants.get("betterave"));
		interactionPlantePlanteRepository.save(interactionPourBetterave7);
		InteractionPlantePlante interactionPourBetterave8 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("concombre"), insertedPlants.get("betterave"));
		interactionPlantePlanteRepository.save(interactionPourBetterave8);
	}

	private void interactionPourAsperge(Map<String, Plante> insertedPlants) {
		log.info("interactionPourAsperge"); // Pour asperge
		InteractionPlantePlante interactionPourAsperge1 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("tomate"), insertedPlants.get("asperge"));
		interactionPlantePlanteRepository.save(interactionPourAsperge1);
		InteractionPlantePlante interactionPourAsperge2 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("persil"), insertedPlants.get("asperge"));
		interactionPlantePlanteRepository.save(interactionPourAsperge2);
		InteractionPlantePlante interactionPourAsperge3 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("basilic"), insertedPlants.get("asperge"));
		interactionPlantePlanteRepository.save(interactionPourAsperge3);
		InteractionPlantePlante interactionPourAsperge4 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("haricot"), insertedPlants.get("asperge"));
		interactionPlantePlanteRepository.save(interactionPourAsperge4);
	}

	private void interactionPourTomate(Map<String, Plante> insertedPlants) {
		log.info("interactionPourTomate"); // Pour tomate
		InteractionPlantePlante interactionPourTomate1 = new InteractionPlantePlante("+",
				"Le persil rend la tomate plus résistante aux maladies", null, insertedPlants.get("persil"),
				insertedPlants.get("tomate"));
		interactionPlantePlanteRepository.save(interactionPourTomate1);
		InteractionPlantePlante interactionPourTomate2 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("oignon"), insertedPlants.get("tomate"));
		interactionPlantePlanteRepository.save(interactionPourTomate2);
		InteractionPlantePlante interactionPourTomate3 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("poireau"), insertedPlants.get("tomate"));
		interactionPlantePlanteRepository.save(interactionPourTomate3);
		InteractionPlantePlante interactionPourTomate4 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("oeillet d'inde"), insertedPlants.get("tomate"));
		interactionPlantePlanteRepository.save(interactionPourTomate4);
		InteractionPlantePlante interactionPourTomate5 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("carotte"), insertedPlants.get("tomate"));
		interactionPlantePlanteRepository.save(interactionPourTomate5);
		InteractionPlantePlante interactionPourTomate6 = new InteractionPlantePlante("+",
				"Le basilic rend la tomate plus résistante aux maladies", null, insertedPlants.get("basilic"),
				insertedPlants.get("tomate"));
		interactionPlantePlanteRepository.save(interactionPourTomate6);
		InteractionPlantePlante interactionPourTomate7 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("asperge"), insertedPlants.get("tomate"));
		interactionPlantePlanteRepository.save(interactionPourTomate7);
		InteractionPlantePlante interactionPourTomate8 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("souci officinal"), insertedPlants.get("tomate"));
		interactionPlantePlanteRepository.save(interactionPourTomate8);
		InteractionPlantePlante interactionPourTomate9 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("bourrache"), insertedPlants.get("tomate"));
		interactionPlantePlanteRepository.save(interactionPourTomate9);
		InteractionPlantePlante interactionPourTomate10 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("grande capucine"), insertedPlants.get("tomate"));
		interactionPlantePlanteRepository.save(interactionPourTomate10);
	}

	private void interactionPourPommeDeTerre(Map<String, Plante> insertedPlants) {
		log.info("interactionPourPommeDeTerre"); // Pour pommeDeTerre
		InteractionPlantePlante interactionPourPommeDeTerre1 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("chou commun"), insertedPlants.get("pomme de terre"));
		interactionPlantePlanteRepository.save(interactionPourPommeDeTerre1);
		InteractionPlantePlante interactionPourPommeDeTerre2 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("fève"), insertedPlants.get("pomme de terre"));
		interactionPlantePlanteRepository.save(interactionPourPommeDeTerre2);
		InteractionPlantePlante interactionPourPommeDeTerre3 = new InteractionPlantePlante("+",
				"Le doryphore de la pomme de terre est repoussé par le pois", null, insertedPlants.get("pois"),
				insertedPlants.get("pomme de terre"));
		interactionPlantePlanteRepository.save(interactionPourPommeDeTerre3);
		InteractionPlantePlante interactionPourPommeDeTerre4 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("courge"), insertedPlants.get("pomme de terre"));
		interactionPlantePlanteRepository.save(interactionPourPommeDeTerre4);
		InteractionPlantePlante interactionPourPommeDeTerre5 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("potiron"), insertedPlants.get("pomme de terre"));
		interactionPlantePlanteRepository.save(interactionPourPommeDeTerre5);
		InteractionPlantePlante interactionPourPommeDeTerre6 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("haricot"), insertedPlants.get("pomme de terre"));
		interactionPlantePlanteRepository.save(interactionPourPommeDeTerre6);
		InteractionPlantePlante interactionPourPommeDeTerre7 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("oeillet d'inde"), insertedPlants.get("pomme de terre"));
		interactionPlantePlanteRepository.save(interactionPourPommeDeTerre7);
		InteractionPlantePlante interactionPourPommeDeTerre8 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("souci officinal"), insertedPlants.get("pomme de terre"));
		interactionPlantePlanteRepository.save(interactionPourPommeDeTerre8);
		InteractionPlantePlante interactionPourPommeDeTerre9 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("concombre"), insertedPlants.get("pomme de terre"));
		interactionPlantePlanteRepository.save(interactionPourPommeDeTerre9);
		InteractionPlantePlante interactionPourPommeDeTerre10 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("bourrache"), insertedPlants.get("pomme de terre"));
		interactionPlantePlanteRepository.save(interactionPourPommeDeTerre10);
	}

	private void interactionPourConcombre(Map<String, Plante> insertedPlants) {
		log.info("interactionPourConcombre"); // Pour concombre
		InteractionPlantePlante interactionPourConcombre1 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("basilic"), insertedPlants.get("concombre"));
		interactionPlantePlanteRepository.save(interactionPourConcombre1);
		InteractionPlantePlante interactionPourConcombre2 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("chou commun"), insertedPlants.get("concombre"));
		interactionPlantePlanteRepository.save(interactionPourConcombre2);
		InteractionPlantePlante interactionPourConcombre3 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("haricot"), insertedPlants.get("concombre"));
		interactionPlantePlanteRepository.save(interactionPourConcombre3);
		InteractionPlantePlante interactionPourConcombre4 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("laitue"), insertedPlants.get("concombre"));
		interactionPlantePlanteRepository.save(interactionPourConcombre4);
		InteractionPlantePlante interactionPourConcombre5 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("maïs"), insertedPlants.get("concombre"));
		interactionPlantePlanteRepository.save(interactionPourConcombre5);
		InteractionPlantePlante interactionPourConcombre6 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("tournesol"), insertedPlants.get("concombre"));
		interactionPlantePlanteRepository.save(interactionPourConcombre6);
		InteractionPlantePlante interactionPourConcombre7 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("pomme de terre"), insertedPlants.get("concombre"));
		interactionPlantePlanteRepository.save(interactionPourConcombre7);
		InteractionPlantePlante interactionPourConcombre8 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("radis"), insertedPlants.get("concombre"));
		interactionPlantePlanteRepository.save(interactionPourConcombre8);
		InteractionPlantePlante interactionPourConcombre9 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("origan"), insertedPlants.get("concombre"));
		interactionPlantePlanteRepository.save(interactionPourConcombre9);
		InteractionPlantePlante interactionPourConcombre10 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("pois"), insertedPlants.get("concombre"));
		interactionPlantePlanteRepository.save(interactionPourConcombre10);
		InteractionPlantePlante interactionPourConcombre11 = new InteractionPlantePlante("+",
				"L’aneth protège les concombres", null, insertedPlants.get("aneth"), insertedPlants.get("concombre"));
		interactionPlantePlanteRepository.save(interactionPourConcombre11);
	}

	private void interactionPourHaricot(Map<String, Plante> insertedPlants) {
		log.info("interactionPourHaricot"); // Pour haricot
		InteractionPlantePlante interactionPourHaricot1 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("concombre"), insertedPlants.get("haricot"));
		interactionPlantePlanteRepository.save(interactionPourHaricot1);
		InteractionPlantePlante interactionPourHaricot2 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("laitue"), insertedPlants.get("haricot"));
		interactionPlantePlanteRepository.save(interactionPourHaricot2);
		InteractionPlantePlante interactionPourHaricot3 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("tomate"), insertedPlants.get("haricot"));
		interactionPlantePlanteRepository.save(interactionPourHaricot3);
		InteractionPlantePlante interactionPourHaricot4 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("carotte"), insertedPlants.get("haricot"));
		interactionPlantePlanteRepository.save(interactionPourHaricot4);
		InteractionPlantePlante interactionPourHaricot5 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("aubergine"), insertedPlants.get("haricot"));
		interactionPlantePlanteRepository.save(interactionPourHaricot5);
		InteractionPlantePlante interactionPourHaricot6 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("oeillet d'inde"), insertedPlants.get("haricot"));
		interactionPlantePlanteRepository.save(interactionPourHaricot6);
		InteractionPlantePlante interactionPourHaricot7 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("souci officinal"), insertedPlants.get("haricot"));
		interactionPlantePlanteRepository.save(interactionPourHaricot7);
		InteractionPlantePlante interactionPourHaricot8 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("betterave"), insertedPlants.get("haricot"));
		interactionPlantePlanteRepository.save(interactionPourHaricot8);
		InteractionPlantePlante interactionPourHaricot9 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("maïs"), insertedPlants.get("haricot"));
		interactionPlantePlanteRepository.save(interactionPourHaricot9);
		InteractionPlantePlante interactionPourHaricot10 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("pomme de terre"), insertedPlants.get("haricot"));
		interactionPlantePlanteRepository.save(interactionPourHaricot10);
	}

	private void interactionPourChouCommun(Map<String, Plante> insertedPlants) {
		log.info("interactionPourChouCommun"); // Pour chouCommun
		InteractionPlantePlante interactionPourChouCommun1 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("sarriette"), insertedPlants.get("chou commun"));
		interactionPlantePlanteRepository.save(interactionPourChouCommun1);
		InteractionPlantePlante interactionPourChouCommun2 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("betterave"), insertedPlants.get("chou commun"));
		interactionPlantePlanteRepository.save(interactionPourChouCommun2);
		InteractionPlantePlante interactionPourChouCommun3 = new InteractionPlantePlante("+",
				"La piéride du chou est repoussée par la tomate", null, insertedPlants.get("tomate"),
				insertedPlants.get("chou commun"));
		interactionPlantePlanteRepository.save(interactionPourChouCommun3);
		InteractionPlantePlante interactionPourChouCommun4 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("romarin"), insertedPlants.get("chou commun"));
		interactionPlantePlanteRepository.save(interactionPourChouCommun4);
		InteractionPlantePlante interactionPourChouCommun5 = new InteractionPlantePlante("+",
				"La menthe protège les choux des papillons", null, insertedPlants.get("menthe poivrée"),
				insertedPlants.get("chou commun"));
		interactionPlantePlanteRepository.save(interactionPourChouCommun5);
		InteractionPlantePlante interactionPourChouCommun6 = new InteractionPlantePlante("+",
				"La sauge protège les choux des papillons", null, insertedPlants.get("sauge"),
				insertedPlants.get("chou commun"));
		interactionPlantePlanteRepository.save(interactionPourChouCommun6);
		InteractionPlantePlante interactionPourChouCommun7 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("thym commun"), insertedPlants.get("chou commun"));
		interactionPlantePlanteRepository.save(interactionPourChouCommun7);
		InteractionPlantePlante interactionPourChouCommun8 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("grande capucine"), insertedPlants.get("chou commun"));
		interactionPlantePlanteRepository.save(interactionPourChouCommun8);
		InteractionPlantePlante interactionPourChouCommun9 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("pomme de terre"), insertedPlants.get("chou commun"));
		interactionPlantePlanteRepository.save(interactionPourChouCommun9);
		InteractionPlantePlante interactionPourChouCommun10 = new InteractionPlantePlante("+",
				"L'hysope empêche les mouches blanche de pondre dans les choux", null, insertedPlants.get("hysope"),
				insertedPlants.get("chou commun"));
		interactionPlantePlanteRepository.save(interactionPourChouCommun10);
		InteractionPlantePlante interactionPourChouCommun11 = new InteractionPlantePlante("+",
				"La piéride du chou est repoussée par le céleri", null, insertedPlants.get("céleri"),
				insertedPlants.get("chou commun"));
		interactionPlantePlanteRepository.save(interactionPourChouCommun11);
		InteractionPlantePlante interactionPourChouCommun12 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("bourrache"), insertedPlants.get("chou commun"));
		interactionPlantePlanteRepository.save(interactionPourChouCommun12);
		InteractionPlantePlante interactionPourChouCommun13 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("oeillet d'inde"), insertedPlants.get("chou commun"));
		interactionPlantePlanteRepository.save(interactionPourChouCommun13);
		InteractionPlantePlante interactionPourChouCommun14 = new InteractionPlantePlante("+",
				"La piéride du chou n'aime pas l'odeur de la tomate. "
						+ "L'effet protecteur est renforcé lorsqu'on met entre les plantes menacées, les gourmands des tomates.",
				null, insertedPlants.get("tomate"), insertedPlants.get("chou commun"));
		interactionPlantePlanteRepository.save(interactionPourChouCommun14);
		InteractionPlantePlante interactionPourChouCommun15 = new InteractionPlantePlante("+",
				"La piéride du chou n'aime pas l'odeur du céleri", null, insertedPlants.get("céleri"),
				insertedPlants.get("chou commun"));
		interactionPlantePlanteRepository.save(interactionPourChouCommun15);
	}

	private void interactionPourCarotte(Map<String, Plante> insertedPlants) {
		log.info("interactionPourCarotte"); // Pour carotte
		InteractionPlantePlante interactionPourCarotte1 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("ail"), insertedPlants.get("carotte"));

		interactionPlantePlanteRepository.save(interactionPourCarotte1);

		InteractionPlantePlante interactionPourCarotte2 = new InteractionPlantePlante("+",
				"L’aneth protège les carottes et aide à la levée", null, insertedPlants.get("aneth"),
				insertedPlants.get("carotte"));
		interactionPlantePlanteRepository.save(interactionPourCarotte2);

		InteractionPlantePlante interactionPourCarotte3 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("échalotte"), insertedPlants.get("carotte"));
		interactionPlantePlanteRepository.save(interactionPourCarotte3);
		InteractionPlantePlante interactionPourCarotte4 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("poireau"), insertedPlants.get("carotte"));
		interactionPlantePlanteRepository.save(interactionPourCarotte4);
		InteractionPlantePlante interactionPourCarotte5 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("tomate"), insertedPlants.get("carotte"));
		interactionPlantePlanteRepository.save(interactionPourCarotte5);
		InteractionPlantePlante interactionPourCarotte6 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("laitue"), insertedPlants.get("carotte"));
		interactionPlantePlanteRepository.save(interactionPourCarotte6);
		InteractionPlantePlante interactionPourCarotte7 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("ciboulette"), insertedPlants.get("carotte"));
		interactionPlantePlanteRepository.save(interactionPourCarotte7);
		InteractionPlantePlante interactionPourCarotte8 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("radis"), insertedPlants.get("carotte"));
		interactionPlantePlanteRepository.save(interactionPourCarotte8);
		InteractionPlantePlante interactionPourCarotte9 = new InteractionPlantePlante("+",
				"L'oignon repousse la mouche de la carotte", null, insertedPlants.get("oignon"),
				insertedPlants.get("carotte"));
		interactionPlantePlanteRepository.save(interactionPourCarotte9);
		InteractionPlantePlante interactionPourCarotte10 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("pois"), insertedPlants.get("carotte"));
		interactionPlantePlanteRepository.save(interactionPourCarotte10);
		InteractionPlantePlante interactionPourCarotte11 = new InteractionPlantePlante("+",
				"Le poireau repousse la mouche de la carotte", null, insertedPlants.get("poireau"),
				insertedPlants.get("carotte"));
		interactionPlantePlanteRepository.save(interactionPourCarotte11);
		InteractionPlantePlante interactionPourCarotte12 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("carotte"), insertedPlants.get("carotte"));
		interactionPlantePlanteRepository.save(interactionPourCarotte12);
		InteractionPlantePlante interactionPourCarotte13 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("carotte"), insertedPlants.get("carotte"));
		interactionPlantePlanteRepository.save(interactionPourCarotte13);
		InteractionPlantePlante interactionPourCarotte14 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("romarin"), insertedPlants.get("carotte"));
		interactionPlantePlanteRepository.save(interactionPourCarotte14);
		InteractionPlantePlante interactionPourCarotte15 = new InteractionPlantePlante("+", "", null,
				insertedPlants.get("scorsonère des prés"), insertedPlants.get("carotte"));
		interactionPlantePlanteRepository.save(interactionPourCarotte15);
	}

	private void insertVitesseCroissance() {
		try {
			VitesseCroissance tresLente = new VitesseCroissance("tresLente");
			VitesseCroissance lente = new VitesseCroissance("lente");
			VitesseCroissance vcNormale = new VitesseCroissance("normale");
			VitesseCroissance rapide = new VitesseCroissance("rapide");
			VitesseCroissance tresRapide = new VitesseCroissance("tresRapide");
			vitesseCroissanceRepository.save(tresLente);
			vitesseCroissanceRepository.save(lente);
			vitesseCroissanceRepository.save(vcNormale);
			vitesseCroissanceRepository.save(rapide);
			vitesseCroissanceRepository.save(tresRapide);
		} catch (Exception e) {
			log.error("Error when trying to insert in table VitesseCroissance. {" + e.getMessage() + "}");
		}
	}

	private void insertEnsoleillement() {
		try {
			Ensoleillement soleil = new Ensoleillement("soleil");
			Ensoleillement miOmbre = new Ensoleillement("miOmbre");
			Ensoleillement ombre = new Ensoleillement("ombre");
			ensoleillementRepository.save(soleil);
			ensoleillementRepository.save(miOmbre);
			ensoleillementRepository.save(ombre);
		} catch (Exception e) {
			log.error("Error when trying to insert in table Ensoleillement. {" + e.getMessage() + "}");
		}
	}

	private void insertRichesseSol() {
		try {
			RichesseSol tresPauvre = new RichesseSol("tresPauvre");
			RichesseSol pauvre = new RichesseSol("pauvre");
			RichesseSol normale = new RichesseSol("normale");
			RichesseSol riche = new RichesseSol("riche");
			RichesseSol tresRiche = new RichesseSol("tresRiche");
			richesseSolRepository.save(tresPauvre);
			richesseSolRepository.save(pauvre);
			richesseSolRepository.save(normale);
			richesseSolRepository.save(riche);
			richesseSolRepository.save(tresRiche);
		} catch (Exception e) {
			log.error("Error when trying to insert in table RichesseSol. {" + e.getMessage() + "}");
		}
	}

	private void insertTypeTerre() {
		try {
			TypeTerre argileuse = new TypeTerre("argileuse");
			TypeTerre calcaire = new TypeTerre("calcaire");
			TypeTerre humifere = new TypeTerre("humifere");
			TypeTerre sableuse = new TypeTerre("sableuse");
			typeTerreRepository.save(argileuse);
			typeTerreRepository.save(calcaire);
			typeTerreRepository.save(humifere);
			typeTerreRepository.save(sableuse);
		} catch (Exception e) {
			log.error("Error when trying to insert in table TypeTerre. {" + e.getMessage() + "}");
		}
	}

	private void insertTypeFeuillage() {
		try {
			TypeFeuillage persistant = new TypeFeuillage("persistant");
			TypeFeuillage semiPersistant = new TypeFeuillage("semiPersistant");
			TypeFeuillage marcescent = new TypeFeuillage("marcescent");
			TypeFeuillage caduc = new TypeFeuillage("caduc");
			typeFeuillageRepository.save(persistant);
			typeFeuillageRepository.save(semiPersistant);
			typeFeuillageRepository.save(marcescent);
			typeFeuillageRepository.save(caduc);
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
			TypeRacine pivotante = new TypeRacine("pivotante");
			TypeRacine fasciculaire = new TypeRacine("fasciculaire");
			TypeRacine adventice = new TypeRacine("adventice");
			TypeRacine tracante = new TypeRacine("tracante");
			TypeRacine contrefort = new TypeRacine("contrefort");
			TypeRacine crampon = new TypeRacine("crampon");
			TypeRacine echasse = new TypeRacine("echasse");
			TypeRacine aerienne = new TypeRacine("aerienne");
			TypeRacine liane = new TypeRacine("liane");
			TypeRacine ventouse = new TypeRacine("ventouse");
			TypeRacine pneumatophore = new TypeRacine("pneumatophore");
			typeRacineRepository.save(pivotante);
			typeRacineRepository.save(fasciculaire);
			typeRacineRepository.save(adventice);
			typeRacineRepository.save(tracante);
			typeRacineRepository.save(contrefort);
			typeRacineRepository.save(crampon);
			typeRacineRepository.save(echasse);
			typeRacineRepository.save(aerienne);
			typeRacineRepository.save(liane);
			typeRacineRepository.save(ventouse);
			typeRacineRepository.save(pneumatophore);
		} catch (Exception e) {
			log.error("Error when trying to insert in table TypeRacine. {" + e.getMessage() + "}");
		}
	}

	private void insertMois() {
		try {
			Mois janvier = new Mois("Janvier");
			Mois fevrier = new Mois("fevrier");
			Mois mars = new Mois("mars");
			Mois avril = new Mois("avril");
			Mois mai = new Mois("mai");
			Mois juin = new Mois("juin");
			Mois juillet = new Mois("juillet");
			Mois aout = new Mois("aout");
			Mois septembre = new Mois("septembre");
			Mois octobre = new Mois("octobre");
			Mois novembre = new Mois("novembre");
			Mois decembre = new Mois("decembre");
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
		Ordre ordre = new Ordre(data.get("Ordre"));
		Famille famille = new Famille(data.get("Famille"));
		Genre genre = new Genre(data.get("Genre"));
		Espece espece = new Espece(data.get("Espece"));
		String plantName = data.get("Plante");

		Plante plante;
		try {
			if (!ordreRepository.exists(Example.of(ordre))) {
				ordreRepository.save(ordre);
			} else {
				Optional<Ordre> returned = ordreRepository.findOne(Example.of(ordre));
				if (returned.isPresent()) {
					ordre = returned.get();
					log.info("Existing order : " + ordre.toString());
				} else {
					log.error("Unable to get instance of : " + ordre.toString());
				}

			}

			if (!familleRepository.exists(Example.of(famille))) {
				familleRepository.save(famille);
			} else {
				Optional<Famille> returned = familleRepository.findOne(Example.of(famille));
				if (returned.isPresent()) {
					famille = returned.get();
					log.info("Existing familly : " + famille.toString());
				} else {
					log.error("Unable to get instance of : " + famille.toString());
				}

			}

			if (!genreRepository.exists(Example.of(genre))) {
				genreRepository.save(genre);
			} else {
				Optional<Genre> returned = genreRepository.findOne(Example.of(genre));
				if (returned.isPresent()) {
					genre = returned.get();
					log.info("Existing genre : " + genre.toString());
				} else {
					log.error("Unable to get instance of : " + genre.toString());
				}

			}

			if (!especeRepository.exists(Example.of(espece))) {
				especeRepository.save(espece);
			} else {
				Optional<Espece> returned = especeRepository.findOne(Example.of(espece));
				if (returned.isPresent()) {
					espece = returned.get();
					log.info("Existing espece : " + espece.toString());
				} else {
					log.error("Unable to get instance of : " + espece.toString());
				}

			}

			ClassificationCronquist classificationCronquist = new ClassificationCronquist(ordre, famille, genre,
					espece);
			if (!classificationCronquistRepository.exists(Example.of(classificationCronquist))) {
				classificationCronquistRepository.save(classificationCronquist);
			} else {
				Optional<ClassificationCronquist> returned = classificationCronquistRepository
						.findOne(Example.of(classificationCronquist));
				if (returned.isPresent()) {
					classificationCronquist = returned.get();
					log.info("Existing classification : " + classificationCronquist.toString());
				} else {
					log.error("Unable to get instance of : " + classificationCronquist.toString());
				}
			}
			plante = new Plante(null, null, null, null, plantName, classificationCronquist, null, null, null, null,
					null, null, null, null, null);
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
			log.error("Unable to insert {" + plantName + "}");
		}
		return null;
	}
}
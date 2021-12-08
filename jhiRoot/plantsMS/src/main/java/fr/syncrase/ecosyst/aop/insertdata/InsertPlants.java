package fr.syncrase.ecosyst.aop.insertdata;

import fr.syncrase.ecosyst.domain.Classification;
import fr.syncrase.ecosyst.domain.Cronquist;
import fr.syncrase.ecosyst.domain.NomVernaculaire;
import fr.syncrase.ecosyst.domain.Plante;
import fr.syncrase.ecosyst.repository.ClassificationRepository;
import fr.syncrase.ecosyst.repository.CronquistRepository;
import fr.syncrase.ecosyst.repository.NomVernaculaireRepository;
import fr.syncrase.ecosyst.repository.PlanteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InsertPlants {

    private final Logger log = LoggerFactory.getLogger(InsertPlants.class);

    private final ClassificationRepository classificationRepository;
    private final CronquistRepository cronquistRepository;
    private final NomVernaculaireRepository nomVernaculaireRepository;
    private final PlanteRepository planteRepository;

    public InsertPlants(ClassificationRepository classificationRepository, CronquistRepository cronquistRepository, NomVernaculaireRepository nomVernaculaireRepository, PlanteRepository planteRepository) {
        this.classificationRepository = classificationRepository;
        this.cronquistRepository = cronquistRepository;
        this.nomVernaculaireRepository = nomVernaculaireRepository;
        this.planteRepository = planteRepository;
    }

    /**
     * insert all plants in database and
     */
    protected Map<String, Plante> insertAllPlants() {
        Map<String, Plante> insertedPlants = new HashMap<>();
        Map<String, String> plantAttributes = new HashMap<>();

        plantAttributes.put("Ordre", "apiales");
        plantAttributes.put("Famille", "apiaceae");
        plantAttributes.put("Genre", "daucus");
        plantAttributes.put("Espece", "daucus carota");
        plantAttributes.put("Plante", "carotte");
        insertedPlants.put("carotte", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "carotte jaune du doubs");
        insertedPlants.put("carotte jaune du doubs", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "apiales");
        plantAttributes.put("Famille", "apiaceae");
        plantAttributes.put("Genre", "anethum");
        plantAttributes.put("Espece", "anethum graveolens");
        plantAttributes.put("Plante", "aneth");
        insertedPlants.put("aneth", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "capparales");
        plantAttributes.put("Famille", "brassicaceae");
        plantAttributes.put("Genre", "lepidium");
        plantAttributes.put("Espece", "lepidium sativum");
        plantAttributes.put("Plante", "cresson");
        insertedPlants.put("cresson", insertPlant(plantAttributes));

        plantAttributes.put("Genre", "raphanus");
        plantAttributes.put("Espece", "raphanus sativus");
        plantAttributes.put("Plante", "radis");
        insertedPlants.put("radis", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "radis 18 jours");
        insertedPlants.put("radis 18 jours", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "radis kocto hf1");
        insertedPlants.put("radis kocto hf1", insertPlant(plantAttributes));

        plantAttributes.put("Genre", "armoracia");
        plantAttributes.put("Espece", "armoracia rusticana");
        plantAttributes.put("Plante", "raifort");
        insertedPlants.put("raifort", insertPlant(plantAttributes));

        plantAttributes.put("Genre", "brassica");
        plantAttributes.put("Espece", "brassica oleracea");
        plantAttributes.put("Plante", "chou commun");
        insertedPlants.put("chou commun", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "chou rave");
        insertedPlants.put("chou rave", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "brocoli");
        insertedPlants.put("brocoli", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "chou fleur");
        insertedPlants.put("chou fleur", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "chou cabus");
        insertedPlants.put("chou cabus", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "chou cabus coeur de boeuf des vertus");
        insertedPlants.put("chou cabus coeur de boeuf des vertus", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "chou fleur merveille de toutes saisons");
        insertedPlants.put("chou fleur merveille de toutes saisons", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "navet rouge plat hâtif à feuille entière");
        insertedPlants.put("navet rouge plat hâtif à feuille entière", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "chou de chine");
        insertedPlants.put("chou de chine", insertPlant(plantAttributes));

        plantAttributes.put("Genre", "eruca");
        plantAttributes.put("Espece", "eruca sativa");
        plantAttributes.put("Plante", "roquette");
        insertedPlants.put("roquette", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "roquette cultivée");
        insertedPlants.put("roquette cultivée", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "roquette sauvage");
        insertedPlants.put("roquette sauvage", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "lamiales");
        plantAttributes.put("Famille", "lamiaceae");
        plantAttributes.put("Genre", "satureja");
        plantAttributes.put("Espece", "satureja hortensis");
        plantAttributes.put("Plante", "sarriette");
        insertedPlants.put("sarriette", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "sarriette commune");
        insertedPlants.put("sarriette commune", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "caryophyllales");
        plantAttributes.put("Famille", "chenopodiaceae");
        plantAttributes.put("Genre", "beta");
        plantAttributes.put("Espece", "beta vulgaris");
        plantAttributes.put("Plante", "betterave");
        insertedPlants.put("betterave", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "bette");
        insertedPlants.put("bette", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "betterave de détroit améliorée 3");
        insertedPlants.put("betterave de détroit améliorée 3", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "solanales");
        plantAttributes.put("Famille", "solanaceae");
        plantAttributes.put("Genre", "solanum");
        plantAttributes.put("Espece", "solanum lycopersicum");
        plantAttributes.put("Plante", "tomate");
        insertedPlants.put("tomate", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "tomate noire russe");
        insertedPlants.put("tomate noire russe", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "tomate beefsteak");
        insertedPlants.put("tomate beefsteak", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "asterales");
        plantAttributes.put("Famille", "asteraceae");
        plantAttributes.put("Genre", "lactuca");
        plantAttributes.put("Espece", "lactuca sativa");
        plantAttributes.put("Plante", "laitue");
        insertedPlants.put("laitue", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "laitue rouge grenobloise");
        insertedPlants.put("laitue rouge grenobloise", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "laitue reine de mai de pleine terre");
        insertedPlants.put("laitue reine de mai de pleine terre", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "laitue batavia lollo rossa");
        insertedPlants.put("laitue batavia lollo rossa", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "laitue pommée grosse blonde paresseuse");
        insertedPlants.put("laitue pommée grosse blonde paresseuse", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "laitue pommée reine de mai");
        insertedPlants.put("laitue pommée reine de mai", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "laitue batavia iceberg reine des glaces");
        insertedPlants.put("laitue batavia iceberg reine des glaces", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "laitue cressonnette marocaine");
        insertedPlants.put("laitue cressonnette marocaine", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "laitue à couper");
        insertedPlants.put("laitue à couper", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "violales");
        plantAttributes.put("Famille", "cucurbitaceae");
        plantAttributes.put("Genre", "cucumis");
        plantAttributes.put("Espece", "cucumis sativus");
        plantAttributes.put("Plante", "concombre");
        insertedPlants.put("concombre", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "concombre vert long maraîcher");
        insertedPlants.put("concombre vert long maraîcher", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "lamiales");
        plantAttributes.put("Famille", "lamiaceae");
        plantAttributes.put("Genre", "ocimum");
        plantAttributes.put("Espece", "ocimum basilicum");
        plantAttributes.put("Plante", "basilic");
        insertedPlants.put("basilic", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "fabales");
        plantAttributes.put("Famille", "fabaceae");
        plantAttributes.put("Genre", "phaseolus");
        plantAttributes.put("Espece", "phaseolus vulgaris");
        plantAttributes.put("Plante", "haricot");
        insertedPlants.put("haricot", insertPlant(plantAttributes));

        plantAttributes.put("Genre", "pisum");
        plantAttributes.put("Espece", "pisum sativum");
        plantAttributes.put("Plante", "pois");
        insertedPlants.put("pois", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "pois nain norli grain rond");
        insertedPlants.put("pois nain norli grain rond", insertPlant(plantAttributes));

        plantAttributes.put("Genre", "vicia");
        plantAttributes.put("Espece", "vicia faba");
        plantAttributes.put("Plante", "fève");
        insertedPlants.put("fève", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "fève aguadulce à très longue cosse");
        insertedPlants.put("fève aguadulce à très longue cosse", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "solanales");
        plantAttributes.put("Famille", "solanaceae");
        plantAttributes.put("Genre", "solanum");
        plantAttributes.put("Espece", "solanum tuberosum");
        plantAttributes.put("Plante", "pomme de terre");
        insertedPlants.put("pomme de terre", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "apiales");
        plantAttributes.put("Famille", "apiaceae");
        plantAttributes.put("Genre", "petroselinum");
        plantAttributes.put("Espece", "petroselinum crispum");
        plantAttributes.put("Plante", "persil");
        insertedPlants.put("persil", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "persil frisé vert foncé ou double");
        insertedPlants.put("persil frisé vert foncé ou double", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "apiales");
        plantAttributes.put("Famille", "apiaceae");
        plantAttributes.put("Genre", "coriandrum");
        plantAttributes.put("Espece", "coriandrum sativum");
        plantAttributes.put("Plante", "coriandre");
        insertedPlants.put("coriandre", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "cyperales");
        plantAttributes.put("Famille", "poaceae");
        plantAttributes.put("Genre", "zea");
        plantAttributes.put("Espece", "zea mays");
        plantAttributes.put("Plante", "maïs");
        insertedPlants.put("maïs", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "maïs doux bantam");
        insertedPlants.put("maïs doux bantam", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "violales");
        plantAttributes.put("Famille", "cucurbitaceae");
        plantAttributes.put("Genre", "cucurbita");
        plantAttributes.put("Espece", "cucurbita maxima");
        plantAttributes.put("Plante", "courge");
        insertedPlants.put("courge", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "courge pâtisson blanc");
        insertedPlants.put("courge pâtisson blanc", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "potiron");
        insertedPlants.put("potiron", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "potiron petit bonnet turc");
        insertedPlants.put("potiron petit bonnet turc", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "courgette");
        insertedPlants.put("courgette", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "rosales");
        plantAttributes.put("Famille", "rosaceae");
        plantAttributes.put("Genre", "rubus");
        plantAttributes.put("Espece", "rubus idaeus");
        plantAttributes.put("Plante", "framboisier");
        insertedPlants.put("framboisier", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "lamiales");
        plantAttributes.put("Famille", "boraginaceae");
        plantAttributes.put("Genre", "myosotis");
        plantAttributes.put("Espece", "myosotis arvensis");
        plantAttributes.put("Plante", "myosotis des champs");
        insertedPlants.put("myosotis des champs", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "geraniales");
        plantAttributes.put("Famille", "tropaeolaceae");
        plantAttributes.put("Genre", "tropaeolum");
        plantAttributes.put("Espece", "tropaeolum majus");
        plantAttributes.put("Plante", "grande capucine");
        insertedPlants.put("grande capucine", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "asterales");
        plantAttributes.put("Famille", "asteraceae");
        plantAttributes.put("Genre", "tagetes");
        plantAttributes.put("Espece", "tagetes patula");
        plantAttributes.put("Plante", "oeillet d'inde");
        insertedPlants.put("oeillet d'inde", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "liliales");
        plantAttributes.put("Famille", "liliaceae");
        plantAttributes.put("Genre", "allium");
        plantAttributes.put("Espece", "allium sativum");
        plantAttributes.put("Plante", "ail");
        insertedPlants.put("ail", insertPlant(plantAttributes));

        plantAttributes.put("Espece", "allium cepa");
        plantAttributes.put("Plante", "échalote");
        insertedPlants.put("échalote", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "oignon");
        insertedPlants.put("oignon", insertPlant(plantAttributes));

        plantAttributes.put("Espece", "allium porrum");
        plantAttributes.put("Plante", "poireau");
        insertedPlants.put("poireau", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "poireau monstrueux de carentan");
        insertedPlants.put("poireau monstrueux de carentan", insertPlant(plantAttributes));

        plantAttributes.put("Espece", "allium schoenoprasum");
        plantAttributes.put("Plante", "ciboulette");
        insertedPlants.put("ciboulette", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "ciboulette commune");
        insertedPlants.put("ciboulette commune", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "civette");
        insertedPlants.put("civette", insertPlant(plantAttributes));

        plantAttributes.put("Espece", "allium tuberosum");
        plantAttributes.put("Plante", "ciboule de chine");
        insertedPlants.put("ciboule de chine", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "rosales");
        plantAttributes.put("Famille", "rosaceae");
        plantAttributes.put("Genre", "rosa");
        plantAttributes.put("Espece", "rosa gallica");
        plantAttributes.put("Plante", "rosier de france");
        insertedPlants.put("rosier de france", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "lamiales");
        plantAttributes.put("Famille", "lamiaceae");
        plantAttributes.put("Genre", "rosmarinus");
        plantAttributes.put("Espece", "rosmarinus officinalis");
        plantAttributes.put("Plante", "romarin");
        insertedPlants.put("romarin", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "lamiales");
        plantAttributes.put("Famille", "lamiaceae");
        plantAttributes.put("Genre", "thymus");
        plantAttributes.put("Espece", "thymus vulgaris");
        plantAttributes.put("Plante", "thym commun");
        insertedPlants.put("thym commun", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "thym ordinaire");
        insertedPlants.put("thym ordinaire", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "liliales");
        plantAttributes.put("Famille", "liliaceae");
        plantAttributes.put("Genre", "asparagus");
        plantAttributes.put("Espece", "asparagus officinalis");
        plantAttributes.put("Plante", "asperge");
        insertedPlants.put("asperge", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "solanales");
        plantAttributes.put("Famille", "solanaceae");
        plantAttributes.put("Genre", "solanum");
        plantAttributes.put("Espece", "solanum melongena");
        plantAttributes.put("Plante", "aubergine");
        insertedPlants.put("aubergine", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "aubergine violetta di firenze");
        insertedPlants.put("aubergine violetta di firenze", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "lamiales");
        plantAttributes.put("Famille", "lamiaceae");
        plantAttributes.put("Genre", "salvia");
        plantAttributes.put("Espece", "alvia officinalis");
        plantAttributes.put("Plante", "sauge");
        insertedPlants.put("sauge", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "sauge officinale");
        insertedPlants.put("sauge officinale", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "lamiales");
        plantAttributes.put("Famille", "lamiaceae");
        plantAttributes.put("Genre", "mentha");
        plantAttributes.put("Espece", "mentha xpiperita");
        plantAttributes.put("Plante", "menthe poivrée");
        insertedPlants.put("menthe poivrée", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "lamiales");
        plantAttributes.put("Famille", "lamiaceae");
        plantAttributes.put("Genre", "origanum");
        plantAttributes.put("Espece", "origanum vulgare");
        plantAttributes.put("Plante", "origan");
        insertedPlants.put("origan", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "origan marjolaine");
        insertedPlants.put("origan marjolaine", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "marjolaine vivace");
        insertedPlants.put("marjolaine vivace", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "asterales");
        plantAttributes.put("Famille", "asteraceae");
        plantAttributes.put("Genre", "helianthus");
        plantAttributes.put("Espece", "helianthus annuu");
        plantAttributes.put("Plante", "tournesol");
        insertedPlants.put("tournesol", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "rosales");
        plantAttributes.put("Famille", "rosaceae");
        plantAttributes.put("Genre", "fragaria");
        plantAttributes.put("Espece", "fragaria vesca");
        plantAttributes.put("Plante", "fraisier des bois");
        insertedPlants.put("fraisier des bois", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "caryophyllales");
        plantAttributes.put("Famille", "chenopodiaceae");
        plantAttributes.put("Genre", "spinacia");
        plantAttributes.put("Espece", "spinacia oleracea");
        plantAttributes.put("Plante", "épinard");
        insertedPlants.put("épinard", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "épinard d'été matador");
        insertedPlants.put("épinard d'été matador", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "épinard géant d'hiver");
        insertedPlants.put("épinard géant d'hiver", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "lamiales");
        plantAttributes.put("Famille", "boraginaceae");
        plantAttributes.put("Genre", "borago");
        plantAttributes.put("Espece", "borago officinalis");
        plantAttributes.put("Plante", "bourrache");
        insertedPlants.put("bourrache", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "asterales");
        plantAttributes.put("Famille", "asteraceae");
        plantAttributes.put("Genre", "calendula");
        plantAttributes.put("Espece", "calendula officinalis");
        plantAttributes.put("Plante", "souci officinal");
        insertedPlants.put("souci officinal", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "violales");
        plantAttributes.put("Famille", "cucurbitaceae");
        plantAttributes.put("Genre", "cucumis");
        plantAttributes.put("Espece", "cucumis melo");
        plantAttributes.put("Plante", "melon");
        insertedPlants.put("melon", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "melon ancien vieille france");
        insertedPlants.put("melon ancien vieille france", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "asterales");
        plantAttributes.put("Famille", "asteraceae");
        plantAttributes.put("Genre", "tanacetum");
        plantAttributes.put("Espece", "tanacetum vulgare");
        plantAttributes.put("Plante", "tanaisie");
        insertedPlants.put("tanaisie", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "rosales");
        plantAttributes.put("Famille", "rosaceae");
        plantAttributes.put("Genre", "prunus");
        plantAttributes.put("Espece", "prunus persica");
        plantAttributes.put("Plante", "pêcher");
        insertedPlants.put("pêcher", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "rosales");
        plantAttributes.put("Famille", "rosaceae");
        plantAttributes.put("Genre", "pyrus");
        plantAttributes.put("Espece", "pyrus communis");
        plantAttributes.put("Plante", "poirier");
        insertedPlants.put("poirier", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "rosales");
        plantAttributes.put("Famille", "rosaceae");
        plantAttributes.put("Genre", "malus");
        plantAttributes.put("Espece", "malus domestica");
        plantAttributes.put("Plante", "pommier");
        insertedPlants.put("pommier", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "fagales");
        plantAttributes.put("Famille", "betulaceae");
        plantAttributes.put("Genre", "betula");
        plantAttributes.put("Espece", "betula pendula");
        plantAttributes.put("Plante", "bouleau");
        insertedPlants.put("bouleau", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "geraniales");
        plantAttributes.put("Famille", "geraniaceae");
        plantAttributes.put("Genre", "geranium");
        plantAttributes.put("Espece", "geranium pratense");
        plantAttributes.put("Plante", "géranium des prés");
        insertedPlants.put("géranium des prés", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "lamiales");
        plantAttributes.put("Famille", "lamiaceae");
        plantAttributes.put("Genre", "hyssopus");
        plantAttributes.put("Espece", "hyssopus officinalis");
        plantAttributes.put("Plante", "hysope");
        insertedPlants.put("hysope", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "apiales");
        plantAttributes.put("Famille", "apiaceae");
        plantAttributes.put("Genre", "apium");
        plantAttributes.put("Espece", "apium graveolens");
        plantAttributes.put("Plante", "céleri");
        insertedPlants.put("céleri", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "lamiales");
        plantAttributes.put("Famille", "lamiaceae");
        plantAttributes.put("Genre", "nepeta");
        plantAttributes.put("Espece", "nepeta cataria");
        plantAttributes.put("Plante", "cataire");
        insertedPlants.put("cataire", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "solanales");
        plantAttributes.put("Famille", "solanaceae");
        plantAttributes.put("Genre", "capsicum");
        plantAttributes.put("Espece", "capsicum annuum");
        plantAttributes.put("Plante", "poivron");
        insertedPlants.put("poivron", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "piment");
        insertedPlants.put("piment", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "piment fort de cayenne");
        insertedPlants.put("piment fort de cayenne", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "asterales");
        plantAttributes.put("Famille", "asteraceae");
        plantAttributes.put("Genre", "cynara");
        plantAttributes.put("Espece", "cynara cardunculus");
        plantAttributes.put("Plante", "artichaut");
        insertedPlants.put("artichaut", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "artichaut gros vert de laon");
        insertedPlants.put("artichaut gros vert de laon", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "asterales");
        plantAttributes.put("Famille", "asteraceae");
        plantAttributes.put("Genre", "scorzonera");
        plantAttributes.put("Espece", "scorzonera humilis");
        plantAttributes.put("Plante", "scorsonère des prés");
        insertedPlants.put("scorsonère des prés", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "fabales");
        plantAttributes.put("Famille", "fabaceae");
        plantAttributes.put("Genre", "lens");
        plantAttributes.put("Espece", "lens culinaris");
        plantAttributes.put("Plante", "lentille");
        insertedPlants.put("lentille", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "apiales");
        plantAttributes.put("Famille", "apiaceae");
        plantAttributes.put("Genre", "carum");
        plantAttributes.put("Espece", "carum carvi");
        plantAttributes.put("Plante", "carvi");
        insertedPlants.put("carvi", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "apiales");
        plantAttributes.put("Famille", "apiaceae");
        plantAttributes.put("Genre", "pimpinella");
        plantAttributes.put("Espece", "pimpinella anisum");
        plantAttributes.put("Plante", "anis vert");
        insertedPlants.put("anis vert", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "apiales");
        plantAttributes.put("Famille", "apiaceae");
        plantAttributes.put("Genre", "foeniculum");
        plantAttributes.put("Espece", "foeniculum vulgare");
        plantAttributes.put("Plante", "fenouil");
        insertedPlants.put("fenouil", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "asterales");
        plantAttributes.put("Famille", "asteraceae");
        plantAttributes.put("Genre", "artemisia");
        plantAttributes.put("Espece", "artemisia absinthium");
        plantAttributes.put("Plante", "absinthe");
        insertedPlants.put("absinthe", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "sapindales");
        plantAttributes.put("Famille", "rutaceae");
        plantAttributes.put("Genre", "ruta");
        plantAttributes.put("Espece", "ruta graveolens");
        plantAttributes.put("Plante", "rue officinale");
        insertedPlants.put("rue officinale", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "juglandales");
        plantAttributes.put("Famille", "juglandaceae");
        plantAttributes.put("Genre", "juglans");
        plantAttributes.put("Espece", "juglans regia");
        plantAttributes.put("Plante", "noyer");
        insertedPlants.put("noyer", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "apiales");
        plantAttributes.put("Famille", "apiaceae");
        plantAttributes.put("Genre", "anthriscus");
        plantAttributes.put("Espece", "anthriscus cerefolium");
        plantAttributes.put("Plante", "cerfeuil");
        insertedPlants.put("cerfeuil", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "caryophyllales");
        plantAttributes.put("Famille", "chenopodiaceae");
        plantAttributes.put("Genre", "atriplex");
        plantAttributes.put("Espece", "atriplex hortensis");
        plantAttributes.put("Plante", "arroche des jardins");
        insertedPlants.put("arroche des jardins", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "caryophyllales");
        plantAttributes.put("Famille", "portulacaceae");
        plantAttributes.put("Genre", "portulaca");
        plantAttributes.put("Espece", "portulaca oleracea");
        plantAttributes.put("Plante", "pourpier");
        insertedPlants.put("pourpier", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "dipsacales");
        plantAttributes.put("Famille", "valerianaceae");
        plantAttributes.put("Genre", "valerianella");
        plantAttributes.put("Espece", "valerianella locusta");
        plantAttributes.put("Plante", "mâche");
        insertedPlants.put("mâche", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "mâche à grosse graine");
        insertedPlants.put("mâche à grosse graine", insertPlant(plantAttributes));
        return insertedPlants;
    }

    /**
     * @param data null if failed to insert something
     * @return The newly created plant
     */
    private Plante insertPlant(Map<String, String> data) {
        try {
            Cronquist cronquist = getOrInsertCronquist(data);
            Classification classification = getOrInsertClassification(cronquist);

            NomVernaculaire nom = getOrInsertNomVernaculaire(data);
            Plante plante = getOrInsertPlante(nom, classification);

            log.info("Plant saved : " + plante);
            return plante;
        } catch (Exception e) {
            log.error("Unable to insert {" + data.get("Plante") + "}" + e.getLocalizedMessage());
        }
        return null;
    }

    private NomVernaculaire getOrInsertNomVernaculaire(Map<String, String> data) {
        NomVernaculaire nom = new NomVernaculaire();
        nom.setNom(data.get("Plante"));

        if (!nomVernaculaireRepository.exists(Example.of(nom))) {
            nomVernaculaireRepository.save(nom);
        } else {
            Optional<NomVernaculaire> returned = nomVernaculaireRepository.findOne(Example.of(nom));
            if (returned.isPresent()) {
                nom = returned.get();
            } else {
                log.error("Unable to get instance of : " + nom);
            }
        }
        return nom;
    }

    private Plante getOrInsertPlante(NomVernaculaire nom, Classification classification) {
        Plante plante;
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
                log.error("Unable to get instance of : " + plante);
            }
        }
        return plante;
    }

    private Cronquist getOrInsertCronquist(Map<String, String> data) {
        Cronquist cronquist = new Cronquist();
        cronquist.setOrdre(data.get("Ordre"));
        cronquist.setFamille(data.get("Famille"));
        cronquist.setGenre(data.get("Genre"));
        cronquist.setEspece(data.get("Espece"));

        if (!cronquistRepository.exists(Example.of(cronquist))) {
            cronquistRepository.save(cronquist);
        } else {
            Optional<Cronquist> returned = cronquistRepository.findOne(Example.of(cronquist));
            if (returned.isPresent()) {
                cronquist = returned.get();
                log.info("Existing cronquist : " + cronquist);
            } else {
                log.error("Unable to get instance of : " + cronquist);
            }
        }
        return cronquist;
    }

    private Classification getOrInsertClassification(Cronquist cronquist) {
        Classification classification = new Classification();
        classification.setCronquist(cronquist);
        if (!classificationRepository.exists(Example.of(classification))) {
            classificationRepository.save(classification);
        } else {
            Optional<Classification> returned = classificationRepository.findOne(Example.of(classification));
            if (returned.isPresent()) {
                classification = returned.get();
                log.info("Existing classification : " + classification);
            } else {
                log.error("Unable to get instance of : " + classification);
            }
        }
        return classification;
    }


}

package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.domain.*;
import fr.syncrase.ecosyst.repository.*;
import fr.syncrase.ecosyst.service.*;
import fr.syncrase.ecosyst.service.criteria.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.StringFilter;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class CronquistService {

    /**
     * Deux rangs taxonomiques sont séparés par d'autres rangs dont on ne connait pas forcément le nom.<br>
     * Une valeur par défaut permet de lier ces deux rangs avec des rangs vides.<br>
     * Si ultérieurement ces rangs sont déterminés, les valeurs par défaut sont mises à jour
     */
    public static final String DEFAULT_NAME_FOR_CONNECTOR_RANK = "";
    private static final String WARN_PARENT_IS_DIFFERENT = "Tentative d'enregistrer {} avec pour parent {} alors que dans la base sont parent est {}.\n Enregistrement du nouveau parent comme synonyme du parent actuel";
    private final Logger log = LoggerFactory.getLogger(CronquistService.class);

    // Repositories
    private SuperRegneRepository superRegneRepository;
    private RegneRepository regneRepository;
    private SousRegneRepository sousRegneRepository;
    private RameauRepository rameauRepository;
    private InfraRegneRepository infraRegneRepository;
    private SuperDivisionRepository superDivisionRepository;
    private DivisionRepository divisionRepository;
    private SousDivisionRepository sousDivisionRepository;
    private InfraEmbranchementRepository infraEmbranchementRepository;
    private MicroEmbranchementRepository microEmbranchementRepository;
    private SuperClasseRepository superClasseRepository;
    private ClasseRepository classeRepository;
    private SousClasseRepository sousClasseRepository;
    private InfraClasseRepository infraClasseRepository;
    private SuperOrdreRepository superOrdreRepository;
    private OrdreRepository ordreRepository;
    private SousOrdreRepository sousOrdreRepository;
    private InfraOrdreRepository infraOrdreRepository;
    private MicroOrdreRepository microOrdreRepository;
    private SuperFamilleRepository superFamilleRepository;
    private FamilleRepository familleRepository;
    private SousFamilleRepository sousFamilleRepository;
    private TribuRepository tribuRepository;
    private SousTribuRepository sousTribuRepository;
    private GenreRepository genreRepository;
    private SousGenreRepository sousGenreRepository;
    private SectionRepository sectionRepository;
    private SousSectionRepository sousSectionRepository;
    private EspeceRepository especeRepository;
    private SousEspeceRepository sousEspeceRepository;
    private VarieteRepository varieteRepository;
    private SousVarieteRepository sousVarieteRepository;
    private FormeRepository formeRepository;
    private SousFormeRepository sousFormeRepository;

    // Query services
    private SuperRegneQueryService superRegneQueryService;
    private RegneQueryService regneQueryService;
    private SousRegneQueryService sousRegneQueryService;
    private RameauQueryService rameauQueryService;
    private InfraRegneQueryService infraRegneQueryService;
    private SuperDivisionQueryService superDivisionQueryService;
    private DivisionQueryService divisionQueryService;
    private SousDivisionQueryService sousDivisionQueryService;
    private InfraEmbranchementQueryService infraEmbranchementQueryService;
    private MicroEmbranchementQueryService microEmbranchementQueryService;
    private SuperClasseQueryService superClasseQueryService;
    private ClasseQueryService classeQueryService;
    private SousClasseQueryService sousClasseQueryService;
    private InfraClasseQueryService infraClasseQueryService;
    private SuperOrdreQueryService superOrdreQueryService;
    private OrdreQueryService ordreQueryService;
    private SousOrdreQueryService sousOrdreQueryService;
    private InfraOrdreQueryService infraOrdreQueryService;
    private MicroOrdreQueryService microOrdreQueryService;
    private SuperFamilleQueryService superFamilleQueryService;
    private FamilleQueryService familleQueryService;
    private SousFamilleQueryService sousFamilleQueryService;
    private TribuQueryService tribuQueryService;
    private SousTribuQueryService sousTribuQueryService;
    private GenreQueryService genreQueryService;
    private SousGenreQueryService sousGenreQueryService;
    private SectionQueryService sectionQueryService;
    private SousSectionQueryService sousSectionQueryService;
    private EspeceQueryService especeQueryService;
    private SousEspeceQueryService sousEspeceQueryService;
    private VarieteQueryService varieteQueryService;
    private SousVarieteQueryService sousVarieteQueryService;
    private FormeQueryService formeQueryService;
    private SousFormeQueryService sousFormeQueryService;


    @Autowired
    public void setSuperRegneQueryService(SuperRegneQueryService superRegneQueryService) {
        this.superRegneQueryService = superRegneQueryService;
    }

    @Autowired
    public void setRegneQueryService(RegneQueryService regneQueryService) {
        this.regneQueryService = regneQueryService;
    }

    @Autowired
    public void setSousRegneQueryService(SousRegneQueryService sousRegneQueryService) {
        this.sousRegneQueryService = sousRegneQueryService;
    }

    @Autowired
    public void setRameauQueryService(RameauQueryService rameauQueryService) {
        this.rameauQueryService = rameauQueryService;
    }

    @Autowired
    public void setInfraRegneQueryService(InfraRegneQueryService infraRegneQueryService) {
        this.infraRegneQueryService = infraRegneQueryService;
    }

    @Autowired
    public void setSuperDivisionQueryService(SuperDivisionQueryService superDivisionQueryService) {
        this.superDivisionQueryService = superDivisionQueryService;
    }

    @Autowired
    public void setDivisionQueryService(DivisionQueryService divisionQueryService) {
        this.divisionQueryService = divisionQueryService;
    }

    @Autowired
    public void setSousDivisionQueryService(SousDivisionQueryService sousDivisionQueryService) {
        this.sousDivisionQueryService = sousDivisionQueryService;
    }

    @Autowired
    public void setInfraEmbranchementQueryService(InfraEmbranchementQueryService infraEmbranchementQueryService) {
        this.infraEmbranchementQueryService = infraEmbranchementQueryService;
    }

    @Autowired
    public void setMicroEmbranchementQueryService(MicroEmbranchementQueryService microEmbranchementQueryService) {
        this.microEmbranchementQueryService = microEmbranchementQueryService;
    }

    @Autowired
    public void setSuperClasseQueryService(SuperClasseQueryService superClasseQueryService) {
        this.superClasseQueryService = superClasseQueryService;
    }

    @Autowired
    public void setClasseQueryService(ClasseQueryService classeQueryService) {
        this.classeQueryService = classeQueryService;
    }

    @Autowired
    public void setSousClasseQueryService(SousClasseQueryService sousClasseQueryService) {
        this.sousClasseQueryService = sousClasseQueryService;
    }

    @Autowired
    public void setInfraClasseQueryService(InfraClasseQueryService infraClasseQueryService) {
        this.infraClasseQueryService = infraClasseQueryService;
    }

    @Autowired
    public void setSuperOrdreQueryService(SuperOrdreQueryService superOrdreQueryService) {
        this.superOrdreQueryService = superOrdreQueryService;
    }

    @Autowired
    public void setOrdreQueryService(OrdreQueryService ordreQueryService) {
        this.ordreQueryService = ordreQueryService;
    }

    @Autowired
    public void setSousOrdreQueryService(SousOrdreQueryService sousOrdreQueryService) {
        this.sousOrdreQueryService = sousOrdreQueryService;
    }

    @Autowired
    public void setInfraOrdreQueryService(InfraOrdreQueryService infraOrdreQueryService) {
        this.infraOrdreQueryService = infraOrdreQueryService;
    }

    @Autowired
    public void setMicroOrdreQueryService(MicroOrdreQueryService microOrdreQueryService) {
        this.microOrdreQueryService = microOrdreQueryService;
    }

    @Autowired
    public void setSuperFamilleQueryService(SuperFamilleQueryService superFamilleQueryService) {
        this.superFamilleQueryService = superFamilleQueryService;
    }

    @Autowired
    public void setFamilleQueryService(FamilleQueryService familleQueryService) {
        this.familleQueryService = familleQueryService;
    }

    @Autowired
    public void setSousFamilleQueryService(SousFamilleQueryService sousFamilleQueryService) {
        this.sousFamilleQueryService = sousFamilleQueryService;
    }

    @Autowired
    public void setTribuQueryService(TribuQueryService tribuQueryService) {
        this.tribuQueryService = tribuQueryService;
    }

    @Autowired
    public void setSousTribuQueryService(SousTribuQueryService sousTribuQueryService) {
        this.sousTribuQueryService = sousTribuQueryService;
    }

    @Autowired
    public void setGenreQueryService(GenreQueryService genreQueryService) {
        this.genreQueryService = genreQueryService;
    }

    @Autowired
    public void setSousGenreQueryService(SousGenreQueryService sousGenreQueryService) {
        this.sousGenreQueryService = sousGenreQueryService;
    }

    @Autowired
    public void setSectionQueryService(SectionQueryService sectionQueryService) {
        this.sectionQueryService = sectionQueryService;
    }

    @Autowired
    public void setSousSectionQueryService(SousSectionQueryService sousSectionQueryService) {
        this.sousSectionQueryService = sousSectionQueryService;
    }

    @Autowired
    public void setEspeceQueryService(EspeceQueryService especeQueryService) {
        this.especeQueryService = especeQueryService;
    }

    @Autowired
    public void setSousEspeceQueryService(SousEspeceQueryService sousEspeceQueryService) {
        this.sousEspeceQueryService = sousEspeceQueryService;
    }

    @Autowired
    public void setVarieteQueryService(VarieteQueryService varieteQueryService) {
        this.varieteQueryService = varieteQueryService;
    }

    @Autowired
    public void setSousVarieteQueryService(SousVarieteQueryService sousVarieteQueryService) {
        this.sousVarieteQueryService = sousVarieteQueryService;
    }

    @Autowired
    public void setFormeQueryService(FormeQueryService formeQueryService) {
        this.formeQueryService = formeQueryService;
    }

    @Autowired
    public void setSousFormeQueryService(SousFormeQueryService sousFormeQueryService) {
        this.sousFormeQueryService = sousFormeQueryService;
    }


    @Autowired
    public void setSuperRegneRepository(SuperRegneRepository superRegneRepository) {
        this.superRegneRepository = superRegneRepository;
    }

    @Autowired
    public void setRegneRepository(RegneRepository regneRepository) {
        this.regneRepository = regneRepository;
    }

    @Autowired
    public void setSousRegneRepository(SousRegneRepository sousRegneRepository) {
        this.sousRegneRepository = sousRegneRepository;
    }

    @Autowired
    public void setRameauRepository(RameauRepository rameauRepository) {
        this.rameauRepository = rameauRepository;
    }

    @Autowired
    public void setInfraRegneRepository(InfraRegneRepository infraRegneRepository) {
        this.infraRegneRepository = infraRegneRepository;
    }

    @Autowired
    public void setSuperDivisionRepository(SuperDivisionRepository superDivisionRepository) {
        this.superDivisionRepository = superDivisionRepository;
    }

    @Autowired
    public void setDivisionRepository(DivisionRepository divisionRepository) {
        this.divisionRepository = divisionRepository;
    }

    @Autowired
    public void setSousDivisionRepository(SousDivisionRepository sousDivisionRepository) {
        this.sousDivisionRepository = sousDivisionRepository;
    }

    @Autowired
    public void setInfraEmbranchementRepository(InfraEmbranchementRepository infraEmbranchementRepository) {
        this.infraEmbranchementRepository = infraEmbranchementRepository;
    }

    @Autowired
    public void setMicroEmbranchementRepository(MicroEmbranchementRepository microEmbranchementRepository) {
        this.microEmbranchementRepository = microEmbranchementRepository;
    }

    @Autowired
    public void setSuperClasseRepository(SuperClasseRepository superClasseRepository) {
        this.superClasseRepository = superClasseRepository;
    }

    @Autowired
    public void setClasseRepository(ClasseRepository classeRepository) {
        this.classeRepository = classeRepository;
    }

    @Autowired
    public void setSousClasseRepository(SousClasseRepository sousClasseRepository) {
        this.sousClasseRepository = sousClasseRepository;
    }

    @Autowired
    public void setInfraClasseRepository(InfraClasseRepository infraClasseRepository) {
        this.infraClasseRepository = infraClasseRepository;
    }

    @Autowired
    public void setSuperOrdreRepository(SuperOrdreRepository superOrdreRepository) {
        this.superOrdreRepository = superOrdreRepository;
    }

    @Autowired
    public void setOrdreRepository(OrdreRepository ordreRepository) {
        this.ordreRepository = ordreRepository;
    }

    @Autowired
    public void setSousOrdreRepository(SousOrdreRepository sousOrdreRepository) {
        this.sousOrdreRepository = sousOrdreRepository;
    }

    @Autowired
    public void setInfraOrdreRepository(InfraOrdreRepository infraOrdreRepository) {
        this.infraOrdreRepository = infraOrdreRepository;
    }

    @Autowired
    public void setMicroOrdreRepository(MicroOrdreRepository microOrdreRepository) {
        this.microOrdreRepository = microOrdreRepository;
    }

    @Autowired
    public void setSuperFamilleRepository(SuperFamilleRepository superFamilleRepository) {
        this.superFamilleRepository = superFamilleRepository;
    }

    @Autowired
    public void setFamilleRepository(FamilleRepository familleRepository) {
        this.familleRepository = familleRepository;
    }

    @Autowired
    public void setSousFamilleRepository(SousFamilleRepository sousFamilleRepository) {
        this.sousFamilleRepository = sousFamilleRepository;
    }

    @Autowired
    public void setTribuRepository(TribuRepository tribuRepository) {
        this.tribuRepository = tribuRepository;
    }

    @Autowired
    public void setSousTribuRepository(SousTribuRepository sousTribuRepository) {
        this.sousTribuRepository = sousTribuRepository;
    }

    @Autowired
    public void setGenreRepository(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Autowired
    public void setSousGenreRepository(SousGenreRepository sousGenreRepository) {
        this.sousGenreRepository = sousGenreRepository;
    }

    @Autowired
    public void setSectionRepository(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Autowired
    public void setSousSectionRepository(SousSectionRepository sousSectionRepository) {
        this.sousSectionRepository = sousSectionRepository;
    }

    @Autowired
    public void setEspeceRepository(EspeceRepository especeRepository) {
        this.especeRepository = especeRepository;
    }

    @Autowired
    public void setSousEspeceRepository(SousEspeceRepository sousEspeceRepository) {
        this.sousEspeceRepository = sousEspeceRepository;
    }

    @Autowired
    public void setVarieteRepository(VarieteRepository varieteRepository) {
        this.varieteRepository = varieteRepository;
    }

    @Autowired
    public void setSousVarieteRepository(SousVarieteRepository sousVarieteRepository) {
        this.sousVarieteRepository = sousVarieteRepository;
    }

    @Autowired
    public void setFormeRepository(FormeRepository formeRepository) {
        this.formeRepository = formeRepository;
    }

    @Autowired
    public void setSousFormeRepository(SousFormeRepository sousFormeRepository) {
        this.sousFormeRepository = sousFormeRepository;
    }

    public void saveCronquist(SuperRegneWrapper superRegneWrapper) {

        /*
        1 - Etant donné qu'un rang inférieur doit contenir le rang supérieur, l'enregistrement des classifications doit se faire en commençant par le rang supérieur
        2 - Il est primordial de pouvoir itérer sur les objets, pour appliquer une logique similaire : interface
        3 - Les rangs inférieurs sans valeur n'ont pas de raison d'être puisque la raison d'être des rangs vides et de lier deux rangs dont les noms sont connus : nettoyage de l'arborescence au commencement
        4 - les synonymes : d'abord revoir le JDL. Pas de gestion de synonyme pour l'instant. Gérer des wrapper extends entities qui comporte des informations complémentaires sur les traitements à effectuer ?
         */
//         idée ? SuperRegneWrapper<rangTaxonomique> arborescence = new SuperRegneWrapper<>();
        superRegneWrapper.clearTail();
        addIds(superRegneWrapper);
        manageSynonymes(superRegneWrapper);
        saveArborescence(superRegneWrapper);
        // Si un rang taxonomique existe déjà, mais qu'il ne contient pas le parent,
        getExistingDataAndCheckIntegrity(superRegneWrapper);
        saveOrUpdateSuperRegne(superRegneWrapper.getSuperRegne());
    }

    /**
     * Tous les rangs sont mis à jour avec l'ID et les noms, mais ascendant ni descendants <br>
     * Vérifie s'il n'y a pas de différence entre la classification enregistrée et celle que l'on souhaite enregistrer. <br>
     * Par exemple : L'ordre 'Arecales' a une unique ascendance, s'il y a une différence dans l'ascendance, il faut le repérer TODO et la corriger scrapper aussi aussi <br>
     * Vérifie tous les rangs à partir du plus inférieur pour trouver un rang où les taxons correspondent. À partir de celui-là, la classification doit être commune <br>
     *
     * <h1>Pourquoi précharger tous les IDs ?</h1>
     * Ça évite de persister des rangs intermédiaires vides qui seront dé-associer plus bas dans l'arbre quand un rang existant (avec son parent) sera récupéré<br>
     * <b>Chemin unique</b> : Pour chacun des rangs, les sous-rangs ne sont pas récupérés. L'arborescence sans dichotomie est préservée.
     *
     * <h1>Les synonymes</h1>
     * Par définition d'un rang taxonomique, un même rang ne peut avoir qu'une unique arborescence sans dichotomie.<br>
     * Lors de l'insertion d'un rang, si les parents sont différents alors ce sont les mêmes (ie. des synonymes)<br>
     * Ces différents synonymes se positionnent exactement de la même manière dans l'arborescence. Ils ont :<br>
     * <ul>
     *     <li>Les mêmes enfants</li>
     *     <li>Le même parent</li>
     * </ul>
     *
     * @param superRegneWrapper side effect
     */
    private void getExistingDataAndCheckIntegrity(@NotNull SuperRegneWrapper superRegneWrapper) {
        boolean isCheckingAncestorsIntegrity = false;
        boolean isEmptyTail = true;


        SousForme sousForme = superRegneWrapper.getSousForme();
//        if(sousForme.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && isEmptyTail){
//            superRegneWrapper.removeSousForme();
//        } else {
//        }

        isEmptyTail = false;
        SousFormeCriteria sousFormeCrit = new SousFormeCriteria();
        sousFormeCrit.setNomFr(getStringFilterEquals(sousForme.getNomFr()));
        List<SousForme> matchingSousFormes = sousFormeQueryService.findByCriteria(sousFormeCrit);
        if (!sousForme.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && matchingSousFormes.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            SousForme returned = matchingSousFormes.get(0);
            sousForme.setId(returned.getId());
            sousForme.setNomFr(returned.getNomFr());
            sousForme.setNomLatin(returned.getNomLatin());
            log.info("Existing sousForme : " + sousForme);
            if (!Objects.equals(returned.getForme().getNomFr(), sousForme.getForme().getNomFr())) {
                // TODO si les parents sont différents parcequ'un parent existe en base mais que je ne l'ai pas ? => le parent en base devient le parent que je vais enregistrer
                log.warn(WARN_PARENT_IS_DIFFERENT, sousForme.getNomFr(), sousForme.getForme().getNomFr(), returned.getForme().getNomFr());
                sousForme.getForme().getSousFormes().addAll(returned.getForme().getSousFormes());// addAll Enfants
                sousForme.getForme().getSousVariete().setId(returned.getForme().getSousVariete().getId());// set parent's parent
                sousForme.getForme().getSynonymes().addAll(returned.getForme().getSynonymes());
                returned.getForme().addSynonymes(sousForme.getForme());
                formeRepository.save(returned.getForme());
            } else {
                sousForme.getForme().setId(returned.getForme().getId());
            }
        }

        Forme forme = superRegneWrapper.getForme();
        FormeCriteria formeCrit = new FormeCriteria();
        formeCrit.setNomFr(getStringFilterEquals(forme.getNomFr()));
        List<Forme> matchingFormes = formeQueryService.findByCriteria(formeCrit);
        if ((!forme.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingFormes.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            Forme returned = matchingFormes.get(0);
            forme.setId(returned.getId());
            forme.setNomFr(returned.getNomFr());
            forme.setNomLatin(returned.getNomLatin());
            log.info("Existing forme : " + forme);
            if (!Objects.equals(returned.getSousVariete().getNomFr(), forme.getSousVariete().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, forme.getNomFr(), forme.getSousVariete().getNomFr(), returned.getSousVariete().getNomFr());
                forme.getSousVariete().getFormes().addAll(returned.getSousVariete().getFormes());// addAll Enfants
                forme.getSousVariete().getVariete().setId(returned.getSousVariete().getVariete().getId());// set parent's parent
                forme.getSousVariete().getSynonymes().addAll(returned.getSousVariete().getSynonymes());
                returned.getSousVariete().addSynonymes(forme.getSousVariete());
                sousVarieteRepository.save(returned.getSousVariete());
            } else {
                forme.getSousVariete().setId(returned.getSousVariete().getId());
            }

        }
        SousVariete sousVariete = superRegneWrapper.getSousVariete();
        SousVarieteCriteria sousVarieteCrit = new SousVarieteCriteria();
        sousVarieteCrit.setNomFr(getStringFilterEquals(sousVariete.getNomFr()));
        List<SousVariete> matchingSousVarietes = sousVarieteQueryService.findByCriteria(sousVarieteCrit);
        if ((!sousVariete.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingSousVarietes.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            SousVariete returned = matchingSousVarietes.get(0);
            sousVariete.setId(returned.getId());
            sousVariete.setNomFr(returned.getNomFr());
            sousVariete.setNomLatin(returned.getNomLatin());
            log.info("Existing sousVariete : " + sousVariete);
            if (!Objects.equals(returned.getVariete().getNomFr(), sousVariete.getVariete().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, sousVariete.getNomFr(), sousVariete.getVariete().getNomFr(), returned.getVariete().getNomFr());
                sousVariete.getVariete().getSousVarietes().addAll(returned.getVariete().getSousVarietes());// addAllEnfants
                sousVariete.getVariete().getSousEspece().setId(returned.getVariete().getSousEspece().getId());// set parent's parent
                sousVariete.getVariete().getSynonymes().addAll(returned.getVariete().getSynonymes());
                returned.getVariete().addSynonymes(sousVariete.getVariete());
                varieteRepository.save(returned.getVariete());
            } else {
                sousVariete.getVariete().setId(returned.getVariete().getId());
            }
        }
        Variete variete = superRegneWrapper.getVariete();
        VarieteCriteria varieteCrit = new VarieteCriteria();
        varieteCrit.setNomFr(getStringFilterEquals(variete.getNomFr()));
        List<Variete> matchingVarietes = varieteQueryService.findByCriteria(varieteCrit);
        if ((!variete.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingVarietes.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            Variete returned = matchingVarietes.get(0);
            variete.setId(returned.getId());
            variete.setNomFr(returned.getNomFr());
            variete.setNomLatin(returned.getNomLatin());
            log.info("Existing variete : " + variete);
            if (!Objects.equals(returned.getSousEspece().getNomFr(), variete.getSousEspece().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, variete.getNomFr(), variete.getSousEspece().getNomFr(), returned.getSousEspece().getNomFr());
                variete.getSousEspece().getVarietes().addAll(returned.getSousEspece().getVarietes());// addAll Enfants
                variete.getSousEspece().getEspece().setId(returned.getSousEspece().getEspece().getId());// set parent's parent
                variete.getSousEspece().getSynonymes().addAll(returned.getSousEspece().getSynonymes());
                returned.getSousEspece().addSynonymes(variete.getSousEspece());// add the new inserted as synonyme
                sousEspeceRepository.save(returned.getSousEspece());
            } else {
                variete.getSousEspece().setId(returned.getSousEspece().getId());
            }
        }
        SousEspece sousEspece = superRegneWrapper.getSousEspece();
        SousEspeceCriteria sousEspeceCrit = new SousEspeceCriteria();
        sousEspeceCrit.setNomFr(getStringFilterEquals(sousEspece.getNomFr()));
        List<SousEspece> matchingSousEspeces = sousEspeceQueryService.findByCriteria(sousEspeceCrit);
        if ((!sousEspece.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingSousEspeces.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            SousEspece returned = matchingSousEspeces.get(0);
            sousEspece.setId(returned.getId());
            sousEspece.setNomFr(returned.getNomFr());
            sousEspece.setNomLatin(returned.getNomLatin());
            log.info("Existing sousEspece : " + sousEspece);
            if (!Objects.equals(returned.getEspece().getNomFr(), sousEspece.getEspece().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, sousEspece.getNomFr(), sousEspece.getEspece().getNomFr(), returned.getEspece().getNomFr());
                sousEspece.getEspece().getSousEspeces().addAll(returned.getEspece().getSousEspeces());// addAll Enfants
                sousEspece.getEspece().getSousSection().setId(returned.getEspece().getSousSection().getId());// set parent's parent
                sousEspece.getEspece().getSynonymes().addAll(returned.getEspece().getSynonymes());
                returned.getEspece().addSynonymes(sousEspece.getEspece());// add the new inserted as synonyme
                especeRepository.save(returned.getEspece());
            } else {
                sousEspece.getEspece().setId(returned.getEspece().getId());
            }
        }
        Espece espece = superRegneWrapper.getEspece();
        EspeceCriteria especeCrit = new EspeceCriteria();
        especeCrit.setNomFr(getStringFilterEquals(espece.getNomFr()));
        List<Espece> matchingEspeces = especeQueryService.findByCriteria(especeCrit);
        if ((!espece.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingEspeces.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            Espece returned = matchingEspeces.get(0);
            espece.setId(returned.getId());
            espece.setNomFr(returned.getNomFr());
            espece.setNomLatin(returned.getNomLatin());
            log.info("Existing espece : " + espece);
            if (!Objects.equals(returned.getSousSection().getNomFr(), espece.getSousSection().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, espece.getNomFr(), espece.getSousSection().getNomFr(), returned.getSousSection().getNomFr());
                espece.getSousSection().getEspeces().addAll(returned.getSousSection().getEspeces());// addAll Enfants
                espece.getSousSection().getSection().setId(returned.getSousSection().getSection().getId());// set parent's parent
                espece.getSousSection().getSynonymes().addAll(returned.getSousSection().getSynonymes());
                returned.getSousSection().addSynonymes(espece.getSousSection());// add the new inserted as synonyme
                sousSectionRepository.save(returned.getSousSection());
            } else {
                espece.getSousSection().setId(returned.getSousSection().getId());
            }
        }
        SousSection sousSection = superRegneWrapper.getSousSection();
        SousSectionCriteria sousSectionCrit = new SousSectionCriteria();
        sousSectionCrit.setNomFr(getStringFilterEquals(sousSection.getNomFr()));
        List<SousSection> matchingSousSections = sousSectionQueryService.findByCriteria(sousSectionCrit);
        if ((!sousSection.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingSousSections.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            SousSection returned = matchingSousSections.get(0);
            sousSection.setId(returned.getId());
            sousSection.setNomFr(returned.getNomFr());
            sousSection.setNomLatin(returned.getNomLatin());
            log.info("Existing sousSection : " + sousSection);
            if (!Objects.equals(returned.getSection().getNomFr(), sousSection.getSection().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, sousSection.getNomFr(), sousSection.getSection().getNomFr(), returned.getSection().getNomFr());
                sousSection.getSection().getSousSections().addAll(returned.getSection().getSousSections());// addAll Enfants
                sousSection.getSection().getSousGenre().setId(returned.getSection().getSousGenre().getId());// set parent's parent
                sousSection.getSection().getSynonymes().addAll(returned.getSection().getSynonymes());
                returned.getSection().addSynonymes(sousSection.getSection());// add the new inserted as synonyme
                sectionRepository.save(returned.getSection());
            } else {
                sousSection.getSection().setId(returned.getSection().getId());
            }
        }
        Section section = superRegneWrapper.getSection();
        SectionCriteria sectionCrit = new SectionCriteria();
        sectionCrit.setNomFr(getStringFilterEquals(section.getNomFr()));
        List<Section> matchingSections = sectionQueryService.findByCriteria(sectionCrit);
        if ((!section.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingSections.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            Section returned = matchingSections.get(0);
            section.setId(returned.getId());
            section.setNomFr(returned.getNomFr());
            section.setNomLatin(returned.getNomLatin());
            log.info("Existing section : " + section);
            if (!Objects.equals(returned.getSousGenre().getNomFr(), section.getSousGenre().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, section.getNomFr(), section.getSousGenre().getNomFr(), returned.getSousGenre().getNomFr());
                section.getSousGenre().getSections().addAll(returned.getSousGenre().getSections());// addAll Enfants
                section.getSousGenre().getSousGenre().setId(returned.getSousGenre().getSousGenre().getId());// set parent's parent
                section.getSousGenre().getSynonymes().addAll(returned.getSousGenre().getSynonymes());
                returned.getSousGenre().addSynonymes(section.getSousGenre());// add the new inserted as synonyme
                sousGenreRepository.save(returned.getSousGenre());
            } else {
                section.getSousGenre().setId(returned.getSousGenre().getId());
            }
        }
        SousGenre sousGenre = superRegneWrapper.getSousGenre();
        SousGenreCriteria sousGenreCrit = new SousGenreCriteria();
        sousGenreCrit.setNomFr(getStringFilterEquals(sousGenre.getNomFr()));
        List<SousGenre> matchingSousGenres = sousGenreQueryService.findByCriteria(sousGenreCrit);
        if ((!sousGenre.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingSousGenres.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            SousGenre returned = matchingSousGenres.get(0);
            sousGenre.setId(returned.getId());
            sousGenre.setNomFr(returned.getNomFr());
            sousGenre.setNomLatin(returned.getNomLatin());
            log.info("Existing sousGenre : " + sousGenre);
            if (!Objects.equals(returned.getGenre().getNomFr(), sousGenre.getGenre().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, sousGenre.getNomFr(), sousGenre.getGenre().getNomFr(), returned.getGenre().getNomFr());
                sousGenre.getGenre().getSousGenres().addAll(returned.getGenre().getSousGenres());// addAll Enfants
                sousGenre.getGenre().getSousTribu().setId(returned.getGenre().getSousTribu().getId());// set parent's parent
                sousGenre.getGenre().getSynonymes().addAll(returned.getGenre().getSynonymes());
                returned.getGenre().addSynonymes(sousGenre.getGenre());// add the new inserted as synonyme
                genreRepository.save(returned.getGenre());
            } else {
                sousGenre.getGenre().setId(returned.getGenre().getId());
            }
        }
        Genre genre = superRegneWrapper.getGenre();
        GenreCriteria genreCrit = new GenreCriteria();
        genreCrit.setNomFr(getStringFilterEquals(genre.getNomFr()));
        List<Genre> matchingGenres = genreQueryService.findByCriteria(genreCrit);
        if ((!genre.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingGenres.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            Genre returned = matchingGenres.get(0);
            genre.setId(returned.getId());
            genre.setNomFr(returned.getNomFr());
            genre.setNomLatin(returned.getNomLatin());
            log.info("Existing genre : " + genre);
            if (!Objects.equals(returned.getSousTribu().getNomFr(), genre.getSousTribu().getNomFr())) {
                if (returned.getSousTribu().getSynonymes().stream().anyMatch(st -> st.getNomFr().equals(genre.getSousTribu().getNomFr()))) {
                    // TODO je dois checker que le nom différent n'est pas déjà enregistré en tant que synonyme

                }
                log.warn(WARN_PARENT_IS_DIFFERENT, genre.getNomFr(), genre.getSousTribu().getNomFr(), returned.getSousTribu().getNomFr());
                // TODO ne pas ajouter tous ces enfants => créer un nouvel objet
                genre.getSousTribu().getGenres().addAll(returned.getSousTribu().getGenres());// addAll Enfants
                genre.getSousTribu().getTribu().setId(returned.getSousTribu().getTribu().getId());// set parent's parent
                genre.getSousTribu().getSynonymes().addAll(returned.getSousTribu().getSynonymes());// set known synonyms
                returned.getSousTribu().addSynonymes(genre.getSousTribu());// add the new inserted as synonyme
                sousTribuRepository.save(returned.getSousTribu());// TODO Transient ! la persistence de l'association avec le synonyme doit se faire après que le synonyme existe
            } else {
                genre.getSousTribu().setId(returned.getSousTribu().getId());
            }
        }
        SousTribu sousTribu = superRegneWrapper.getSousTribu();
        SousTribuCriteria sousTribuCrit = new SousTribuCriteria();
        sousTribuCrit.setNomFr(getStringFilterEquals(sousTribu.getNomFr()));
        List<SousTribu> matchingSousTribus = sousTribuQueryService.findByCriteria(sousTribuCrit);
        if ((!sousTribu.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingSousTribus.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            SousTribu returned = matchingSousTribus.get(0);
            sousTribu.setId(returned.getId());
            sousTribu.setNomFr(returned.getNomFr());
            sousTribu.setNomLatin(returned.getNomLatin());
            log.info("Existing sousTribu : " + sousTribu);
            if (!Objects.equals(returned.getTribu().getNomFr(), sousTribu.getTribu().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, sousTribu.getNomFr(), sousTribu.getTribu().getNomFr(), returned.getTribu().getNomFr());
                sousTribu.getTribu().getSousTribuses().addAll(returned.getTribu().getSousTribuses());// addAll Enfants
                sousTribu.getTribu().getSousFamille().setId(returned.getTribu().getSousFamille().getId());// set parent's parent
                sousTribu.getTribu().getSynonymes().addAll(returned.getTribu().getSynonymes());
                returned.getTribu().addSynonymes(sousTribu.getTribu());// add the new inserted as synonyme
                tribuRepository.save(returned.getTribu());
            } else {
                sousTribu.getTribu().setId(returned.getTribu().getId());
            }
        }
        Tribu tribu = superRegneWrapper.getTribu();
        TribuCriteria tribuCrit = new TribuCriteria();
        tribuCrit.setNomFr(getStringFilterEquals(tribu.getNomFr()));
        List<Tribu> matchingTribus = tribuQueryService.findByCriteria(tribuCrit);
        if ((!tribu.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingTribus.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            Tribu returned = matchingTribus.get(0);
            tribu.setId(returned.getId());
            tribu.setNomFr(returned.getNomFr());
            tribu.setNomLatin(returned.getNomLatin());
            log.info("Existing tribu : " + tribu);
            if (!Objects.equals(returned.getSousFamille().getNomFr(), tribu.getSousFamille().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, tribu.getNomFr(), tribu.getSousFamille().getNomFr(), returned.getSousFamille().getNomFr());
                tribu.getSousFamille().getTribuses().addAll(returned.getSousFamille().getTribuses());// addAll Enfants
                tribu.getSousFamille().getFamille().setId(returned.getSousFamille().getFamille().getId());// set parent's parent
                tribu.getSousFamille().getSynonymes().addAll(returned.getSousFamille().getSynonymes());
                returned.getSousFamille().addSynonymes(tribu.getSousFamille());// add the new inserted as synonyme
                sousFamilleRepository.save(returned.getSousFamille());
            } else {
                tribu.getSousFamille().setId(returned.getSousFamille().getId());
            }
        }
        SousFamille sousFamille = superRegneWrapper.getSousFamille();
        SousFamilleCriteria sousFamilleCrit = new SousFamilleCriteria();
        sousFamilleCrit.setNomFr(getStringFilterEquals(sousFamille.getNomFr()));
        List<SousFamille> matchingSousFamilles = sousFamilleQueryService.findByCriteria(sousFamilleCrit);
        if ((!sousFamille.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingSousFamilles.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            SousFamille returned = matchingSousFamilles.get(0);
            sousFamille.setId(returned.getId());
            sousFamille.setNomFr(returned.getNomFr());
            sousFamille.setNomLatin(returned.getNomLatin());
            log.info("Existing sousFamille : " + sousFamille);
            if (!Objects.equals(returned.getFamille().getNomFr(), sousFamille.getFamille().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, sousFamille.getNomFr(), sousFamille.getFamille().getNomFr(), returned.getFamille().getNomFr());
                sousFamille.getFamille().getSousFamilles().addAll(returned.getFamille().getSousFamilles());// addAll Enfants
                sousFamille.getFamille().getSuperFamille().setId(returned.getFamille().getSuperFamille().getId());// set parent's parent
                sousFamille.getFamille().getSynonymes().addAll(returned.getFamille().getSynonymes());
                returned.getFamille().addSynonymes(sousFamille.getFamille());// add the new inserted as synonyme
                familleRepository.save(returned.getFamille());
            } else {
                sousFamille.getFamille().setId(returned.getFamille().getId());
            }
        }
        Famille famille = superRegneWrapper.getFamille();
        // familleQueryService.exists(Example.of(famille)) ne trouve pas la famille qui existe déjà. Pourquoi ?
        FamilleCriteria familleCrit = new FamilleCriteria();
        familleCrit.setNomFr(getStringFilterEquals(famille.getNomFr()));
        List<Famille> matchingFamilles = familleQueryService.findByCriteria(familleCrit);
        if ((!famille.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingFamilles.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            Famille returned = matchingFamilles.get(0);
            famille.setId(returned.getId());
            famille.setNomFr(returned.getNomFr());
            famille.setNomLatin(returned.getNomLatin());
            log.info("Existing famille : " + famille);
            if (!Objects.equals(returned.getSuperFamille().getNomFr(), famille.getSuperFamille().getNomFr())) {
                if (returned.getSuperFamille().getSynonymes().stream().anyMatch(sf -> sf.getNomFr().equals(genre.getSousTribu().getNomFr()))) {
                    // TODO je dois checker que le nom différent n'est pas déjà enregistré en tant que synonyme

                }
                log.warn(WARN_PARENT_IS_DIFFERENT, famille.getNomFr(), famille.getSuperFamille().getNomFr(), returned.getSuperFamille().getNomFr());
                famille.getSuperFamille().getFamilles().addAll(returned.getSuperFamille().getFamilles());// addAll Enfants
                famille.getSuperFamille().getMicroOrdre().setId(returned.getSuperFamille().getMicroOrdre().getId());// set parent's parent
                famille.getSuperFamille().getSynonymes().addAll(returned.getSuperFamille().getSynonymes());
                returned.getSuperFamille().addSynonymes(famille.getSuperFamille());// add the new inserted as synonyme
                superFamilleRepository.save(returned.getSuperFamille());
            } else {
                famille.getSuperFamille().setId(returned.getSuperFamille().getId());
            }
        }
        SuperFamille superFamille = superRegneWrapper.getSuperFamille();
        SuperFamilleCriteria superFamilleCrit = new SuperFamilleCriteria();
        superFamilleCrit.setNomFr(getStringFilterEquals(superFamille.getNomFr()));
        List<SuperFamille> matchingSuperFamilles = superFamilleQueryService.findByCriteria(superFamilleCrit);
        if ((!superFamille.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingSuperFamilles.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            SuperFamille returned = matchingSuperFamilles.get(0);
            superFamille.setId(returned.getId());
            superFamille.setNomFr(returned.getNomFr());
            superFamille.setNomLatin(returned.getNomLatin());
            log.info("Existing superFamille : " + superFamille);
            if (!Objects.equals(returned.getMicroOrdre().getNomFr(), superFamille.getMicroOrdre().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, superFamille.getNomFr(), superFamille.getMicroOrdre().getNomFr(), returned.getMicroOrdre().getNomFr());
                superFamille.getMicroOrdre().getSuperFamilles().addAll(returned.getMicroOrdre().getSuperFamilles());// addAll Enfants
                superFamille.getMicroOrdre().getInfraOrdre().setId(returned.getMicroOrdre().getInfraOrdre().getId());// set parent's parent
                superFamille.getMicroOrdre().getSynonymes().addAll(returned.getMicroOrdre().getSynonymes());
                returned.getMicroOrdre().addSynonymes(superFamille.getMicroOrdre());// add the new inserted as synonyme
                microOrdreRepository.save(returned.getMicroOrdre());
            } else {
                superFamille.getMicroOrdre().setId(returned.getMicroOrdre().getId());
            }
        }
        MicroOrdre microOrdre = superRegneWrapper.getMicroOrdre();
        MicroOrdreCriteria microOrdreCrit = new MicroOrdreCriteria();
        microOrdreCrit.setNomFr(getStringFilterEquals(microOrdre.getNomFr()));
        List<MicroOrdre> matchingMicroOrdres = microOrdreQueryService.findByCriteria(microOrdreCrit);
        if ((!microOrdre.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingMicroOrdres.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            MicroOrdre returned = matchingMicroOrdres.get(0);
            microOrdre.setId(returned.getId());
            microOrdre.setNomFr(returned.getNomFr());
            microOrdre.setNomLatin(returned.getNomLatin());
            log.info("Existing microOrdre : " + microOrdre);
            if (!Objects.equals(returned.getInfraOrdre().getNomFr(), microOrdre.getInfraOrdre().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, microOrdre.getNomFr(), microOrdre.getInfraOrdre().getNomFr(), returned.getInfraOrdre().getNomFr());
                microOrdre.getInfraOrdre().getMicroOrdres().addAll(returned.getInfraOrdre().getMicroOrdres());// addAll Enfants
                microOrdre.getInfraOrdre().getSousOrdre().setId(returned.getInfraOrdre().getSousOrdre().getId());// set parent's parent
                microOrdre.getInfraOrdre().getSynonymes().addAll(returned.getInfraOrdre().getSynonymes());
                returned.getInfraOrdre().addSynonymes(microOrdre.getInfraOrdre());// add the new inserted as synonyme
                infraOrdreRepository.save(returned.getInfraOrdre());
            } else {
                microOrdre.getInfraOrdre().setId(returned.getInfraOrdre().getId());
            }
        }

        InfraOrdre infraOrdre = superRegneWrapper.getInfraOrdre();
        InfraOrdreCriteria infraOrdreCrit = new InfraOrdreCriteria();
        infraOrdreCrit.setNomFr(getStringFilterEquals(infraOrdre.getNomFr()));
        List<InfraOrdre> matchingInfraOrdres = infraOrdreQueryService.findByCriteria(infraOrdreCrit);
        if ((!infraOrdre.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingInfraOrdres.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            InfraOrdre returned = matchingInfraOrdres.get(0);
            infraOrdre.setId(returned.getId());
            infraOrdre.setNomFr(returned.getNomFr());
            infraOrdre.setNomLatin(returned.getNomLatin());
            log.info("Existing infraOrdre : " + infraOrdre);
            if (!Objects.equals(returned.getSousOrdre().getNomFr(), infraOrdre.getSousOrdre().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, infraOrdre.getNomFr(), infraOrdre.getSousOrdre().getNomFr(), returned.getSousOrdre().getNomFr());
                infraOrdre.getSousOrdre().getInfraOrdres().addAll(returned.getSousOrdre().getInfraOrdres());// addAll Enfants
                infraOrdre.getSousOrdre().getOrdre().setId(returned.getSousOrdre().getOrdre().getId());// set parent's parent
                infraOrdre.getSousOrdre().getSynonymes().addAll(returned.getSousOrdre().getSynonymes());
                returned.getSousOrdre().addSynonymes(infraOrdre.getSousOrdre());// add the new inserted as synonyme
                sousOrdreRepository.save(returned.getSousOrdre());
            } else {
                infraOrdre.getSousOrdre().setId(returned.getSousOrdre().getId());
            }
        }


        SousOrdre sousOrdre = superRegneWrapper.getSousOrdre();
        SousOrdreCriteria sousOrdreCrit = new SousOrdreCriteria();
        sousOrdreCrit.setNomFr(getStringFilterEquals(sousOrdre.getNomFr()));
        List<SousOrdre> matchingSousOrdres = sousOrdreQueryService.findByCriteria(sousOrdreCrit);
        if ((!sousOrdre.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingSousOrdres.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            SousOrdre returned = matchingSousOrdres.get(0);
            sousOrdre.setId(returned.getId());
            sousOrdre.setNomFr(returned.getNomFr());
            sousOrdre.setNomLatin(returned.getNomLatin());
            log.info("Existing sousOrdre : " + sousOrdre);
            if (!Objects.equals(returned.getOrdre().getNomFr(), sousOrdre.getOrdre().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, sousOrdre.getNomFr(), sousOrdre.getOrdre().getNomFr(), returned.getOrdre().getNomFr());
                sousOrdre.getOrdre().getSousOrdres().addAll(returned.getOrdre().getSousOrdres());// addAll Enfants
                sousOrdre.getOrdre().getSuperOrdre().setId(returned.getOrdre().getSuperOrdre().getId());// set parent's parent
                sousOrdre.getOrdre().getSynonymes().addAll(returned.getOrdre().getSynonymes());
                returned.getOrdre().addSynonymes(sousOrdre.getOrdre());// add the new inserted as synonyme
                ordreRepository.save(returned.getOrdre());
            } else {
                sousOrdre.getOrdre().setId(returned.getOrdre().getId());
            }
        }


        Ordre ordre = superRegneWrapper.getOrdre();
        OrdreCriteria ordreCrit = new OrdreCriteria();
        ordreCrit.setNomFr(getStringFilterEquals(ordre.getNomFr()));
        List<Ordre> matchingOrdres = ordreQueryService.findByCriteria(ordreCrit);
        if ((!ordre.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingOrdres.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            Ordre returned = matchingOrdres.get(0);
            ordre.setId(returned.getId());
            ordre.setNomFr(returned.getNomFr());
            ordre.setNomLatin(returned.getNomLatin());
            log.info("Existing ordre : " + ordre);
            if (!Objects.equals(returned.getSuperOrdre().getNomFr(), ordre.getSuperOrdre().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, ordre.getNomFr(), ordre.getSuperOrdre().getNomFr(), returned.getSuperOrdre().getNomFr());
                ordre.getSuperOrdre().getOrdres().addAll(returned.getSuperOrdre().getOrdres());// addAll Enfants
                ordre.getSuperOrdre().getInfraClasse().setId(returned.getSuperOrdre().getInfraClasse().getId());// set parent's parent
                ordre.getSuperOrdre().getSynonymes().addAll(returned.getSuperOrdre().getSynonymes());
                returned.getSuperOrdre().addSynonymes(ordre.getSuperOrdre());// add the new inserted as synonyme
                superOrdreRepository.save(returned.getSuperOrdre());
            } else {
                ordre.getSuperOrdre().setId(returned.getSuperOrdre().getId());
            }
        }
        SuperOrdre superOrdre = superRegneWrapper.getSuperOrdre();
        SuperOrdreCriteria superOrdreCrit = new SuperOrdreCriteria();
        superOrdreCrit.setNomFr(getStringFilterEquals(superOrdre.getNomFr()));
        List<SuperOrdre> matchingSuperOrdres = superOrdreQueryService.findByCriteria(superOrdreCrit);
        if ((!superOrdre.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingSuperOrdres.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            SuperOrdre returned = matchingSuperOrdres.get(0);
            superOrdre.setId(returned.getId());
            superOrdre.setNomFr(returned.getNomFr());
            superOrdre.setNomLatin(returned.getNomLatin());
            log.info("Existing superOrdre : " + superOrdre);
            if (!Objects.equals(returned.getInfraClasse().getNomFr(), superOrdre.getInfraClasse().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, superOrdre.getNomFr(), superOrdre.getInfraClasse().getNomFr(), returned.getInfraClasse().getNomFr());
                superOrdre.getInfraClasse().getSuperOrdres().addAll(returned.getInfraClasse().getSuperOrdres());// addAll Enfants
                superOrdre.getInfraClasse().getSousClasse().setId(returned.getInfraClasse().getSousClasse().getId());// set parent's parent
                superOrdre.getInfraClasse().getSynonymes().addAll(returned.getInfraClasse().getSynonymes());
                returned.getInfraClasse().addSynonymes(superOrdre.getInfraClasse());// add the new inserted as synonyme
                infraClasseRepository.save(returned.getInfraClasse());
            } else {
                superOrdre.getInfraClasse().setId(returned.getInfraClasse().getId());
            }
        }
        InfraClasse infraClasse = superRegneWrapper.getInfraClasse();
        InfraClasseCriteria infraClasseCrit = new InfraClasseCriteria();
        infraClasseCrit.setNomFr(getStringFilterEquals(infraClasse.getNomFr()));
        List<InfraClasse> matchingInfraClasses = infraClasseQueryService.findByCriteria(infraClasseCrit);
        if ((!infraClasse.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingInfraClasses.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            InfraClasse returned = matchingInfraClasses.get(0);
            infraClasse.setId(returned.getId());
            infraClasse.setNomFr(returned.getNomFr());
            infraClasse.setNomLatin(returned.getNomLatin());
            log.info("Existing infraClasse : " + infraClasse);
            if (!Objects.equals(returned.getSousClasse().getNomFr(), infraClasse.getSousClasse().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, infraClasse.getNomFr(), infraClasse.getSousClasse().getNomFr(), returned.getSousClasse().getNomFr());
                infraClasse.getSousClasse().getInfraClasses().addAll(returned.getSousClasse().getInfraClasses());// addAll Enfants
                infraClasse.getSousClasse().getClasse().setId(returned.getSousClasse().getClasse().getId());// set parent's parent
                infraClasse.getSousClasse().getSynonymes().addAll(returned.getSousClasse().getSynonymes());
                returned.getSousClasse().addSynonymes(infraClasse.getSousClasse());// add the new inserted as synonyme
                sousClasseRepository.save(returned.getSousClasse());
            } else {
                infraClasse.getSousClasse().setId(returned.getSousClasse().getId());
            }
        }
        SousClasse sousClasse = superRegneWrapper.getSousClasse();
        SousClasseCriteria sousClasseCrit = new SousClasseCriteria();
        sousClasseCrit.setNomFr(getStringFilterEquals(sousClasse.getNomFr()));
        List<SousClasse> matchingSousClasses = sousClasseQueryService.findByCriteria(sousClasseCrit);
        if ((!sousClasse.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingSousClasses.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            SousClasse returned = matchingSousClasses.get(0);
            sousClasse.setId(returned.getId());
            sousClasse.setNomFr(returned.getNomFr());
            sousClasse.setNomLatin(returned.getNomLatin());
            log.info("Existing sousClasse : " + sousClasse);
            if (!Objects.equals(returned.getClasse().getNomFr(), sousClasse.getClasse().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, sousClasse.getNomFr(), sousClasse.getClasse().getNomFr(), returned.getClasse().getNomFr());
                sousClasse.getClasse().getSousClasses().addAll(returned.getClasse().getSousClasses());// addAll Enfants
                sousClasse.getClasse().getSuperClasse().setId(returned.getClasse().getSuperClasse().getId());// set parent's parent
                sousClasse.getClasse().getSynonymes().addAll(returned.getClasse().getSynonymes());
                returned.getClasse().addSynonymes(sousClasse.getClasse());// add the new inserted as synonyme
                classeRepository.save(returned.getClasse());
            } else {
                sousClasse.getClasse().setId(returned.getClasse().getId());
            }
        }
        Classe classe = superRegneWrapper.getClasse();
        ClasseCriteria classeCrit = new ClasseCriteria();
        classeCrit.setNomFr(getStringFilterEquals(classe.getNomFr()));
        List<Classe> matchingClasses = classeQueryService.findByCriteria(classeCrit);
        if ((!classe.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingClasses.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            Classe returned = matchingClasses.get(0);
            classe.setId(returned.getId());
            classe.setNomFr(returned.getNomFr());
            classe.setNomLatin(returned.getNomLatin());
            log.info("Existing classe : " + classe);
            if (!Objects.equals(returned.getSuperClasse().getNomFr(), classe.getSuperClasse().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, classe.getNomFr(), classe.getSuperClasse().getNomFr(), returned.getSuperClasse().getNomFr());
                classe.getSuperClasse().getClasses().addAll(returned.getSuperClasse().getClasses());// addAll Enfants
                classe.getSuperClasse().getMicroEmbranchement().setId(returned.getSuperClasse().getMicroEmbranchement().getId());// set parent's parent
                classe.getSuperClasse().getSynonymes().addAll(returned.getSuperClasse().getSynonymes());
                returned.getSuperClasse().addSynonymes(classe.getSuperClasse());// add the new inserted as synonyme
                superClasseRepository.save(returned.getSuperClasse());
            } else {
                classe.getSuperClasse().setId(returned.getSuperClasse().getId());
            }
        }
        SuperClasse superClasse = superRegneWrapper.getSuperClasse();
        SuperClasseCriteria superClasseCrit = new SuperClasseCriteria();
        superClasseCrit.setNomFr(getStringFilterEquals(superClasse.getNomFr()));
        List<SuperClasse> matchingSuperClasses = superClasseQueryService.findByCriteria(superClasseCrit);
        if ((!superClasse.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingSuperClasses.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            SuperClasse returned = matchingSuperClasses.get(0);
            superClasse.setId(returned.getId());
            superClasse.setNomFr(returned.getNomFr());
            superClasse.setNomLatin(returned.getNomLatin());
            log.info("Existing superClasse : " + superClasse);
            if (!Objects.equals(returned.getMicroEmbranchement().getNomFr(), superClasse.getMicroEmbranchement().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, superClasse.getNomFr(), superClasse.getMicroEmbranchement().getNomFr(), returned.getMicroEmbranchement().getNomFr());
                superClasse.getMicroEmbranchement().getSuperClasses().addAll(returned.getMicroEmbranchement().getSuperClasses());// addAll Enfants
                superClasse.getMicroEmbranchement().getInfraEmbranchement().setId(returned.getMicroEmbranchement().getInfraEmbranchement().getId());// set parent's parent
                superClasse.getMicroEmbranchement().getSynonymes().addAll(returned.getMicroEmbranchement().getSynonymes());
                returned.getMicroEmbranchement().addSynonymes(superClasse.getMicroEmbranchement());// add the new inserted as synonyme
                microEmbranchementRepository.save(returned.getMicroEmbranchement());
            } else {
                superClasse.getMicroEmbranchement().setId(returned.getMicroEmbranchement().getId());
            }
        }
        MicroEmbranchement microEmbranchement = superRegneWrapper.getMicroEmbranchement();
        MicroEmbranchementCriteria microEmbranchementCrit = new MicroEmbranchementCriteria();
        microEmbranchementCrit.setNomFr(getStringFilterEquals(microEmbranchement.getNomFr()));
        List<MicroEmbranchement> matchingMicroEmbranchements = microEmbranchementQueryService.findByCriteria(microEmbranchementCrit);
        if ((!microEmbranchement.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingMicroEmbranchements.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            MicroEmbranchement returned = matchingMicroEmbranchements.get(0);
            microEmbranchement.setId(returned.getId());
            microEmbranchement.setNomFr(returned.getNomFr());
            microEmbranchement.setNomLatin(returned.getNomLatin());
            log.info("Existing microEmbranchement : " + microEmbranchement);
            if (!Objects.equals(returned.getInfraEmbranchement().getNomFr(), microEmbranchement.getInfraEmbranchement().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, microEmbranchement.getNomFr(), microEmbranchement.getInfraEmbranchement().getNomFr(), returned.getInfraEmbranchement().getNomFr());
                microEmbranchement.getInfraEmbranchement().getMicroEmbranchements().addAll(returned.getInfraEmbranchement().getMicroEmbranchements());// addAll Enfants
                microEmbranchement.getInfraEmbranchement().getSousDivision().setId(returned.getInfraEmbranchement().getSousDivision().getId());// set parent's parent
                microEmbranchement.getInfraEmbranchement().getSynonymes().addAll(returned.getInfraEmbranchement().getSynonymes());
                returned.getInfraEmbranchement().addSynonymes(microEmbranchement.getInfraEmbranchement());// add the new inserted as synonyme
                infraEmbranchementRepository.save(returned.getInfraEmbranchement());
            } else {
                microEmbranchement.getInfraEmbranchement().setId(returned.getInfraEmbranchement().getId());
            }
        }
        InfraEmbranchement infraEmbranchement = superRegneWrapper.getInfraEmbranchement();
        InfraEmbranchementCriteria infraEmbranchementCrit = new InfraEmbranchementCriteria();
        infraEmbranchementCrit.setNomFr(getStringFilterEquals(infraEmbranchement.getNomFr()));
        List<InfraEmbranchement> matchingInfraEmbranchements = infraEmbranchementQueryService.findByCriteria(infraEmbranchementCrit);
        if ((!infraEmbranchement.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingInfraEmbranchements.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            InfraEmbranchement returned = matchingInfraEmbranchements.get(0);
            infraEmbranchement.setId(returned.getId());
            infraEmbranchement.setNomFr(returned.getNomFr());
            infraEmbranchement.setNomLatin(returned.getNomLatin());
            log.info("Existing infraEmbranchement : " + infraEmbranchement);
            if (!Objects.equals(returned.getSousDivision().getNomFr(), infraEmbranchement.getSousDivision().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, infraEmbranchement.getNomFr(), infraEmbranchement.getSousDivision().getNomFr(), returned.getSousDivision().getNomFr());
                infraEmbranchement.getSousDivision().getInfraEmbranchements().addAll(returned.getSousDivision().getInfraEmbranchements());// addAll Enfants
                infraEmbranchement.getSousDivision().getDivision().setId(returned.getSousDivision().getDivision().getId());// set parent's parent
                infraEmbranchement.getSousDivision().getSynonymes().addAll(returned.getSousDivision().getSynonymes());
                returned.getSousDivision().addSynonymes(infraEmbranchement.getSousDivision());// add the new inserted as synonyme
                sousDivisionRepository.save(returned.getSousDivision());
            } else {
                infraEmbranchement.getSousDivision().setId(returned.getSousDivision().getId());
            }
        }
        SousDivision sousDivision = superRegneWrapper.getSousDivision();
        SousDivisionCriteria sousDivisionCrit = new SousDivisionCriteria();
        sousDivisionCrit.setNomFr(getStringFilterEquals(sousDivision.getNomFr()));
        List<SousDivision> matchingSousDivisions = sousDivisionQueryService.findByCriteria(sousDivisionCrit);
        if ((!sousDivision.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingSousDivisions.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            SousDivision returned = matchingSousDivisions.get(0);
            sousDivision.setId(returned.getId());
            sousDivision.setNomFr(returned.getNomFr());
            sousDivision.setNomLatin(returned.getNomLatin());
            log.info("Existing sousDivision : " + sousDivision);
            if (!Objects.equals(returned.getDivision().getNomFr(), sousDivision.getDivision().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, sousDivision.getNomFr(), sousDivision.getDivision().getNomFr(), returned.getDivision().getNomFr());
                sousDivision.getDivision().getSousDivisions().addAll(returned.getDivision().getSousDivisions());// addAll Enfants
                sousDivision.getDivision().getSuperDivision().setId(returned.getDivision().getSuperDivision().getId());// set parent's parent
                sousDivision.getDivision().getSynonymes().addAll(returned.getDivision().getSynonymes());
                returned.getDivision().addSynonymes(sousDivision.getDivision());// add the new inserted as synonyme
                divisionRepository.save(returned.getDivision());
            } else {
                sousDivision.getDivision().setId(returned.getDivision().getId());
            }
        }
        Division division = superRegneWrapper.getDivision();
        DivisionCriteria divisionCrit = new DivisionCriteria();
        divisionCrit.setNomFr(getStringFilterEquals(division.getNomFr()));
        List<Division> matchingDivisions = divisionQueryService.findByCriteria(divisionCrit);
        if ((!division.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingDivisions.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            Division returned = matchingDivisions.get(0);
            division.setId(returned.getId());
            division.setNomFr(returned.getNomFr());
            division.setNomLatin(returned.getNomLatin());
            log.info("Existing division : " + division);
            if (!Objects.equals(returned.getSuperDivision().getNomFr(), division.getSuperDivision().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, division.getNomFr(), division.getSuperDivision().getNomFr(), returned.getSuperDivision().getNomFr());
                division.getSuperDivision().getDivisions().addAll(returned.getSuperDivision().getDivisions());// addAll Enfants
                division.getSuperDivision().getInfraRegne().setId(returned.getSuperDivision().getInfraRegne().getId());// set parent's parent
                division.getSuperDivision().getSynonymes().addAll(returned.getSuperDivision().getSynonymes());
                returned.getSuperDivision().addSynonymes(division.getSuperDivision());// add the new inserted as synonyme
                superDivisionRepository.save(returned.getSuperDivision());
            } else {
                division.getSuperDivision().setId(returned.getSuperDivision().getId());
            }
        }
        SuperDivision superDivision = superRegneWrapper.getSuperDivision();
        SuperDivisionCriteria superDivisionCrit = new SuperDivisionCriteria();
        superDivisionCrit.setNomFr(getStringFilterEquals(superDivision.getNomFr()));
        List<SuperDivision> matchingSuperDivisions = superDivisionQueryService.findByCriteria(superDivisionCrit);
        if ((!superDivision.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingSuperDivisions.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            SuperDivision returned = matchingSuperDivisions.get(0);
            superDivision.setId(returned.getId());
            superDivision.setNomFr(returned.getNomFr());
            superDivision.setNomLatin(returned.getNomLatin());
            log.info("Existing superDivision : " + superDivision);
            if (!Objects.equals(returned.getInfraRegne().getNomFr(), superDivision.getInfraRegne().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, superDivision.getNomFr(), superDivision.getInfraRegne().getNomFr(), returned.getInfraRegne().getNomFr());
                superDivision.getInfraRegne().getSuperDivisions().addAll(returned.getInfraRegne().getSuperDivisions());// addAll Enfants
                superDivision.getInfraRegne().getRameau().setId(returned.getInfraRegne().getRameau().getId());// set parent's parent
                superDivision.getInfraRegne().getSynonymes().addAll(returned.getInfraRegne().getSynonymes());
                returned.getInfraRegne().addSynonymes(superDivision.getInfraRegne());// add the new inserted as synonyme
                infraRegneRepository.save(returned.getInfraRegne());
            } else {
                superDivision.getInfraRegne().setId(returned.getInfraRegne().getId());
            }
        }
        InfraRegne infraRegne = superRegneWrapper.getInfraRegne();
        InfraRegneCriteria infraRegneCrit = new InfraRegneCriteria();
        infraRegneCrit.setNomFr(getStringFilterEquals(infraRegne.getNomFr()));
        List<InfraRegne> matchingInfraRegnes = infraRegneQueryService.findByCriteria(infraRegneCrit);
        if ((!infraRegne.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingInfraRegnes.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            InfraRegne returned = matchingInfraRegnes.get(0);
            infraRegne.setId(returned.getId());
            infraRegne.setNomFr(returned.getNomFr());
            infraRegne.setNomLatin(returned.getNomLatin());
            log.info("Existing infraRegne : " + infraRegne);
            if (!Objects.equals(returned.getRameau().getNomFr(), infraRegne.getRameau().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, infraRegne.getNomFr(), infraRegne.getRameau().getNomFr(), returned.getRameau().getNomFr());
                infraRegne.getRameau().getInfraRegnes().addAll(returned.getRameau().getInfraRegnes());// addAll Enfants
                infraRegne.getRameau().getSousRegne().setId(returned.getRameau().getSousRegne().getId());// set parent's parent
                infraRegne.getRameau().getSynonymes().addAll(returned.getRameau().getSynonymes());
                returned.getRameau().addSynonymes(infraRegne.getRameau());// add the new inserted as synonyme
                rameauRepository.save(returned.getRameau());
            } else {
                infraRegne.getRameau().setId(returned.getRameau().getId());
            }
        }
        Rameau rameau = superRegneWrapper.getRameau();
        RameauCriteria rameauCrit = new RameauCriteria();
        rameauCrit.setNomFr(getStringFilterEquals(rameau.getNomFr()));
        List<Rameau> matchingRameaus = rameauQueryService.findByCriteria(rameauCrit);
        if ((!rameau.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingRameaus.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            Rameau returned = matchingRameaus.get(0);
            rameau.setId(returned.getId());
            rameau.setNomFr(returned.getNomFr());
            rameau.setNomLatin(returned.getNomLatin());
            log.info("Existing rameau : " + rameau);
            if (!Objects.equals(returned.getSousRegne().getNomFr(), rameau.getSousRegne().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, rameau.getNomFr(), rameau.getSousRegne().getNomFr(), returned.getSousRegne().getNomFr());
                rameau.getSousRegne().getRameaus().addAll(returned.getSousRegne().getRameaus());// addAll Enfants
                rameau.getSousRegne().getRegne().setId(returned.getSousRegne().getRegne().getId());// set parent's parent
                rameau.getSousRegne().getSynonymes().addAll(returned.getSousRegne().getSynonymes());
                returned.getSousRegne().addSynonymes(rameau.getSousRegne());// add the new inserted as synonyme
                sousRegneRepository.save(returned.getSousRegne());
            } else {
                rameau.getSousRegne().setId(returned.getSousRegne().getId());
            }
        }
        SousRegne sousRegne = superRegneWrapper.getSousRegne();
        SousRegneCriteria sousRegneCrit = new SousRegneCriteria();
        sousRegneCrit.setNomFr(getStringFilterEquals(sousRegne.getNomFr()));
        List<SousRegne> matchingSousRegnes = sousRegneQueryService.findByCriteria(sousRegneCrit);
        if ((!sousRegne.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingSousRegnes.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            SousRegne returned = matchingSousRegnes.get(0);
            sousRegne.setId(returned.getId());
            sousRegne.setNomFr(returned.getNomFr());
            sousRegne.setNomLatin(returned.getNomLatin());
            log.info("Existing sousRegne : " + sousRegne);
            if (!Objects.equals(returned.getRegne().getNomFr(), sousRegne.getRegne().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, sousRegne.getNomFr(), sousRegne.getRegne().getNomFr(), returned.getRegne().getNomFr());
                sousRegne.getRegne().getSousRegnes().addAll(returned.getRegne().getSousRegnes());// addAll Enfants
                sousRegne.getRegne().getSuperRegne().setId(returned.getRegne().getSuperRegne().getId());// set parent's parent
                sousRegne.getRegne().getSynonymes().addAll(returned.getRegne().getSynonymes());
                returned.getRegne().addSynonymes(sousRegne.getRegne());// add the new inserted as synonyme
                regneRepository.save(returned.getRegne());
            } else {
                sousRegne.getRegne().setId(returned.getRegne().getId());
            }
        }
        Regne regne = superRegneWrapper.getRegne();
        RegneCriteria regneCrit = new RegneCriteria();
        regneCrit.setNomFr(getStringFilterEquals(regne.getNomFr()));
        List<Regne> matchingRegnes = regneQueryService.findByCriteria(regneCrit);
        if ((!regne.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingRegnes.size() > 0) {
            isCheckingAncestorsIntegrity = true;
            Regne returned = matchingRegnes.get(0);
            regne.setId(returned.getId());
            regne.setNomFr(returned.getNomFr());
            regne.setNomLatin(returned.getNomLatin());
            log.info("Existing regne : " + regne);
            if (!Objects.equals(returned.getSuperRegne().getNomFr(), regne.getSuperRegne().getNomFr())) {
                log.warn(WARN_PARENT_IS_DIFFERENT, regne.getNomFr(), regne.getSuperRegne().getNomFr(), returned.getSuperRegne().getNomFr());
                regne.getSuperRegne().getRegnes().addAll(returned.getSuperRegne().getRegnes());// addAll Enfants
                regne.getSuperRegne().getSuperRegne().setId(returned.getSuperRegne().getSuperRegne().getId());// set parent's parent
                regne.getSuperRegne().getSynonymes().addAll(returned.getSuperRegne().getSynonymes());
                returned.getSuperRegne().addSynonymes(regne.getSuperRegne());// add the new inserted as synonyme
                superRegneRepository.save(returned.getSuperRegne());
            } else {
                regne.getSuperRegne().setId(returned.getSuperRegne().getId());
            }
        }
        SuperRegne superRegne = superRegneWrapper.getSuperRegne();
        SuperRegneCriteria superRegneCrit = new SuperRegneCriteria();
        superRegneCrit.setNomFr(getStringFilterEquals(superRegne.getNomFr()));
        List<SuperRegne> matchingSuperRegnes = superRegneQueryService.findByCriteria(superRegneCrit);
        if ((!superRegne.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) || isCheckingAncestorsIntegrity) && matchingSuperRegnes.size() > 0) {
            SuperRegne returned = matchingSuperRegnes.get(0);
            superRegne.setId(returned.getId());
            superRegne.setNomFr(returned.getNomFr());
            superRegne.setNomLatin(returned.getNomLatin());
            log.info("Existing superRegne : " + superRegneWrapper);
        }
    }

    @NotNull
    private StringFilter getStringFilterEquals(String nomFr) {
        StringFilter nomFrFilter = new StringFilter();
        nomFrFilter.equals(nomFr);
        return nomFrFilter;
    }

    private void saveOrUpdateSuperRegne(@NotNull SuperRegne superRegne) {
        if (superRegne.getId() == null) {
            superRegneRepository.save(superRegne);
        }
        saveOrUpdateRegne(superRegne.getRegnes().stream().findFirst().get());
    }

    private void saveOrUpdateRegne(@NotNull Regne regne) {
        if (regne.getId() == null) {
            regneRepository.save(regne);
        }
        saveOrUpdateSousRegne(regne.getSousRegnes().stream().findFirst().get());
    }

    private void saveOrUpdateSousRegne(@NotNull SousRegne sousRegne) {
        if (sousRegne.getId() == null) {
            sousRegneRepository.save(sousRegne);
        }
        saveOrUpdateRameau(sousRegne.getRameaus().stream().findFirst().get());
    }

    private void saveOrUpdateRameau(@NotNull Rameau rameau) {
        if (rameau.getId() == null) {
            rameauRepository.save(rameau);
        }
        saveOrUpdateInfraRegne(rameau.getInfraRegnes().stream().findFirst().get());
    }

    private void saveOrUpdateInfraRegne(@NotNull InfraRegne infraRegne) {
        if (infraRegne.getId() == null) {
            infraRegneRepository.save(infraRegne);
        }

        saveOrUpdateSuperDivision(infraRegne.getSuperDivisions().stream().findFirst().get());
    }

    private void saveOrUpdateSuperDivision(@NotNull SuperDivision superDivision) {
        if (superDivision.getId() == null) {
            superDivisionRepository.save(superDivision);
        }

        saveOrUpdateDivision(superDivision.getDivisions().stream().findFirst().get());
    }

    private void saveOrUpdateDivision(@NotNull Division division) {
        if (division.getId() == null) {
            divisionRepository.save(division);
        }

        saveOrUpdateSousDivision(division.getSousDivisions().stream().findFirst().get());
    }

    private void saveOrUpdateSousDivision(@NotNull SousDivision sousDivision) {
        if (sousDivision.getId() == null) {
            sousDivisionRepository.save(sousDivision);
        }

        saveOrUpdateInfraEmbranchement(sousDivision.getInfraEmbranchements().stream().findFirst().get());
    }

    private void saveOrUpdateInfraEmbranchement(@NotNull InfraEmbranchement infraEmbranchement) {
        if (infraEmbranchement.getId() == null) {
            infraEmbranchementRepository.save(infraEmbranchement);
        }

        saveOrUpdateMicroEmbranchement(infraEmbranchement.getMicroEmbranchements().stream().findFirst().get());
    }

    private void saveOrUpdateMicroEmbranchement(@NotNull MicroEmbranchement microEmbranchement) {
        if (microEmbranchement.getId() == null) {
            microEmbranchementRepository.save(microEmbranchement);
        }

        saveOrUpdateSuperClasse(microEmbranchement.getSuperClasses().stream().findFirst().get());
    }

    private void saveOrUpdateSuperClasse(@NotNull SuperClasse superClasse) {
        if (superClasse.getId() == null) {
            superClasseRepository.save(superClasse);
        }

        saveOrUpdateClasse(superClasse.getClasses().stream().findFirst().get());
    }

    private void saveOrUpdateClasse(@NotNull Classe classe) {
        if (classe.getId() == null) {
            classeRepository.save(classe);
        }

        saveOrUpdateSousClasse(classe.getSousClasses().stream().findFirst().get());
    }

    private void saveOrUpdateSousClasse(@NotNull SousClasse sousClasse) {
        if (sousClasse.getId() == null) {
            sousClasseRepository.save(sousClasse);
        }

        saveOrUpdateInfraClasse(sousClasse.getInfraClasses().stream().findFirst().get());
    }

    private void saveOrUpdateInfraClasse(@NotNull InfraClasse infraClasse) {
        if (infraClasse.getId() == null) {
            infraClasseRepository.save(infraClasse);
        }

        saveOrUpdateSuperOrdre(infraClasse.getSuperOrdres().stream().findFirst().get());
    }

    private void saveOrUpdateSuperOrdre(@NotNull SuperOrdre superOrdre) {
        if (superOrdre.getId() == null) {
            superOrdreRepository.save(superOrdre);
        }

        saveOrUpdateOrdre(superOrdre.getOrdres().stream().findFirst().get());
    }

    private void saveOrUpdateOrdre(@NotNull Ordre ordre) {
        if (ordre.getId() == null) {
            ordreRepository.save(ordre);
        }

        saveOrUpdateSousOrdre(ordre.getSousOrdres().stream().findFirst().get());
    }

    private void saveOrUpdateSousOrdre(@NotNull SousOrdre sousOrdre) {
        if (sousOrdre.getId() == null) {
            sousOrdreRepository.save(sousOrdre);
        }

        saveOrUpdateInfraOrdre(sousOrdre.getInfraOrdres().stream().findFirst().get());
    }

    private void saveOrUpdateInfraOrdre(@NotNull InfraOrdre infraOrdre) {
        if (infraOrdre.getId() == null) {
            infraOrdreRepository.save(infraOrdre);
        }

        saveOrUpdateMicroOrdre(infraOrdre.getMicroOrdres().stream().findFirst().get());
    }

    private void saveOrUpdateMicroOrdre(@NotNull MicroOrdre microOrdre) {
        if (microOrdre.getId() == null) {
            microOrdreRepository.save(microOrdre);
        }

        saveOrUpdateSuperFamille(microOrdre.getSuperFamilles().stream().findFirst().get());
    }

    private void saveOrUpdateSuperFamille(@NotNull SuperFamille superFamille) {
        if (superFamille.getId() == null) {
            superFamilleRepository.save(superFamille);
        }

        saveOrUpdateFamille(superFamille.getFamilles().stream().findFirst().get());
    }

    private void saveOrUpdateFamille(@NotNull Famille famille) {
        if (famille.getId() == null) {
            familleRepository.save(famille);
        }

        saveOrUpdateSousFamille(famille.getSousFamilles().stream().findFirst().get());
    }

    private void saveOrUpdateSousFamille(@NotNull SousFamille sousFamille) {
        if (sousFamille.getId() == null) {
            sousFamilleRepository.save(sousFamille);
        }

        saveOrUpdateTribu(sousFamille.getTribuses().stream().findFirst().get());
    }

    private void saveOrUpdateTribu(@NotNull Tribu tribu) {
        if (tribu.getId() == null) {
            tribuRepository.save(tribu);
        }

        saveOrUpdateSousTribu(tribu.getSousTribuses().stream().findFirst().get());
    }

    private void saveOrUpdateSousTribu(@NotNull SousTribu sousTribu) {
        if (sousTribu.getId() == null) {
            sousTribuRepository.save(sousTribu);
        }

        saveOrUpdateGenre(sousTribu.getGenres().stream().findFirst().get());
    }

    private void saveOrUpdateGenre(@NotNull Genre genre) {
        if (genre.getId() == null) {
            genreRepository.save(genre);
        }

        saveOrUpdateSousGenre(genre.getSousGenres().stream().findFirst().get());
    }

    private void saveOrUpdateSousGenre(@NotNull SousGenre sousGenre) {
        if (sousGenre.getId() == null) {
            sousGenreRepository.save(sousGenre);
        }

        saveOrUpdateSection(sousGenre.getSections().stream().findFirst().get());
    }

    private void saveOrUpdateSection(@NotNull Section section) {
        if (section.getId() == null) {
            sectionRepository.save(section);
        }

        saveOrUpdateSousSection(section.getSousSections().stream().findFirst().get());
    }

    private void saveOrUpdateSousSection(@NotNull SousSection sousSection) {
        if (sousSection.getId() == null) {
            sousSectionRepository.save(sousSection);
        }

        saveOrUpdateEspece(sousSection.getEspeces().stream().findFirst().get());
    }

    private void saveOrUpdateEspece(@NotNull Espece espece) {
        if (espece.getId() == null) {
            especeRepository.save(espece);
        }

        saveOrUpdateSousEspece(espece.getSousEspeces().stream().findFirst().get());
    }

    private void saveOrUpdateSousEspece(@NotNull SousEspece sousEspece) {
        if (sousEspece.getId() == null) {
            sousEspeceRepository.save(sousEspece);
        }

        saveOrUpdateVariete(sousEspece.getVarietes().stream().findFirst().get());
    }

    private void saveOrUpdateVariete(@NotNull Variete variete) {
        if (variete.getId() == null) {
            varieteRepository.save(variete);
        }

        saveOrUpdateSousVariete(variete.getSousVarietes().stream().findFirst().get());
    }

    private void saveOrUpdateSousVariete(@NotNull SousVariete sousVariete) {
        if (sousVariete.getId() == null) {
            sousVarieteRepository.save(sousVariete);
        }

        saveOrUpdateForme(sousVariete.getFormes().stream().findFirst().get());
    }

    private void saveOrUpdateForme(@NotNull Forme forme) {
        if (forme.getId() == null) {
            formeRepository.save(forme);
        }

        saveOrUpdateSousForme(forme.getSousFormes().stream().findFirst().get());
    }

    private void saveOrUpdateSousForme(@NotNull SousForme sousForme) {
        if (sousForme.getId() == null) {
            sousFormeRepository.save(sousForme);
        }
    }
}

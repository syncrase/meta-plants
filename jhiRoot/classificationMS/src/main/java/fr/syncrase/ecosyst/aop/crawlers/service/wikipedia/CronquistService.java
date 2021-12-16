package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.repository.*;
import fr.syncrase.ecosyst.service.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CronquistService {

    /**
     * Deux rangs taxonomiques sont séparés par d'autres rangs dont on ne connait pas forcément le nom.<br>
     * Une valeur par défaut permet de lier ces deux rangs avec des rangs vides.<br>
     * Si ultérieurement ces rangs sont déterminés, les valeurs par défaut sont mises à jour
     */
    public static final String DEFAULT_NAME_FOR_CONNECTOR_RANK = "";
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

    public SuperRegneRepository getSuperRegneRepository() {
        return superRegneRepository;
    }

    @Autowired
    public void setSuperRegneRepository(SuperRegneRepository superRegneRepository) {
        this.superRegneRepository = superRegneRepository;
    }

    public RegneRepository getRegneRepository() {
        return regneRepository;
    }

    @Autowired
    public void setRegneRepository(RegneRepository regneRepository) {
        this.regneRepository = regneRepository;
    }

    public SousRegneRepository getSousRegneRepository() {
        return sousRegneRepository;
    }

    @Autowired
    public void setSousRegneRepository(SousRegneRepository sousRegneRepository) {
        this.sousRegneRepository = sousRegneRepository;
    }

    public RameauRepository getRameauRepository() {
        return rameauRepository;
    }

    @Autowired
    public void setRameauRepository(RameauRepository rameauRepository) {
        this.rameauRepository = rameauRepository;
    }

    public InfraRegneRepository getInfraRegneRepository() {
        return infraRegneRepository;
    }

    @Autowired
    public void setInfraRegneRepository(InfraRegneRepository infraRegneRepository) {
        this.infraRegneRepository = infraRegneRepository;
    }

    public SuperDivisionRepository getSuperDivisionRepository() {
        return superDivisionRepository;
    }

    @Autowired
    public void setSuperDivisionRepository(SuperDivisionRepository superDivisionRepository) {
        this.superDivisionRepository = superDivisionRepository;
    }

    public DivisionRepository getDivisionRepository() {
        return divisionRepository;
    }

    @Autowired
    public void setDivisionRepository(DivisionRepository divisionRepository) {
        this.divisionRepository = divisionRepository;
    }

    public SousDivisionRepository getSousDivisionRepository() {
        return sousDivisionRepository;
    }

    @Autowired
    public void setSousDivisionRepository(SousDivisionRepository sousDivisionRepository) {
        this.sousDivisionRepository = sousDivisionRepository;
    }

    public InfraEmbranchementRepository getInfraEmbranchementRepository() {
        return infraEmbranchementRepository;
    }

    @Autowired
    public void setInfraEmbranchementRepository(InfraEmbranchementRepository infraEmbranchementRepository) {
        this.infraEmbranchementRepository = infraEmbranchementRepository;
    }

    public MicroEmbranchementRepository getMicroEmbranchementRepository() {
        return microEmbranchementRepository;
    }

    @Autowired
    public void setMicroEmbranchementRepository(MicroEmbranchementRepository microEmbranchementRepository) {
        this.microEmbranchementRepository = microEmbranchementRepository;
    }

    public SuperClasseRepository getSuperClasseRepository() {
        return superClasseRepository;
    }

    @Autowired
    public void setSuperClasseRepository(SuperClasseRepository superClasseRepository) {
        this.superClasseRepository = superClasseRepository;
    }

    public ClasseRepository getClasseRepository() {
        return classeRepository;
    }

    @Autowired
    public void setClasseRepository(ClasseRepository classeRepository) {
        this.classeRepository = classeRepository;
    }

    public SousClasseRepository getSousClasseRepository() {
        return sousClasseRepository;
    }

    @Autowired
    public void setSousClasseRepository(SousClasseRepository sousClasseRepository) {
        this.sousClasseRepository = sousClasseRepository;
    }

    public InfraClasseRepository getInfraClasseRepository() {
        return infraClasseRepository;
    }

    @Autowired
    public void setInfraClasseRepository(InfraClasseRepository infraClasseRepository) {
        this.infraClasseRepository = infraClasseRepository;
    }

    public SuperOrdreRepository getSuperOrdreRepository() {
        return superOrdreRepository;
    }

    @Autowired
    public void setSuperOrdreRepository(SuperOrdreRepository superOrdreRepository) {
        this.superOrdreRepository = superOrdreRepository;
    }

    public OrdreRepository getOrdreRepository() {
        return ordreRepository;
    }

    @Autowired
    public void setOrdreRepository(OrdreRepository ordreRepository) {
        this.ordreRepository = ordreRepository;
    }

    public SousOrdreRepository getSousOrdreRepository() {
        return sousOrdreRepository;
    }

    @Autowired
    public void setSousOrdreRepository(SousOrdreRepository sousOrdreRepository) {
        this.sousOrdreRepository = sousOrdreRepository;
    }

    public InfraOrdreRepository getInfraOrdreRepository() {
        return infraOrdreRepository;
    }

    @Autowired
    public void setInfraOrdreRepository(InfraOrdreRepository infraOrdreRepository) {
        this.infraOrdreRepository = infraOrdreRepository;
    }

    public MicroOrdreRepository getMicroOrdreRepository() {
        return microOrdreRepository;
    }

    @Autowired
    public void setMicroOrdreRepository(MicroOrdreRepository microOrdreRepository) {
        this.microOrdreRepository = microOrdreRepository;
    }

    public SuperFamilleRepository getSuperFamilleRepository() {
        return superFamilleRepository;
    }

    @Autowired
    public void setSuperFamilleRepository(SuperFamilleRepository superFamilleRepository) {
        this.superFamilleRepository = superFamilleRepository;
    }

    public FamilleRepository getFamilleRepository() {
        return familleRepository;
    }

    @Autowired
    public void setFamilleRepository(FamilleRepository familleRepository) {
        this.familleRepository = familleRepository;
    }

    public SousFamilleRepository getSousFamilleRepository() {
        return sousFamilleRepository;
    }

    @Autowired
    public void setSousFamilleRepository(SousFamilleRepository sousFamilleRepository) {
        this.sousFamilleRepository = sousFamilleRepository;
    }

    public TribuRepository getTribuRepository() {
        return tribuRepository;
    }

    @Autowired
    public void setTribuRepository(TribuRepository tribuRepository) {
        this.tribuRepository = tribuRepository;
    }

    public SousTribuRepository getSousTribuRepository() {
        return sousTribuRepository;
    }

    @Autowired
    public void setSousTribuRepository(SousTribuRepository sousTribuRepository) {
        this.sousTribuRepository = sousTribuRepository;
    }

    public GenreRepository getGenreRepository() {
        return genreRepository;
    }

    @Autowired
    public void setGenreRepository(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public SousGenreRepository getSousGenreRepository() {
        return sousGenreRepository;
    }

    @Autowired
    public void setSousGenreRepository(SousGenreRepository sousGenreRepository) {
        this.sousGenreRepository = sousGenreRepository;
    }

    public SectionRepository getSectionRepository() {
        return sectionRepository;
    }

    @Autowired
    public void setSectionRepository(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public SousSectionRepository getSousSectionRepository() {
        return sousSectionRepository;
    }

    @Autowired
    public void setSousSectionRepository(SousSectionRepository sousSectionRepository) {
        this.sousSectionRepository = sousSectionRepository;
    }

    public EspeceRepository getEspeceRepository() {
        return especeRepository;
    }

    @Autowired
    public void setEspeceRepository(EspeceRepository especeRepository) {
        this.especeRepository = especeRepository;
    }

    public SousEspeceRepository getSousEspeceRepository() {
        return sousEspeceRepository;
    }

    @Autowired
    public void setSousEspeceRepository(SousEspeceRepository sousEspeceRepository) {
        this.sousEspeceRepository = sousEspeceRepository;
    }

    public VarieteRepository getVarieteRepository() {
        return varieteRepository;
    }

    @Autowired
    public void setVarieteRepository(VarieteRepository varieteRepository) {
        this.varieteRepository = varieteRepository;
    }

    public SousVarieteRepository getSousVarieteRepository() {
        return sousVarieteRepository;
    }

    @Autowired
    public void setSousVarieteRepository(SousVarieteRepository sousVarieteRepository) {
        this.sousVarieteRepository = sousVarieteRepository;
    }

    public FormeRepository getFormeRepository() {
        return formeRepository;
    }

    @Autowired
    public void setFormeRepository(FormeRepository formeRepository) {
        this.formeRepository = formeRepository;
    }

    public SousFormeRepository getSousFormeRepository() {
        return sousFormeRepository;
    }

    @Autowired
    public void setSousFormeRepository(SousFormeRepository sousFormeRepository) {
        this.sousFormeRepository = sousFormeRepository;
    }

    public SuperRegneQueryService getSuperRegneQueryService() {
        return superRegneQueryService;
    }

    @Autowired
    public void setSuperRegneQueryService(SuperRegneQueryService superRegneQueryService) {
        this.superRegneQueryService = superRegneQueryService;
    }

    public RegneQueryService getRegneQueryService() {
        return regneQueryService;
    }

    @Autowired
    public void setRegneQueryService(RegneQueryService regneQueryService) {
        this.regneQueryService = regneQueryService;
    }

    public SousRegneQueryService getSousRegneQueryService() {
        return sousRegneQueryService;
    }

    @Autowired
    public void setSousRegneQueryService(SousRegneQueryService sousRegneQueryService) {
        this.sousRegneQueryService = sousRegneQueryService;
    }

    public RameauQueryService getRameauQueryService() {
        return rameauQueryService;
    }

    @Autowired
    public void setRameauQueryService(RameauQueryService rameauQueryService) {
        this.rameauQueryService = rameauQueryService;
    }

    public InfraRegneQueryService getInfraRegneQueryService() {
        return infraRegneQueryService;
    }

    @Autowired
    public void setInfraRegneQueryService(InfraRegneQueryService infraRegneQueryService) {
        this.infraRegneQueryService = infraRegneQueryService;
    }

    public SuperDivisionQueryService getSuperDivisionQueryService() {
        return superDivisionQueryService;
    }

    @Autowired
    public void setSuperDivisionQueryService(SuperDivisionQueryService superDivisionQueryService) {
        this.superDivisionQueryService = superDivisionQueryService;
    }

    public DivisionQueryService getDivisionQueryService() {
        return divisionQueryService;
    }

    @Autowired
    public void setDivisionQueryService(DivisionQueryService divisionQueryService) {
        this.divisionQueryService = divisionQueryService;
    }

    public SousDivisionQueryService getSousDivisionQueryService() {
        return sousDivisionQueryService;
    }

    @Autowired
    public void setSousDivisionQueryService(SousDivisionQueryService sousDivisionQueryService) {
        this.sousDivisionQueryService = sousDivisionQueryService;
    }

    public InfraEmbranchementQueryService getInfraEmbranchementQueryService() {
        return infraEmbranchementQueryService;
    }

    @Autowired
    public void setInfraEmbranchementQueryService(InfraEmbranchementQueryService infraEmbranchementQueryService) {
        this.infraEmbranchementQueryService = infraEmbranchementQueryService;
    }

    public MicroEmbranchementQueryService getMicroEmbranchementQueryService() {
        return microEmbranchementQueryService;
    }

    @Autowired
    public void setMicroEmbranchementQueryService(MicroEmbranchementQueryService microEmbranchementQueryService) {
        this.microEmbranchementQueryService = microEmbranchementQueryService;
    }

    public SuperClasseQueryService getSuperClasseQueryService() {
        return superClasseQueryService;
    }

    @Autowired
    public void setSuperClasseQueryService(SuperClasseQueryService superClasseQueryService) {
        this.superClasseQueryService = superClasseQueryService;
    }

    public ClasseQueryService getClasseQueryService() {
        return classeQueryService;
    }

    @Autowired
    public void setClasseQueryService(ClasseQueryService classeQueryService) {
        this.classeQueryService = classeQueryService;
    }

    public SousClasseQueryService getSousClasseQueryService() {
        return sousClasseQueryService;
    }

    @Autowired
    public void setSousClasseQueryService(SousClasseQueryService sousClasseQueryService) {
        this.sousClasseQueryService = sousClasseQueryService;
    }

    public InfraClasseQueryService getInfraClasseQueryService() {
        return infraClasseQueryService;
    }

    @Autowired
    public void setInfraClasseQueryService(InfraClasseQueryService infraClasseQueryService) {
        this.infraClasseQueryService = infraClasseQueryService;
    }

    public SuperOrdreQueryService getSuperOrdreQueryService() {
        return superOrdreQueryService;
    }

    @Autowired
    public void setSuperOrdreQueryService(SuperOrdreQueryService superOrdreQueryService) {
        this.superOrdreQueryService = superOrdreQueryService;
    }

    public OrdreQueryService getOrdreQueryService() {
        return ordreQueryService;
    }

    @Autowired
    public void setOrdreQueryService(OrdreQueryService ordreQueryService) {
        this.ordreQueryService = ordreQueryService;
    }

    public SousOrdreQueryService getSousOrdreQueryService() {
        return sousOrdreQueryService;
    }

    @Autowired
    public void setSousOrdreQueryService(SousOrdreQueryService sousOrdreQueryService) {
        this.sousOrdreQueryService = sousOrdreQueryService;
    }

    public InfraOrdreQueryService getInfraOrdreQueryService() {
        return infraOrdreQueryService;
    }

    @Autowired
    public void setInfraOrdreQueryService(InfraOrdreQueryService infraOrdreQueryService) {
        this.infraOrdreQueryService = infraOrdreQueryService;
    }

    public MicroOrdreQueryService getMicroOrdreQueryService() {
        return microOrdreQueryService;
    }

    @Autowired
    public void setMicroOrdreQueryService(MicroOrdreQueryService microOrdreQueryService) {
        this.microOrdreQueryService = microOrdreQueryService;
    }

    public SuperFamilleQueryService getSuperFamilleQueryService() {
        return superFamilleQueryService;
    }

    @Autowired
    public void setSuperFamilleQueryService(SuperFamilleQueryService superFamilleQueryService) {
        this.superFamilleQueryService = superFamilleQueryService;
    }

    public FamilleQueryService getFamilleQueryService() {
        return familleQueryService;
    }

    @Autowired
    public void setFamilleQueryService(FamilleQueryService familleQueryService) {
        this.familleQueryService = familleQueryService;
    }

    public SousFamilleQueryService getSousFamilleQueryService() {
        return sousFamilleQueryService;
    }

    @Autowired
    public void setSousFamilleQueryService(SousFamilleQueryService sousFamilleQueryService) {
        this.sousFamilleQueryService = sousFamilleQueryService;
    }

    public TribuQueryService getTribuQueryService() {
        return tribuQueryService;
    }

    @Autowired
    public void setTribuQueryService(TribuQueryService tribuQueryService) {
        this.tribuQueryService = tribuQueryService;
    }

    public SousTribuQueryService getSousTribuQueryService() {
        return sousTribuQueryService;
    }

    @Autowired
    public void setSousTribuQueryService(SousTribuQueryService sousTribuQueryService) {
        this.sousTribuQueryService = sousTribuQueryService;
    }

    public GenreQueryService getGenreQueryService() {
        return genreQueryService;
    }

    @Autowired
    public void setGenreQueryService(GenreQueryService genreQueryService) {
        this.genreQueryService = genreQueryService;
    }

    public SousGenreQueryService getSousGenreQueryService() {
        return sousGenreQueryService;
    }

    @Autowired
    public void setSousGenreQueryService(SousGenreQueryService sousGenreQueryService) {
        this.sousGenreQueryService = sousGenreQueryService;
    }

    public SectionQueryService getSectionQueryService() {
        return sectionQueryService;
    }

    @Autowired
    public void setSectionQueryService(SectionQueryService sectionQueryService) {
        this.sectionQueryService = sectionQueryService;
    }

    public SousSectionQueryService getSousSectionQueryService() {
        return sousSectionQueryService;
    }

    @Autowired
    public void setSousSectionQueryService(SousSectionQueryService sousSectionQueryService) {
        this.sousSectionQueryService = sousSectionQueryService;
    }

    public EspeceQueryService getEspeceQueryService() {
        return especeQueryService;
    }

    @Autowired
    public void setEspeceQueryService(EspeceQueryService especeQueryService) {
        this.especeQueryService = especeQueryService;
    }

    public SousEspeceQueryService getSousEspeceQueryService() {
        return sousEspeceQueryService;
    }

    @Autowired
    public void setSousEspeceQueryService(SousEspeceQueryService sousEspeceQueryService) {
        this.sousEspeceQueryService = sousEspeceQueryService;
    }

    public VarieteQueryService getVarieteQueryService() {
        return varieteQueryService;
    }

    @Autowired
    public void setVarieteQueryService(VarieteQueryService varieteQueryService) {
        this.varieteQueryService = varieteQueryService;
    }

    public SousVarieteQueryService getSousVarieteQueryService() {
        return sousVarieteQueryService;
    }

    @Autowired
    public void setSousVarieteQueryService(SousVarieteQueryService sousVarieteQueryService) {
        this.sousVarieteQueryService = sousVarieteQueryService;
    }

    public FormeQueryService getFormeQueryService() {
        return formeQueryService;
    }

    @Autowired
    public void setFormeQueryService(FormeQueryService formeQueryService) {
        this.formeQueryService = formeQueryService;
    }

    public SousFormeQueryService getSousFormeQueryService() {
        return sousFormeQueryService;
    }

    @Autowired
    public void setSousFormeQueryService(SousFormeQueryService sousFormeQueryService) {
        this.sousFormeQueryService = sousFormeQueryService;
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
     * @param cronquistClassification side effect
     */
    public void saveCronquist(@NotNull CronquistClassification cronquistClassification) {

        // Les rangs inférieurs sans valeur n'ont pas de raison d'être puisque la raison d'être des rangs vides et de lier deux rangs dont les noms sont connus : nettoyage de l'arborescence au commencement
        cronquistClassification.clearTail();
        List<CronquistRank> list = cronquistClassification.getAsList(this);
        // Différence de taille entre la classification connue et celle que l'on souhaite enregistrer
        Integer offset = 0;
        // Parcours du plus bas rang jusqu'au plus élevé pour trouver le premier rang existant en base
        List<CronquistRank> existingClassification = null;
        for (CronquistRank cronquistRank : list) {
            // question : si je modifie un élément dans le liste, ce même élément dans cronquistClassification est-il modifié ? OUI dans la liste entre parent et enfants ainsi que dans le cronquistClassification
            // TODO possible : retirer l'implémentation de getParent et getChildren dans les domain + method default qui throw une erreur pour dire qu'elle n'est pas implémentée (puisque je le définie dans le wrapper)
            // Si je n'ai toujours pas récupéré de rang connu, je regarde encore pour ce nouveau rang
            if (existingClassification == null) {
                List<? extends CronquistRank> existing = cronquistRank.findExistingRank();
                // Si je viens d'en trouver un
                if (existing != null && existing.size() != 0) {
                    // À partir de ce moment-là, il n'est plus nécessaire d'interroger la base.
                    // On a obtenu l'arborescence ascendante complète du rang taxonomique
                    existingClassification = getCronquistRankList(existing);
                } else {
                    offset++;
                }
            }
        }

        setIdsOfExistingRanks(list, existingClassification, offset);
        save(list);
    }


    private void save(@NotNull List<CronquistRank> list) {
        // Etant donné qu'un rang inférieur doit contenir le rang supérieur, l'enregistrement des classifications doit se faire en commençant par le rang supérieur
        int size = list.size() - 1;
        for (int i = 0; i < list.size(); i++) {
            // TODO question les IDs des parents sont-ils bien contenus dans les enfants ?
            list.get(size - i).save();
        }
    }

    /**
     * Mis à jour des IDs des éléments de la première liste pour qu'ils pointent vers les éléments déjà enregistrés en base
     *
     * @param rankToInsertList           Liste des rangs que l'on souhaite enregistrer. Aucun des rangs n'a d'ID
     * @param existingClassificationList List des rangs de l'arborescence
     * @param offset                     Écart de taille entre les deux listes. Correspond à tous les rangs qu'il a fallu tester pour obtenir un rang existant en base de données
     */
    private void setIdsOfExistingRanks(@NotNull List<CronquistRank> rankToInsertList, List<CronquistRank> existingClassificationList, int offset) {
        // checks
        if (existingClassificationList == null || existingClassificationList.size() == 0) {
            log.info("No existing entity in the database");
            return;
        }
        // Les deux premiers items sont égaux
        if (!rankToInsertList.get(offset).getNomFr().equals(existingClassificationList.get(0).getNomFr())) {
            log.error("Erreur : les deux premiers items égaux sont censé être égaux");
            return;
        }
        // Les tailles sont cohérentes
        if (rankToInsertList.size() != existingClassificationList.size() + offset) {
            log.error("Impossible de traiter ces deux listes. Tailles incohérentes.{offset = {}, ranksSize = {}, existingRanksSize = {}}", offset, rankToInsertList.size(), existingClassificationList.size());
            return;
        }

        CronquistRank existingRank, rankToInsert;
        // Parcours des éléments à partir de l'élément connu en base pour compléter avec les IDs correspondants
        for (int i = offset; i < rankToInsertList.size(); i++) {
            // Possibilité de sortir cette gestion pour ne pas mélanger avec la récupération du premier item connu
            existingRank = existingClassificationList.get(i - offset);
            rankToInsert = rankToInsertList.get(i);
            // Chaine vide && chaine non vide
            if (rankToInsert.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && !existingRank.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
                // mon rang intermédiaire se trouve avoir un nom en base → ajout du nom et de l'id dans mon objet
                rankToInsert.setId(existingRank.getId());
                rankToInsert.setNomFr(existingRank.getNomFr());
                continue;
            }
            // Quelque current && chaine vide
            if (existingRank.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
                // mon rang avec (ou sans) nom n'en a pas en base → ajout de l'id à mon rang pour que soit update le nom trouvé
                rankToInsert.setId(existingRank.getId());
                continue;
            }
            // Chaine non vide && chaine non vide
            if (!rankToInsert.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK) && !existingRank.getNomFr().equals(DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
                // Les deux noms existent
                if (!rankToInsert.getNomFr().equals(existingRank.getNomFr())) {
                    // TODO gestion des synonymes (après mise à jour du modèle)
                    // Ils sont différents
                    // En base j'ai un autre nom, je la crois et je met à jour mon objet
                    rankToInsert.setId(existingRank.getId());
                    rankToInsert.setNomFr(existingRank.getNomFr());
                } else {
                    // Ils sont égaux
                    rankToInsert.setId(existingRank.getId());
                }
            }
        }
    }

    private @NotNull List<CronquistRank> getCronquistRankList(@NotNull List<? extends CronquistRank> existing) {
        List<CronquistRank> existingClassification = new ArrayList<>();
        CronquistRank cronquistRank = existing.get(0);
        // currentRank != null dans le cas du super regne qui retourne null en parent
        for (CronquistRank currentRank = cronquistRank.get(); currentRank != null && currentRank.get() != null; currentRank = currentRank.getParent()) {
            existingClassification.add(currentRank);
        }
        return existingClassification;
    }

}

package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.classification.AtomicCronquistRank;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.mappers.CronquistRankMapper;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.wrapper.ClassificationNomWrapper;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.wrapper.CronquistRankWrapper;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.exceptions.ClassificationReconstructionException;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.exceptions.InconsistentRank;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.exceptions.MoreThanOneResultException;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.exceptions.UnknownRankId;
import fr.syncrase.ecosyst.domain.*;
import fr.syncrase.ecosyst.domain.enumeration.RankName;
import fr.syncrase.ecosyst.repository.ClassificationNomRepository;
import fr.syncrase.ecosyst.repository.CronquistRankRepository;
import fr.syncrase.ecosyst.repository.UrlRepository;
import fr.syncrase.ecosyst.service.ClassificationNomQueryService;
import fr.syncrase.ecosyst.service.CronquistRankQueryService;
import fr.syncrase.ecosyst.service.criteria.ClassificationNomCriteria;
import fr.syncrase.ecosyst.service.criteria.CronquistRankCriteria;
import org.apache.commons.collections4.map.LinkedMap;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class ClassificationRepository {

    private final Logger log = LoggerFactory.getLogger(ClassificationRepository.class);

    private ClassificationNomQueryService classificationNomQueryService;
    private CronquistRankQueryService cronquistRankQueryService;
    private CronquistRankRepository cronquistRankRepository;
    private ClassificationNomRepository classificationNomRepository;
    private UrlRepository urlRepository;

    @Autowired
    public void setUrlRepository(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Autowired
    public void setClassificationNomRepository(ClassificationNomRepository classificationNomRepository) {
        this.classificationNomRepository = classificationNomRepository;
    }

    @Autowired
    public void setCronquistRankRepository(CronquistRankRepository cronquistRankRepository) {
        this.cronquistRankRepository = cronquistRankRepository;
    }

    @Autowired
    public void setClassificationNomQueryService(ClassificationNomQueryService classificationNomQueryService) {
        this.classificationNomQueryService = classificationNomQueryService;
    }

    @Autowired
    public void setCronquistRankQueryService(CronquistRankQueryService cronquistRankQueryService) {
        this.cronquistRankQueryService = cronquistRankQueryService;
    }

    public CronquistClassificationBranch findExistingPartOfThisClassification(@NotNull CronquistClassificationBranch classification) throws ClassificationReconstructionException, MoreThanOneResultException {
        CronquistClassificationBranch existingClassification = null;
        List<ICronquistRank> cronquistRanks = new ArrayList<>(classification.getClassification());
        ICronquistRank cronquistRank;
        for (int i = cronquistRanks.size() - 1; i >= 0; i--) {
            cronquistRank = findExistingRank(cronquistRanks.get(i));
            if (cronquistRank != null) {

                existingClassification = findExistingClassification(cronquistRank);
                assert existingClassification != null;
                break;// I got it!
            }
        }
        return existingClassification;
    }

    /**
     * Récupère la classification ascendante à partir du rang passé en paramètre
     *
     * @param cronquistRank Rang dont il faut vérifier la présence en base
     * @return Le rang qui correspond. Null si le rang n'existe pas
     */
    @Nullable
    public CronquistClassificationBranch findExistingClassification(@NotNull ICronquistRank cronquistRank) throws ClassificationReconstructionException, MoreThanOneResultException {
        ICronquistRank existingRank = findExistingRank(cronquistRank);
        if (existingRank != null) {
            return new CronquistClassificationBranch(existingRank);
        }
        return null;
    }

    public Set<ICronquistRank> getTaxons(ICronquistRank parentRank) {
        Set<ICronquistRank> taxons = new HashSet<>();
        try {
            ICronquistRank existingParent = queryForCronquistRank(parentRank);
            if (existingParent != null) {
                existingParent.getChildren().forEach(taxon -> taxons.add(new CronquistRankWrapper(taxon)));
                return taxons;
            }
        } catch (MoreThanOneResultException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * Merge la classification du rangScrapé dans la brancheCible.
     * Cette méthode est utilisée quand on se rend compte que deux rangs différents correspondent en fait au même rang.
     * Cette méthode met à jour les données en base de manière à pouvoir insérer sans conflit le rang scrapé
     *
     * @return Le rang scrapé mis à jour pour être rendu cohérent avec les données déjà présentes en base
     */
    public ICronquistRank merge(
        @NotNull CronquistClassificationBranch brancheCible,
        @NotNull ICronquistRank scrappedRankFoundInDatabase
                               ) throws UnknownRankId, MoreThanOneResultException, InconsistentRank, ClassificationReconstructionException {
        // TODO move the merge logic into another class : ClassificationMerger
        CronquistClassificationBranch brancheSource = findExistingClassification(scrappedRankFoundInDatabase);
        assert brancheSource != null;
        RankName replacedRankName = scrappedRankFoundInDatabase.getRankName();

        ICronquistRank rankWithNewNames = copyDataFromRankToRank(
            brancheSource.getRang(replacedRankName),
            brancheCible.getRang(replacedRankName)
                                                                );

        ICronquistRank rankWithAllHisNewTaxons = switchAncestry(
            brancheSource.getRang(replacedRankName),
            rankWithNewNames
                                                               );
        brancheCible.put(replacedRankName, rankWithAllHisNewTaxons);

        supprimeLiaisonAscendante(brancheSource.getRang(replacedRankName));

        return brancheCible.getRang(replacedRankName);
    }

    /**
     * @param cronquistRank Rang dont il faut vérifier la présence en base
     * @return Le rang qui correspond. Null si le rang n'existe pas
     */
    @Nullable
    public ICronquistRank findExistingRank(@NotNull ICronquistRank cronquistRank) throws MoreThanOneResultException {
        if (cronquistRank.isRangDeLiaison() && cronquistRank.getId() == null) {
            return null;
        }
        return queryForCronquistRank(cronquistRank);
    }

    @Contract(pure = true)
    private Set<ICronquistRank> findTaxons(@NotNull ICronquistRank rank) {
        LongFilter rankIdFilter = new LongFilter();
        rankIdFilter.setEquals(rank.getId());

        CronquistRankCriteria rankCrit = new CronquistRankCriteria();
        rankCrit.setId(rankIdFilter);

        return cronquistRankQueryService.findByCriteria(rankCrit).get(0).getChildren().stream().map(CronquistRankWrapper::new).collect(Collectors.toSet());
    }

    private void supprimeLiaisonAscendante(@NotNull ICronquistRank rangAPartirDuquelLAscendanceEstSupprimee) {
        Optional<CronquistRank> baseDesRangsASupprimer = cronquistRankRepository.findById(rangAPartirDuquelLAscendanceEstSupprimee.getId());
        baseDesRangsASupprimer.ifPresent(rang -> {
            ICronquistRank wrapper = new CronquistRankWrapper(rang);
            do {
                Set<Long> nomsIds = wrapper.getNoms().stream().map(IClassificationNom::getId).collect(Collectors.toSet());
                log.debug("Suppression des noms " + nomsIds);
                classificationNomRepository.deleteAllById(nomsIds);
                Set<Long> urlsIds = wrapper.getUrls().stream().map(IUrl::getId).collect(Collectors.toSet());
                log.debug("Suppression des urls " + urlsIds);
                urlRepository.deleteAllById(urlsIds);
                log.debug("Suppression du wrapper " + wrapper.getId());
                cronquistRankRepository.deleteById(wrapper.getId());
                wrapper = new CronquistRankWrapper(wrapper.getParent());
            } while (wrapper.isRangDeLiaison());
        });
    }

    /**
     * Les taxons du rangSource changent de parents.
     * Les changements sont effectifs après l'exécution de la méthode
     *
     * @param rangSource Rang qui perd tous ses taxons
     * @param rangCible  Rang qui récupère tous les taxons
     * @throws MoreThanOneResultException Si l'un des rangs ne permet pas de récupérer un unique résultat en base de données
     */
    @Contract("_, _ -> new")
    private @NotNull ICronquistRank switchAncestry(ICronquistRank rangSource, ICronquistRank rangCible) throws MoreThanOneResultException {
        // rangCible est déjà synchronisé avec la DB, TODO ne pas fetch de nouveau ?
        ICronquistRank newParent = queryForCronquistRank(rangCible);
        assert newParent != null;

        // Tous les taxons changent de parent
        // → enregistrement des taxons avec le nouveau parent
        Set<ICronquistRank> taxons = findTaxons(rangSource);
        Set<CronquistRank> taxonsWithNewParent = taxons.stream().map(taxon -> {
            CronquistRank cronquistRank = taxon.getCronquistRank();
            cronquistRank.setParent(newParent.getCronquistRank());
            newParent.getCronquistRank().addChildren(cronquistRank);
            return cronquistRank;
        }).collect(Collectors.toSet());
        cronquistRankRepository.saveAll(taxonsWithNewParent);


        // Enregistrement de l'ancien parent sans ses taxons
        ICronquistRank aSupprimer = queryForCronquistRank(new AtomicCronquistRank().id(rangSource.getId()));// TODO pas nécessaire de fetch, je l'ai déjà
        assert aSupprimer != null;
        aSupprimer.removeTaxons();
        cronquistRankRepository.save(aSupprimer.getCronquistRank());

        return new CronquistRankWrapper(cronquistRankRepository.save(newParent.getCronquistRank()));
    }

    /**
     * Associe tous les noms du rangSource au rangCible
     * Associe toutes les urls du rangSource au rangCible
     * Les modifications sont effectives à l'exécution de la méthode
     *
     * @param rangSource Rang qui est dépossédé de ses noms et urls
     * @param rangCible  Rang qui récupère les noms et urls
     * @return Le rang cible avec ses nouveaux noms et urls
     * @throws UnknownRankId    Quand l'un des rangs passés en paramètre ne permet pas de récupérer un rang existant
     * @throws InconsistentRank Quand un rang de liaison possède plusieurs noms
     */
    @Contract("_, _ -> new")
    private @NotNull ICronquistRank copyDataFromRankToRank(
        @NotNull ICronquistRank rangSource,
        @NotNull ICronquistRank rangCible
                                                          ) throws UnknownRankId, InconsistentRank {
        Optional<CronquistRank> rangQuiSeraSupprimeOptional = cronquistRankRepository.findById(rangSource.getId());
        Optional<CronquistRank> rangQuiRecupereLesInfosOptional = cronquistRankRepository.findById(rangCible.getId());
        if (rangQuiSeraSupprimeOptional.isPresent() && rangQuiRecupereLesInfosOptional.isPresent()) {
            ICronquistRank rangQuiSeraSupprime = new CronquistRankWrapper(rangQuiSeraSupprimeOptional.get());
            ICronquistRank rangQuiRecupereLesInfos = new CronquistRankWrapper(rangQuiRecupereLesInfosOptional.get());
            log.debug("Copie les informations du rang " + rangQuiSeraSupprime.getId() + " vers le rang " + rangQuiRecupereLesInfos.getId());

            addNamesFromRankToRank(rangQuiSeraSupprime, rangQuiRecupereLesInfos);
            rangQuiRecupereLesInfos.addAllUrlsToCronquistRank(rangQuiSeraSupprime.getUrls());

            // Enregistrement du rang avec les nouveaux noms
            CronquistRankWrapper rangCibleAInserer = new CronquistRankWrapper(cronquistRankRepository.save(rangQuiRecupereLesInfos.getCronquistRank()));

            // Enregistrement des noms avec le nouveau rang
            rangCibleAInserer.newNames().forEach(classificationNom -> classificationNom.setCronquistRank(rangCibleAInserer.getCronquistRank()));
            classificationNomRepository.saveAll(rangCibleAInserer.newNames());

            // Enregistrement des urls avec le nouveau rang
            rangCibleAInserer.newUrls().forEach(url -> url.setCronquistRank(rangCibleAInserer.getCronquistRank()));
            urlRepository.saveAll(rangCibleAInserer.newUrls());

            // Suppression des noms et des urls de l'ancien rang
            rangQuiSeraSupprime.removeNames();
            rangQuiSeraSupprime.removeUrls();
            cronquistRankRepository.save(rangQuiSeraSupprime.getCronquistRank());
            return new CronquistRankWrapper(rangCibleAInserer.getCronquistRank());
        } else {
            throw new UnknownRankId();
        }

    }

    private void addNamesFromRankToRank(
        @NotNull ICronquistRank rangSource,
        @NotNull ICronquistRank rangCible
                                       ) throws InconsistentRank {
        if (rangCible.isRangDeLiaison() && rangSource.isRangSignificatif()) {
            if (rangCible.getNoms().size() != 1) {
                throw new InconsistentRank("Le rang de liaison ne doit pas posséder plus d'un nom " + rangCible);
            }
            classificationNomRepository.deleteAllById(
                rangCible.getNoms().stream().map(IClassificationNom::getId).collect(Collectors.toSet())
                                                     );
        }
        rangCible.addAllNamesToCronquistRank(rangSource.getNoms());
    }

    private @NotNull Set<IClassificationNom> findExistingNames(@NotNull ICronquistRank cronquistRank) {
        Set<IClassificationNom> noms = new HashSet<>();
        Iterator<IClassificationNom> nomIterator = cronquistRank.getNoms().iterator();
        IClassificationNom nom;
        while (nomIterator.hasNext()) {
            nom = nomIterator.next();
            if (nom.getId() != null) {
                noms.add(nom);
            } else {

                StringFilter classifNomFilter = new StringFilter();
                classifNomFilter.setEquals(nom.getNomFr());

                ClassificationNomCriteria classificationNomCriteria = new ClassificationNomCriteria();
                classificationNomCriteria.setNomFr(classifNomFilter);

                List<ClassificationNom> classificationNoms = classificationNomQueryService.findByCriteria(classificationNomCriteria);
                if (classificationNoms.size() == 1) {// Soit inexistant, soit un unique résultat (unique constraint)
                    //                    nom.setId(classificationNoms.get(0).getId());
                    noms.add(new ClassificationNomWrapper(classificationNoms.get(0)));
                }
            }
        }
        return noms;
    }

    private @Nullable ICronquistRank queryForCronquistRank(@NotNull ICronquistRank cronquistRank) throws MoreThanOneResultException {
        CronquistRankCriteria rankCrit = new CronquistRankCriteria();

        if (cronquistRank.getId() != null) {
            LongFilter idFilter = new LongFilter();
            idFilter.setEquals(cronquistRank.getId());
            rankCrit.setId(idFilter);
        }

        if (!cronquistRank.getNoms().isEmpty()) {
            Set<IClassificationNom> noms = findExistingNames(cronquistRank);
            LongFilter nomFilter = new LongFilter();
            nomFilter.setIn(noms.stream().map(IClassificationNom::getId).filter(Objects::nonNull).collect(Collectors.toList()));
            rankCrit.setNomsId(nomFilter);
        }

        if (cronquistRank.getRankName() != null) {
            CronquistRankCriteria.CronquistTaxonomikRanksFilter rankFilter = new CronquistRankCriteria.CronquistTaxonomikRanksFilter();
            rankFilter.setEquals(cronquistRank.getRankName());
            rankCrit.setRank(rankFilter);
        }

        rankCrit.setDistinct(true);
        List<CronquistRank> rank = cronquistRankQueryService.findByCriteria(rankCrit);
        switch (rank.size()) {
            case 0:
                return null;
            case 1:
                return new CronquistRankWrapper(rank.get(0));
            //                return rank.get(0);
            default:
                throw new MoreThanOneResultException("Provided criteria do not correspond to only one result : " + cronquistRank);
        }
    }

    /**
     * Enregistre la classification du rang supérieur (Super règne) jusqu'au rang le plus profond
     *
     * @param consistentClassification Classification à enregistrer. Aucune vérification sur la consistence des données ne se fait dans la méthode
     * @return La branche de classification qui vient d'être enregistrée
     */
    public CronquistClassificationBranch save(@NotNull CronquistClassificationBranch consistentClassification) {

        if (consistentClassification.getClassification() == null) {
            return null;
        }
        CronquistRankMapper mapper = new CronquistRankMapper();
        LinkedMap<RankName, CronquistRank> classificationToSave = mapper.getClassificationToSave(consistentClassification);
        // L'enregistrement des classifications doit se faire en commençant par le rang supérieur
        RankName[] rankNames = RankName.values();
        CronquistClassificationBranch savedClassification = new CronquistClassificationBranch();
        for (RankName rankName : rankNames) {
            CronquistRank rank = classificationToSave.get(rankName);
            if (rank == null) {
                break;
            }
            cronquistRankRepository.save(rank);
            classificationNomRepository.saveAll(rank.getNoms());
            urlRepository.saveAll(rank.getUrls());
            savedClassification.put(rankName, new CronquistRankWrapper(rank));
        }
        savedClassification.clearTail();
        return savedClassification;
    }
}

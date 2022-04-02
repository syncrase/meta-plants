package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.classification.AtomicCronquistRank;
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

    public CronquistClassificationBranch findExistingPartOfThisClassification(@NotNull CronquistClassificationBranch classification) {
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
    public CronquistClassificationBranch findExistingClassification(@NotNull ICronquistRank cronquistRank) {
        if (cronquistRank.isRangDeLiaison() && cronquistRank.getId() == null) {
            return null;
        }

        cronquistRank = cronquistRank.clone();
        updateNomsIds(cronquistRank);// TODO récupère encore les noms
        if (cronquistRank.getNoms().stream().anyMatch(classificationNom -> classificationNom.getId() != null)) {
            return fetchExistingClassification(cronquistRank);
        }
        return null;
    }

    @Contract("_ -> new")
    public @Nullable CronquistClassificationBranch fetchExistingClassification(@NotNull ICronquistRank cronquistRank) {
        ICronquistRank cronquistRank1;
        try {
            cronquistRank1 = queryForCronquistRank(cronquistRank);// TODO récupère encore le rang
            if (cronquistRank1 != null) {
                return new CronquistClassificationBranch(cronquistRank1);
            }
        } catch (MoreThanOneResultException e) {
            log.debug(e.getMessage());
        } catch (ClassificationReconstructionException e) {
            log.debug("Impossible de reconstruire la classification de ce rang : " + cronquistRank);
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
     * Remplace la portion de classification existante pour qu'elle corresponde à la classification corrigée
     * depuis le rang intermédiaire existant jusqu'au rang taxonomique suivant
     * par cette même portion au-dessus du rang taxonomique récupéré
     * et stocke-les ids des rangs intermédiaires qui ont été déconnectés pour pouvoir les supprimer
     */
    public void merge(
        @NotNull CronquistClassificationBranch brancheCible,
        @NotNull ICronquistRank scrappedRankFoundInDatabase
                     ) throws UnknownRankId, MoreThanOneResultException, InconsistentRank {
// TODO move the merge logic into another class : ClassificationMerger
        CronquistClassificationBranch brancheSource = findExistingClassification(scrappedRankFoundInDatabase);
        assert brancheSource != null;
        RankName replacedRankName = scrappedRankFoundInDatabase.getRank();

        ICronquistRank rankWithNewNames = copyDataFromRankToRank(
            brancheSource.getRang(replacedRankName),
            brancheCible.getRang(replacedRankName)
                                                                );
        brancheCible.put(replacedRankName, rankWithNewNames);

        switchAncestry(
            brancheSource.getRang(replacedRankName),
            brancheCible.getRang(replacedRankName)
                      );

        supprimeLiaisonAscendante(brancheSource.getRang(replacedRankName));

    }

    /**
     * @param cronquistRank Rang dont il faut vérifier la présence en base
     * @return Le rang qui correspond. Null si le rang n'existe pas
     */
    @Nullable
    public ICronquistRank findExistingRank(@NotNull ICronquistRank cronquistRank) {
        cronquistRank = cronquistRank.clone();
        if (cronquistRank.isRangDeLiaison() && cronquistRank.getId() == null) {
            return null;
        }
        updateNomsIds(cronquistRank);
        if (cronquistRank.isAnyNameHasAnId()) {
            return fetchExistingRank(cronquistRank);
        }
        return null;
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

    private void switchAncestry(ICronquistRank rangSource, ICronquistRank rangCible) throws MoreThanOneResultException {
        // rangCible est déjà synchronisé avec la DB, TODO ne pas fetch de nouveau ?
        ICronquistRank newParent = queryForCronquistRank(rangCible);

        // Tous les taxons changent de parent
        Set<ICronquistRank> taxons = findTaxons(rangSource);
        Set<CronquistRank> taxonsWithNewParent = taxons.stream().map(atomicCronquistRank -> {
            CronquistRank cronquistRank = atomicCronquistRank.newRank();
            assert newParent != null;
            cronquistRank.setParent(newParent.newRank());
            return cronquistRank;
        }).collect(Collectors.toSet());
        cronquistRankRepository.saveAll(taxonsWithNewParent);

        // Le parent perd tous ses taxons
        ICronquistRank aSupprimer = queryForCronquistRank(new AtomicCronquistRank().id(rangSource.getId()));// TODO pas nécessaire de fetch, je l'ai déjà
        assert aSupprimer != null;
        aSupprimer.removeTaxons();
        cronquistRankRepository.save(aSupprimer.newRank());

    }

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
            CronquistRankWrapper rangCibleAInserer = new CronquistRankWrapper(cronquistRankRepository.save(rangQuiRecupereLesInfos.newRank()));

            // Enregistrement des noms avec le nouveau rang
            rangCibleAInserer.newNames().forEach(classificationNom -> classificationNom.setCronquistRank(rangCibleAInserer.newRank()));
            classificationNomRepository.saveAll(rangCibleAInserer.newNames());

            // Enregistrement des urls avec le nouveau rang
            rangCibleAInserer.newUrls().forEach(url -> url.setCronquistRank(rangCibleAInserer.newRank()));
            urlRepository.saveAll(rangCibleAInserer.newUrls());

            // Suppression des noms et des urls de l'ancien rang
            rangQuiSeraSupprime.removeNames();
            rangQuiSeraSupprime.removeUrls();
            cronquistRankRepository.save(rangQuiSeraSupprime.newRank());
            return new CronquistRankWrapper(rangCibleAInserer.newRank());
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
//        rangCible.getNoms().forEach(iClassificationNom -> iClassificationNom.set);
    }

    private void updateNomsIds(@NotNull ICronquistRank cronquistRank) {
        Iterator<IClassificationNom> nomIterator = cronquistRank.getNoms().iterator();
        IClassificationNom nom;
        while (nomIterator.hasNext()) {
            nom = nomIterator.next();
            StringFilter classifNomFilter = new StringFilter();
            classifNomFilter.setEquals(nom.getNomFr());

            ClassificationNomCriteria classificationNomCriteria = new ClassificationNomCriteria();
            classificationNomCriteria.setNomFr(classifNomFilter);

            List<ClassificationNom> classificationNoms = classificationNomQueryService.findByCriteria(classificationNomCriteria);
            if (classificationNoms.size() == 1) {// Soit inexistant, soit un unique résultat (unique constraint)
                nom.setId(classificationNoms.get(0).getId());
            }
        }
    }

    private @Nullable ICronquistRank queryForCronquistRank(@NotNull ICronquistRank cronquistRank) throws MoreThanOneResultException {
        CronquistRankCriteria rankCrit = new CronquistRankCriteria();

        if (cronquistRank.getId() != null) {
            LongFilter idFilter = new LongFilter();
            idFilter.setEquals(cronquistRank.getId());
            rankCrit.setId(idFilter);
        }

        if (!cronquistRank.getNoms().isEmpty()) {
            LongFilter nomFilter = new LongFilter();
            nomFilter.setIn(cronquistRank.getNoms().stream().map(IClassificationNom::getId).filter(Objects::nonNull).collect(Collectors.toList()));
            rankCrit.setNomsId(nomFilter);
        }

        if (cronquistRank.getRank() != null) {
            CronquistRankCriteria.CronquistTaxonomikRanksFilter rankFilter = new CronquistRankCriteria.CronquistTaxonomikRanksFilter();
            rankFilter.setEquals(cronquistRank.getRank());
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

    @Contract("_ -> new")
    private @Nullable ICronquistRank fetchExistingRank(@NotNull ICronquistRank cronquistRank) {
        ICronquistRank cronquistRank1;
        try {
            cronquistRank1 = queryForCronquistRank(cronquistRank);
            if (cronquistRank1 != null) {
                //                return new AtomicCronquistRank(cronquistRank1);
                return cronquistRank1;
            }
        } catch (MoreThanOneResultException e) {
            log.debug(e.getMessage());
        }
        return null;
    }
}

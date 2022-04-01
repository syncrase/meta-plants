package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.AtomicCronquistRank;
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
        updateNomsIds(cronquistRank);
        if (cronquistRank.getNoms().stream().anyMatch(classificationNom -> classificationNom.getId() != null)) {
            return fetchExistingClassification(cronquistRank);
        }
        return null;
    }

    @Contract("_ -> new")
    public @Nullable CronquistClassificationBranch fetchExistingClassification(@NotNull ICronquistRank cronquistRank) {
        ICronquistRank cronquistRank1;
        try {
            cronquistRank1 = queryForCronquistRank(cronquistRank);
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

    public Set<ICronquistRank> getTaxons(ICronquistRank cronquistRank) {
        Set<ICronquistRank> taxons = new HashSet<>();
        try {
            ICronquistRank cronquistRank1 = queryForCronquistRank(cronquistRank);
            if (cronquistRank1 != null) {
                cronquistRank1.getChildren().forEach(cronquistRank2 -> taxons.add(new AtomicCronquistRank(cronquistRank2)));
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

        return cronquistRankQueryService.findByCriteria(rankCrit).get(0).getChildren().stream().map(AtomicCronquistRank::new).collect(Collectors.toSet());
    }

    private void supprimeLiaisonAscendante(@NotNull ICronquistRank rangAPartirDuquelLAscendanceEstSupprimee) {
        Optional<CronquistRank> baseDesRangsASupprimer = cronquistRankRepository.findById(rangAPartirDuquelLAscendanceEstSupprimee.getId());
        baseDesRangsASupprimer.ifPresent(rang -> {
            do {
                Set<Long> nomsIds = rang.getNoms().stream().map(ClassificationNom::getId).collect(Collectors.toSet());
                log.debug("Suppression des noms " + nomsIds);
                classificationNomRepository.deleteAllById(nomsIds);
                Set<Long> urlsIds = rang.getUrls().stream().map(Url::getId).collect(Collectors.toSet());
                log.debug("Suppression des urls " + urlsIds);
                urlRepository.deleteAllById(urlsIds);
                log.debug("Suppression du rang " + rang.getId());
                cronquistRankRepository.deleteById(rang.getId());
                rang = rang.getParent();
            } while (AtomicCronquistRank.newRank((ICronquistRank) rang).isRangDeLiaison());
        });
    }

    private void switchAncestry(ICronquistRank rangSource, ICronquistRank rangCible) throws MoreThanOneResultException {
        ICronquistRank newParent = queryForCronquistRank(rangCible);

        Set<ICronquistRank> taxons = findTaxons(rangSource);
        Set<CronquistRank> taxonsWithNewParent = taxons.stream().map(atomicCronquistRank -> {
            CronquistRank cronquistRank = atomicCronquistRank.newRank();
            assert newParent != null;
            cronquistRank.setParent(newParent.newRank());
            return cronquistRank;
        }).collect(Collectors.toSet());
        cronquistRankRepository.saveAll(taxonsWithNewParent);

        ICronquistRank aSupprimer = queryForCronquistRank(new AtomicCronquistRank().id(rangSource.getId()));
        assert aSupprimer != null;
        aSupprimer.getChildren().removeIf(cronquistRank -> true);
        cronquistRankRepository.saveAll(taxonsWithNewParent);

    }

    private @NotNull ICronquistRank copyDataFromRankToRank(
        @NotNull ICronquistRank rangSource,
        ICronquistRank rangCible
                                                          ) throws UnknownRankId, InconsistentRank {
        Optional<CronquistRank> rangQuiSeraSupprime = cronquistRankRepository.findById(rangSource.getId());
        if (rangQuiSeraSupprime.isPresent()) {
            CronquistRank rank = rangQuiSeraSupprime.get();
            log.debug("Copie les informations du rang " + rangSource.getId() + " vers le rang " + rangCible.getId());
            addNamesFromRankToRank(rangSource, rangCible);
            rangCible.addAllUrlsToCronquistRank(rangSource.getUrls());
            CronquistRank rangCibleAInserer = rangCible.newRank();
            CronquistRank save = cronquistRankRepository.save(rangCibleAInserer);

            rangCibleAInserer.getNoms().forEach(classificationNom -> classificationNom.setCronquistRank(save));
            classificationNomRepository.saveAll(rangCibleAInserer.getNoms());

            rangCibleAInserer.getUrls().forEach(url -> url.setCronquistRank(save));
            urlRepository.saveAll(rangCibleAInserer.getUrls());

            rank.getNoms().removeIf(classificationNom -> true);
            rank.getUrls().removeIf(classificationNom -> true);
            cronquistRankRepository.save(rank);
            return AtomicCronquistRank.newRank(new CronquistRankWrapper((save)));
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
                return new AtomicCronquistRank(cronquistRank1);
            }
        } catch (MoreThanOneResultException e) {
            log.debug(e.getMessage());
        }
        return null;
    }
}

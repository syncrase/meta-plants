package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification;

import fr.syncrase.ecosyst.domain.ClassificationNom;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.Url;
import fr.syncrase.ecosyst.domain.enumeration.CronquistTaxonomikRanks;
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
        List<AtomicCronquistRank> cronquistRanks = new ArrayList<>(classification.getClassification());
        AtomicCronquistRank cronquistRank;
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
    public CronquistClassificationBranch findExistingClassification(@NotNull AtomicCronquistRank cronquistRank) {
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

    @Contract(pure = true)
    private Set<AtomicCronquistRank> findTaxons(@NotNull AtomicCronquistRank rank) {
        LongFilter rankIdFilter = new LongFilter();
        rankIdFilter.setEquals(rank.getId());

        CronquistRankCriteria rankCrit = new CronquistRankCriteria();
        rankCrit.setId(rankIdFilter);

        return cronquistRankQueryService.findByCriteria(rankCrit).get(0).getChildren().stream().map(AtomicCronquistRank::new).collect(Collectors.toSet());
    }

    /**
     * Remplace la portion de classification existante pour qu'elle corresponde à la classification corrigée
     * depuis le rang intermédiaire existant jusqu'au rang taxonomique suivant
     * par cette même portion au-dessus du rang taxonomique récupéré
     * et stocke-les ids des rangs intermédiaires qui ont été déconnectés pour pouvoir les supprimer
     */
    public void merge(
        @NotNull CronquistClassificationBranch brancheCible,
        @NotNull AtomicCronquistRank scrappedRankFoundInDatabase
                     ) throws UnknownRankId, MoreThanOneResultException {

        CronquistClassificationBranch brancheSource = findExistingClassification(scrappedRankFoundInDatabase);
        assert brancheSource != null;
        CronquistTaxonomikRanks replacedRankName = scrappedRankFoundInDatabase.getRank();

        AtomicCronquistRank rankWithNewNames = copyDataFromRankToRank(
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

    private void supprimeLiaisonAscendante(@NotNull AtomicCronquistRank rangAPartirDuquelLAscendanceEstSupprimee) {
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
            } while (AtomicCronquistRank.newRank(rang).isRangDeLiaison());// TODO ajouter méthode isRangDeLiaison pour les entités
        });
    }

    private void switchAncestry(AtomicCronquistRank rangSource, AtomicCronquistRank rangCible) throws MoreThanOneResultException {
        CronquistRank newParent = queryForCronquistRank(rangCible);

        Set<AtomicCronquistRank> taxons = findTaxons(rangSource);
        Set<CronquistRank> taxonsWithNewParent = taxons.stream().map(atomicCronquistRank -> {
            CronquistRank cronquistRank = atomicCronquistRank.newRank();
            cronquistRank.setParent(newParent);
            return cronquistRank;
        }).collect(Collectors.toSet());
        cronquistRankRepository.saveAll(taxonsWithNewParent);

        CronquistRank aSupprimer = queryForCronquistRank(new AtomicCronquistRank().id(rangSource.getId()));
        assert aSupprimer != null;
        aSupprimer.getChildren().removeIf(cronquistRank -> true);
        cronquistRankRepository.saveAll(taxonsWithNewParent);

    }

    private @NotNull AtomicCronquistRank copyDataFromRankToRank(
        @NotNull AtomicCronquistRank rangSource,
        AtomicCronquistRank rangCible
                                                               ) throws UnknownRankId {
        Optional<CronquistRank> rangQuiSeraSupprime = cronquistRankRepository.findById(rangSource.getId());
        if (rangQuiSeraSupprime.isPresent()) {
            CronquistRank rank = rangQuiSeraSupprime.get();
            log.debug("Copie les informations du rang " + rangSource.getId() + " vers le rang " + rangCible.getId());
            // TODO maintenant j'aoute dans des rangs de liaison, mais je ne les supprime pas. Il le faut! A voir avec les test, si ça se trouve il sont supprimé par hibernate
            rangCible.addAllNamesToCronquistRank(rangSource.getNoms());
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
            return AtomicCronquistRank.newRank(save);
        } else {
            throw new UnknownRankId();
        }

    }

    private void updateNomsIds(@NotNull AtomicCronquistRank cronquistRank) {
        Iterator<AtomicClassificationNom> nomIterator = cronquistRank.getNoms().iterator();
        AtomicClassificationNom nom;
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

    private @Nullable CronquistRank queryForCronquistRank(@NotNull AtomicCronquistRank cronquistRank) throws MoreThanOneResultException {
        CronquistRankCriteria rankCrit = new CronquistRankCriteria();

        if (cronquistRank.getId() != null) {
            LongFilter idFilter = new LongFilter();
            idFilter.setEquals(cronquistRank.getId());
            rankCrit.setId(idFilter);
        }

        if (!cronquistRank.getNoms().isEmpty()) {
            LongFilter nomFilter = new LongFilter();
            nomFilter.setIn(cronquistRank.getNoms().stream().map(AtomicClassificationNom::getId).filter(Objects::nonNull).collect(Collectors.toList()));
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
                return rank.get(0);
            default:
                throw new MoreThanOneResultException("Provided criteria do not correspond to only one result : " + cronquistRank);
        }
    }

    /**
     * @param cronquistRank Rang dont il faut vérifier la présence en base
     * @return Le rang qui correspond. Null si le rang n'existe pas
     */
    @Nullable
    public AtomicCronquistRank findExistingRank(@NotNull AtomicCronquistRank cronquistRank) {
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

    @Contract("_ -> new")
    private @Nullable AtomicCronquistRank fetchExistingRank(@NotNull AtomicCronquistRank cronquistRank) {
        CronquistRank cronquistRank1;
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

    @Contract("_ -> new")
    public @Nullable CronquistClassificationBranch fetchExistingClassification(@NotNull AtomicCronquistRank cronquistRank) {
        CronquistRank cronquistRank1;
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

    public Set<AtomicCronquistRank> getTaxons(AtomicCronquistRank cronquistRank) {
        Set<AtomicCronquistRank> taxons = new HashSet<>();
        try {
            CronquistRank cronquistRank1 = queryForCronquistRank(cronquistRank);
            if (cronquistRank1 != null) {
                cronquistRank1.getChildren().forEach(cronquistRank2 -> taxons.add(new AtomicCronquistRank(cronquistRank2)));
                return taxons;
            }
        } catch (MoreThanOneResultException e) {
            log.debug(e.getMessage());
        }
        return null;
    }
}

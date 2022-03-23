package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification;

import fr.syncrase.ecosyst.domain.ClassificationNom;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.Url;
import fr.syncrase.ecosyst.domain.enumeration.CronquistTaxonomikRanks;
import fr.syncrase.ecosyst.repository.ClassificationNomRepository;
import fr.syncrase.ecosyst.repository.CronquistRankRepository;
import fr.syncrase.ecosyst.service.ClassificationNomQueryService;
import fr.syncrase.ecosyst.service.CronquistRankQueryService;
import fr.syncrase.ecosyst.service.UrlQueryService;
import fr.syncrase.ecosyst.service.criteria.ClassificationNomCriteria;
import fr.syncrase.ecosyst.service.criteria.CronquistRankCriteria;
import fr.syncrase.ecosyst.service.criteria.UrlCriteria;
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
    private UrlQueryService urlQueryService;
    private CronquistRankRepository cronquistRankRepository;
    private ClassificationNomRepository classificationNomRepository;

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

    @Autowired
    public void setUrlQueryService(UrlQueryService urlQueryService) {
        this.urlQueryService = urlQueryService;
    }

    public CronquistClassificationBranch findExistingPartOfThisClassification(@NotNull CronquistClassificationBranch classification) throws ClassificationReconstructionException {
        CronquistClassificationBranch existingClassification = null;
        List<AtomicCronquistRank> cronquistRanks = new ArrayList<>(classification.getClassification());
        AtomicCronquistRank cronquistRank = null;
        for (int i = cronquistRanks.size() - 1; i >= 0; i--) {
            cronquistRank = findExistingRank(cronquistRanks.get(i));
            if (cronquistRank != null) {

                existingClassification = findExistingClassification(cronquistRank);
                assert existingClassification != null;
                eagerLoadAllClassificationData(existingClassification);
                break;// I got it!
            }
        }
        return existingClassification;
    }

    /**
     * @param cronquistRank Rang dont il faut vérifier la présence en base
     * @return Le rang qui correspond. Null si le rang n'existe pas
     */
    @Nullable
    public CronquistClassificationBranch findExistingClassification(@NotNull AtomicCronquistRank cronquistRank) throws ClassificationReconstructionException {
        if (cronquistRank.isRangDeLiaison() && cronquistRank.getId() == null) {
            return null;
        }
        updateNomsIds(cronquistRank);
        // Pas de nom => rang inconnu en base
        if (cronquistRank.getNoms().stream().anyMatch(classificationNom -> classificationNom.getId() != null)) {
            return fetchExistingClassification(cronquistRank);
        }
        return null;
    }

    /**
     * Remplace la portion de classification existante pour qu'elle corresponde à la classification corrigée
     * depuis le rang intermédiaire existant jusqu'au rang taxonomique suivant
     * par cette même portion au-dessus du rang taxonomique récupéré
     * et stocke-les ids des rangs intermédiaires qui ont été déconnectés pour pouvoir les supprimer
     *
     * @param existingClassification
     * @param synonym
     */
    public void mergeDeuxRangsSignificatifs(@NotNull CronquistClassificationBranch existingClassification, @NotNull AtomicCronquistRank synonym) {
        AtomicCronquistRank rangQuiRecupereLesTaxons = existingClassification.getRang(synonym.getRank());

        Set<AtomicCronquistRank> taxons = findTaxons(synonym);
        changeParentOfTheTaxons(taxons, rangQuiRecupereLesTaxons);
        supprimeLiaisonAscendante(synonym);
        // Ajout du nom synonyme
        rangQuiRecupereLesTaxons.addAllNamesToCronquistRank(synonym.getNoms());
        rangQuiRecupereLesTaxons.addAllUrlsToCronquistRank(synonym.getUrls());
    }

    private void supprimeLiaisonAscendante(@NotNull AtomicCronquistRank synonym) {
        Optional<CronquistRank> baseDesRangsDeLiaisonASupprimer = cronquistRankRepository.findById(synonym.getId());
        baseDesRangsDeLiaisonASupprimer.ifPresent(rang -> {
            // Suppression des rangs de liaison ascendants qui deviennent inutiles par la fusion
            AtomicCronquistRank atomicCronquistRank = AtomicCronquistRank.newRank(rang);
            do {
                classificationNomRepository.deleteAllById(atomicCronquistRank.getNoms().stream().map(AtomicClassificationNom::getId).collect(Collectors.toSet()));// TODO pas nécessaire, ce sont des rangs de liaison!
                cronquistRankRepository.deleteById(atomicCronquistRank.getId());
                atomicCronquistRank = AtomicCronquistRank.newRank(rang.getParent());
            } while (atomicCronquistRank.isRangDeLiaison());
        });
    }

    private void changeParentOfTheTaxons(@NotNull Set<AtomicCronquistRank> taxons, @NotNull AtomicCronquistRank nouveauParent) {
        taxons.forEach(taxon -> {
            updateParent(taxon, nouveauParent);
        });

    }

    private void updateParent(@NotNull AtomicCronquistRank taxon, @NotNull AtomicCronquistRank nouveauParent) {
        Optional<CronquistRank> cronquistRank = cronquistRankRepository.findById(taxon.getId());
        cronquistRank.ifPresentOrElse(rank -> {
            rank.setParent(new CronquistRank().id(nouveauParent.getId()));
            cronquistRankRepository.save(rank);
        }, () -> {
            String todo = "le rank existe forcément. Quoi faire quand le repo ne me retourne rien?";
        });
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
    public void mergeUnRangSignificatifDansUnRangDeLiaison(
        @NotNull CronquistClassificationBranch existingClassification,
        @NotNull AtomicCronquistRank scrappedRankFoundInDatabase
                                                          ) throws ClassificationReconstructionException {

        CronquistClassificationBranch scrappedExistingClassification = findExistingClassification(scrappedRankFoundInDatabase);
        assert scrappedExistingClassification != null;
        CronquistTaxonomikRanks replacedRankName = scrappedRankFoundInDatabase.getRank();

        //        cronquistRankRepository.deleteById(existingClassification.getRang(replacedRankName.getRangInferieur()).getId());
        // Sur le rang de liaison inférieur au rang de liaison à mettre à jour, corriger la version du parent
        updateParent(
            existingClassification.getRang(replacedRankName.getRangInferieur()),
            scrappedExistingClassification.getRang(replacedRankName)
                    );

        for (
            CronquistTaxonomikRanks currentRankName = replacedRankName;
            existingClassification.getRang(currentRankName).isRangDeLiaison() && currentRankName != null;
            currentRankName = currentRankName.getRangSuperieur()
        ) {
            classificationNomRepository.deleteAllById(
                existingClassification.getRang(currentRankName).getNoms().stream()
                    .map(AtomicClassificationNom::getId)
                    .collect(Collectors.toSet())
                                                     );
            cronquistRankRepository.deleteById(existingClassification.getRang(currentRankName).getId());
            existingClassification.put(currentRankName, scrappedExistingClassification.getRang(currentRankName));
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
            //            this.getCronquistRank
            List<ClassificationNom> classificationNoms = classificationNomQueryService.findByCriteria(classificationNomCriteria);
            if (classificationNoms.size() > 0) {// Soit inexistant, soit un unique résultat (unique constraint)
                nom.setId(classificationNoms.get(0).getId());
            }
        }
    }

    private @Nullable CronquistRank getCronquistRank(@NotNull AtomicCronquistRank cronquistRank) throws MoreThanOneResultException {
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
        if (cronquistRank.isRangDeLiaison() && cronquistRank.getId() == null) {
            return null;
        }
        updateNomsIds(cronquistRank);
        // Pas de nom => rang inconnu en base
        if (cronquistRank.getNoms().stream().anyMatch(classificationNom -> classificationNom.getId() != null)) {
            return fetchExistingRank(cronquistRank);
        }
        return null;
    }

    @Contract("_ -> new")
    private @Nullable AtomicCronquistRank fetchExistingRank(@NotNull AtomicCronquistRank cronquistRank) {
        CronquistRank cronquistRank1 = null;
        try {
            cronquistRank1 = getCronquistRank(cronquistRank);
            if (cronquistRank1 != null) {
                return new AtomicCronquistRank(cronquistRank1);
            }
        } catch (MoreThanOneResultException e) {
            log.trace(e.getMessage());
        }
        return null;
    }

    @Contract("_ -> new")
    public @Nullable CronquistClassificationBranch fetchExistingClassification(@NotNull AtomicCronquistRank cronquistRank) {
        CronquistRank cronquistRank1 = null;
        try {
            cronquistRank1 = getCronquistRank(cronquistRank);
            if (cronquistRank1 != null) {
                return new CronquistClassificationBranch(cronquistRank1);
            }
        } catch (MoreThanOneResultException e) {
            log.trace(e.getMessage());
        } catch (ClassificationReconstructionException e) {
            log.trace("Impossible de reconstruire la classification de ce rang : " + cronquistRank);
        }
        return null;
    }

    private void eagerLoadAllClassificationData(@NotNull CronquistClassificationBranch classification) {
        for (AtomicCronquistRank rank : classification.getClassification()) {
            addAllNames(rank);
            addAllUrls(rank);
        }
    }

    private void addAllNames(@NotNull AtomicCronquistRank cronquistRank) {
        cronquistRank.getNoms().removeIf(classificationNom -> classificationNom.getId() == null);
        LongFilter idFilter = new LongFilter();
        idFilter.setEquals(cronquistRank.getId());
        ClassificationNomCriteria classificationNomCriteria = new ClassificationNomCriteria();
        classificationNomCriteria.setCronquistRankId(idFilter);

        List<ClassificationNom> classificationNoms = classificationNomQueryService.findByCriteria(classificationNomCriteria);
        Set<AtomicClassificationNom> classificationNoms1 = classificationNoms.stream().map(AtomicClassificationNom::new).collect(Collectors.toSet());
        cronquistRank.getNoms().addAll(classificationNoms1);
    }

    private void addAllUrls(@NotNull AtomicCronquistRank cronquistRank) {
        cronquistRank.getUrls().removeIf(url -> url.getId() == null);
        LongFilter idFilter = new LongFilter();
        idFilter.setEquals(cronquistRank.getId());
        UrlCriteria urlCriteria = new UrlCriteria();
        urlCriteria.setCronquistRankId(idFilter);

        List<Url> urlByCriteria = urlQueryService.findByCriteria(urlCriteria);
        Set<AtomicUrl> urls = urlByCriteria.stream().map(AtomicUrl::new).collect(Collectors.toSet());
        cronquistRank.getUrls().addAll(urls);
    }


}

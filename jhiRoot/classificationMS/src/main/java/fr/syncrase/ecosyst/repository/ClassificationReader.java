package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.classification.CronquistClassificationBranch;
import fr.syncrase.ecosyst.domain.classification.entities.wrapper.ClassificationNomWrapper;
import fr.syncrase.ecosyst.domain.classification.entities.wrapper.CronquistRankWrapper;
import fr.syncrase.ecosyst.domain.classification.consistency.ClassificationReconstructionException;
import fr.syncrase.ecosyst.domain.classification.entities.database.ClassificationNom;
import fr.syncrase.ecosyst.domain.classification.entities.database.CronquistRank;
import fr.syncrase.ecosyst.domain.classification.entities.IClassificationNom;
import fr.syncrase.ecosyst.domain.classification.entities.ICronquistRank;
import fr.syncrase.ecosyst.service.database.ClassificationNomQueryService;
import fr.syncrase.ecosyst.service.database.CronquistRankQueryService;
import fr.syncrase.ecosyst.service.criteria.ClassificationNomCriteria;
import fr.syncrase.ecosyst.service.criteria.CronquistRankCriteria;
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
public class ClassificationReader {

    private final Logger log = LoggerFactory.getLogger(ClassificationReader.class);


    private ClassificationNomQueryService classificationNomQueryService;
    private CronquistRankQueryService cronquistRankQueryService;

    @Autowired
    public void setClassificationNomQueryService(ClassificationNomQueryService classificationNomQueryService) {
        this.classificationNomQueryService = classificationNomQueryService;
    }

    @Autowired
    public void setCronquistRankQueryService(CronquistRankQueryService cronquistRankQueryService) {
        this.cronquistRankQueryService = cronquistRankQueryService;
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
                existingParent.getTaxons().forEach(taxon -> taxons.add(new CronquistRankWrapper(taxon)));
                return taxons;
            }
        } catch (MoreThanOneResultException e) {
            log.debug(e.getMessage());
        }
        return null;
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

    public @Nullable ICronquistRank queryForCronquistRank(@NotNull ICronquistRank cronquistRank) throws MoreThanOneResultException {
        CronquistRankCriteria rankCrit = new CronquistRankCriteria();

        if (cronquistRank.getId() != null) {
            LongFilter idFilter = new LongFilter();
            idFilter.setEquals(cronquistRank.getId());
            rankCrit.setId(idFilter);
        }

        if (!cronquistRank.getNomsWrappers().isEmpty()) {
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


    private @NotNull Set<IClassificationNom> findExistingNames(@NotNull ICronquistRank cronquistRank) {
        Set<IClassificationNom> noms = new HashSet<>();
        Iterator<IClassificationNom> nomIterator = cronquistRank.getNomsWrappers().iterator();
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

}

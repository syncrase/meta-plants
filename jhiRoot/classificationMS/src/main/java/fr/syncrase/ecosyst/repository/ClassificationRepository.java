package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.classification.CronquistClassificationBranch;
import fr.syncrase.ecosyst.domain.classification.consistency.ClassificationMerger;
import fr.syncrase.ecosyst.domain.classification.entities.mappers.CronquistRankMapper;
import fr.syncrase.ecosyst.domain.classification.entities.wrapper.CronquistRankWrapper;
import fr.syncrase.ecosyst.domain.classification.consistency.ClassificationReconstructionException;
import fr.syncrase.ecosyst.domain.classification.consistency.InconsistentRank;
import fr.syncrase.ecosyst.domain.classification.entities.database.CronquistRank;
import fr.syncrase.ecosyst.domain.classification.entities.ICronquistRank;
import fr.syncrase.ecosyst.domain.classification.enumeration.RankName;
import fr.syncrase.ecosyst.repository.database.ClassificationNomRepository;
import fr.syncrase.ecosyst.repository.database.CronquistRankRepository;
import fr.syncrase.ecosyst.repository.database.UrlRepository;
import org.apache.commons.collections4.map.LinkedMap;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ClassificationRepository {

    private final Logger log = LoggerFactory.getLogger(ClassificationRepository.class);

    private CronquistRankRepository cronquistRankRepository;
    private ClassificationNomRepository classificationNomRepository;
    private UrlRepository urlRepository;
    private ClassificationMerger classificationMerger;
    private ClassificationReader classificationReader;

    @Autowired
    public void setClassificationMerger(ClassificationMerger classificationMerger) {
        this.classificationMerger = classificationMerger;
    }

    @Autowired
    public void setCronquistRankReader(ClassificationReader classificationReader) {
        this.classificationReader = classificationReader;
    }

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


    /**
     * Merge la classification du rangScrap?? dans la brancheCible.
     * Cette m??thode est utilis??e quand on se rend compte que deux rangs diff??rents correspondent en fait au m??me rang.
     * Cette m??thode met ?? jour les donn??es en base de mani??re ?? pouvoir ins??rer sans conflit le rang scrap??
     *
     * @return Le rang scrap?? mis ?? jour pour ??tre rendu coh??rent avec les donn??es d??j?? pr??sentes en base
     */
    public ICronquistRank merge(
        @NotNull CronquistClassificationBranch brancheCible,
        @NotNull ICronquistRank scrappedRankFoundInDatabase
                               ) throws UnknownRankId, MoreThanOneResultException, InconsistentRank, ClassificationReconstructionException {
        log.info(String.format("Merge %s into %s", scrappedRankFoundInDatabase.getId(), brancheCible.getRang(scrappedRankFoundInDatabase.getRankName()).getId()));
        CronquistClassificationBranch brancheSource = classificationReader.findExistingClassification(scrappedRankFoundInDatabase);
        assert brancheSource != null;

        ICronquistRank mergeResult = classificationMerger.merge(brancheSource, brancheCible, scrappedRankFoundInDatabase.getRankName());
        log.info("Merge successfully");
        return mergeResult;
    }

    /**
     * Enregistre la classification du rang sup??rieur (Super r??gne) jusqu'au rang le plus profond
     *
     * @param consistentClassification Classification ?? enregistrer. Aucune v??rification sur la consistence des donn??es ne se fait dans la m??thode
     * @return La branche de classification qui vient d'??tre enregistr??e
     */
    public CronquistClassificationBranch save(@NotNull CronquistClassificationBranch consistentClassification) {
        log.info(String.format("Save the classification %s", consistentClassification));
        if (consistentClassification.getClassification() == null) {
            return null;
        }
        CronquistRankMapper mapper = new CronquistRankMapper();
        LinkedMap<RankName, CronquistRank> classificationToSave = mapper.getClassificationToSave(consistentClassification);
        // L'enregistrement des classifications doit se faire en commen??ant par le rang sup??rieur
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

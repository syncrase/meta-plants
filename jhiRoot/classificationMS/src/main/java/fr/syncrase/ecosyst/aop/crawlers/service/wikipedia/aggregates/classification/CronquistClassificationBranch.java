package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.AtomicCronquistRank;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.exceptions.ClassificationReconstructionException;
import fr.syncrase.ecosyst.domain.ICronquistRank;
import fr.syncrase.ecosyst.domain.enumeration.RankName;
import org.apache.commons.collections4.map.LinkedMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;


public class CronquistClassificationBranch {

    private final Logger log = LoggerFactory.getLogger(CronquistClassificationBranch.class);

    /**
     * Liste de tous les rangs de la classification<br>
     * L'élément 0 est le rang le plus haut : le super règne
     */
    private LinkedMap<RankName, ICronquistRank> classificationCronquistMap;

    /**
     * Construit une classification vierge
     */
    public CronquistClassificationBranch() {
        initEmptyClassification();
    }

    /**
     * Construit une classification à partir d'un rang
     */
    public CronquistClassificationBranch(@NotNull ICronquistRank cronquistRank) throws ClassificationReconstructionException {
        initEmptyClassification();
        ICronquistRank currentRank = cronquistRank;
        while (currentRank != null) {
            classificationCronquistMap.put(currentRank.getRank(), AtomicCronquistRank.newRank(currentRank));
            currentRank = currentRank.getParent();
        }
        this.clearTail();
    }

    @Nullable
    private ICronquistRank getParent(@NotNull ICronquistRank currentRank) {
        boolean isSuperRegne = currentRank.getRank().equals(RankName.SUPERREGNE);
        if (isSuperRegne) {
            return null;
        }
        return classificationCronquistMap.get(currentRank.getRank().getRangSuperieur());
    }

    private void initEmptyClassification() {
        classificationCronquistMap = new LinkedMap<>(34);
        initDefaultValues();
    }

    private void initDefaultValues() {
        RankName[] rangsDisponibles = RankName.values();
        for (RankName hauteurDeRangEnCours : rangsDisponibles) {
            classificationCronquistMap.put(hauteurDeRangEnCours, AtomicCronquistRank.getDefaultRank(hauteurDeRangEnCours));
        }
    }

    /**
     * La raison d'être des rangs de liaison (sans nom) est de lier deux rangs dont les noms sont connus.<br>
     * Cette méthode nettoie les rangs liaison inférieurs au dernier rang de la classification
     */
    public void clearTail() {
        RankName[] rangsDisponibles = RankName.values();
        for (int positionDansLaClassification = rangsDisponibles.length - 1; positionDansLaClassification >= 0; positionDansLaClassification--) {
            RankName nomDuRangEnCours = rangsDisponibles[positionDansLaClassification];
            boolean cestUnRangConnu = classificationCronquistMap.get(nomDuRangEnCours) != null && !classificationCronquistMap.get(nomDuRangEnCours).isRangDeLiaison();
            if (cestUnRangConnu) {
                break;
            }
            removeTaxon(nomDuRangEnCours);
        }
    }

    private void removeTaxon(@NotNull RankName nomDuRangEnCours) {
        classificationCronquistMap.remove(nomDuRangEnCours);
    }

    public ICronquistRank getRang(RankName rang) {
        if (rang != null) {
            return classificationCronquistMap.get(rang);
        } else {
            return null;
        }
    }

    public ICronquistRank getRangDeBase() {
        return classificationCronquistMap.get(classificationCronquistMap.lastKey());
    }

    public Collection<ICronquistRank> getClassification() {
        return classificationCronquistMap.values();
    }

    public LinkedMap<RankName, ICronquistRank> getClassificationBranch() {
        return classificationCronquistMap;
    }

    public void put(RankName currentRankName, ICronquistRank currentRank) {
        classificationCronquistMap.put(currentRankName, currentRank);
    }

    public int ranksCount() {
        return classificationCronquistMap.size();
    }

    @Override
    public String toString() {
        return "CronquistClassificationBranch{" + "classificationCronquistMap=" + classificationCronquistMap + '}';
    }

    public void inferAllRank() {
        classificationCronquistMap.forEach((rankName, rank) -> rank.setRank(rankName));
    }
}

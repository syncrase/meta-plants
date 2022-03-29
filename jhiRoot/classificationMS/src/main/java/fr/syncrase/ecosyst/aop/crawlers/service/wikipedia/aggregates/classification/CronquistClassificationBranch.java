package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification;

import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.enumeration.CronquistTaxonomikRanks;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.lang3.ArrayUtils;
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
    private LinkedMap<CronquistTaxonomikRanks, AtomicCronquistRank> classificationCronquistMap;

    /**
     * Construit une classification vierge
     */
    public CronquistClassificationBranch() {
        initEmptyClassification();
    }

    /**
     * Construit une classification à partir d'un rang
     */
    public CronquistClassificationBranch(@NotNull CronquistRank cronquistRank) throws ClassificationReconstructionException {
        initEmptyClassification();
        CronquistTaxonomikRanks[] rangsDisponibles = CronquistTaxonomikRanks.values();
        int indexDuRangDeBase = ArrayUtils.indexOf(rangsDisponibles, cronquistRank.getRank());
        CronquistRank currentRank = cronquistRank;
        // TODO utiliser un while
        for (int positionDansLaClassification = indexDuRangDeBase;
             positionDansLaClassification >= 0 && currentRank != null;
             positionDansLaClassification--, currentRank = currentRank.getParent()
        ) {
            if (!rangsDisponibles[positionDansLaClassification].equals(currentRank.getRank())) {
                log.error("Tentative d'assigner à un rang les valeurs d'un rang différent");
                throw new ClassificationReconstructionException();
            }
            classificationCronquistMap.put(rangsDisponibles[positionDansLaClassification], AtomicCronquistRank.newRank(currentRank));// Problème de désynchronisation avec parent enfant
        }
        this.clearTail();
    }

    @Nullable
    private AtomicCronquistRank getParent(@NotNull AtomicCronquistRank currentRank) throws ClassificationReconstructionException {
        boolean isSuperRegne = currentRank.getRank().equals(CronquistTaxonomikRanks.SUPERREGNE);
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
        CronquistTaxonomikRanks[] rangsDisponibles = CronquistTaxonomikRanks.values();
        for (CronquistTaxonomikRanks hauteurDeRangEnCours : rangsDisponibles) {
            classificationCronquistMap.put(hauteurDeRangEnCours, AtomicCronquistRank.getDefaultRank(hauteurDeRangEnCours));
        }
    }

    /**
     * La raison d'être des rangs de liaison (sans nom) est de lier deux rangs dont les noms sont connus.<br>
     * Cette méthode nettoie les rangs liaison inférieurs au dernier rang de la classification
     */
    public void clearTail() {
        CronquistTaxonomikRanks[] rangsDisponibles = CronquistTaxonomikRanks.values();
        for (int positionDansLaClassification = rangsDisponibles.length - 1; positionDansLaClassification >= 0; positionDansLaClassification--) {
            CronquistTaxonomikRanks nomDuRangEnCours = rangsDisponibles[positionDansLaClassification];
            boolean cestUnRangConnu = classificationCronquistMap.get(nomDuRangEnCours) != null && !classificationCronquistMap.get(nomDuRangEnCours).isRangDeLiaison();
            if (cestUnRangConnu) {
                break;
            }
            removeTaxon(nomDuRangEnCours);
        }
    }

    private void removeTaxon(@NotNull CronquistTaxonomikRanks nomDuRangEnCours) {
        classificationCronquistMap.remove(nomDuRangEnCours);
    }

    public AtomicCronquistRank getRang(CronquistTaxonomikRanks rang) {
        if (rang != null) {
            return classificationCronquistMap.get(rang);
        } else {
            return null;
        }
    }

    public AtomicCronquistRank getRangDeBase() {
        return classificationCronquistMap.get(classificationCronquistMap.lastKey());
    }

    public Collection<AtomicCronquistRank> getClassification() {
        return classificationCronquistMap.values();
    }

    public LinkedMap<CronquistTaxonomikRanks, AtomicCronquistRank> getClassificationBranch() {
        return classificationCronquistMap;
    }

    public void put(CronquistTaxonomikRanks currentRankName, AtomicCronquistRank currentRank) {
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
        classificationCronquistMap.forEach((rankName, rank) -> {
            rank.setRank(rankName);
        });
    }
}

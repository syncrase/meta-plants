package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.ClassificationReconstructionException;
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
//    private List<AtomicCronquistRank> classificationCronquist;
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
    public CronquistClassificationBranch(@NotNull AtomicCronquistRank existingRank) throws ClassificationReconstructionException {
        initEmptyClassification();
        if (classificationCronquistMap.containsKey(existingRank.getRank())) {// Remove this: always true
            CronquistTaxonomikRanks[] rangsDisponibles = CronquistTaxonomikRanks.values();
            int indexDuRangDeBase = ArrayUtils.indexOf(rangsDisponibles, existingRank.getRank());
            AtomicCronquistRank currentRank = existingRank;
            // TODO utiliser un while
            for (
                int positionDansLaClassification = indexDuRangDeBase;
                positionDansLaClassification >= 0 && currentRank != null;
                positionDansLaClassification--, currentRank = getParent(currentRank)
            ) {
                if (!rangsDisponibles[positionDansLaClassification].equals(currentRank.getRank())) {
                    log.error("Tentative d'assigner à un rang les valeurs d'un rang différent");
                    throw new ClassificationReconstructionException();
                }
                classificationCronquistMap.put(rangsDisponibles[positionDansLaClassification], currentRank);// Problème de désynchro avec parent enfant
            }
        }
        this.clearTail();
    }

    @Nullable
    private AtomicCronquistRank getParent(@NotNull AtomicCronquistRank currentRank) throws ClassificationReconstructionException {
//        currentRank = currentRank.getParent();
//        AtomicCronquistRank parent = currentRank.getParent();
        boolean isSuperRegne = currentRank.getRank().equals(CronquistTaxonomikRanks.SUPERREGNE);
        if (isSuperRegne) {
            return null;
        }
//        boolean hasParent = parent != null;
//        boolean superRegneHasParent = isSuperRegne && hasParent;
//        boolean rangIntermediaireHasNoParent = !isSuperRegne && !hasParent;
//        if (superRegneHasParent) {
//            log.error("Arrivé au bout des rangs à renseigner mais il en reste à traiter dans la classification reçu. Revoir les index!");
//            throw new ClassificationReconstructionException();
//        }
//        if (rangIntermediaireHasNoParent) {
//            log.error("Pas encore arrivé au bout des rangs à renseigner mais il n'y en a plus à traiter dans la classification reçu. Revoir les index!");
//            throw new ClassificationReconstructionException();
//        }
//        return parent;
        return classificationCronquistMap.get(currentRank.getRank().getRangSuperieur());
    }

    private void initEmptyClassification() {
        classificationCronquistMap = new LinkedMap<>(34);
        CronquistTaxonomikRanks[] rangsDisponibles = CronquistTaxonomikRanks.values();
        for (CronquistTaxonomikRanks hauteurDeRangEnCours : rangsDisponibles) {
            classificationCronquistMap.put(
                hauteurDeRangEnCours,
                AtomicCronquistRank.getDefaultRank(hauteurDeRangEnCours));
        }
    }

    /**
     * La raison d'être des rangs de liaison (sans nom) est de lier deux rangs dont les noms sont connus.<br>
     * Cette méthode nettoie les rangs liaison inférieurs au dernier rang de la classification
     */
    public void clearTail() {
        CronquistTaxonomikRanks[] rangsDisponibles = CronquistTaxonomikRanks.values();
        for (
            int positionDansLaClassification = rangsDisponibles.length - 1;
            positionDansLaClassification >= 0;
            positionDansLaClassification--
        ) {
            CronquistTaxonomikRanks nomDuRangEnCours = rangsDisponibles[positionDansLaClassification];
            boolean cestUnRangConnu = !classificationCronquistMap.get(nomDuRangEnCours).isRangDeLiaison();
            if (cestUnRangConnu) {
                break;
            }
            removeTaxon(nomDuRangEnCours);
        }
    }

    private void removeTaxon(@NotNull CronquistTaxonomikRanks nomDuRangEnCours) {
//        if (classificationCronquistMap.containsKey(nomDuRangEnCours.getRangSuperieur())) {
//            classificationCronquistMap.get(nomDuRangEnCours.getRangSuperieur()).removeChildren(classificationCronquistMap.remove(nomDuRangEnCours));
//        }
        classificationCronquistMap.remove(nomDuRangEnCours);
    }

    public AtomicCronquistRank getRang(CronquistTaxonomikRanks rang) {
        if (rang != null) {
//            if (getSiblings(rang).size() > 1) {
//                log.error("Le parent du rang désiré a plusieurs enfants. Erreur dans l'algorithme");
//            }
            return classificationCronquistMap.get(rang);
        } else {
            return null;
        }
    }

//    private Set<AtomicCronquistRank> getSiblings(@NotNull CronquistTaxonomikRanks rang) {
//        AtomicCronquistRank parent = classificationCronquistMap.get(rang.getRangSuperieur());
//        return parent != null ? parent.getChildren() : new HashSet<>();
//    }

    public AtomicCronquistRank getRangDeBase() {
        return classificationCronquistMap.get(classificationCronquistMap.lastKey());
    }

    public Collection<AtomicCronquistRank> getClassification() {
        return classificationCronquistMap.values();
    }

    public void put(CronquistTaxonomikRanks currentRankName, AtomicCronquistRank currentRank) {
        classificationCronquistMap.put(currentRankName, currentRank);
    }

    public int ranksCount() {
        return classificationCronquistMap.size();
    }

    /**
     * Construit une liste descendante des éléments de la classification
     *
     * @return La classification de cronquist complète sous forme de liste. Les éléments null ne sont pas intégré à la liste
     */
//    public List<AtomicCronquistRank> getList() {
//        return classificationCronquist;
//    }

    /**
     * Construit une liste ascendante des éléments de la classification
     *
     * @return La classification de cronquist complète sous forme de liste
     */
//    public List<AtomicCronquistRank> getClassificationAscendante() {
//        List<AtomicCronquistRank> list = new ArrayList<>();
//        for (int i = classificationCronquist.size() - 1; i >= 0; i--) {
//            list.add(classificationCronquist.get(i));
//        }
//        return list;
//    }
}

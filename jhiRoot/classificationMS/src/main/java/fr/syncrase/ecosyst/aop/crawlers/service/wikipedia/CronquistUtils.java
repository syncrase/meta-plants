package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.domain.ClassificationNom;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.Url;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CronquistUtils {

    /**
     * Deux rangs taxonomiques sont séparés par d'autres rangs dont on ne connait pas forcément le nom.<br>
     * Une valeur par défaut permet de lier ces deux rangs avec des rangs vides.<br>
     * Si ultérieurement ces rangs sont déterminés, les valeurs par défaut sont mises à jour
     */
    public static final String DEFAULT_NAME_FOR_CONNECTOR_RANK = null;

    /**
     * Vérifie si le rang est un rang intermédiaire
     *
     * @param rank rang dont on vérifie le nom
     * @return true si le rang en question est un rang intermédiaire
     */
    public static boolean isRangIntermediaire(@NotNull CronquistRank rank) {
        return hasThisName(rank, DEFAULT_NAME_FOR_CONNECTOR_RANK);
    }

    /**
     * Vérifie si tous les rangs sont intermédiaires
     *
     * @param ranks rangs dont on vérifie qu'ils sont tous intermédiaires
     * @return true si le rang en question est un rang intermédiaire
     */
    public static boolean isRangsIntermediaires(@NotNull CronquistRank... ranks) {
        if (ranks.length == 0) {
            return false;
        }
        Iterator<@NotNull CronquistRank> cronquistRankIterator = Arrays.stream(ranks).iterator();
        while (cronquistRankIterator.hasNext()) {
            if (!isRangIntermediaire(cronquistRankIterator.next())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Vérifie si le rang possède le nom passé en paramètre
     *
     * @param rank rang à vérifier
     * @param name nom que le rang a, ou n'a pas
     * @return true si le rang possède le nom
     */
    public static boolean hasThisName(@NotNull CronquistRank rank, String name) {
        TreeSet<ClassificationNom> classificationNoms = getClassificationNomTreeSet();
        classificationNoms.addAll(rank.getNoms());
        return classificationNoms.contains(new ClassificationNom().nomFr(name));
    }

    /**
     * Vérifie si le rang possède l'un des noms passés en paramètre
     *
     * @param rank  rang à vérifier
     * @param names noms que le rang a, ou n'a pas
     * @return true si le rang possède le nom
     */
    public static boolean doTheRankHasOneOfTheseNames(@NotNull CronquistRank rank, Set<ClassificationNom> names) {
        TreeSet<ClassificationNom> classificationNoms = getClassificationNomTreeSet();
        classificationNoms.addAll(names);
        for (ClassificationNom classificationNom : rank.getNoms()) {
            if (classificationNoms.contains(classificationNom)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retourne un set vide de ClassificationNom dont la comparaison se fait sur le nomFr
     *
     * @return Treeset vide avec comparaison personnalisée
     */
    @NotNull
    private static TreeSet<ClassificationNom> getClassificationNomTreeSet() {
        return new TreeSet<>(Comparator.comparing(ClassificationNom::getNomFr, (nomFrExistant, nomFrAAjouter) -> {
            if (nomFrExistant == null && nomFrAAjouter == null) {
                return 0;
            }
            if (nomFrExistant == null) {
                return 1;
            }
            if (nomFrAAjouter == null) {
                return -1;
            }
            return nomFrExistant.compareTo(nomFrAAjouter);
        }));
    }

    /**
     * Remplace le nom du rang intermédiaire par le nom connu ou ajoute le nom au set si c'est un rang taxonomique.<br>
     * Pour l'instant, ne gère que les nomsFr TODO gérer en plus le nom latin
     *
     * @param rank  rang auquel est ajouté le nom
     * @param nomFr nomFr à ajouter au rang
     */
    public static void addNameToCronquistRank(@NotNull CronquistRank rank, ClassificationNom nomFr) {
        if (isRangIntermediaire(rank)) {
            rank.getNoms().removeIf(classificationNom -> classificationNom.getNomFr() == null);
        }
        rank.addNoms(new ClassificationNom().nomFr(nomFr.getNomFr()).cronquistRank(rank).id(nomFr.getId()));
    }

    /**
     * Ajoute tous les noms et les ids au rang
     *
     * @param rank  rang auquel sont ajouté les noms
     * @param names set des noms à ajouter
     */
    public static void addAllNamesToCronquistRank(@NotNull CronquistRank rank, @NotNull Set<ClassificationNom> names) {
        for (ClassificationNom classificationNom : names) {
            addNameToCronquistRank(rank, classificationNom);
        }
    }

    /**
     * Ajoute toutes les urls au rang
     *
     * @param rank rang auquel sont ajouté les noms
     * @param urls set des noms à ajouter
     */
    public static void addAllUrlsToCronquistRank(@NotNull CronquistRank rank, @NotNull Set<Url> urls) {
        for (Url url : urls) {
            rank.addUrls(new Url().url(url.getUrl()).id(url.getId()).cronquistRank(rank));
        }
    }

}

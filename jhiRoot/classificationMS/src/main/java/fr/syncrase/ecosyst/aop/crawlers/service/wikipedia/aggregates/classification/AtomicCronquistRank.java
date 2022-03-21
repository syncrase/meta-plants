package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification;

import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.enumeration.CronquistTaxonomikRanks;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.AtomicClassificationNom.getAtomicClassificationNomTreeSet;
import static fr.syncrase.ecosyst.domain.CronquistRank.DEFAULT_NAME_FOR_CONNECTOR_RANK;

/**
 * Un rang qui ne possède pas le rang parent ou les taxons
 */
public class AtomicCronquistRank {
    private Long id;
    private CronquistTaxonomikRanks rank;
    private Set<AtomicUrl> urls = new HashSet<>();
    private Set<AtomicClassificationNom> noms = new HashSet<>();

    public AtomicCronquistRank(@NotNull CronquistRank cronquistRank) {
        this.id = cronquistRank.getId();
        this.rank = cronquistRank.getRank();
        this.urls = cronquistRank.getUrls().stream().map(AtomicUrl::new).collect(Collectors.toSet());
        this.noms = cronquistRank.getNoms().stream().map(AtomicClassificationNom::new).collect(Collectors.toSet());
    }

    public AtomicCronquistRank() {

    }

    /**
     * Factory method For Default rank
     *
     * @param rankName
     * @return
     */
    public static AtomicCronquistRank getDefaultRank(CronquistTaxonomikRanks rankName) {
        return new AtomicCronquistRank().rank(rankName).addNoms(new AtomicClassificationNom().nomFr(DEFAULT_NAME_FOR_CONNECTOR_RANK));
    }

    @Contract("_ -> new")
    public static @NotNull AtomicCronquistRank newRank(CronquistRank rang) {
        return new AtomicCronquistRank(rang);
    }

    /**
     * Vérifie si tous les rangs sont intermédiaires
     *
     * @param ranks rangs dont on vérifie qu'ils sont tous intermédiaires
     * @return true si le rang en question est un rang intermédiaire
     */
    public static boolean isRangsIntermediaires(AtomicCronquistRank @NotNull ... ranks) {
        if (ranks.length == 0) {
            return false;
        }
        Iterator<@NotNull AtomicCronquistRank> cronquistRankIterator = Arrays.stream(ranks).iterator();
        while (cronquistRankIterator.hasNext()) {
            if (!cronquistRankIterator.next().isRangDeLiaison()) {
                return false;
            }
        }
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AtomicCronquistRank id(Long id) {
        this.setId(id);
        return this;
    }

    public CronquistTaxonomikRanks getRank() {
        return rank;
    }

    public void setRank(CronquistTaxonomikRanks rank) {
        this.rank = rank;
    }

    public Set<AtomicClassificationNom> getNoms() {
        return noms;
    }

    private AtomicCronquistRank rank(CronquistTaxonomikRanks rankName) {
        this.setRank(rank);
        return this;
    }

    public AtomicCronquistRank addNoms(AtomicClassificationNom classificationNom) {
        this.noms.add(classificationNom);
        return this;
    }

    public Set<AtomicUrl> getUrls() {
        return this.urls;
    }

    public void setUrls(Set<AtomicUrl> urls) {
        this.urls = urls;
    }

    public AtomicCronquistRank urls(Set<AtomicUrl> urls) {
        this.setUrls(urls);
        return this;
    }

    public AtomicCronquistRank addUrls(AtomicUrl url) {
        this.urls.add(url);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AtomicCronquistRank)) {
            return false;
        }
        return id != null && id.equals(((AtomicCronquistRank) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AtomicCronquistRank{" + "id=" + getId() + ", rank='" + getRank() + "'" + "}";
    }

    /**
     * Vérifie si le rang est un rang intermédiaire
     *
     * @return true si le rang en question est un rang intermédiaire
     */
    public boolean isRangDeLiaison() {
        return hasThisName(DEFAULT_NAME_FOR_CONNECTOR_RANK);
    }

    /**
     * Vérifie si le rang possède le nom passé en paramètre
     *
     * @param name nom que le rang a, ou n'a pas
     * @return true si le rang possède le nom
     */
    public boolean hasThisName(String name) {
        TreeSet<AtomicClassificationNom> classificationNoms = getAtomicClassificationNomTreeSet();
        classificationNoms.addAll(this.getNoms());
        return classificationNoms.contains(new AtomicClassificationNom().nomFr(name));
    }

    /**
     * Vérifie si le rang possède l'un des noms passés en paramètre
     *
     * @param names noms que le rang a, ou n'a pas
     * @return true si le rang possède le nom
     */
    public boolean doTheRankHasOneOfTheseNames(Set<AtomicClassificationNom> names) {
        TreeSet<AtomicClassificationNom> classificationNoms = getAtomicClassificationNomTreeSet();
        classificationNoms.addAll(names);
        for (AtomicClassificationNom classificationNom : this.getNoms()) {
            if (classificationNoms.contains(classificationNom)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Remplace le nom du rang intermédiaire par le nom connu ou ajoute le nom au set si c'est un rang taxonomique.<br>
     * Pour l'instant, ne gère que les nomsFr TODO gérer en plus le nom latin
     *
     * @param nomFr nomFr à ajouter au rang
     */
    public void addNameToCronquistRank(AtomicClassificationNom nomFr) {
        if (this.isRangDeLiaison()) {
            this.getNoms().removeIf(classificationNom -> classificationNom.getNomFr() == null);
        }
        this.addNoms(new AtomicClassificationNom().nomFr(nomFr.getNomFr())
                         //                .cronquistRank(this)
                         .id(nomFr.getId()));
    }

    /**
     * Ajoute tous les noms et les ids au rang
     *
     * @param names set des noms à ajouter
     */
    public void addAllNamesToCronquistRank(@NotNull Set<AtomicClassificationNom> names) {
        for (AtomicClassificationNom classificationNom : names) {
            addNameToCronquistRank(classificationNom);
        }
    }

    /**
     * Ajoute toutes les urls au rang
     *
     * @param urls set des noms à ajouter
     */
    public void addAllUrlsToCronquistRank(@NotNull Set<AtomicUrl> urls) {
        for (AtomicUrl url : urls) {
            this.addUrls(new AtomicUrl().url(url.getAtomicUrl()).id(url.getId()));
        }
    }

    public CronquistRank newRank() {
        return new CronquistRank().id(this.id).rank(this.rank).urls(this.urls.stream().map(AtomicUrl::newUrl).collect(Collectors.toSet())).noms(this.noms.stream().map(AtomicClassificationNom::newClassificationNom).collect(Collectors.toSet()));
    }
}

package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.wrapper;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.classification.AtomicClassificationNom;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.classification.AtomicCronquistRank;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.classification.AtomicUrl;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.mappers.ClassificationNomMapper;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.mappers.UrlMapper;
import fr.syncrase.ecosyst.domain.*;
import fr.syncrase.ecosyst.domain.enumeration.RankName;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.classification.AtomicClassificationNom.getAtomicClassificationNomTreeSet;
import static fr.syncrase.ecosyst.domain.CronquistRank.DEFAULT_NAME_FOR_CONNECTOR_RANK;

/**
 * Wrap le rang cronquist stocké en base de données.
 * Se construit à partir d'un objet existant en base de donnée uniquement
 */
public class CronquistRankWrapper implements ICronquistRank {

    private final CronquistRank cronquistRank;

    public CronquistRankWrapper(CronquistRank cronquistRank) {
        this.cronquistRank = cronquistRank;
    }

    public CronquistRankWrapper(@NotNull ICronquistRank rank) {
        this.cronquistRank = rank.newRank();
    }

    @Override
    public CronquistRank newRank() {
        return this.cronquistRank;
    }

    @Override
    public RankName getRank() {
        return this.cronquistRank.getRank();
    }

    @Override
    public void setRank(RankName rankName) {
        this.cronquistRank.setRank(rankName);
    }

    @Override
    public Long getId() {
        return this.cronquistRank.getId();
    }

    @Override
    public void setId(Long id) {
        this.cronquistRank.setId(id);
    }

    @Override
    public Set<IUrl> getUrls() {
        return this.cronquistRank.getUrls().stream().map(AtomicUrl::new).collect(Collectors.toSet());
    }

    @Override
    public ICronquistRank urls(@NotNull Set<IUrl> urls) {
        this.cronquistRank.setUrls(urls.stream().map(UrlMapper::get).collect(Collectors.toSet()));
        return this;
    }

    @Override
    public void addAllUrlsToCronquistRank(@NotNull Set<IUrl> urls) {
        for (IUrl url : urls) {
            this.addUrl(new AtomicUrl().url(url.getUrl()).id(url.getId())); // TODO check si elle existe déjà
        }
    }

    @Override
    public ICronquistRank addNom(IClassificationNom nom) {
        this.cronquistRank.getNoms().add(ClassificationNomMapper.get(nom));
        return this;
    }

    @Override
    public ICronquistRank noms(@NotNull Set<IClassificationNom> noms) {
        this.cronquistRank.setNoms(noms.stream().map(ClassificationNomMapper::get).collect(Collectors.toSet()));
        return this;
    }

    public Set<ClassificationNom> newNames() {// TODO rename to getClassificationNoms
        return this.cronquistRank.getNoms();
    }

    @Override
    public Set<IClassificationNom> getNoms() {
        return this.cronquistRank.getNoms().stream().map(ClassificationNomWrapper::new).collect(Collectors.toSet());
    }

    @Override
    public Set<ICronquistRank> getChildren() {
        return this.cronquistRank.getChildren().stream().map(AtomicCronquistRank::new).collect(Collectors.toSet());
    }

    @Override
    public boolean isRangSignificatif() {
        return !this.isRangDeLiaison();
    }

    @Override
    public boolean isRangDeLiaison() {
        return hasThisName(DEFAULT_NAME_FOR_CONNECTOR_RANK);
    }

    /**
     * Vérifie si le rang possède le nom passé en paramètre
     *
     * @param name nom que le rang a, ou n'a pas
     * @return true si le rang possède le nom
     */
    public boolean hasThisName(String name) {// TODO default method
        TreeSet<IClassificationNom> classificationNoms = getAtomicClassificationNomTreeSet();
        classificationNoms.addAll(this.getNoms());
        return classificationNoms.contains(new AtomicClassificationNom().nomFr(name));
    }

    @Override
    public void addAllNamesToCronquistRank(@NotNull Set<IClassificationNom> noms) {
        for (IClassificationNom classificationNom : noms) {
            addNameToCronquistRank(classificationNom);
        }
    }

    @Override
    public boolean isAnyNameHasAnId() {
        return getNoms().stream().anyMatch(classificationNom -> classificationNom.getId() != null);// TODO default final ?
    }

    @Override
    public ICronquistRank getParent() {
        CronquistRank parent = this.cronquistRank.getParent();
        return parent == null ? null : new CronquistRankWrapper(parent);
    }

    @Override
    public ICronquistRank clone() {
        return new AtomicCronquistRank(this.cronquistRank).clone();
    }

    @Override
    public boolean doTheRankHasOneOfTheseNames(Set<IClassificationNom> noms) {
        TreeSet<IClassificationNom> classificationNoms = getAtomicClassificationNomTreeSet();
        classificationNoms.addAll(noms);
        for (IClassificationNom classificationNom : this.getNoms()) {
            if (classificationNoms.contains(classificationNom)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addNameToCronquistRank(IClassificationNom nom) {
        // TODO default method ?
        if (this.isRangDeLiaison()) {
            this.getNoms().removeIf(classificationNom -> classificationNom.getNomFr() == null);
        }
        this.addNom(new AtomicClassificationNom().nomFr(nom.getNomFr())
                        .id(nom.getId()));
    }

    @Override
    public ICronquistRank addUrl(IUrl newAtomicUrl) {
        this.cronquistRank.getUrls().add(UrlMapper.get(newAtomicUrl));
        return this;
    }

    @Override
    public void removeNames() {
        this.cronquistRank.getNoms().clear();
    }

    @Override
    public void removeUrls() {
        this.cronquistRank.getUrls().clear();// TODO default method
    }

    @Override
    public void removeTaxons() {
        this.cronquistRank.getChildren().clear();
    }

    public Set<Url> newUrls() {
        return this.cronquistRank.getUrls();
    }// TODO default method
}

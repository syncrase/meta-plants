package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.*;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.IClassificationNom;
import fr.syncrase.ecosyst.domain.ICronquistRank;
import fr.syncrase.ecosyst.domain.IUrl;
import fr.syncrase.ecosyst.domain.enumeration.RankName;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.AtomicClassificationNom.getAtomicClassificationNomTreeSet;

/**
 * Wrap le rang cronquist stocké en base de données.
 * Se construit à partir d'un objet existant en base de donnée uniquement
 */
public class CronquistRankWrapper implements ICronquistRank {

    private final CronquistRank cronquistRank;

    public CronquistRankWrapper(CronquistRank cronquistRank) {
        this.cronquistRank = cronquistRank;
    }

    @Override
    public CronquistRank newRank() {
        throw new UnsupportedOperationException("Méthode non implémentée");
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
    public boolean isRangDeLiaison() {
        throw new UnsupportedOperationException("Méthode non implémentée");
    }

    @Override
    public Set<IClassificationNom> getNoms() {
        return this.cronquistRank.getNoms().stream().map(AtomicClassificationNom::new).collect(Collectors.toSet());
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
    public ICronquistRank addNom(IClassificationNom nom) {
        this.cronquistRank.getNoms().add(ClassificationNomMapper.get(nom));
        return this;
    }

    @Override
    public ICronquistRank noms(@NotNull Set<IClassificationNom> noms) {
        this.cronquistRank.setNoms(noms.stream().map(ClassificationNomMapper::get).collect(Collectors.toSet()));
        return this;
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
        return new CronquistRankWrapper(this.cronquistRank.getParent());
    }

    @Override
    public void addAllUrlsToCronquistRank(@NotNull Set<IUrl> urls) {
        for (IUrl url : urls) {
            this.addUrl(new AtomicUrl().url(url.getUrl()).id(url.getId())); // TODO check si elle existe déjà
        }
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
}

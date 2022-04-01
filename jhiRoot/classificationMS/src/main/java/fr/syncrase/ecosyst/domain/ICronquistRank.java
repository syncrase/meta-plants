package fr.syncrase.ecosyst.domain;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.AtomicUrl;
import fr.syncrase.ecosyst.domain.enumeration.RankName;

import java.util.Set;

public interface ICronquistRank extends Cloneable {


    CronquistRank newRank();

    RankName getRank();

    void setRank(RankName rankName);

    Long getId();

    void setId(Long id);

    boolean isRangDeLiaison();

    Set<IClassificationNom> getNoms();

    Set<IUrl> getUrls();

    ICronquistRank urls(Set<IUrl> urls);

    ICronquistRank addNom(IClassificationNom nomFr);

    ICronquistRank noms(Set<IClassificationNom> noms);

    Set<ICronquistRank> getChildren();

    boolean isRangSignificatif();

    void addAllNamesToCronquistRank(Set<IClassificationNom> noms);

    boolean isAnyNameHasAnId();

    ICronquistRank getParent();

    void addAllUrlsToCronquistRank(Set<IUrl> urls);

    public ICronquistRank clone();

    boolean doTheRankHasOneOfTheseNames(Set<IClassificationNom> noms);

    void addNameToCronquistRank(IClassificationNom existingNom);

    ICronquistRank addUrl(IUrl url);
}

package fr.syncrase.ecosyst.domain;

import fr.syncrase.ecosyst.domain.enumeration.RankName;

import java.util.Set;

public interface ICronquistRank extends Cloneable {


    CronquistRank getCronquistRank();

    RankName getRankName();

    void setRankName(RankName rankName);

    Long getId();

    void setId(Long id);

    boolean isRangDeLiaison();

    Set<IClassificationNom> getNoms();

    Set<IUrl> getUrls();

    ICronquistRank urls(Set<IUrl> urls);

    void setNoms(Set<IClassificationNom> classificationNoms);

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

    void removeNames();

    void removeUrls();

    void removeTaxons();
}

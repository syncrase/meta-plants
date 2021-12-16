package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import tech.jhipster.service.filter.StringFilter;

import java.util.List;
import java.util.Set;

public interface CronquistRank {

    CronquistRank getParent();

    Set<? extends CronquistRank> getChildren();

    List<? extends CronquistRank> findExistingRank();

    @NotNull
    default StringFilter getStringFilterEquals(String nomFr) {
        StringFilter nomFrFilter = new StringFilter();
        nomFrFilter.equals(nomFr);
        return nomFrFilter;
    }

    Long getId();

    void setId(Long id);

    String getNomFr();

    void setNomFr(String nomFr);

    default CronquistRank get() {
        return this;
//        throw new NotImplementedException("Cette méthode doit être surchargée");
    }

    default void save() {
        throw new NotImplementedException("Cette méthode doit être surchargée");
    }
//
//    String getNomLatin();
//
//    void setNomLatin(String nomLatin);
//
//    Set<? extends CronquistRank> getSynonymes();
//
//    void setSynonymes(Set<? extends CronquistRank> classes);
//
//    CronquistRank addSynonymes(CronquistRank classe);
//
//    CronquistRank removeSynonymes(CronquistRank classe);
}

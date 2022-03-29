package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.domain.ClassificationNom;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.enumeration.CronquistTaxonomikRanks;
import org.apache.commons.collections4.map.LinkedMap;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class TestUtils {


    @NotNull
    public LinkedMap<CronquistTaxonomikRanks, CronquistRank> transformToMapOfRanksByName(@NotNull Collection<CronquistRank> corylopsisRanks) {
        LinkedMap<CronquistTaxonomikRanks, CronquistRank> corylopsisClassification = new LinkedMap<>();
        corylopsisRanks.forEach(cronquistRank -> corylopsisClassification.put(cronquistRank.getRank(), cronquistRank));
        return corylopsisClassification;
    }


    @NotNull
    public Set<String> getNamesFromMapRank(
        @NotNull LinkedMap<CronquistTaxonomikRanks,
            CronquistRank> corylopsisClassification,
        CronquistTaxonomikRanks ordre
                                          ) {
        return corylopsisClassification.get(ordre).getNoms().stream().map(ClassificationNom::getNomFr).collect(Collectors.toSet());
    }
}

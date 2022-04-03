package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.mappers;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.CronquistClassificationBranch;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.classification.AtomicClassificationNom;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.classification.AtomicCronquistRank;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.classification.AtomicUrl;
import fr.syncrase.ecosyst.domain.ClassificationNom;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.Url;
import fr.syncrase.ecosyst.domain.enumeration.RankName;
import org.apache.commons.collections4.map.LinkedMap;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.stream.Collectors;

public class CronquistRankMapper {

    /**
     * Ajouter les ranksName
     * Ajouter les parents et les taxons
     *
     * @param classification map d'objets atomiques
     * @return L'objet pouvant être enregistré en base construit à partir de la map d'objets atomiques
     */
    public LinkedMap<RankName, CronquistRank> getClassificationToSave(
        @NotNull CronquistClassificationBranch classification
                                                                     ) {
        LinkedMap<RankName, CronquistRank> dBFriendlyClassification = new LinkedMap<>();

        classification.getClassificationBranch().forEach((rankName, rank) -> {
            rank.setRankName(rankName);
            CronquistRank cronquistRank = rank.getCronquistRank();
            dBFriendlyClassification.put(rankName, cronquistRank);
        });

        dBFriendlyClassification.forEach((rankName, rank) -> {
            CronquistRank parent = dBFriendlyClassification.get(rankName.getRangSuperieur());
            if (parent != null) {
                dBFriendlyClassification.get(rankName).setParent(parent);
            }
            CronquistRank taxon = dBFriendlyClassification.get(rankName.getRangInferieur());
            if (taxon != null) {
                dBFriendlyClassification.get(rankName).setChildren(Collections.singleton(taxon));
            }
            dBFriendlyClassification.get(rankName).getNoms().forEach(classificationNom -> classificationNom.setCronquistRank(dBFriendlyClassification.get(rankName)));
        });

        return dBFriendlyClassification;
    }

    private CronquistRank getCronquistRank(@NotNull AtomicCronquistRank atomicCronquistRank) {
        return new CronquistRank()
            .rank(atomicCronquistRank.getRankName())
            .noms(atomicCronquistRank.getNoms().stream().map(ClassificationNomMapper::get).collect(Collectors.toSet()))
            .urls(atomicCronquistRank.getIUrls().stream().map(UrlMapper::get).collect(Collectors.toSet()))
            .id(atomicCronquistRank.getId());
    }

    private Url getUrl(@NotNull AtomicUrl atomicUrl) {
        return new Url()
            .id(atomicUrl.getId())
            .url(atomicUrl.getUrl());
    }

    private ClassificationNom getClassificationNom(@NotNull AtomicClassificationNom atomicClassificationNom) {
        return new ClassificationNom()
            .nomFr(atomicClassificationNom.getNomFr())
            .nomLatin(atomicClassificationNom.getNomLatin())
            .id(atomicClassificationNom.getId());
    }
}

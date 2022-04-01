package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.*;
import fr.syncrase.ecosyst.domain.ClassificationNom;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.ICronquistRank;
import fr.syncrase.ecosyst.domain.Url;
import fr.syncrase.ecosyst.domain.enumeration.RankName;
import org.apache.commons.collections4.map.LinkedMap;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
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
    public Collection<CronquistRank> getClassificationToSave(
        @NotNull LinkedMap<RankName, ICronquistRank> classification
                                                            ) {
        LinkedMap<RankName, CronquistRank> dBFriendlyClassification = new LinkedMap<>();

        classification.forEach((rankName, rank) -> {
            rank.setRank(rankName);
            CronquistRank cronquistRank = rank.newRank();
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

        return dBFriendlyClassification.values();
    }

    private CronquistRank getCronquistRank(@NotNull AtomicCronquistRank atomicCronquistRank) {
        return new CronquistRank()
            .rank(atomicCronquistRank.getRank())
            .noms(atomicCronquistRank.getNoms().stream().map(ClassificationNomMapper::get).collect(Collectors.toSet()))
            .urls(atomicCronquistRank.getUrls().stream().map(UrlMapper::get).collect(Collectors.toSet()))
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

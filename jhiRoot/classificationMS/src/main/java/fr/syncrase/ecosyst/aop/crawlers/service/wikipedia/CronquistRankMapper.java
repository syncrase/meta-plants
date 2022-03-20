package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.AtomicClassificationNom;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.AtomicCronquistRank;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.AtomicUrl;
import fr.syncrase.ecosyst.domain.ClassificationNom;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.Url;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.stream.Collectors;

public class CronquistRankMapper {

    public Collection<CronquistRank> getClassificationToSave(@NotNull Collection<AtomicCronquistRank> classification) {
        return classification.stream().map(this::getCronquistRank).collect(Collectors.toSet());
    }

    private CronquistRank getCronquistRank(@NotNull AtomicCronquistRank atomicCronquistRank) {
        return new CronquistRank().rank(atomicCronquistRank.getRank())
            .noms(atomicCronquistRank.getNoms().stream().map(this::getClassificationNom).collect(Collectors.toSet()))
            .urls(atomicCronquistRank.getUrls().stream().map(this::getUrl).collect(Collectors.toSet()))
            .id(atomicCronquistRank.getId());
    }

    private Url getUrl(@NotNull AtomicUrl atomicUrl) {
        return new Url().id(atomicUrl.getId())
            .url(atomicUrl.getAtomicUrl());
    }

    private ClassificationNom getClassificationNom(@NotNull AtomicClassificationNom atomicClassificationNom) {
        return new ClassificationNom()
            .nomFr(atomicClassificationNom.getNomFr())
            .nomLatin(atomicClassificationNom.getNomLatin())
            .id(atomicClassificationNom.getId());
    }
}

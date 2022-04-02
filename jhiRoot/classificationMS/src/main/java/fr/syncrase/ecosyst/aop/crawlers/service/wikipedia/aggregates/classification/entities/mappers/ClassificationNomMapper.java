package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.mappers;

import fr.syncrase.ecosyst.domain.ClassificationNom;
import fr.syncrase.ecosyst.domain.IClassificationNom;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ClassificationNomMapper {

    @Contract("_ -> new")
    public static @NotNull ClassificationNom get(@NotNull IClassificationNom url) {
        return new ClassificationNom(url.getId(), url.getNomFr(), url.getNomLatin());// TODO déplacer dans les objets. La responsabilité revient à AtomicClassificationNom
    }

}

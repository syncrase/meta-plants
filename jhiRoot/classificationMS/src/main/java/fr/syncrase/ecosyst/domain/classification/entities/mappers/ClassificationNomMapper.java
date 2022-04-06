package fr.syncrase.ecosyst.domain.classification.entities.mappers;

import fr.syncrase.ecosyst.domain.classification.entities.IClassificationNom;
import fr.syncrase.ecosyst.domain.classification.entities.database.ClassificationNom;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ClassificationNomMapper {

    @Contract("_ -> new")
    public static @NotNull ClassificationNom get(@NotNull IClassificationNom url) {
        return new ClassificationNom(url.getId(), url.getNomFr(), url.getNomLatin());// TODO déplacer dans les objets. La responsabilité revient à AtomicClassificationNom
    }

}

package fr.syncrase.ecosyst.domain.classification.entities.mappers;

import fr.syncrase.ecosyst.domain.classification.entities.IUrl;
import fr.syncrase.ecosyst.domain.classification.entities.database.Url;
import org.jetbrains.annotations.NotNull;

public class UrlMapper {

    //    public AtomicUrl get(@NotNull Url url) {
    //        return new AtomicUrl(url.getUrl(), url.getId());
    //    }

    public static Url get(@NotNull IUrl url) {
        return new Url(url.getUrl(), url.getId());
    }

}

package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities;

import fr.syncrase.ecosyst.domain.IUrl;
import fr.syncrase.ecosyst.domain.Url;
import org.jetbrains.annotations.NotNull;

public class UrlMapper {

    //    public AtomicUrl get(@NotNull Url url) {
    //        return new AtomicUrl(url.getUrl(), url.getId());
    //    }

    public static Url get(@NotNull IUrl url) {
        return new Url(url.getUrl(), url.getId());
    }

}

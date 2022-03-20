package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Wikipedia {

    public static final String scrappingBaseUrl = "https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Classification_de_Cronquist";

    @Contract(pure = true)
    static @NotNull String getValidUrl(@NotNull String scrappedUrl) {
        if (scrappedUrl.contains(("http"))) {
            return scrappedUrl;
        }
        return "https://fr.wikipedia.org" + scrappedUrl;
    }
}

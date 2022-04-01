package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities;

import fr.syncrase.ecosyst.domain.IUrl;
import fr.syncrase.ecosyst.domain.Url;
import org.jetbrains.annotations.NotNull;

public class AtomicUrl implements IUrl {
    private String url;
    private Long id;

    public AtomicUrl(@NotNull IUrl url) {
        this.id = url.getId();
        this.url = url.getUrl();
    }

    public AtomicUrl(@NotNull Url url) {
        this.id = url.getId();
        this.url = url.getUrl();
    }

    public AtomicUrl() {
    }

    public static AtomicUrl newAtomicUrl(String urlWiki) {
        return new AtomicUrl().url(urlWiki);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AtomicUrl id(Long id) {
        this.setId(id);
        return this;
    }

    public String getUrl() {
        return this.url;
    }

    public void setAtomicUrl(String url) {
        this.url = url;
    }

    public AtomicUrl url(String url) {
        this.setAtomicUrl(url);
        return this;
    }

    public Url newUrl() {
        return new Url()
            .url(this.url)
            .id(this.id);
    }

    @Override
    public AtomicUrl clone() {
        try {
            AtomicUrl clone = (AtomicUrl) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

package fr.syncrase.ecosyst.domain;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.classification.AtomicUrl;

public interface IUrl extends Cloneable {
    Long getId();

    void setId(Long id);

    public AtomicUrl id(Long id);

    String getUrl();

    Url newUrl();

    public IUrl clone();
}

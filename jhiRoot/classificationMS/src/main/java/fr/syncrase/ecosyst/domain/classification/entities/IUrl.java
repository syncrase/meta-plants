package fr.syncrase.ecosyst.domain.classification.entities;

import fr.syncrase.ecosyst.domain.classification.entities.atomic.AtomicUrl;
import fr.syncrase.ecosyst.domain.classification.entities.database.Url;

public interface IUrl extends Cloneable {
    Long getId();

    void setId(Long id);

    public AtomicUrl id(Long id);

    String getUrl();

    Url newUrl();

    public IUrl clone();
}

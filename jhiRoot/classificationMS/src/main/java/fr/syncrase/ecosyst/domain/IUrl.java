package fr.syncrase.ecosyst.domain;

public interface IUrl extends Cloneable {
    Long getId();

    String getUrl();

    Url newUrl();

    public IUrl clone();
}

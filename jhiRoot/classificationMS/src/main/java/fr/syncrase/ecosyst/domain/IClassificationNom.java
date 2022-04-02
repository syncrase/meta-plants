package fr.syncrase.ecosyst.domain;

public interface IClassificationNom extends Cloneable {
    String getNomFr();

    Long getId();

    void setId(Long id);

    String getNomLatin();

    public IClassificationNom clone();

    ClassificationNom getClassificationNom();
}

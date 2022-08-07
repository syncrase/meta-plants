package fr.syncrase.ecosyst.domain.classification.entities;

import fr.syncrase.ecosyst.domain.classification.entities.database.ClassificationNom;

public interface IClassificationNom extends Cloneable {
    String getNomFr();

    Long getId();

    void setId(Long id);

    String getNomLatin();

    public IClassificationNom clone();

    ClassificationNom getClassificationNom();

    IClassificationNom nomFr(String nom);
}

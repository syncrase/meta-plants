package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification;


import fr.syncrase.ecosyst.domain.ClassificationNom;
import org.jetbrains.annotations.NotNull;

public class AtomicClassificationNom {
    private Long id;
    private String nomFr;
    private String nomLatin;

    public AtomicClassificationNom(@NotNull ClassificationNom classificationNom) {
        this.id = classificationNom.getId();
        this.nomFr = classificationNom.getNomFr();
        this.nomLatin = classificationNom.getNomLatin();
    }

    public AtomicClassificationNom() {
    }

    public AtomicClassificationNom nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public Long getId() {
        return this.id;
    }

    public AtomicClassificationNom id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AtomicClassificationNom nomLatin(String nomLatin) {
        this.setNomLatin(nomLatin);
        return this;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public String getNomLatin() {
        return this.nomLatin;
    }

}

package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.wrapper;

import fr.syncrase.ecosyst.domain.ClassificationNom;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.IClassificationNom;

public class ClassificationNomWrapper implements IClassificationNom {

    private final ClassificationNom classificationNom;

    public ClassificationNomWrapper(ClassificationNom classificationNom) {
        this.classificationNom = classificationNom;
    }

    @Override
    public String getNomFr() {
        return this.classificationNom.getNomFr();
    }

    @Override
    public Long getId() {
        return this.classificationNom.getId();
    }

    @Override
    public void setId(Long id) {
        this.classificationNom.setId(id);
    }

    @Override
    public String getNomLatin() {
        return this.classificationNom.getNomLatin();
    }

    @Override
    public ClassificationNomWrapper clone() {
        //        return new ClassificationNomWrapper(this.classificationNom);
        throw new UnsupportedOperationException("Le clone du nom wrapper n'a pas été prévu");
    }

    @Override
    public ClassificationNom getClassificationNom() {
        return this.classificationNom;
    }

    public void setCronquistRank(CronquistRank save) {
        this.classificationNom.setCronquistRank(save);
    }
}
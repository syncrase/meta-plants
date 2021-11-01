package fr.syncrase.perma.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link fr.syncrase.perma.domain.Plante} entity. This class is used
 * in {@link fr.syncrase.perma.web.rest.PlanteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /plantes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PlanteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomLatin;

    private StringFilter entretien;

    private StringFilter histoire;

    private StringFilter exposition;

    private StringFilter rusticite;

    private LongFilter cycleDeVieId;

    private LongFilter classificationId;

    private LongFilter confusionsId;

    private LongFilter interactionsId;

    private LongFilter nomsVernaculairesId;

    private LongFilter allelopathieRecueId;

    private LongFilter allelopathieProduiteId;

    public PlanteCriteria() {
    }

    public PlanteCriteria(PlanteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.entretien = other.entretien == null ? null : other.entretien.copy();
        this.histoire = other.histoire == null ? null : other.histoire.copy();
        this.exposition = other.exposition == null ? null : other.exposition.copy();
        this.rusticite = other.rusticite == null ? null : other.rusticite.copy();
        this.cycleDeVieId = other.cycleDeVieId == null ? null : other.cycleDeVieId.copy();
        this.classificationId = other.classificationId == null ? null : other.classificationId.copy();
        this.confusionsId = other.confusionsId == null ? null : other.confusionsId.copy();
        this.interactionsId = other.interactionsId == null ? null : other.interactionsId.copy();
        this.nomsVernaculairesId = other.nomsVernaculairesId == null ? null : other.nomsVernaculairesId.copy();
        this.allelopathieRecueId = other.allelopathieRecueId == null ? null : other.allelopathieRecueId.copy();
        this.allelopathieProduiteId = other.allelopathieProduiteId == null ? null : other.allelopathieProduiteId.copy();
    }

    @Override
    public PlanteCriteria copy() {
        return new PlanteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNomLatin() {
        return nomLatin;
    }

    public void setNomLatin(StringFilter nomLatin) {
        this.nomLatin = nomLatin;
    }

    public StringFilter getEntretien() {
        return entretien;
    }

    public void setEntretien(StringFilter entretien) {
        this.entretien = entretien;
    }

    public StringFilter getHistoire() {
        return histoire;
    }

    public void setHistoire(StringFilter histoire) {
        this.histoire = histoire;
    }

    public StringFilter getExposition() {
        return exposition;
    }

    public void setExposition(StringFilter exposition) {
        this.exposition = exposition;
    }

    public StringFilter getRusticite() {
        return rusticite;
    }

    public void setRusticite(StringFilter rusticite) {
        this.rusticite = rusticite;
    }

    public LongFilter getCycleDeVieId() {
        return cycleDeVieId;
    }

    public void setCycleDeVieId(LongFilter cycleDeVieId) {
        this.cycleDeVieId = cycleDeVieId;
    }

    public LongFilter getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(LongFilter classificationId) {
        this.classificationId = classificationId;
    }

    public LongFilter getConfusionsId() {
        return confusionsId;
    }

    public void setConfusionsId(LongFilter confusionsId) {
        this.confusionsId = confusionsId;
    }

    public LongFilter getInteractionsId() {
        return interactionsId;
    }

    public void setInteractionsId(LongFilter interactionsId) {
        this.interactionsId = interactionsId;
    }

    public LongFilter getNomsVernaculairesId() {
        return nomsVernaculairesId;
    }

    public void setNomsVernaculairesId(LongFilter nomsVernaculairesId) {
        this.nomsVernaculairesId = nomsVernaculairesId;
    }

    public LongFilter getAllelopathieRecueId() {
        return allelopathieRecueId;
    }

    public void setAllelopathieRecueId(LongFilter allelopathieRecueId) {
        this.allelopathieRecueId = allelopathieRecueId;
    }

    public LongFilter getAllelopathieProduiteId() {
        return allelopathieProduiteId;
    }

    public void setAllelopathieProduiteId(LongFilter allelopathieProduiteId) {
        this.allelopathieProduiteId = allelopathieProduiteId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PlanteCriteria that = (PlanteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(entretien, that.entretien) &&
            Objects.equals(histoire, that.histoire) &&
            Objects.equals(exposition, that.exposition) &&
            Objects.equals(rusticite, that.rusticite) &&
            Objects.equals(cycleDeVieId, that.cycleDeVieId) &&
            Objects.equals(classificationId, that.classificationId) &&
            Objects.equals(confusionsId, that.confusionsId) &&
            Objects.equals(interactionsId, that.interactionsId) &&
            Objects.equals(nomsVernaculairesId, that.nomsVernaculairesId) &&
            Objects.equals(allelopathieRecueId, that.allelopathieRecueId) &&
            Objects.equals(allelopathieProduiteId, that.allelopathieProduiteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nomLatin,
        entretien,
        histoire,
        exposition,
        rusticite,
        cycleDeVieId,
        classificationId,
        confusionsId,
        interactionsId,
        nomsVernaculairesId,
        allelopathieRecueId,
        allelopathieProduiteId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlanteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
                (entretien != null ? "entretien=" + entretien + ", " : "") +
                (histoire != null ? "histoire=" + histoire + ", " : "") +
                (exposition != null ? "exposition=" + exposition + ", " : "") +
                (rusticite != null ? "rusticite=" + rusticite + ", " : "") +
                (cycleDeVieId != null ? "cycleDeVieId=" + cycleDeVieId + ", " : "") +
                (classificationId != null ? "classificationId=" + classificationId + ", " : "") +
                (confusionsId != null ? "confusionsId=" + confusionsId + ", " : "") +
                (interactionsId != null ? "interactionsId=" + interactionsId + ", " : "") +
                (nomsVernaculairesId != null ? "nomsVernaculairesId=" + nomsVernaculairesId + ", " : "") +
                (allelopathieRecueId != null ? "allelopathieRecueId=" + allelopathieRecueId + ", " : "") +
                (allelopathieProduiteId != null ? "allelopathieProduiteId=" + allelopathieProduiteId + ", " : "") +
            "}";
    }

}

package fr.syncrase.perma.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link fr.syncrase.perma.domain.Exposition} entity. This class is used
 * in {@link fr.syncrase.perma.web.rest.ExpositionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /expositions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExpositionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter valeur;

    private DoubleFilter ensoleilement;

    private LongFilter planteId;

    private Boolean distinct;

    public ExpositionCriteria() {}

    public ExpositionCriteria(ExpositionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.valeur = other.valeur == null ? null : other.valeur.copy();
        this.ensoleilement = other.ensoleilement == null ? null : other.ensoleilement.copy();
        this.planteId = other.planteId == null ? null : other.planteId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ExpositionCriteria copy() {
        return new ExpositionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getValeur() {
        return valeur;
    }

    public StringFilter valeur() {
        if (valeur == null) {
            valeur = new StringFilter();
        }
        return valeur;
    }

    public void setValeur(StringFilter valeur) {
        this.valeur = valeur;
    }

    public DoubleFilter getEnsoleilement() {
        return ensoleilement;
    }

    public DoubleFilter ensoleilement() {
        if (ensoleilement == null) {
            ensoleilement = new DoubleFilter();
        }
        return ensoleilement;
    }

    public void setEnsoleilement(DoubleFilter ensoleilement) {
        this.ensoleilement = ensoleilement;
    }

    public LongFilter getPlanteId() {
        return planteId;
    }

    public LongFilter planteId() {
        if (planteId == null) {
            planteId = new LongFilter();
        }
        return planteId;
    }

    public void setPlanteId(LongFilter planteId) {
        this.planteId = planteId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ExpositionCriteria that = (ExpositionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(valeur, that.valeur) &&
            Objects.equals(ensoleilement, that.ensoleilement) &&
            Objects.equals(planteId, that.planteId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, valeur, ensoleilement, planteId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExpositionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (valeur != null ? "valeur=" + valeur + ", " : "") +
            (ensoleilement != null ? "ensoleilement=" + ensoleilement + ", " : "") +
            (planteId != null ? "planteId=" + planteId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

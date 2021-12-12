package fr.syncrase.ecosyst.service.criteria;

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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.Variete} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.VarieteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /varietes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VarieteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter sousVarietesId;

    private LongFilter synonymesId;

    private LongFilter sousEspeceId;

    private LongFilter varieteId;

    private Boolean distinct;

    public VarieteCriteria() {}

    public VarieteCriteria(VarieteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.sousVarietesId = other.sousVarietesId == null ? null : other.sousVarietesId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.sousEspeceId = other.sousEspeceId == null ? null : other.sousEspeceId.copy();
        this.varieteId = other.varieteId == null ? null : other.varieteId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public VarieteCriteria copy() {
        return new VarieteCriteria(this);
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

    public StringFilter getNomFr() {
        return nomFr;
    }

    public StringFilter nomFr() {
        if (nomFr == null) {
            nomFr = new StringFilter();
        }
        return nomFr;
    }

    public void setNomFr(StringFilter nomFr) {
        this.nomFr = nomFr;
    }

    public StringFilter getNomLatin() {
        return nomLatin;
    }

    public StringFilter nomLatin() {
        if (nomLatin == null) {
            nomLatin = new StringFilter();
        }
        return nomLatin;
    }

    public void setNomLatin(StringFilter nomLatin) {
        this.nomLatin = nomLatin;
    }

    public LongFilter getSousVarietesId() {
        return sousVarietesId;
    }

    public LongFilter sousVarietesId() {
        if (sousVarietesId == null) {
            sousVarietesId = new LongFilter();
        }
        return sousVarietesId;
    }

    public void setSousVarietesId(LongFilter sousVarietesId) {
        this.sousVarietesId = sousVarietesId;
    }

    public LongFilter getSynonymesId() {
        return synonymesId;
    }

    public LongFilter synonymesId() {
        if (synonymesId == null) {
            synonymesId = new LongFilter();
        }
        return synonymesId;
    }

    public void setSynonymesId(LongFilter synonymesId) {
        this.synonymesId = synonymesId;
    }

    public LongFilter getSousEspeceId() {
        return sousEspeceId;
    }

    public LongFilter sousEspeceId() {
        if (sousEspeceId == null) {
            sousEspeceId = new LongFilter();
        }
        return sousEspeceId;
    }

    public void setSousEspeceId(LongFilter sousEspeceId) {
        this.sousEspeceId = sousEspeceId;
    }

    public LongFilter getVarieteId() {
        return varieteId;
    }

    public LongFilter varieteId() {
        if (varieteId == null) {
            varieteId = new LongFilter();
        }
        return varieteId;
    }

    public void setVarieteId(LongFilter varieteId) {
        this.varieteId = varieteId;
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
        final VarieteCriteria that = (VarieteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(sousVarietesId, that.sousVarietesId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(sousEspeceId, that.sousEspeceId) &&
            Objects.equals(varieteId, that.varieteId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, sousVarietesId, synonymesId, sousEspeceId, varieteId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VarieteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (sousVarietesId != null ? "sousVarietesId=" + sousVarietesId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (sousEspeceId != null ? "sousEspeceId=" + sousEspeceId + ", " : "") +
            (varieteId != null ? "varieteId=" + varieteId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

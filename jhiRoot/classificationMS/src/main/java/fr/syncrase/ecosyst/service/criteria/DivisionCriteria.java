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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.Division} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.DivisionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /divisions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DivisionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter sousDivisionsId;

    private LongFilter synonymesId;

    private LongFilter superDivisionId;

    private LongFilter divisionId;

    private Boolean distinct;

    public DivisionCriteria() {}

    public DivisionCriteria(DivisionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.sousDivisionsId = other.sousDivisionsId == null ? null : other.sousDivisionsId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.superDivisionId = other.superDivisionId == null ? null : other.superDivisionId.copy();
        this.divisionId = other.divisionId == null ? null : other.divisionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DivisionCriteria copy() {
        return new DivisionCriteria(this);
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

    public LongFilter getSousDivisionsId() {
        return sousDivisionsId;
    }

    public LongFilter sousDivisionsId() {
        if (sousDivisionsId == null) {
            sousDivisionsId = new LongFilter();
        }
        return sousDivisionsId;
    }

    public void setSousDivisionsId(LongFilter sousDivisionsId) {
        this.sousDivisionsId = sousDivisionsId;
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

    public LongFilter getSuperDivisionId() {
        return superDivisionId;
    }

    public LongFilter superDivisionId() {
        if (superDivisionId == null) {
            superDivisionId = new LongFilter();
        }
        return superDivisionId;
    }

    public void setSuperDivisionId(LongFilter superDivisionId) {
        this.superDivisionId = superDivisionId;
    }

    public LongFilter getDivisionId() {
        return divisionId;
    }

    public LongFilter divisionId() {
        if (divisionId == null) {
            divisionId = new LongFilter();
        }
        return divisionId;
    }

    public void setDivisionId(LongFilter divisionId) {
        this.divisionId = divisionId;
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
        final DivisionCriteria that = (DivisionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(sousDivisionsId, that.sousDivisionsId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(superDivisionId, that.superDivisionId) &&
            Objects.equals(divisionId, that.divisionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, sousDivisionsId, synonymesId, superDivisionId, divisionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DivisionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (sousDivisionsId != null ? "sousDivisionsId=" + sousDivisionsId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (superDivisionId != null ? "superDivisionId=" + superDivisionId + ", " : "") +
            (divisionId != null ? "divisionId=" + divisionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

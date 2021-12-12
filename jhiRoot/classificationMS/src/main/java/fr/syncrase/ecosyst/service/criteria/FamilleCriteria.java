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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.Famille} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.FamilleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /familles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FamilleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter sousFamillesId;

    private LongFilter synonymesId;

    private LongFilter superFamilleId;

    private LongFilter familleId;

    private Boolean distinct;

    public FamilleCriteria() {}

    public FamilleCriteria(FamilleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.sousFamillesId = other.sousFamillesId == null ? null : other.sousFamillesId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.superFamilleId = other.superFamilleId == null ? null : other.superFamilleId.copy();
        this.familleId = other.familleId == null ? null : other.familleId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FamilleCriteria copy() {
        return new FamilleCriteria(this);
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

    public LongFilter getSousFamillesId() {
        return sousFamillesId;
    }

    public LongFilter sousFamillesId() {
        if (sousFamillesId == null) {
            sousFamillesId = new LongFilter();
        }
        return sousFamillesId;
    }

    public void setSousFamillesId(LongFilter sousFamillesId) {
        this.sousFamillesId = sousFamillesId;
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

    public LongFilter getSuperFamilleId() {
        return superFamilleId;
    }

    public LongFilter superFamilleId() {
        if (superFamilleId == null) {
            superFamilleId = new LongFilter();
        }
        return superFamilleId;
    }

    public void setSuperFamilleId(LongFilter superFamilleId) {
        this.superFamilleId = superFamilleId;
    }

    public LongFilter getFamilleId() {
        return familleId;
    }

    public LongFilter familleId() {
        if (familleId == null) {
            familleId = new LongFilter();
        }
        return familleId;
    }

    public void setFamilleId(LongFilter familleId) {
        this.familleId = familleId;
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
        final FamilleCriteria that = (FamilleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(sousFamillesId, that.sousFamillesId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(superFamilleId, that.superFamilleId) &&
            Objects.equals(familleId, that.familleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, sousFamillesId, synonymesId, superFamilleId, familleId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FamilleCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (sousFamillesId != null ? "sousFamillesId=" + sousFamillesId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (superFamilleId != null ? "superFamilleId=" + superFamilleId + ", " : "") +
            (familleId != null ? "familleId=" + familleId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

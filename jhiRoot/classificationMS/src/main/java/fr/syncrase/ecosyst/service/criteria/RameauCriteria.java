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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.Rameau} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.RameauResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rameaus?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RameauCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter infraRegnesId;

    private LongFilter synonymesId;

    private LongFilter sousRegneId;

    private LongFilter rameauId;

    private Boolean distinct;

    public RameauCriteria() {}

    public RameauCriteria(RameauCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.infraRegnesId = other.infraRegnesId == null ? null : other.infraRegnesId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.sousRegneId = other.sousRegneId == null ? null : other.sousRegneId.copy();
        this.rameauId = other.rameauId == null ? null : other.rameauId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RameauCriteria copy() {
        return new RameauCriteria(this);
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

    public LongFilter getInfraRegnesId() {
        return infraRegnesId;
    }

    public LongFilter infraRegnesId() {
        if (infraRegnesId == null) {
            infraRegnesId = new LongFilter();
        }
        return infraRegnesId;
    }

    public void setInfraRegnesId(LongFilter infraRegnesId) {
        this.infraRegnesId = infraRegnesId;
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

    public LongFilter getSousRegneId() {
        return sousRegneId;
    }

    public LongFilter sousRegneId() {
        if (sousRegneId == null) {
            sousRegneId = new LongFilter();
        }
        return sousRegneId;
    }

    public void setSousRegneId(LongFilter sousRegneId) {
        this.sousRegneId = sousRegneId;
    }

    public LongFilter getRameauId() {
        return rameauId;
    }

    public LongFilter rameauId() {
        if (rameauId == null) {
            rameauId = new LongFilter();
        }
        return rameauId;
    }

    public void setRameauId(LongFilter rameauId) {
        this.rameauId = rameauId;
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
        final RameauCriteria that = (RameauCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(infraRegnesId, that.infraRegnesId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(sousRegneId, that.sousRegneId) &&
            Objects.equals(rameauId, that.rameauId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, infraRegnesId, synonymesId, sousRegneId, rameauId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RameauCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (infraRegnesId != null ? "infraRegnesId=" + infraRegnesId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (sousRegneId != null ? "sousRegneId=" + sousRegneId + ", " : "") +
            (rameauId != null ? "rameauId=" + rameauId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

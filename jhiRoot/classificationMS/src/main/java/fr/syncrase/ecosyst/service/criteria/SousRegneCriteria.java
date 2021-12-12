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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.SousRegne} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.SousRegneResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sous-regnes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SousRegneCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter rameausId;

    private LongFilter synonymesId;

    private LongFilter regneId;

    private LongFilter sousRegneId;

    private Boolean distinct;

    public SousRegneCriteria() {}

    public SousRegneCriteria(SousRegneCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.rameausId = other.rameausId == null ? null : other.rameausId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.regneId = other.regneId == null ? null : other.regneId.copy();
        this.sousRegneId = other.sousRegneId == null ? null : other.sousRegneId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SousRegneCriteria copy() {
        return new SousRegneCriteria(this);
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

    public LongFilter getRameausId() {
        return rameausId;
    }

    public LongFilter rameausId() {
        if (rameausId == null) {
            rameausId = new LongFilter();
        }
        return rameausId;
    }

    public void setRameausId(LongFilter rameausId) {
        this.rameausId = rameausId;
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

    public LongFilter getRegneId() {
        return regneId;
    }

    public LongFilter regneId() {
        if (regneId == null) {
            regneId = new LongFilter();
        }
        return regneId;
    }

    public void setRegneId(LongFilter regneId) {
        this.regneId = regneId;
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
        final SousRegneCriteria that = (SousRegneCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(rameausId, that.rameausId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(regneId, that.regneId) &&
            Objects.equals(sousRegneId, that.sousRegneId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, rameausId, synonymesId, regneId, sousRegneId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SousRegneCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (rameausId != null ? "rameausId=" + rameausId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (regneId != null ? "regneId=" + regneId + ", " : "") +
            (sousRegneId != null ? "sousRegneId=" + sousRegneId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

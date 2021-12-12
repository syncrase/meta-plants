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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.SuperRegne} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.SuperRegneResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /super-regnes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SuperRegneCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter regnesId;

    private LongFilter synonymesId;

    private LongFilter superRegneId;

    private Boolean distinct;

    public SuperRegneCriteria() {}

    public SuperRegneCriteria(SuperRegneCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.regnesId = other.regnesId == null ? null : other.regnesId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.superRegneId = other.superRegneId == null ? null : other.superRegneId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SuperRegneCriteria copy() {
        return new SuperRegneCriteria(this);
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

    public LongFilter getRegnesId() {
        return regnesId;
    }

    public LongFilter regnesId() {
        if (regnesId == null) {
            regnesId = new LongFilter();
        }
        return regnesId;
    }

    public void setRegnesId(LongFilter regnesId) {
        this.regnesId = regnesId;
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

    public LongFilter getSuperRegneId() {
        return superRegneId;
    }

    public LongFilter superRegneId() {
        if (superRegneId == null) {
            superRegneId = new LongFilter();
        }
        return superRegneId;
    }

    public void setSuperRegneId(LongFilter superRegneId) {
        this.superRegneId = superRegneId;
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
        final SuperRegneCriteria that = (SuperRegneCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(regnesId, that.regnesId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(superRegneId, that.superRegneId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, regnesId, synonymesId, superRegneId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SuperRegneCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (regnesId != null ? "regnesId=" + regnesId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (superRegneId != null ? "superRegneId=" + superRegneId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

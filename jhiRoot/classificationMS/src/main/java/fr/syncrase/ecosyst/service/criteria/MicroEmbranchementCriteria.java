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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.MicroEmbranchement} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.MicroEmbranchementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /micro-embranchements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MicroEmbranchementCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter superClassesId;

    private LongFilter synonymesId;

    private LongFilter infraEmbranchementId;

    private LongFilter microEmbranchementId;

    private Boolean distinct;

    public MicroEmbranchementCriteria() {}

    public MicroEmbranchementCriteria(MicroEmbranchementCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.superClassesId = other.superClassesId == null ? null : other.superClassesId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.infraEmbranchementId = other.infraEmbranchementId == null ? null : other.infraEmbranchementId.copy();
        this.microEmbranchementId = other.microEmbranchementId == null ? null : other.microEmbranchementId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MicroEmbranchementCriteria copy() {
        return new MicroEmbranchementCriteria(this);
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

    public LongFilter getSuperClassesId() {
        return superClassesId;
    }

    public LongFilter superClassesId() {
        if (superClassesId == null) {
            superClassesId = new LongFilter();
        }
        return superClassesId;
    }

    public void setSuperClassesId(LongFilter superClassesId) {
        this.superClassesId = superClassesId;
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

    public LongFilter getInfraEmbranchementId() {
        return infraEmbranchementId;
    }

    public LongFilter infraEmbranchementId() {
        if (infraEmbranchementId == null) {
            infraEmbranchementId = new LongFilter();
        }
        return infraEmbranchementId;
    }

    public void setInfraEmbranchementId(LongFilter infraEmbranchementId) {
        this.infraEmbranchementId = infraEmbranchementId;
    }

    public LongFilter getMicroEmbranchementId() {
        return microEmbranchementId;
    }

    public LongFilter microEmbranchementId() {
        if (microEmbranchementId == null) {
            microEmbranchementId = new LongFilter();
        }
        return microEmbranchementId;
    }

    public void setMicroEmbranchementId(LongFilter microEmbranchementId) {
        this.microEmbranchementId = microEmbranchementId;
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
        final MicroEmbranchementCriteria that = (MicroEmbranchementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(superClassesId, that.superClassesId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(infraEmbranchementId, that.infraEmbranchementId) &&
            Objects.equals(microEmbranchementId, that.microEmbranchementId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, superClassesId, synonymesId, infraEmbranchementId, microEmbranchementId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MicroEmbranchementCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (superClassesId != null ? "superClassesId=" + superClassesId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (infraEmbranchementId != null ? "infraEmbranchementId=" + infraEmbranchementId + ", " : "") +
            (microEmbranchementId != null ? "microEmbranchementId=" + microEmbranchementId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

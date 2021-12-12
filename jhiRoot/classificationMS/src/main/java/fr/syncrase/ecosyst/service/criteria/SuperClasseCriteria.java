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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.SuperClasse} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.SuperClasseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /super-classes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SuperClasseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter classesId;

    private LongFilter synonymesId;

    private LongFilter microEmbranchementId;

    private LongFilter superClasseId;

    private Boolean distinct;

    public SuperClasseCriteria() {}

    public SuperClasseCriteria(SuperClasseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.classesId = other.classesId == null ? null : other.classesId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.microEmbranchementId = other.microEmbranchementId == null ? null : other.microEmbranchementId.copy();
        this.superClasseId = other.superClasseId == null ? null : other.superClasseId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SuperClasseCriteria copy() {
        return new SuperClasseCriteria(this);
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

    public LongFilter getClassesId() {
        return classesId;
    }

    public LongFilter classesId() {
        if (classesId == null) {
            classesId = new LongFilter();
        }
        return classesId;
    }

    public void setClassesId(LongFilter classesId) {
        this.classesId = classesId;
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

    public LongFilter getSuperClasseId() {
        return superClasseId;
    }

    public LongFilter superClasseId() {
        if (superClasseId == null) {
            superClasseId = new LongFilter();
        }
        return superClasseId;
    }

    public void setSuperClasseId(LongFilter superClasseId) {
        this.superClasseId = superClasseId;
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
        final SuperClasseCriteria that = (SuperClasseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(classesId, that.classesId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(microEmbranchementId, that.microEmbranchementId) &&
            Objects.equals(superClasseId, that.superClasseId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, classesId, synonymesId, microEmbranchementId, superClasseId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SuperClasseCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (classesId != null ? "classesId=" + classesId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (microEmbranchementId != null ? "microEmbranchementId=" + microEmbranchementId + ", " : "") +
            (superClasseId != null ? "superClasseId=" + superClasseId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

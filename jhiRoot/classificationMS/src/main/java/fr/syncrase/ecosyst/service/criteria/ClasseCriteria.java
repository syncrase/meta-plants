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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.Classe} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.ClasseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /classes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClasseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter sousClassesId;

    private LongFilter synonymesId;

    private LongFilter superClasseId;

    private LongFilter classeId;

    private Boolean distinct;

    public ClasseCriteria() {}

    public ClasseCriteria(ClasseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.sousClassesId = other.sousClassesId == null ? null : other.sousClassesId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.superClasseId = other.superClasseId == null ? null : other.superClasseId.copy();
        this.classeId = other.classeId == null ? null : other.classeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ClasseCriteria copy() {
        return new ClasseCriteria(this);
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

    public LongFilter getSousClassesId() {
        return sousClassesId;
    }

    public LongFilter sousClassesId() {
        if (sousClassesId == null) {
            sousClassesId = new LongFilter();
        }
        return sousClassesId;
    }

    public void setSousClassesId(LongFilter sousClassesId) {
        this.sousClassesId = sousClassesId;
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

    public LongFilter getClasseId() {
        return classeId;
    }

    public LongFilter classeId() {
        if (classeId == null) {
            classeId = new LongFilter();
        }
        return classeId;
    }

    public void setClasseId(LongFilter classeId) {
        this.classeId = classeId;
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
        final ClasseCriteria that = (ClasseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(sousClassesId, that.sousClassesId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(superClasseId, that.superClasseId) &&
            Objects.equals(classeId, that.classeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, sousClassesId, synonymesId, superClasseId, classeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClasseCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (sousClassesId != null ? "sousClassesId=" + sousClassesId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (superClasseId != null ? "superClasseId=" + superClasseId + ", " : "") +
            (classeId != null ? "classeId=" + classeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

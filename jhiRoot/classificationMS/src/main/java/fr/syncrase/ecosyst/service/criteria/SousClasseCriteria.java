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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.SousClasse} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.SousClasseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sous-classes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SousClasseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter infraClassesId;

    private LongFilter synonymesId;

    private LongFilter classeId;

    private LongFilter sousClasseId;

    private Boolean distinct;

    public SousClasseCriteria() {}

    public SousClasseCriteria(SousClasseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.infraClassesId = other.infraClassesId == null ? null : other.infraClassesId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.classeId = other.classeId == null ? null : other.classeId.copy();
        this.sousClasseId = other.sousClasseId == null ? null : other.sousClasseId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SousClasseCriteria copy() {
        return new SousClasseCriteria(this);
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

    public LongFilter getInfraClassesId() {
        return infraClassesId;
    }

    public LongFilter infraClassesId() {
        if (infraClassesId == null) {
            infraClassesId = new LongFilter();
        }
        return infraClassesId;
    }

    public void setInfraClassesId(LongFilter infraClassesId) {
        this.infraClassesId = infraClassesId;
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

    public LongFilter getSousClasseId() {
        return sousClasseId;
    }

    public LongFilter sousClasseId() {
        if (sousClasseId == null) {
            sousClasseId = new LongFilter();
        }
        return sousClasseId;
    }

    public void setSousClasseId(LongFilter sousClasseId) {
        this.sousClasseId = sousClasseId;
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
        final SousClasseCriteria that = (SousClasseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(infraClassesId, that.infraClassesId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(classeId, that.classeId) &&
            Objects.equals(sousClasseId, that.sousClasseId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, infraClassesId, synonymesId, classeId, sousClasseId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SousClasseCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (infraClassesId != null ? "infraClassesId=" + infraClassesId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (classeId != null ? "classeId=" + classeId + ", " : "") +
            (sousClasseId != null ? "sousClasseId=" + sousClasseId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

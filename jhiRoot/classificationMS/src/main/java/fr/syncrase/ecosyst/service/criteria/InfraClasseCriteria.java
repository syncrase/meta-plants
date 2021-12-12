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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.InfraClasse} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.InfraClasseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /infra-classes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InfraClasseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter superOrdresId;

    private LongFilter synonymesId;

    private LongFilter sousClasseId;

    private LongFilter infraClasseId;

    private Boolean distinct;

    public InfraClasseCriteria() {}

    public InfraClasseCriteria(InfraClasseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.superOrdresId = other.superOrdresId == null ? null : other.superOrdresId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.sousClasseId = other.sousClasseId == null ? null : other.sousClasseId.copy();
        this.infraClasseId = other.infraClasseId == null ? null : other.infraClasseId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public InfraClasseCriteria copy() {
        return new InfraClasseCriteria(this);
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

    public LongFilter getSuperOrdresId() {
        return superOrdresId;
    }

    public LongFilter superOrdresId() {
        if (superOrdresId == null) {
            superOrdresId = new LongFilter();
        }
        return superOrdresId;
    }

    public void setSuperOrdresId(LongFilter superOrdresId) {
        this.superOrdresId = superOrdresId;
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

    public LongFilter getInfraClasseId() {
        return infraClasseId;
    }

    public LongFilter infraClasseId() {
        if (infraClasseId == null) {
            infraClasseId = new LongFilter();
        }
        return infraClasseId;
    }

    public void setInfraClasseId(LongFilter infraClasseId) {
        this.infraClasseId = infraClasseId;
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
        final InfraClasseCriteria that = (InfraClasseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(superOrdresId, that.superOrdresId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(sousClasseId, that.sousClasseId) &&
            Objects.equals(infraClasseId, that.infraClasseId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, superOrdresId, synonymesId, sousClasseId, infraClasseId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InfraClasseCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (superOrdresId != null ? "superOrdresId=" + superOrdresId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (sousClasseId != null ? "sousClasseId=" + sousClasseId + ", " : "") +
            (infraClasseId != null ? "infraClasseId=" + infraClasseId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

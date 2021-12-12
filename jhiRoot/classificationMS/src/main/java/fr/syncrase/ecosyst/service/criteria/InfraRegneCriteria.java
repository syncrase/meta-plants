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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.InfraRegne} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.InfraRegneResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /infra-regnes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InfraRegneCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter superDivisionsId;

    private LongFilter synonymesId;

    private LongFilter rameauId;

    private LongFilter infraRegneId;

    private Boolean distinct;

    public InfraRegneCriteria() {}

    public InfraRegneCriteria(InfraRegneCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.superDivisionsId = other.superDivisionsId == null ? null : other.superDivisionsId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.rameauId = other.rameauId == null ? null : other.rameauId.copy();
        this.infraRegneId = other.infraRegneId == null ? null : other.infraRegneId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public InfraRegneCriteria copy() {
        return new InfraRegneCriteria(this);
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

    public LongFilter getSuperDivisionsId() {
        return superDivisionsId;
    }

    public LongFilter superDivisionsId() {
        if (superDivisionsId == null) {
            superDivisionsId = new LongFilter();
        }
        return superDivisionsId;
    }

    public void setSuperDivisionsId(LongFilter superDivisionsId) {
        this.superDivisionsId = superDivisionsId;
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

    public LongFilter getInfraRegneId() {
        return infraRegneId;
    }

    public LongFilter infraRegneId() {
        if (infraRegneId == null) {
            infraRegneId = new LongFilter();
        }
        return infraRegneId;
    }

    public void setInfraRegneId(LongFilter infraRegneId) {
        this.infraRegneId = infraRegneId;
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
        final InfraRegneCriteria that = (InfraRegneCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(superDivisionsId, that.superDivisionsId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(rameauId, that.rameauId) &&
            Objects.equals(infraRegneId, that.infraRegneId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, superDivisionsId, synonymesId, rameauId, infraRegneId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InfraRegneCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (superDivisionsId != null ? "superDivisionsId=" + superDivisionsId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (rameauId != null ? "rameauId=" + rameauId + ", " : "") +
            (infraRegneId != null ? "infraRegneId=" + infraRegneId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

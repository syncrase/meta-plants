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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.InfraEmbranchement} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.InfraEmbranchementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /infra-embranchements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InfraEmbranchementCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter microEmbranchementsId;

    private LongFilter synonymesId;

    private LongFilter sousDivisionId;

    private LongFilter infraEmbranchementId;

    private Boolean distinct;

    public InfraEmbranchementCriteria() {}

    public InfraEmbranchementCriteria(InfraEmbranchementCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.microEmbranchementsId = other.microEmbranchementsId == null ? null : other.microEmbranchementsId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.sousDivisionId = other.sousDivisionId == null ? null : other.sousDivisionId.copy();
        this.infraEmbranchementId = other.infraEmbranchementId == null ? null : other.infraEmbranchementId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public InfraEmbranchementCriteria copy() {
        return new InfraEmbranchementCriteria(this);
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

    public LongFilter getMicroEmbranchementsId() {
        return microEmbranchementsId;
    }

    public LongFilter microEmbranchementsId() {
        if (microEmbranchementsId == null) {
            microEmbranchementsId = new LongFilter();
        }
        return microEmbranchementsId;
    }

    public void setMicroEmbranchementsId(LongFilter microEmbranchementsId) {
        this.microEmbranchementsId = microEmbranchementsId;
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

    public LongFilter getSousDivisionId() {
        return sousDivisionId;
    }

    public LongFilter sousDivisionId() {
        if (sousDivisionId == null) {
            sousDivisionId = new LongFilter();
        }
        return sousDivisionId;
    }

    public void setSousDivisionId(LongFilter sousDivisionId) {
        this.sousDivisionId = sousDivisionId;
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
        final InfraEmbranchementCriteria that = (InfraEmbranchementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(microEmbranchementsId, that.microEmbranchementsId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(sousDivisionId, that.sousDivisionId) &&
            Objects.equals(infraEmbranchementId, that.infraEmbranchementId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, microEmbranchementsId, synonymesId, sousDivisionId, infraEmbranchementId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InfraEmbranchementCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (microEmbranchementsId != null ? "microEmbranchementsId=" + microEmbranchementsId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (sousDivisionId != null ? "sousDivisionId=" + sousDivisionId + ", " : "") +
            (infraEmbranchementId != null ? "infraEmbranchementId=" + infraEmbranchementId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

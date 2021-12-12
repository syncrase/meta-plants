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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.InfraOrdre} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.InfraOrdreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /infra-ordres?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InfraOrdreCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter microOrdresId;

    private LongFilter synonymesId;

    private LongFilter sousOrdreId;

    private LongFilter infraOrdreId;

    private Boolean distinct;

    public InfraOrdreCriteria() {}

    public InfraOrdreCriteria(InfraOrdreCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.microOrdresId = other.microOrdresId == null ? null : other.microOrdresId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.sousOrdreId = other.sousOrdreId == null ? null : other.sousOrdreId.copy();
        this.infraOrdreId = other.infraOrdreId == null ? null : other.infraOrdreId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public InfraOrdreCriteria copy() {
        return new InfraOrdreCriteria(this);
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

    public LongFilter getMicroOrdresId() {
        return microOrdresId;
    }

    public LongFilter microOrdresId() {
        if (microOrdresId == null) {
            microOrdresId = new LongFilter();
        }
        return microOrdresId;
    }

    public void setMicroOrdresId(LongFilter microOrdresId) {
        this.microOrdresId = microOrdresId;
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

    public LongFilter getSousOrdreId() {
        return sousOrdreId;
    }

    public LongFilter sousOrdreId() {
        if (sousOrdreId == null) {
            sousOrdreId = new LongFilter();
        }
        return sousOrdreId;
    }

    public void setSousOrdreId(LongFilter sousOrdreId) {
        this.sousOrdreId = sousOrdreId;
    }

    public LongFilter getInfraOrdreId() {
        return infraOrdreId;
    }

    public LongFilter infraOrdreId() {
        if (infraOrdreId == null) {
            infraOrdreId = new LongFilter();
        }
        return infraOrdreId;
    }

    public void setInfraOrdreId(LongFilter infraOrdreId) {
        this.infraOrdreId = infraOrdreId;
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
        final InfraOrdreCriteria that = (InfraOrdreCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(microOrdresId, that.microOrdresId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(sousOrdreId, that.sousOrdreId) &&
            Objects.equals(infraOrdreId, that.infraOrdreId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, microOrdresId, synonymesId, sousOrdreId, infraOrdreId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InfraOrdreCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (microOrdresId != null ? "microOrdresId=" + microOrdresId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (sousOrdreId != null ? "sousOrdreId=" + sousOrdreId + ", " : "") +
            (infraOrdreId != null ? "infraOrdreId=" + infraOrdreId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.MicroOrdre} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.MicroOrdreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /micro-ordres?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MicroOrdreCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter superFamillesId;

    private LongFilter synonymesId;

    private LongFilter infraOrdreId;

    private LongFilter microOrdreId;

    private Boolean distinct;

    public MicroOrdreCriteria() {}

    public MicroOrdreCriteria(MicroOrdreCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.superFamillesId = other.superFamillesId == null ? null : other.superFamillesId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.infraOrdreId = other.infraOrdreId == null ? null : other.infraOrdreId.copy();
        this.microOrdreId = other.microOrdreId == null ? null : other.microOrdreId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MicroOrdreCriteria copy() {
        return new MicroOrdreCriteria(this);
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

    public LongFilter getSuperFamillesId() {
        return superFamillesId;
    }

    public LongFilter superFamillesId() {
        if (superFamillesId == null) {
            superFamillesId = new LongFilter();
        }
        return superFamillesId;
    }

    public void setSuperFamillesId(LongFilter superFamillesId) {
        this.superFamillesId = superFamillesId;
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

    public LongFilter getMicroOrdreId() {
        return microOrdreId;
    }

    public LongFilter microOrdreId() {
        if (microOrdreId == null) {
            microOrdreId = new LongFilter();
        }
        return microOrdreId;
    }

    public void setMicroOrdreId(LongFilter microOrdreId) {
        this.microOrdreId = microOrdreId;
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
        final MicroOrdreCriteria that = (MicroOrdreCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(superFamillesId, that.superFamillesId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(infraOrdreId, that.infraOrdreId) &&
            Objects.equals(microOrdreId, that.microOrdreId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, superFamillesId, synonymesId, infraOrdreId, microOrdreId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MicroOrdreCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (superFamillesId != null ? "superFamillesId=" + superFamillesId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (infraOrdreId != null ? "infraOrdreId=" + infraOrdreId + ", " : "") +
            (microOrdreId != null ? "microOrdreId=" + microOrdreId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

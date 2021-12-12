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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.SuperOrdre} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.SuperOrdreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /super-ordres?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SuperOrdreCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter ordresId;

    private LongFilter synonymesId;

    private LongFilter infraClasseId;

    private LongFilter superOrdreId;

    private Boolean distinct;

    public SuperOrdreCriteria() {}

    public SuperOrdreCriteria(SuperOrdreCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.ordresId = other.ordresId == null ? null : other.ordresId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.infraClasseId = other.infraClasseId == null ? null : other.infraClasseId.copy();
        this.superOrdreId = other.superOrdreId == null ? null : other.superOrdreId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SuperOrdreCriteria copy() {
        return new SuperOrdreCriteria(this);
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

    public LongFilter getOrdresId() {
        return ordresId;
    }

    public LongFilter ordresId() {
        if (ordresId == null) {
            ordresId = new LongFilter();
        }
        return ordresId;
    }

    public void setOrdresId(LongFilter ordresId) {
        this.ordresId = ordresId;
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

    public LongFilter getSuperOrdreId() {
        return superOrdreId;
    }

    public LongFilter superOrdreId() {
        if (superOrdreId == null) {
            superOrdreId = new LongFilter();
        }
        return superOrdreId;
    }

    public void setSuperOrdreId(LongFilter superOrdreId) {
        this.superOrdreId = superOrdreId;
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
        final SuperOrdreCriteria that = (SuperOrdreCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(ordresId, that.ordresId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(infraClasseId, that.infraClasseId) &&
            Objects.equals(superOrdreId, that.superOrdreId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, ordresId, synonymesId, infraClasseId, superOrdreId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SuperOrdreCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (ordresId != null ? "ordresId=" + ordresId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (infraClasseId != null ? "infraClasseId=" + infraClasseId + ", " : "") +
            (superOrdreId != null ? "superOrdreId=" + superOrdreId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

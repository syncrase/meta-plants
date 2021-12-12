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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.Ordre} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.OrdreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ordres?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrdreCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter sousOrdresId;

    private LongFilter synonymesId;

    private LongFilter superOrdreId;

    private LongFilter ordreId;

    private Boolean distinct;

    public OrdreCriteria() {}

    public OrdreCriteria(OrdreCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.sousOrdresId = other.sousOrdresId == null ? null : other.sousOrdresId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.superOrdreId = other.superOrdreId == null ? null : other.superOrdreId.copy();
        this.ordreId = other.ordreId == null ? null : other.ordreId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OrdreCriteria copy() {
        return new OrdreCriteria(this);
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

    public LongFilter getSousOrdresId() {
        return sousOrdresId;
    }

    public LongFilter sousOrdresId() {
        if (sousOrdresId == null) {
            sousOrdresId = new LongFilter();
        }
        return sousOrdresId;
    }

    public void setSousOrdresId(LongFilter sousOrdresId) {
        this.sousOrdresId = sousOrdresId;
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

    public LongFilter getOrdreId() {
        return ordreId;
    }

    public LongFilter ordreId() {
        if (ordreId == null) {
            ordreId = new LongFilter();
        }
        return ordreId;
    }

    public void setOrdreId(LongFilter ordreId) {
        this.ordreId = ordreId;
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
        final OrdreCriteria that = (OrdreCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(sousOrdresId, that.sousOrdresId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(superOrdreId, that.superOrdreId) &&
            Objects.equals(ordreId, that.ordreId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, sousOrdresId, synonymesId, superOrdreId, ordreId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrdreCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (sousOrdresId != null ? "sousOrdresId=" + sousOrdresId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (superOrdreId != null ? "superOrdreId=" + superOrdreId + ", " : "") +
            (ordreId != null ? "ordreId=" + ordreId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

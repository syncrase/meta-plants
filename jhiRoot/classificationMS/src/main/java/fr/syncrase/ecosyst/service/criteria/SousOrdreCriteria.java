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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.SousOrdre} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.SousOrdreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sous-ordres?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SousOrdreCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter infraOrdresId;

    private LongFilter synonymesId;

    private LongFilter ordreId;

    private LongFilter sousOrdreId;

    private Boolean distinct;

    public SousOrdreCriteria() {}

    public SousOrdreCriteria(SousOrdreCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.infraOrdresId = other.infraOrdresId == null ? null : other.infraOrdresId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.ordreId = other.ordreId == null ? null : other.ordreId.copy();
        this.sousOrdreId = other.sousOrdreId == null ? null : other.sousOrdreId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SousOrdreCriteria copy() {
        return new SousOrdreCriteria(this);
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

    public LongFilter getInfraOrdresId() {
        return infraOrdresId;
    }

    public LongFilter infraOrdresId() {
        if (infraOrdresId == null) {
            infraOrdresId = new LongFilter();
        }
        return infraOrdresId;
    }

    public void setInfraOrdresId(LongFilter infraOrdresId) {
        this.infraOrdresId = infraOrdresId;
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
        final SousOrdreCriteria that = (SousOrdreCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(infraOrdresId, that.infraOrdresId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(ordreId, that.ordreId) &&
            Objects.equals(sousOrdreId, that.sousOrdreId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, infraOrdresId, synonymesId, ordreId, sousOrdreId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SousOrdreCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (infraOrdresId != null ? "infraOrdresId=" + infraOrdresId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (ordreId != null ? "ordreId=" + ordreId + ", " : "") +
            (sousOrdreId != null ? "sousOrdreId=" + sousOrdreId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

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
 * Criteria class for the {@link fr.syncrase.ecosyst.domain.Tribu} entity. This class is used
 * in {@link fr.syncrase.ecosyst.web.rest.TribuResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tribus?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TribuCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomFr;

    private StringFilter nomLatin;

    private LongFilter sousTribusId;

    private LongFilter synonymesId;

    private LongFilter sousFamilleId;

    private LongFilter tribuId;

    private Boolean distinct;

    public TribuCriteria() {}

    public TribuCriteria(TribuCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomLatin = other.nomLatin == null ? null : other.nomLatin.copy();
        this.sousTribusId = other.sousTribusId == null ? null : other.sousTribusId.copy();
        this.synonymesId = other.synonymesId == null ? null : other.synonymesId.copy();
        this.sousFamilleId = other.sousFamilleId == null ? null : other.sousFamilleId.copy();
        this.tribuId = other.tribuId == null ? null : other.tribuId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TribuCriteria copy() {
        return new TribuCriteria(this);
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

    public LongFilter getSousTribusId() {
        return sousTribusId;
    }

    public LongFilter sousTribusId() {
        if (sousTribusId == null) {
            sousTribusId = new LongFilter();
        }
        return sousTribusId;
    }

    public void setSousTribusId(LongFilter sousTribusId) {
        this.sousTribusId = sousTribusId;
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

    public LongFilter getSousFamilleId() {
        return sousFamilleId;
    }

    public LongFilter sousFamilleId() {
        if (sousFamilleId == null) {
            sousFamilleId = new LongFilter();
        }
        return sousFamilleId;
    }

    public void setSousFamilleId(LongFilter sousFamilleId) {
        this.sousFamilleId = sousFamilleId;
    }

    public LongFilter getTribuId() {
        return tribuId;
    }

    public LongFilter tribuId() {
        if (tribuId == null) {
            tribuId = new LongFilter();
        }
        return tribuId;
    }

    public void setTribuId(LongFilter tribuId) {
        this.tribuId = tribuId;
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
        final TribuCriteria that = (TribuCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomLatin, that.nomLatin) &&
            Objects.equals(sousTribusId, that.sousTribusId) &&
            Objects.equals(synonymesId, that.synonymesId) &&
            Objects.equals(sousFamilleId, that.sousFamilleId) &&
            Objects.equals(tribuId, that.tribuId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomFr, nomLatin, sousTribusId, synonymesId, sousFamilleId, tribuId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TribuCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomLatin != null ? "nomLatin=" + nomLatin + ", " : "") +
            (sousTribusId != null ? "sousTribusId=" + sousTribusId + ", " : "") +
            (synonymesId != null ? "synonymesId=" + synonymesId + ", " : "") +
            (sousFamilleId != null ? "sousFamilleId=" + sousFamilleId + ", " : "") +
            (tribuId != null ? "tribuId=" + tribuId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
